package com.shanshui.musicapp.di.module;

import com.jess.arms.di.scope.FragmentScope;

import dagger.Module;
import dagger.Provides;

import com.shanshui.musicapp.mvp.contract.ChartsContract;
import com.shanshui.musicapp.mvp.model.ChartsModel;


@Module
public class ChartsModule {
    private ChartsContract.View view;

    /**
     * 构建ChartsModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ChartsModule(ChartsContract.View view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    ChartsContract.View provideChartsView() {
        return this.view;
    }

    @FragmentScope
    @Provides
    ChartsContract.Model provideChartsModel(ChartsModel model) {
        return model;
    }
}