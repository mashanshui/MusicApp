package com.shanshui.musicapp.mvp.presenter;

import android.app.Application;

import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import io.reactivex.functions.Consumer;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

import javax.inject.Inject;

import com.shanshui.musicapp.app.utils.RxUtils;
import com.shanshui.musicapp.mvp.contract.SongSheetContract;
import com.shanshui.musicapp.mvp.model.bean.ChartsBean;
import com.shanshui.musicapp.mvp.model.bean.SongSheetBean;

import java.util.List;


@FragmentScope
public class SongSheetPresenter extends BasePresenter<SongSheetContract.Model, SongSheetContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public SongSheetPresenter(SongSheetContract.Model model, SongSheetContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    public void loadData() {
        mModel.getSongSheetList(mRootView.getPage())
                .compose(RxUtils.applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<SongSheetBean>(mErrorHandler) {
                    @Override
                    public void onNext(SongSheetBean songSheetBean) {
                        List<SongSheetBean> songSheetBeans = songSheetBean.getRank().getList().getInfo();
                        mRootView.requestSuccess(songSheetBeans);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        mRootView.requestFailure();
                    }

                });
    }
}
