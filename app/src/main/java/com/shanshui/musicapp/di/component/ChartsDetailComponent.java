package com.shanshui.musicapp.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.shanshui.musicapp.di.module.ChartsDetailModule;

import com.jess.arms.di.scope.ActivityScope;
import com.shanshui.musicapp.mvp.ui.activity.charts.ChartsDetailActivity;

@ActivityScope
@Component(modules = ChartsDetailModule.class, dependencies = AppComponent.class)
public interface ChartsDetailComponent {
    void inject(ChartsDetailActivity activity);
}