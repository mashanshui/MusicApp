package com.shanshui.musicapp.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.integration.RepositoryManager;
import com.jess.arms.integration.RepositoryManager_Factory;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.FragmentScope;

import javax.inject.Inject;

import com.shanshui.musicapp.mvp.contract.MediaBrowserContract;
import com.shanshui.musicapp.mvp.model.api.service.UserService;
import com.shanshui.musicapp.mvp.model.bean.MusicListBean;
import com.shanshui.musicapp.mvp.model.bean.MusicSourceInfoBean;
import com.shanshui.musicapp.mvp.model.entity.Response2;
import com.shanshui.musicapp.mvp.model.entity.ResponseListObject;

import io.reactivex.Observable;


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
public class MediaBrowserModel extends BaseModel implements MediaBrowserContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public MediaBrowserModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<Response2<ResponseListObject<MusicListBean>>> getChartsMusicList(String rank, int page, int length) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
                .getMusicChartsDetail(rank, page, length);
    }

    @Override
    public Observable<Response2<ResponseListObject<MusicListBean>>> getSongSheetMusicList(String specialId, int page, int length) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
                .getSongSheetDetail(specialId, page, length);
    }

    @Override
    public Observable<Response2<ResponseListObject<MusicListBean>>> getSingerMusicList(String singerId, int page, int length) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
                .getSingerDetail(singerId, page, length);
    }

    @Override
    public Observable<Response2<ResponseListObject<MusicListBean>>> getSearchMusicList(String keyword, int page, int length) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
                .getSearchMusic(keyword, page, length);
    }

    @Override
    public Observable<MusicSourceInfoBean> getMusicInfo(String source) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
                .getMusicInfo(source);
    }
}