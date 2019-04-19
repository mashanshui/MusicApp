package com.shanshui.musicapp.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.shanshui.musicapp.di.module.SearchMusicModule;
import com.shanshui.musicapp.mvp.contract.SearchMusicContract;

import com.jess.arms.di.scope.ActivityScope;
import com.shanshui.musicapp.mvp.ui.activity.music.SearchMusicActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/18/2019 17:32
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = SearchMusicModule.class, dependencies = AppComponent.class)
public interface SearchMusicComponent {
    void inject(SearchMusicActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        SearchMusicComponent.Builder view(SearchMusicContract.View view);

        SearchMusicComponent.Builder appComponent(AppComponent appComponent);

        SearchMusicComponent build();
    }
}