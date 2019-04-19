package com.shanshui.musicapp.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.shanshui.musicapp.di.module.FullScreenPlayerModule;

import com.jess.arms.di.scope.ActivityScope;
import com.shanshui.musicapp.mvp.ui.activity.music.FullScreenPlayerActivity;

@ActivityScope
@Component(modules = FullScreenPlayerModule.class, dependencies = AppComponent.class)
public interface FullScreenPlayerComponent {
    void inject(FullScreenPlayerActivity activity);
}