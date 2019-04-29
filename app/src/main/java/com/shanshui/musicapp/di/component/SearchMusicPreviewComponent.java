package com.shanshui.musicapp.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.shanshui.musicapp.di.module.SearchMusicPreviewModule;
import com.shanshui.musicapp.mvp.contract.SearchMusicPreviewContract;

import com.jess.arms.di.scope.FragmentScope;
import com.shanshui.musicapp.mvp.ui.fragment.music.SearchMusicPreviewFragment;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/19/2019 14:20
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
@Component(modules = SearchMusicPreviewModule.class, dependencies = AppComponent.class)
public interface SearchMusicPreviewComponent {
    void inject(SearchMusicPreviewFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        SearchMusicPreviewComponent.Builder view(SearchMusicPreviewContract.View view);

        SearchMusicPreviewComponent.Builder appComponent(AppComponent appComponent);

        SearchMusicPreviewComponent build();
    }
}