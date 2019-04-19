package com.shanshui.musicapp.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.shanshui.musicapp.di.module.SingerModule;

import com.jess.arms.di.scope.FragmentScope;
import com.shanshui.musicapp.mvp.ui.fragment.home.SingerFragment;

@FragmentScope
@Component(modules = SingerModule.class, dependencies = AppComponent.class)
public interface SingerComponent {
    void inject(SingerFragment fragment);
}