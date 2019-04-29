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
import com.shanshui.musicapp.mvp.contract.SearchMusicInputContract;
import com.shanshui.musicapp.mvp.model.bean.SearchHotBean;
import com.shanshui.musicapp.mvp.model.bean.SearchTipBean;
import com.shanshui.musicapp.mvp.model.entity.Response2;
import com.shanshui.musicapp.mvp.model.entity.ResponseListObject;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/22/2019 09:27
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
public class SearchMusicInputPresenter extends BasePresenter<SearchMusicInputContract.Model, SearchMusicInputContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public SearchMusicInputPresenter(SearchMusicInputContract.Model model, SearchMusicInputContract.View rootView) {
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

    public void getSearchTip(String keyWord) {
        mModel.getSearchTip(keyWord)
                .compose(RxUtils.applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<ResponseListObject<SearchTipBean>>(mErrorHandler) {
                    @Override
                    public void onNext(ResponseListObject<SearchTipBean> responseListObjectResponse2) {
                        mRootView.updateSearchTip(responseListObjectResponse2.getInfo());
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                    }
                });
    }
}
