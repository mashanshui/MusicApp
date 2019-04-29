package com.shanshui.musicapp.mvp.presenter;

import android.app.Application;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

import javax.inject.Inject;

import com.jess.arms.utils.DataHelper;
import com.shanshui.musicapp.app.utils.MusicUtil;
import com.shanshui.musicapp.app.utils.RxUtils;
import com.shanshui.musicapp.mvp.AppConstant;
import com.shanshui.musicapp.mvp.contract.SearchMusicPreviewContract;
import com.shanshui.musicapp.mvp.model.bean.MusicListBean;
import com.shanshui.musicapp.mvp.model.bean.SearchHotBean;
import com.shanshui.musicapp.mvp.model.entity.Response2;
import com.shanshui.musicapp.mvp.model.entity.ResponseListObject;

import java.util.ArrayList;
import java.util.List;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/19/2019 14:20
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
public class SearchMusicPreviewPresenter extends BasePresenter<SearchMusicPreviewContract.Model, SearchMusicPreviewContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public SearchMusicPreviewPresenter(SearchMusicPreviewContract.Model model, SearchMusicPreviewContract.View rootView) {
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

    public void getSearchHotData() {
        mModel.getSearchHotData()
                .compose(RxUtils.applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<Response2<ResponseListObject<SearchHotBean>>>(mErrorHandler) {
                    @Override
                    public void onNext(Response2<ResponseListObject<SearchHotBean>> responseListObjectResponse2) {
                        mRootView.updateSearchHot(responseListObjectResponse2.getData().getInfo());
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                    }
                });
    }

    public void setSearchHistory(String history) {
        if (TextUtils.isEmpty(history)) {
            return;
        }
        List<String> data = loadSearchHistory();
        if (data.contains(history)) {
            data.remove(history);
        }
        data.add(0, history);
        if (data.size() > 30) {
            keepSearchHistory(data.subList(0, 30));
        } else {
            keepSearchHistory(data);
        }
    }

    private void keepSearchHistory(List<String> subList) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String userItemToString = gson.toJson(subList);
        DataHelper.setStringSF(mRootView.getContext(), AppConstant.PREF_USER_SEARCH_HISTORY, userItemToString);
    }

    public void getSearchHistory() {
        mRootView.updateSearchHistory(loadSearchHistory());
    }

    private List<String> loadSearchHistory() {
        List<String> stringList = new ArrayList<>();
        String userString = DataHelper.getStringSF(mRootView.getContext(), AppConstant.PREF_USER_SEARCH_HISTORY);
        if (!TextUtils.isEmpty(userString)) {
            Gson gson = new Gson();
            try {
                return gson.fromJson(userString, ArrayList.class);
            } catch (Exception e) {

            }
        }
        return stringList;
    }

    public void clearSearchHistory() {
        DataHelper.removeSF(mRootView.getContext(), AppConstant.PREF_USER_SEARCH_HISTORY);
    }
}
