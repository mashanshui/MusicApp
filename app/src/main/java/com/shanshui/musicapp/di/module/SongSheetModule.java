package com.shanshui.musicapp.di.module;

import com.jess.arms.di.scope.FragmentScope;

import dagger.Module;
import dagger.Provides;

import com.shanshui.musicapp.mvp.contract.SongSheetContract;
import com.shanshui.musicapp.mvp.model.SongSheetModel;


@Module
public class SongSheetModule {
    private SongSheetContract.View view;

    /**
     * 构建SongSheetModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public SongSheetModule(SongSheetContract.View view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    SongSheetContract.View provideSongSheetView() {
        return this.view;
    }

    @FragmentScope
    @Provides
    SongSheetContract.Model provideSongSheetModel(SongSheetModel model) {
        return model;
    }
}