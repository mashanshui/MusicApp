package com.shanshui.musicapp.mvp.ui.activity.songsheet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowInsets;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.jaeger.library.StatusBarUtil;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.glide.GlideArms;
import com.jess.arms.utils.ArmsUtils;

import com.shanshui.musicapp.app.EventBusTags;
import com.shanshui.musicapp.app.base.BaseMusicActivity;
import com.shanshui.musicapp.app.utils.MusicUtil;
import com.shanshui.musicapp.di.component.DaggerSongSheetDetailComponent;
import com.shanshui.musicapp.mvp.AppConstant;
import com.shanshui.musicapp.mvp.contract.SongSheetDetailContract;
import com.shanshui.musicapp.mvp.presenter.SongSheetDetailPresenter;

import com.shanshui.musicapp.R;
import com.shanshui.musicapp.mvp.ui.fragment.music.MediaBrowserFragment;


import org.simple.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * @author mashanshui
 * @date 2019-04-18
 * @desc 歌单——》歌单详情
 */
public class SongSheetDetailActivity extends BaseMusicActivity<SongSheetDetailPresenter> implements SongSheetDetailContract.View {
    @BindView(R.id.artist_art)
    ImageView artistArt;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.fab_play)
    FloatingActionButton fabPlay;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.cl_content)
    CoordinatorLayout clContent;
    private MediaBrowserFragment mediaBrowserFragment;
    private int specialId;
    private String specialName;
    private String bannerUrl;
    private int primaryColor;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSongSheetDetailComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_song_sheet_detail; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        StatusBarUtil.setTranslucentForImageView(this, AppConstant.DEFAULT_STATUS_BAR_ALPHA, null);
        specialId = getIntent().getIntExtra("specialId", 0);
        specialName = getIntent().getStringExtra("specialName");
        bannerUrl = getIntent().getStringExtra("bannerUrl");
        initToolbar();
        initBanner();
        llContent.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                return clContent.dispatchApplyWindowInsets(insets);
            }
        });
        mediaBrowserFragment = MediaBrowserFragment.newInstance(MediaBrowserFragment.MUSIC_TYPE_SONG_SHEET, String.valueOf(specialId));
        getSupportFragmentManager().beginTransaction().replace(R.id.container
                ,mediaBrowserFragment ).commit();
    }

    private void initBanner() {
        GlideArms.with(this)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .load(MusicUtil.getImageUrl(bannerUrl, AppConstant.ONLINE_IMG_SIZE_400))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        showBanner(resource);
                    }
                });
    }

    private void showBanner(Bitmap resource) {
        artistArt.setImageBitmap(resource);
        new Palette.Builder(resource).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch swatch = getMostPopulousSwatch(palette);
                if (swatch != null) {
                    int color = swatch.getRgb();
                    collapsingToolbar.setContentScrimColor(color);
                    collapsingToolbar.setStatusBarScrimColor(color);
                    primaryColor = color;
                }
            }
        });
    }

    public static @Nullable
    Palette.Swatch getMostPopulousSwatch(Palette palette) {
        Palette.Swatch mostPopulous = null;
        if (palette != null) {
            for (Palette.Swatch swatch : palette.getSwatches()) {
                if (mostPopulous == null || swatch.getPopulation() > mostPopulous.getPopulation()) {
                    mostPopulous = swatch;
                }
            }
        }
        return mostPopulous;
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        collapsingToolbar.setTitle(specialName);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                killMyself();
            }
        });
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    @OnClick(R.id.fab_play)
    public void onViewClicked() {
        EventBus.getDefault().post("", EventBusTags.playAllMusic);
    }

    @Override
    protected void onMediaControllerConnected() {

    }
}
