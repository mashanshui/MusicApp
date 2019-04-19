package com.shanshui.musicapp.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.shanshui.musicapp.mvp.contract.ChartsDetailContract;
import com.shanshui.musicapp.mvp.model.ChartsDetailModel;


@Module
public class ChartsDetailModule {
    private ChartsDetailContract.View view;

    /**
     * 构建ChartsDetailModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ChartsDetailModule(ChartsDetailContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ChartsDetailContract.View provideChartsDetailView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ChartsDetailContract.Model provideChartsDetailModel(ChartsDetailModel model) {
        return model;
    }
}