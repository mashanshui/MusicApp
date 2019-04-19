package com.shanshui.musicapp.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.shanshui.musicapp.di.module.ChartsModule;

import com.jess.arms.di.scope.FragmentScope;
import com.shanshui.musicapp.mvp.ui.fragment.home.ChartsFragment;

@FragmentScope
@Component(modules = ChartsModule.class, dependencies = AppComponent.class)
public interface ChartsComponent {
    void inject(ChartsFragment fragment);
}