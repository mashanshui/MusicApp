package com.shanshui.musicapp.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.shanshui.musicapp.mvp.contract.FullScreenPlayerContract;
import com.shanshui.musicapp.mvp.model.FullScreenPlayerModel;


@Module
public class FullScreenPlayerModule {
    private FullScreenPlayerContract.View view;

    /**
     * 构建FullScreenPlayerModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public FullScreenPlayerModule(FullScreenPlayerContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    FullScreenPlayerContract.View provideFullScreenPlayerView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    FullScreenPlayerContract.Model provideFullScreenPlayerModel(FullScreenPlayerModel model) {
        return model;
    }
}