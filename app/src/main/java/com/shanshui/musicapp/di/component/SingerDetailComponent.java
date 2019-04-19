package com.shanshui.musicapp.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.shanshui.musicapp.di.module.SingerDetailModule;
import com.shanshui.musicapp.mvp.contract.SingerDetailContract;

import com.jess.arms.di.scope.ActivityScope;
import com.shanshui.musicapp.mvp.ui.activity.singer.SingerDetailActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/18/2019 16:34
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = SingerDetailModule.class, dependencies = AppComponent.class)
public interface SingerDetailComponent {
    void inject(SingerDetailActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        SingerDetailComponent.Builder view(SingerDetailContract.View view);

        SingerDetailComponent.Builder appComponent(AppComponent appComponent);

        SingerDetailComponent build();
    }
}