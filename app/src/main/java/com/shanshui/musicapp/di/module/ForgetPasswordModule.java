package com.shanshui.musicapp.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.shanshui.musicapp.mvp.contract.ForgetPasswordContract;
import com.shanshui.musicapp.mvp.model.ForgetPasswordModel;


@Module
public class ForgetPasswordModule {
    private ForgetPasswordContract.View view;

    /**
     * 构建ForgetPasswordModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ForgetPasswordModule(ForgetPasswordContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ForgetPasswordContract.View provideForgetPasswordView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ForgetPasswordContract.Model provideForgetPasswordModel(ForgetPasswordModel model) {
        return model;
    }
}