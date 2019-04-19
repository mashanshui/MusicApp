package com.shanshui.musicapp.di.module;

import android.support.v4.app.FragmentActivity;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.di.scope.FragmentScope;

import dagger.Module;
import dagger.Provides;

import com.shanshui.musicapp.mvp.contract.MineContract;
import com.shanshui.musicapp.mvp.model.MineModel;
import com.tbruyelle.rxpermissions2.RxPermissions;


@Module
public class MineModule {
    private MineContract.View view;

    /**
     * 构建MineModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public MineModule(MineContract.View view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    RxPermissions provideRxPermissions() {
        return new RxPermissions((FragmentActivity) view.getActivity());
    }

    @FragmentScope
    @Provides
    MineContract.View provideMineView() {
        return this.view;
    }

    @FragmentScope
    @Provides
    MineContract.Model provideMineModel(MineModel model) {
        return model;
    }
}