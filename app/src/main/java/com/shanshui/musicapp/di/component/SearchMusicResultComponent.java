package com.shanshui.musicapp.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.shanshui.musicapp.di.module.SearchMusicResultModule;
import com.shanshui.musicapp.mvp.contract.SearchMusicResultContract;

import com.jess.arms.di.scope.FragmentScope;
import com.shanshui.musicapp.mvp.ui.fragment.music.SearchMusicResultFragment;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/22/2019 10:27
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
@Component(modules = SearchMusicResultModule.class, dependencies = AppComponent.class)
public interface SearchMusicResultComponent {
    void inject(SearchMusicResultFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        SearchMusicResultComponent.Builder view(SearchMusicResultContract.View view);

        SearchMusicResultComponent.Builder appComponent(AppComponent appComponent);

        SearchMusicResultComponent build();
    }
}