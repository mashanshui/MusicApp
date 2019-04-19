package com.shanshui.musicapp.mvp.ui.activity.music;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.shanshui.musicapp.app.base.BaseMusicActivity;
import com.shanshui.musicapp.di.component.DaggerSearchMusicComponent;
import com.shanshui.musicapp.mvp.contract.SearchMusicContract;
import com.shanshui.musicapp.mvp.presenter.SearchMusicPresenter;

import com.shanshui.musicapp.R;


import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * @author mashanshui
 * @date 2019-04-18
 * @desc 音乐搜索页面
 */
public class SearchMusicActivity extends BaseMusicActivity<SearchMusicPresenter> implements SearchMusicContract.View {

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSearchMusicComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_search_music; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

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
    protected void onMediaControllerConnected() {

    }
}
