package com.shanshui.musicapp.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.shanshui.musicapp.di.module.SongSheetDetailModule;
import com.shanshui.musicapp.mvp.contract.SongSheetDetailContract;

import com.jess.arms.di.scope.ActivityScope;
import com.shanshui.musicapp.mvp.ui.activity.songsheet.SongSheetDetailActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/18/2019 14:52
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = SongSheetDetailModule.class, dependencies = AppComponent.class)
public interface SongSheetDetailComponent {
    void inject(SongSheetDetailActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        SongSheetDetailComponent.Builder view(SongSheetDetailContract.View view);

        SongSheetDetailComponent.Builder appComponent(AppComponent appComponent);

        SongSheetDetailComponent build();
    }
}