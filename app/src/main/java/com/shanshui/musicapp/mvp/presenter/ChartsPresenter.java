package com.shanshui.musicapp.mvp.presenter;

import android.app.Application;

import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.shanshui.musicapp.app.utils.IToast;
import com.shanshui.musicapp.app.utils.RxUtils;
import com.shanshui.musicapp.mvp.contract.ChartsContract;
import com.shanshui.musicapp.mvp.model.bean.ChartsBean;
import com.shanshui.musicapp.mvp.model.bean.ChartsOutBean;

import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;


@FragmentScope
public class ChartsPresenter extends BasePresenter<ChartsContract.Model, ChartsContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public ChartsPresenter(ChartsContract.Model model, ChartsContract.View rootView) {
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
        mModel.getChartsList()
                .compose(RxUtils.applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<ChartsBean>(mErrorHandler) {
                    @Override
                    public void onNext(ChartsBean responseListResponse) {
                        List<ChartsOutBean> chartsOutBeans = responseListResponse.getData().getInfo();
                        removeOtherData(chartsOutBeans);
                        mRootView.requestSuccess(chartsOutBeans);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        mRootView.requestFailure();
                    }
                });
    }

    private void removeOtherData(List<ChartsOutBean> chartsOutBeans) {
        Iterator<ChartsOutBean> it = chartsOutBeans.iterator();
        while (it.hasNext()) {
            ChartsOutBean bean = it.next();
            if (bean.getRanktype() == 0) {
                it.remove();
            }
        }
    }

}
