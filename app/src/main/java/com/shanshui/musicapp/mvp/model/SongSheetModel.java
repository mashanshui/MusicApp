package com.shanshui.musicapp.mvp.model;

import android.app.Application;

import com.blankj.utilcode.util.NetworkUtils;
import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.FragmentScope;

import javax.inject.Inject;

import com.shanshui.musicapp.mvp.contract.SongSheetContract;
import com.shanshui.musicapp.mvp.model.api.Api;
import com.shanshui.musicapp.mvp.model.api.cache.UserCache;
import com.shanshui.musicapp.mvp.model.api.service.UserService;
import com.shanshui.musicapp.mvp.model.bean.SongSheetBean;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.rx_cache2.EvictProvider;
import io.rx_cache2.Reply;


@FragmentScope
public class SongSheetModel extends BaseModel implements SongSheetContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public SongSheetModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<SongSheetBean> getSongSheetList(int page) {
        return Observable.just(mRepositoryManager
                .obtainRetrofitService(UserService.class)
                .getSongSheet(page))
                .flatMap(new Function<Observable<SongSheetBean>, ObservableSource<SongSheetBean>>() {
                    @Override
                    public ObservableSource<SongSheetBean> apply(Observable<SongSheetBean> songSheetBeanObservable) throws Exception {
                        return mRepositoryManager.obtainCacheService(UserCache.class)
                                .getSongSheet(songSheetBeanObservable, new EvictProvider(true))
                                .map(new Function<Reply<SongSheetBean>, SongSheetBean>() {
                                    @Override
                                    public SongSheetBean apply(Reply<SongSheetBean> songSheetBeanReply) throws Exception {
                                        return songSheetBeanReply.getData();
                                    }
                                });
                    }
                });
    }
}