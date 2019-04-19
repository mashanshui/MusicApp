package com.shanshui.musicapp.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.shanshui.musicapp.di.module.RegisterModule;

import com.jess.arms.di.scope.ActivityScope;
import com.shanshui.musicapp.mvp.ui.activity.user.RegisterActivity;

@ActivityScope
@Component(modules = RegisterModule.class, dependencies = AppComponent.class)
public interface RegisterComponent {
    void inject(RegisterActivity activity);
}