package com.shanshui.musicapp.di.module;

import com.jess.arms.di.scope.FragmentScope;

import dagger.Module;
import dagger.Provides;

import com.shanshui.musicapp.mvp.contract.SingerContract;
import com.shanshui.musicapp.mvp.model.SingerModel;


@Module
public class SingerModule {
    private SingerContract.View view;

    /**
     * 构建SingerModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public SingerModule(SingerContract.View view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    SingerContract.View provideSingerView() {
        return this.view;
    }

    @FragmentScope
    @Provides
    SingerContract.Model provideSingerModel(SingerModel model) {
        return model;
    }
}