package com.shanshui.musicapp.mvp.ui.fragment.music;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.glide.GlideArms;
import com.jess.arms.utils.ArmsUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shanshui.musicapp.R;
import com.shanshui.musicapp.app.EventBusTags;
import com.shanshui.musicapp.app.base.BaseMusicLazyFragment;
import com.shanshui.musicapp.app.eventbus.EventMusicData;
import com.shanshui.musicapp.app.utils.MusicUtil;
import com.shanshui.musicapp.di.component.DaggerMediaBrowserComponent;
import com.shanshui.musicapp.mvp.AppConstant;
import com.shanshui.musicapp.mvp.adapter.MusicListAdapter;
import com.shanshui.musicapp.mvp.contract.MediaBrowserContract;
import com.shanshui.musicapp.mvp.model.bean.MusicBean;
import com.shanshui.musicapp.mvp.model.bean.MusicSourceInfoBean;
import com.shanshui.musicapp.mvp.music.MusicProvider;
import com.shanshui.musicapp.mvp.music.MusicProviderSource;
import com.shanshui.musicapp.mvp.presenter.MediaBrowserPresenter;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * @author mashanshui
 * @date 2019-01-22
 * @desc 在线音乐列表（总）
 */
public class MediaBrowserFragment extends BaseMusicLazyFragment<MediaBrowserPresenter> implements MediaBrowserContract.View
        , OnRefreshListener
        , OnLoadMoreListener
        , BaseQuickAdapter.OnItemClickListener
        , BaseQuickAdapter.OnItemChildClickListener {
    public static final int MUSIC_TYPE_CHARTS = 0;
    public static final int MUSIC_TYPE_SONG_SHEET = 1;
    public static final int MUSIC_TYPE_SINGER = 2;
    public static final int MUSIC_TYPE_SEARCH = 3;
    private static final String MUSIC_TYPE = "musicType";
    private static final String MUSIC_MESSAGE = "musicMessage";
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    Unbinder unbinder;

    private int page = 1;
    private int length = AppConstant.PAGE_SIZE;

    private MediaControllerCompat controller;
    private int musicType;
    private String musicMessage;
    private MusicListAdapter musicListAdapter;
    private List<MusicBean> musicBeanList;
    private List<MediaMetadataCompat> musicOldList;
    private int currentPosition = 0;

    public static MediaBrowserFragment newInstance(int musicType, String musicMessage) {
        MediaBrowserFragment fragment = new MediaBrowserFragment();
        Bundle args = new Bundle();
        args.putInt(MUSIC_TYPE, musicType);
        args.putString(MUSIC_MESSAGE, musicMessage);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            musicType = getArguments().getInt(MUSIC_TYPE);
            musicMessage = getArguments().getString(MUSIC_MESSAGE);
        }
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerMediaBrowserComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    protected MediaBrowserCompat.SubscriptionCallback getSubscriptionCallback() {
        return null;
    }

    @Override
    protected MediaControllerCompat.Callback getMediaControllerCallback() {
        return mCallback;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initRefresh();
        musicListAdapter = new MusicListAdapter(null);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(musicListAdapter);
        musicListAdapter.setOnItemClickListener(this::onItemClick);
        musicListAdapter.setOnItemChildClickListener(this::onItemChildClick);
        loadData();
    }


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.iv_download:
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        currentPosition = position;
        if (TextUtils.equals(musicMessage, MusicProvider.CURRENT_MUSIC_PROVIDE)) {
            MusicBean bean = (MusicBean) musicListAdapter.getItem(position);
            controller.getTransportControls().playFromMediaId(bean.getMetadata().getMediaId(), null);
        } else {
            loadMusicToQueue();
        }
    }

    private final MediaControllerCompat.Callback mCallback = new MediaControllerCompat.Callback() {

        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {
            if (metadata == null) {
                return;
            }
            updateListStatus(metadata);
        }
    };

    private void updateListStatus(MediaMetadataCompat metadata) {
        if (TextUtils.equals(musicMessage, MusicProvider.CURRENT_MUSIC_PROVIDE)) {
            for (int i = 0; i < musicOldList.size(); i++) {
                if (TextUtils.equals(musicOldList.get(i).getDescription().getMediaId(), metadata.getDescription().getMediaId())) {
                    musicListAdapter.setCurrentPosition(i);
                    mPresenter.getMusicInfo(musicOldList.get(i).getString(MusicProviderSource.CUSTOM_METADATA_TRACK_SOURCE));
                }
            }
        }
    }

    /**
     * 播放队列不是当前队列时将该音乐列表加入到队列
     */
    private void loadMusicToQueue() {
        EventBus.getDefault().post(new EventMusicData(musicMessage, musicOldList), EventBusTags.switchMusicProvide);
    }

    private void initRefresh() {
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
        refreshLayout.setEnableLoadMore(true);
        refreshLayout.setEnableRefresh(false);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_media_browser;
    }

    /**
     * 通过此方法可以使 Fragment 能够与外界做一些交互和通信, 比如说外部的 Activity 想让自己持有的某个 Fragment 对象执行一些方法,
     * 建议在有多个需要与外界交互的方法时, 统一传 {@link Message}, 通过 what 字段来区分不同的方法, 在 {@link #setData(Object)}
     * 方法中就可以 {@code switch} 做不同的操作, 这样就可以用统一的入口方法做多个不同的操作, 可以起到分发的作用
     * <p>
     * 调用此方法时请注意调用时 Fragment 的生命周期, 如果调用 {@link #setData(Object)} 方法时 {@link Fragment#onCreate(Bundle)} 还没执行
     * 但在 {@link #setData(Object)} 里却调用了 Presenter 的方法, 是会报空的, 因为 Dagger 注入是在 {@link Fragment#onCreate(Bundle)} 方法中执行的
     * 然后才创建的 Presenter, 如果要做一些初始化操作,可以不必让外部调用 {@link #setData(Object)}, 在 {@link #initData(Bundle)} 中初始化就可以了
     * <p>
     * Example usage:
     * <pre>
     * public void setData(@Nullable Object data) {
     *     if (data != null && data instanceof Message) {
     *         switch (((Message) data).what) {
     *             case 0:
     *                 loadData(((Message) data).arg1);
     *                 break;
     *             case 1:
     *                 refreshUI();
     *                 break;
     *             default:
     *                 //do something
     *                 break;
     *         }
     *     }
     * }
     *
     * // call setData(Object):
     * Message data = new Message();
     * data.what = 0;
     * data.arg1 = 1;
     * fragment.setData(data);
     * </pre>
     *
     * @param data 当不需要参数时 {@code data} 可以为 {@code null}
     */
    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    protected void onMediaControllerConnected() {
        controller = MediaControllerCompat.getMediaController(getActivity());
    }

    @Subscriber(tag = EventBusTags.switchMusicProvideSuccess)
    public void switchMusicProvide(String message) {
        onConnected();
        MusicBean bean = (MusicBean) musicListAdapter.getItem(currentPosition);
        controller.getTransportControls().playFromMediaId(bean.getMetadata().getMediaId(), null);
    }

    @Subscriber(tag = EventBusTags.playAllMusic)
    public void playAllMusic(String message) {
        loadMusicToQueue();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {

    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        loadData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 1;
        loadData();
    }

    private void refreshOrLoadmoreFinish() {
        if (refreshLayout == null) {
            return;
        }
        if (refreshLayout.getState() == RefreshState.Refreshing) {
            refreshLayout.finishRefresh();
        }
        if (refreshLayout.getState() == RefreshState.Loading) {
            refreshLayout.finishLoadMore();
        }
    }

    private void loadData() {
        mPresenter.loadData(musicType, musicMessage);
    }

    @Override
    public int getPage() {
        return page;
    }

    @Override
    public void setPage(int page) {
        this.page = page;
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public void handleSuccess(List<MediaMetadataCompat> datas, int musicType) {
        if (datas == null) {
            return;
        }
        musicOldList = datas;
        musicBeanList = MusicUtil.switchMusicToBean(datas, musicType);
        //下拉刷新
        if (refreshLayout.getState() == RefreshState.Refreshing) {
            musicListAdapter.setNewData(musicBeanList);
        }
        //上拉加载
        else if (refreshLayout.getState() == RefreshState.Loading) {
            //上拉加载后数据小于length，此时认为后台没有更多数据了
            if (datas.size() < length) {
                refreshLayout.setEnableLoadMore(false);
            }
            musicListAdapter.addData(musicBeanList);
        }
        //普通加载
        else {
            musicListAdapter.setNewData(musicBeanList);
        }
        refreshOrLoadmoreFinish();
    }

    @Override
    public void handleFailure() {
        if (refreshLayout == null) {
            return;
        }
        //加载失败将page重置到加载之前
        if (refreshLayout.getState() == RefreshState.Loading) {
            page--;
        }
        refreshOrLoadmoreFinish();
    }

    @Override
    public void updateMusicInfo(MusicSourceInfoBean musicSourceInfoBean) {
        ImageView imageView = (ImageView) musicListAdapter.getViewByPosition(recyclerView, musicListAdapter.getCurrentPosition(), R.id.iv_cover);
        GlideArms.with(getContext()).load(MusicUtil.getImageUrl(musicSourceInfoBean.getImgUrl(), AppConstant.ONLINE_IMG_SIZE_200))
                .placeholder(R.drawable.ic_singer_default)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(imageView);
    }

}
