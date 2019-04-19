package com.shanshui.musicapp.mvp.presenter;

import android.app.Application;

import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

import javax.inject.Inject;

import com.shanshui.musicapp.app.utils.MusicUtil;
import com.shanshui.musicapp.app.utils.RxUtils;
import com.shanshui.musicapp.mvp.contract.MediaBrowserContract;
import com.shanshui.musicapp.mvp.model.bean.MusicListBean;
import com.shanshui.musicapp.mvp.model.entity.Response2;
import com.shanshui.musicapp.mvp.model.entity.ResponseListObject;

import java.util.List;

import static com.shanshui.musicapp.mvp.ui.fragment.music.MediaBrowserFragment.MUSIC_TYPE_CHARTS;
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
        }
    }

    private void loadMusicTypeSinger(String musicMessage) {
        mModel.getSingerMusicList(musicMessage, mRootView.getPage(), mRootView.getLength())
                .compose(RxUtils.applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<Response2<ResponseListObject<MusicListBean>>>(mErrorHandler) {
                    @Override
                    public void onNext(Response2<ResponseListObject<MusicListBean>> data) {
                        List<MusicListBean> musicListBeanList = data.getData().getInfo();
                        mRootView.handleSuccess(MusicUtil.switchNetMusicToMetadata(musicListBeanList));
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
                        mRootView.handleSuccess(MusicUtil.switchNetMusicToMetadata(musicListBeanList));
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
                        mRootView.handleSuccess(MusicUtil.switchNetMusicToMetadata(musicListBeanList));
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        mRootView.handleFailure();
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
