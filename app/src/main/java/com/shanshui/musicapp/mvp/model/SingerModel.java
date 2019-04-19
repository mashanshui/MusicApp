package com.shanshui.musicapp.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.FragmentScope;

import javax.inject.Inject;

import com.shanshui.musicapp.mvp.contract.SingerContract;
import com.shanshui.musicapp.mvp.model.api.cache.UserCache;
import com.shanshui.musicapp.mvp.model.api.service.UserService;
import com.shanshui.musicapp.mvp.model.bean.SingerBean;
import com.shanshui.musicapp.mvp.model.bean.SingerCategoryBean;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.rx_cache2.EvictProvider;
import io.rx_cache2.Reply;


@FragmentScope
public class SingerModel extends BaseModel implements SingerContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public SingerModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<SingerBean> getSingerList(int page, int length) {
        return Observable.just(mRepositoryManager
                .obtainRetrofitService(UserService.class)
                .getSingerList(page, length))
                .flatMap(new Function<Observable<SingerBean>, ObservableSource<SingerBean>>() {
                    @Override
                    public ObservableSource<SingerBean> apply(Observable<SingerBean> testBeanObservable) {
                        return mRepositoryManager.obtainCacheService(UserCache.class)
                                .getSingerList(testBeanObservable, new EvictProvider(true))
                                .map(new Function<Reply<SingerBean>, SingerBean>() {
                                    @Override
                                    public SingerBean apply(Reply<SingerBean> testBeanReply) throws Exception {
                                        return testBeanReply.getData();
                                    }
                                });
                    }
                });
    }

    @Override
    public Observable<SingerCategoryBean> getSingerCategory() {
        return Observable.just(mRepositoryManager
                .obtainRetrofitService(UserService.class)
                .getSingerCategory())
                .flatMap(new Function<Observable<SingerCategoryBean>, ObservableSource<SingerCategoryBean>>() {
                    @Override
                    public ObservableSource<SingerCategoryBean> apply(Observable<SingerCategoryBean> testBeanObservable) {
                        return mRepositoryManager.obtainCacheService(UserCache.class)
                                .getSingerCategory(testBeanObservable, new EvictProvider(true))
                                .map(new Function<Reply<SingerCategoryBean>, SingerCategoryBean>() {
                                    @Override
                                    public SingerCategoryBean apply(Reply<SingerCategoryBean> testBeanReply) throws Exception {
                                        return testBeanReply.getData();
                                    }
                                });
                    }
                });
    }
}