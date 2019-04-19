package com.shanshui.musicapp.mvp.presenter;

import android.app.Application;

import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

import javax.inject.Inject;

import com.shanshui.musicapp.app.utils.RxUtils;
import com.shanshui.musicapp.mvp.contract.SingerContract;
import com.shanshui.musicapp.mvp.model.bean.ChartsBean;
import com.shanshui.musicapp.mvp.model.bean.SingerBean;
import com.shanshui.musicapp.mvp.model.bean.SingerCategoryBean;

import java.util.List;


@FragmentScope
public class SingerPresenter extends BasePresenter<SingerContract.Model, SingerContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public SingerPresenter(SingerContract.Model model, SingerContract.View rootView) {
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

    public void getSinger() {
        mModel.getSingerList(mRootView.getPage(), mRootView.getLength())
                .compose(RxUtils.applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<SingerBean>(mErrorHandler) {
                    @Override
                    public void onNext(SingerBean responseListResponse) {
                        List<SingerBean> singerBeanList = responseListResponse.getData().getInfo();
                        mRootView.requestSuccess(singerBeanList);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        mRootView.requestFailure();
                    }
                });
    }

    public void getSingerCategory() {
        mModel.getSingerCategory()
                .compose(RxUtils.applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<SingerCategoryBean>(mErrorHandler) {
                    @Override
                    public void onNext(SingerCategoryBean singerCategoryBean) {
                        List<SingerCategoryBean> singerCategoryBeanList = singerCategoryBean.getInfo();
                        mRootView.updateSingerCategory(singerCategoryBeanList);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                    }
                });
    }
}
