package com.shanshui.musicapp.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.shanshui.musicapp.di.module.ForgetPasswordModule;

import com.jess.arms.di.scope.ActivityScope;
import com.shanshui.musicapp.mvp.ui.activity.user.ForgetPasswordActivity;

@ActivityScope
@Component(modules = ForgetPasswordModule.class, dependencies = AppComponent.class)
public interface ForgetPasswordComponent {
    void inject(ForgetPasswordActivity activity);
}