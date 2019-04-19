package com.shanshui.musicapp.mvp.model;

import android.app.Application;

import com.blankj.utilcode.util.NetworkUtils;
import com.google.gson.Gson;
import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.shanshui.musicapp.mvp.contract.ChartsContract;
import com.shanshui.musicapp.mvp.model.api.Api;
import com.shanshui.musicapp.mvp.model.api.cache.UserCache;
import com.shanshui.musicapp.mvp.model.api.service.UserService;
import com.shanshui.musicapp.mvp.model.bean.ChartsBean;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.rx_cache2.EvictProvider;
import io.rx_cache2.Reply;


@FragmentScope
public class ChartsModel extends BaseModel implements ChartsContract.Model {
    private static final String TAG = "ChartsModel";
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public ChartsModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<ChartsBean> getChartsList() {
        return Observable.just(mRepositoryManager
                .obtainRetrofitService(UserService.class)
                .getMusicCharts())
                .flatMap(new Function<Observable<ChartsBean>, ObservableSource<ChartsBean>>() {
                    @Override
                    public ObservableSource<ChartsBean> apply(Observable<ChartsBean> testBeanObservable) {
                        return mRepositoryManager.obtainCacheService(UserCache.class)
                                .getMusicCharts(testBeanObservable, new EvictProvider(true))
                                .map(new Function<Reply<ChartsBean>, ChartsBean>() {
                                    @Override
                                    public ChartsBean apply(Reply<ChartsBean> testBeanReply) throws Exception {
                                        return testBeanReply.getData();
                                    }
                                });
                    }
                });
    }

}
