package com.shanshui.musicapp.app.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.integration.cache.CacheType;
import com.jess.arms.integration.lifecycle.FragmentLifecycleable;
import com.jess.arms.mvp.IPresenter;
import com.jess.arms.utils.ArmsUtils;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.shanshui.musicapp.mvp.adapter.MusicListAdapter;
import com.shanshui.musicapp.mvp.model.bean.MusicBean;
import com.shanshui.musicapp.mvp.music.MediaBrowserProvider;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * @author mashanshui
 * @date 2019-01-09
 * @desc TODO
 */
public abstract class BaseMusicLazyFragment<P extends IPresenter> extends LazyFragment implements FragmentLifecycleable {
    protected final String TAG = this.getClass().getSimpleName();
    private final BehaviorSubject<FragmentEvent> mLifecycleSubject = BehaviorSubject.create();
    private Cache<String, Object> mCache;
    QMUITipDialog loadingDialog;
    private String mMediaId;
    protected MediaBrowserProvider mediaBrowserProvider;
    private Unbinder mUnbinder;

    @Inject
    @Nullable
    protected P mPresenter;//如果当前页面逻辑简单, Presenter 可以为 null

    @NonNull
    public synchronized Cache<String, Object> provideCache() {

        if (mCache == null) {
            mCache = ArmsUtils.obtainAppComponentFromContext(getActivity()).cacheFactory().build(CacheType.FRAGMENT_CACHE);
        }
        return mCache;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (useEventBus())//如果要使用eventbus请将此方法返回true
            EventBus.getDefault().register(this);//注册到事件主线
        setupFragmentComponent(ArmsUtils.obtainAppComponentFromContext(getActivity()));
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
            //绑定到butterknife
            mUnbinder = ButterKnife.bind(this, getRealRootView());
        }
        initData(savedInstanceState);
    }

    protected abstract void initData(Bundle savedInstanceState);

    protected abstract int getLayoutId();

    protected abstract void setupFragmentComponent(AppComponent appComponent);

    protected abstract void setData(Object data);

    public void baseShowLoading() {
        if (loadingDialog == null) {
            loadingDialog = new QMUITipDialog.Builder(context)
                    .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                    .create();
        }
        loadingDialog.show();
    }

    public void baseHideLoading() {
        if (loadingDialog == null) {
            return;
        }
        loadingDialog.dismiss();
    }

    @NonNull
    @Override
    public final Subject<FragmentEvent> provideLifecycleSubject() {
        return mLifecycleSubject;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null && mUnbinder != Unbinder.EMPTY)
            mUnbinder.unbind();
        this.mUnbinder = null;
        if (mPresenter != null)
            mPresenter.onDestroy();//释放资源
        this.mPresenter = null;
        if (useEventBus())//如果要使用eventbus请将此方法返回true
            EventBus.getDefault().unregister(this);//注册到事件主线
    }


    /**
     * 是否使用eventBus,默认为使用(true)，
     *
     * @return
     */
    public boolean useEventBus() {
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        MediaBrowserCompat mediaBrowser = mediaBrowserProvider.getMediaBrowser();
        if (mediaBrowser.isConnected()) {
            onConnected();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        MediaBrowserCompat mediaBrowser = mediaBrowserProvider.getMediaBrowser();
        if (mediaBrowser != null && mediaBrowser.isConnected() && mMediaId != null) {
            mediaBrowser.unsubscribe(mMediaId);
        }
        MediaControllerCompat controller = MediaControllerCompat.getMediaController(getActivity());
        if (controller != null) {
            controller.unregisterCallback(mMediaControllerCallback);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MediaBrowserProvider) {
            mediaBrowserProvider = (MediaBrowserProvider) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mediaBrowserProvider = null;
    }

    // 当MediaBrowser已经连接时调用
    // 这个方法可在 fragment.onStart() 或 已知Activity在onStart后完成了连接的情况下调用
    public void onConnected() {
        if (isDetached()) {
            return;
        }
        if (mMediaId == null) {
            mMediaId = mediaBrowserProvider.getMediaBrowser().getRoot();
        }

        mediaBrowserProvider.getMediaBrowser().unsubscribe(mMediaId);

        mediaBrowserProvider.getMediaBrowser().subscribe(mMediaId, getSubscriptionCallback() == null ? mSubscriptionCallback : getSubscriptionCallback());

        // Add MediaController callback so we can redraw the list when metadata changes:
        MediaControllerCompat controller = MediaControllerCompat.getMediaController(getActivity());
        if (controller != null) {
            controller.registerCallback(mMediaControllerCallback);
            onMediaControllerConnected();
        }
    }

    protected abstract MediaBrowserCompat.SubscriptionCallback getSubscriptionCallback();

    protected abstract void onMediaControllerConnected();

    private final MediaControllerCompat.Callback mMediaControllerCallback =
            new MediaControllerCompat.Callback() {
                @Override
                public void onMetadataChanged(MediaMetadataCompat metadata) {
                    super.onMetadataChanged(metadata);
                    if (metadata == null) {
                        return;
                    }
                }//播放的媒体数据发生变化时的回调

                @Override
                public void onPlaybackStateChanged(@NonNull PlaybackStateCompat state) {
                    super.onPlaybackStateChanged(state);

                }//播放状态发生改变时的回调
            };

    /**
     * 向媒体流量服务(MediaBrowserService)发起媒体浏览请求的回调接口
     */
    private final MediaBrowserCompat.SubscriptionCallback mSubscriptionCallback =
            new MediaBrowserCompat.SubscriptionCallback() {
                @Override
                public void onChildrenLoaded(@NonNull String parentId,
                                             @NonNull List<MediaBrowserCompat.MediaItem> children) {
//                    if (!musicBeans.isEmpty()) {
//                        musicBeans.clear();
//                    }
//                    for (MediaBrowserCompat.MediaItem item : children) {
//                        MusicBean musicBean = new MusicBean(MusicListAdapter.MUSIC_ITEM_1);
//                        musicBean.setMetadata(item.getDescription());
//                        musicBeans.add(musicBean);
//                    }
//                    getMusicAdapter().setNewData(musicBeans);
                }

                @Override
                public void onError(@NonNull String id) {

                }
            };

}
