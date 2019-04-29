package com.shanshui.musicapp.mvp.ui.fragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jess.arms.base.BaseApplication;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.shanshui.musicapp.R;
import com.shanshui.musicapp.app.EventBusTags;
import com.shanshui.musicapp.app.base.BaseMusicLazyFragment;
import com.shanshui.musicapp.app.eventbus.EventMusicData;
import com.shanshui.musicapp.app.utils.MusicUtil;
import com.shanshui.musicapp.di.component.DaggerMineComponent;
import com.shanshui.musicapp.di.module.MineModule;
import com.shanshui.musicapp.mvp.AppConstant;
import com.shanshui.musicapp.mvp.adapter.MusicListAdapter;
import com.shanshui.musicapp.mvp.contract.MineContract;
import com.shanshui.musicapp.mvp.model.api.service.UserService;
import com.shanshui.musicapp.mvp.model.bean.MusicBean;
import com.shanshui.musicapp.mvp.model.bean.MusicSourceInfoBean;
import com.shanshui.musicapp.mvp.music.MusicProvider;
import com.shanshui.musicapp.mvp.music.MusicService;
import com.shanshui.musicapp.mvp.presenter.MinePresenter;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class MineFragment extends BaseMusicLazyFragment<MinePresenter> implements MineContract.View
        , BaseQuickAdapter.OnItemClickListener
        , BaseQuickAdapter.OnItemChildClickListener {
    @Inject
    RxPermissions mRxPermissions;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;

    private boolean isFirstConnection = true;
    //音乐在准备完成之后是否播放
    private boolean isPlayOnPrepare = false;
    private MusicListAdapter musicListAdapter;
    private List<MusicBean> musicBeanList;
    private List<MediaMetadataCompat> musicOldList;
    private int currentPosition = 0;
    private MediaControllerCompat controller;

    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerMineComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .mineModule(new MineModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        musicBeanList = new ArrayList<>();
        musicListAdapter = new MusicListAdapter(null);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        musicListAdapter.setEmptyView(R.layout.layout_empty_view, recyclerView);
        recyclerView.setAdapter(musicListAdapter);
        musicListAdapter.setOnItemClickListener(this::onItemClick);
        musicListAdapter.setOnItemChildClickListener(this::onItemChildClick);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        currentPosition = position;
        if (TextUtils.equals(AppConstant.PROVIDE_LOCAL_MUSIC, MusicProvider.CURRENT_MUSIC_PROVIDE)) {
            MusicBean bean = (MusicBean) musicListAdapter.getItem(position);
            controller.getTransportControls().playFromMediaId(bean.getMetadata().getMediaId(), null);
        } else {
            isPlayOnPrepare = true;
            loadMusicToQueue();
        }
    }


    @Override
    protected void onMediaControllerConnected() {
        controller = MediaControllerCompat.getMediaController(getActivity());
        controller.getTransportControls().prepare();
        if (isFirstConnection) {
            isPlayOnPrepare = false;
            isFirstConnection = false;
            mPresenter.requestFilePermission();
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
        if (TextUtils.equals(AppConstant.PROVIDE_LOCAL_MUSIC, MusicProvider.CURRENT_MUSIC_PROVIDE)) {
            for (int i = 0; i < musicOldList.size(); i++) {
                if (TextUtils.equals(musicOldList.get(i).getDescription().getMediaId(), metadata.getDescription().getMediaId())) {
                    musicListAdapter.setCurrentPosition(i);
                }
            }
        }
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
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    /**
     * 播放队列不是当前队列时将该音乐列表加入到队列
     */
    private void loadMusicToQueue() {
        EventBus.getDefault().post(new EventMusicData(AppConstant.LOCAL_MUSIC, AppConstant.PROVIDE_LOCAL_MUSIC, musicOldList), EventBusTags.switchMusicProvide);
    }

    @Subscriber(tag = EventBusTags.switchMusicProvideSuccess)
    public void switchMusicProvide(String message) {
        onConnected();
        if (isPlayOnPrepare) {
            MusicBean bean = (MusicBean) musicListAdapter.getItem(currentPosition);
            controller.getTransportControls().playFromMediaId(bean.getMetadata().getMediaId(), null);
        }
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
    public RxPermissions getRxPermissions() {
        return mRxPermissions;
    }

    @Override
    public void updateList(List<MediaMetadataCompat> datas) {
        if (datas == null) {
            return;
        }
        musicOldList = datas;
        musicBeanList = MusicUtil.switchMusicToBean(datas, MusicListAdapter.MUSIC_ITEM_1);
        musicListAdapter.setNewData(musicBeanList);
        loadMusicToQueue();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mRxPermissions = null;
    }

    @Override
    protected MediaBrowserCompat.SubscriptionCallback getSubscriptionCallback() {
        return mSubscriptionCallback;
    }

    @Override
    protected MediaControllerCompat.Callback getMediaControllerCallback() {
        return mCallback;
    }

    private final MediaBrowserCompat.SubscriptionCallback mSubscriptionCallback =
            new MediaBrowserCompat.SubscriptionCallback() {
                @Override
                public void onChildrenLoaded(@NonNull String parentId,
                                             @NonNull List<MediaBrowserCompat.MediaItem> children) {
//                    if (!isFirstConnection) {
//                        return;
//                    }
//                    if (!musicBeanList.isEmpty()) {
//                        musicBeanList.clear();
//                    }
//                    for (MediaBrowserCompat.MediaItem item : children) {
//                        MusicBean musicBean = new MusicBean(MusicListAdapter.MUSIC_ITEM_1);
//                        musicBean.setMetadata(item.getDescription());
//                        musicBeanList.add(musicBean);
//                    }
//                    musicListAdapter.setNewData(musicBeanList);
                }

                @Override
                public void onError(@NonNull String id) {

                }
            };

}
