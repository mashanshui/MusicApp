package com.shanshui.musicapp.mvp.presenter;

import android.Manifest;
import android.app.Application;
import android.text.TextUtils;

import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import javax.inject.Inject;

import com.jess.arms.utils.PermissionUtil;
import com.shanshui.musicapp.mvp.contract.SplashContract;

import java.util.List;


@ActivityScope
public class SplashPresenter extends BasePresenter<SplashContract.Model, SplashContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public SplashPresenter(SplashContract.Model model, SplashContract.View rootView) {
        super(model, rootView);
    }

    public void requestFilePermission() {
        PermissionUtil.requestPermission(
                new PermissionUtil.RequestPermission() {
                    @Override
                    public void onRequestPermissionSuccess() {
                        mRootView.goMainActivity();
                    }

                    @Override
                    public void onRequestPermissionFailure(List<String> permissions) {
                        for (String permission : permissions) {
                            if (TextUtils.equals(permission, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                mRootView.showMessage("拒绝权限无法查找本地音乐");
                            } else if (TextUtils.equals(permission, Manifest.permission.READ_PHONE_STATE)) {
                                mRootView.showMessage("拒绝权限无法听VIP音乐");
                            }
                        }
                        mRootView.goMainActivity();
                    }

                    @Override
                    public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {
                        for (String permission : permissions) {
                            if (TextUtils.equals(permission, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                mRootView.showMessage("拒绝权限无法查找本地音乐");
                            } else if (TextUtils.equals(permission, Manifest.permission.READ_PHONE_STATE)) {
                                mRootView.showMessage("拒绝权限无法听VIP音乐");
                            }
                        }
                    }
                }, mRootView.getRxPermissions(), mErrorHandler
                , Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.READ_PHONE_STATE);
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
