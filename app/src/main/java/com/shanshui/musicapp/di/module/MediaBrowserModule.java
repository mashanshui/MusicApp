package com.shanshui.musicapp.di.module;

import com.jess.arms.di.scope.FragmentScope;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import com.shanshui.musicapp.mvp.contract.MediaBrowserContract;
import com.shanshui.musicapp.mvp.model.MediaBrowserModel;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 01/22/2019 22:14
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Module
public abstract class MediaBrowserModule {

    @Binds
    abstract MediaBrowserContract.Model bindMediaBrowserModel(MediaBrowserModel model);
}