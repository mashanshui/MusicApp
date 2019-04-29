package com.shanshui.musicapp.mvp.presenter;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.jess.arms.base.BaseApplication;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

import javax.inject.Inject;

import com.shanshui.musicapp.app.utils.MusicUtil;
import com.shanshui.musicapp.app.utils.RxUtils;
import com.shanshui.musicapp.mvp.adapter.MusicListAdapter;
import com.shanshui.musicapp.mvp.contract.MediaBrowserContract;
import com.shanshui.musicapp.mvp.model.api.service.UserService;
import com.shanshui.musicapp.mvp.model.bean.MusicListBean;
import com.shanshui.musicapp.mvp.model.bean.MusicSourceInfoBean;
import com.shanshui.musicapp.mvp.model.entity.Response2;
import com.shanshui.musicapp.mvp.model.entity.ResponseListObject;

import java.util.List;

import static com.shanshui.musicapp.mvp.ui.fragment.music.MediaBrowserFragment.MUSIC_TYPE_CHARTS;
import static com.shanshui.musicapp.mvp.ui.fragment.music.MediaBrowserFragment.MUSIC_TYPE_SEARCH;
import static com.shanshui.musicapp.mvp.ui.fragment.music.MediaBrowserFragment.MUSIC_TYPE_SINGER;
import static com.shanshui.musicapp.mvp.ui.fragment.music.MediaBrowserFragment.MUSIC_TYPE_SONG_SHEET;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 01/22/2019 22:14
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
public class MediaBrowserPresenter extends BasePresenter<MediaBrowserContract.Model, MediaBrowserContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public MediaBrowserPresenter(MediaBrowserContract.Model model, MediaBrowserContract.View rootView) {
        super(model, rootView);
    }

    public void loadData(int musicType, String musicMessage) {
        if (musicType == MUSIC_TYPE_CHARTS) {
            loadMusicTypeCharts(musicMessage);
        } else if (musicType == MUSIC_TYPE_SONG_SHEET) {
            loadMusicTypeSongSheet(musicMessage);
        } else if (musicType == MUSIC_TYPE_SINGER) {
            loadMusicTypeSinger(musicMessage);
        } else if (musicType == MUSIC_TYPE_SEARCH) {
            loadMusicTypeSearch(musicMessage);
        }
    }

    private void loadMusicTypeSearch(String musicMessage) {
        mModel.getSearchMusicList(musicMessage, mRootView.getPage(), mRootView.getLength())
                .compose(RxUtils.applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<Response2<ResponseListObject<MusicListBean>>>(mErrorHandler) {
                    @Override
                    public void onNext(Response2<ResponseListObject<MusicListBean>> data) {
                        List<MusicListBean> musicListBeanList = data.getData().getInfo();
                        mRootView.handleSuccess(MusicUtil.switchNetMusicToMetadata(musicListBeanList), MusicListAdapter.MUSIC_ITEM_0);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        mRootView.handleFailure();
                    }
                });
    }

    private void loadMusicTypeSinger(String musicMessage) {
        mModel.getSingerMusicList(musicMessage, mRootView.getPage(), mRootView.getLength())
                .compose(RxUtils.applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<Response2<ResponseListObject<MusicListBean>>>(mErrorHandler) {
                    @Override
                    public void onNext(Response2<ResponseListObject<MusicListBean>> data) {
                        List<MusicListBean> musicListBeanList = data.getData().getInfo();
                        mRootView.handleSuccess(MusicUtil.switchNetMusicToMetadata(musicListBeanList), MusicListAdapter.MUSIC_ITEM_1);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        mRootView.handleFailure();
                    }
                });
    }

    private void loadMusicTypeSongSheet(String musicMessage) {
        mModel.getSongSheetMusicList(musicMessage, mRootView.getPage(), mRootView.getLength())
                .compose(RxUtils.applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<Response2<ResponseListObject<MusicListBean>>>(mErrorHandler) {
                    @Override
                    public void onNext(Response2<ResponseListObject<MusicListBean>> data) {
                        List<MusicListBean> musicListBeanList = data.getData().getInfo();
                        mRootView.handleSuccess(MusicUtil.switchNetMusicToMetadata(musicListBeanList), MusicListAdapter.MUSIC_ITEM_1);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        mRootView.handleFailure();
                    }
                });
    }

    private void loadMusicTypeCharts(String musicMessage) {
        mModel.getChartsMusicList(musicMessage, mRootView.getPage(), mRootView.getLength())
                .compose(RxUtils.applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<Response2<ResponseListObject<MusicListBean>>>(mErrorHandler) {
                    @Override
                    public void onNext(Response2<ResponseListObject<MusicListBean>> data) {
                        List<MusicListBean> musicListBeanList = data.getData().getInfo();
                        mRootView.handleSuccess(MusicUtil.switchNetMusicToMetadata(musicListBeanList), MusicListAdapter.MUSIC_ITEM_1);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        mRootView.handleFailure();
                    }
                });
    }

    /**
     * @param source 获取音乐详情（主要使用图片信息）
     */
    public void getMusicInfo(String source) {
        mModel.getMusicInfo(source)
                .compose(RxUtils.applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<MusicSourceInfoBean>(mErrorHandler) {
                    @Override
                    public void onNext(MusicSourceInfoBean musicSourceInfoBean) {
                        mRootView.updateMusicInfo(musicSourceInfoBean);
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }
}
