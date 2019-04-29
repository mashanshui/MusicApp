package com.shanshui.musicapp.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.shanshui.musicapp.di.module.SearchMusicInputModule;
import com.shanshui.musicapp.mvp.contract.SearchMusicInputContract;

import com.jess.arms.di.scope.FragmentScope;
import com.shanshui.musicapp.mvp.ui.fragment.music.SearchMusicInputFragment;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/22/2019 09:27
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
@Component(modules = SearchMusicInputModule.class, dependencies = AppComponent.class)
public interface SearchMusicInputComponent {
    void inject(SearchMusicInputFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        SearchMusicInputComponent.Builder view(SearchMusicInputContract.View view);

        SearchMusicInputComponent.Builder appComponent(AppComponent appComponent);

        SearchMusicInputComponent build();
    }
}