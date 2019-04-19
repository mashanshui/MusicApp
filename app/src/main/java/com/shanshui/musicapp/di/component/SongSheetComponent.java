package com.shanshui.musicapp.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.shanshui.musicapp.di.module.SongSheetModule;

import com.jess.arms.di.scope.FragmentScope;
import com.shanshui.musicapp.mvp.ui.fragment.home.SongSheetFragment;

@FragmentScope
@Component(modules = SongSheetModule.class, dependencies = AppComponent.class)
public interface SongSheetComponent {
    void inject(SongSheetFragment fragment);
}