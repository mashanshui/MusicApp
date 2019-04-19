package com.shanshui.musicapp.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.shanshui.musicapp.di.component.DaggerSplashComponent;
import com.shanshui.musicapp.di.module.SplashModule;
import com.shanshui.musicapp.mvp.contract.SplashContract;
import com.shanshui.musicapp.mvp.presenter.SplashPresenter;

import com.shanshui.musicapp.R;
import com.tbruyelle.rxpermissions2.RxPermissions;


import javax.inject.Inject;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class SplashActivity extends BaseActivity<SplashPresenter> implements SplashContract.View {
    @Inject
    RxPermissions mRxPermissions;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSplashComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .splashModule(new SplashModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_splash; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mPresenter.requestFilePermission();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    @Override
    public RxPermissions getRxPermissions() {
        return mRxPermissions;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void goMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
