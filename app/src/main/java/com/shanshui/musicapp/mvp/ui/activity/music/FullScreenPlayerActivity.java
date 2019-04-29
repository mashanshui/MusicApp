package com.shanshui.musicapp.mvp.ui.activity.music;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jaeger.library.StatusBarUtil;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.BaseApplication;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.glide.GlideArms;
import com.jess.arms.utils.ArmsUtils;
import com.shanshui.musicapp.R;
import com.shanshui.musicapp.app.utils.MusicUtil;
import com.shanshui.musicapp.di.component.DaggerFullScreenPlayerComponent;
import com.shanshui.musicapp.di.module.FullScreenPlayerModule;
import com.shanshui.musicapp.mvp.AppConstant;
import com.shanshui.musicapp.mvp.adapter.ViewPageViewAdapter;
import com.shanshui.musicapp.mvp.contract.FullScreenPlayerContract;
import com.shanshui.musicapp.mvp.model.api.service.UserService;
import com.shanshui.musicapp.mvp.model.bean.MusicSourceInfoBean;
import com.shanshui.musicapp.mvp.music.LogHelper;
import com.shanshui.musicapp.mvp.music.MusicProviderSource;
import com.shanshui.musicapp.mvp.music.MusicService;
import com.shanshui.musicapp.mvp.presenter.FullScreenPlayerPresenter;
import com.shanshui.musicapp.mvp.ui.widget.MultiTouchViewPager;
import com.shanshui.musicapp.mvp.ui.widget.PlayPauseView;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * @author mashanshui
 * @date 2019-01-16
 * @desc 音乐全屏播放
 */
public class FullScreenPlayerActivity extends BaseActivity<FullScreenPlayerPresenter> implements FullScreenPlayerContract.View
        , SeekBar.OnSeekBarChangeListener {
    private static final String TAG = LogHelper.makeLogTag(FullScreenPlayerActivity.class);
    @BindView(R.id.playingBgIv)
    ImageView playingBgIv;
    @BindView(R.id.backIv)
    ImageView backIv;
    @BindView(R.id.titleIv)
    TextView titleIv;
    @BindView(R.id.subTitleTv)
    TextView subTitleTv;
    @BindView(R.id.operateSongIv)
    ImageView operateSongIv;
    @BindView(R.id.searchLyricIv)
    ImageView searchLyricIv;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.viewPager)
    MultiTouchViewPager viewPager;
    @BindView(R.id.progressTv)
    TextView progressTv;
    @BindView(R.id.progressSb)
    SeekBar progressSb;
    @BindView(R.id.durationTv)
    TextView durationTv;
    @BindView(R.id.playModeIv)
    ImageView playModeIv;
    @BindView(R.id.prevPlayIv)
    MaterialIconView prevPlayIv;
    @BindView(R.id.playPauseIv)
    PlayPauseView playPauseIv;
    @BindView(R.id.loadingPb)
    ProgressBar loadingPb;
    @BindView(R.id.nextPlayIv)
    MaterialIconView nextPlayIv;
    @BindView(R.id.playQueueIv)
    MaterialIconView playQueueIv;
    @BindView(R.id.detailView)
    LinearLayout detailView;
    @BindView(R.id.topContainer)
    LinearLayout topContainer;

    private View coverView;
    private View lyricView;
    private ObjectAnimator coverAnimator = null;

    private static final long PROGRESS_UPDATE_INTERNAL = 1000;
    private static final long PROGRESS_UPDATE_INITIAL_INTERVAL = 100;
    private String mCurrentArtUrl;
    private final Handler mHandler = new Handler();
    private MediaBrowserCompat mMediaBrowser;

    private final Runnable mUpdateProgressTask = new Runnable() {
        @Override
        public void run() {
            updateProgress();
        }
    };

    private final ScheduledExecutorService mExecutorService =
            Executors.newSingleThreadScheduledExecutor();

    private ScheduledFuture<?> mScheduleFuture;
    private PlaybackStateCompat mLastPlaybackState;

    private final MediaControllerCompat.Callback mCallback = new MediaControllerCompat.Callback() {
        @Override
        public void onPlaybackStateChanged(@NonNull PlaybackStateCompat state) {
            LogHelper.d(TAG, "onPlaybackstate changed", state);
            updatePlaybackState(state);
        }

        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {
            if (metadata != null) {
                FullScreenPlayerActivity.this.onMetadataChanged(metadata);
                updateDuration(metadata);
            }
        }
    };
    private final MediaBrowserCompat.ConnectionCallback mConnectionCallback =
            new MediaBrowserCompat.ConnectionCallback() {
                @Override
                public void onConnected() {
                    LogHelper.d(TAG, "onConnected");
                    try {
                        connectToSession(mMediaBrowser.getSessionToken());
                    } catch (RemoteException e) {
                        LogHelper.e(TAG, e, "could not connect media controller");
                    }
                }
            };

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerFullScreenPlayerComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .fullScreenPlayerModule(new FullScreenPlayerModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_full_screen_player; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        StatusBarUtil.setTranslucent(FullScreenPlayerActivity.this, 55);
        progressSb.setOnSeekBarChangeListener(this);
        initViewPager();
        initAlbumPic();
    }

    private void initAlbumPic() {
        ImageView imageView = coverView.findViewById(R.id.iv_cover);
        if (imageView == null) {
            return;
        }
        coverAnimator = ObjectAnimator.ofFloat(imageView, "rotation", 0F, 360F).setDuration(20 * 1000);
        coverAnimator.setRepeatCount(Animation.INFINITE);
        coverAnimator.setRepeatMode(ObjectAnimator.RESTART);
        coverAnimator.setInterpolator(new LinearInterpolator());
    }

    private void initViewPager() {
        List<View> viewList = new ArrayList<>();
        coverView = LayoutInflater.from(this).inflate(R.layout.frag_player_coverview, viewPager, false);
        coverView.findViewById(R.id.iv_cover);
//        lyricView = LayoutInflater.from(this).inflate(R.layout.frag_player_lrcview, viewPager, false);
        viewList.add(coverView);
        ViewPageViewAdapter adapter = new ViewPageViewAdapter(viewList);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(5);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setPlayingBitmap(String url) {
        ImageView imageView = coverView.findViewById(R.id.iv_cover);
        if (imageView == null) {
            return;
        }
        GlideArms.with(FullScreenPlayerActivity.this).load(MusicUtil.getImageUrl(url, AppConstant.ONLINE_IMG_SIZE_480))
                .placeholder(R.drawable.default_cover)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(imageView);
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
        overridePendingTransition(0, 0);
        ActivityCompat.finishAfterTransition(this);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mMediaBrowser = new MediaBrowserCompat(this,
                new ComponentName(this, MusicService.class), mConnectionCallback, null);
        mMediaBrowser.connect();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        MediaControllerCompat controllerCompat = MediaControllerCompat.getMediaController(FullScreenPlayerActivity.this);
        if (controllerCompat != null) {
            controllerCompat.unregisterCallback(mCallback);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSeekbarUpdate();
        mExecutorService.shutdown();
        if (mMediaBrowser != null) {
            mMediaBrowser.disconnect();
        }
        coverAnimator.cancel();
        coverAnimator = null;
    }


    @OnClick({R.id.playModeIv, R.id.prevPlayIv, R.id.playPauseIv, R.id.loadingPb, R.id.nextPlayIv, R.id.playQueueIv, R.id.backIv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.playModeIv:
                break;
            case R.id.prevPlayIv:
                previousMedia();
                break;
            case R.id.playPauseIv:
                if (playPauseIv.isPlaying()) {
                    //播放状态
                    pauseMedia();
                } else {
                    //暂停状态
                    playMedia();
                }
                break;
            case R.id.loadingPb:
                break;
            case R.id.nextPlayIv:
                nextMedia();
                break;
            case R.id.playQueueIv:
                break;
            case R.id.backIv:
                killMyself();
                break;
            default:
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        progressTv.setText(DateUtils.formatElapsedTime(progress / 1000));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        stopSeekbarUpdate();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        MediaControllerCompat.getMediaController(FullScreenPlayerActivity.this).getTransportControls().seekTo(seekBar.getProgress());
        scheduleSeekbarUpdate();
    }


    private void scheduleSeekbarUpdate() {
        stopSeekbarUpdate();
        if (!mExecutorService.isShutdown()) {
            mScheduleFuture = mExecutorService.scheduleAtFixedRate(
                    new Runnable() {
                        @Override
                        public void run() {
                            mHandler.post(mUpdateProgressTask);
                        }
                    }, PROGRESS_UPDATE_INITIAL_INTERVAL,
                    PROGRESS_UPDATE_INTERNAL, TimeUnit.MILLISECONDS);
        }
    }


    private void stopSeekbarUpdate() {
        if (mScheduleFuture != null) {
            mScheduleFuture.cancel(false);
        }
    }

    private void connectToSession(MediaSessionCompat.Token token) throws RemoteException {
        MediaControllerCompat mediaController = new MediaControllerCompat(
                FullScreenPlayerActivity.this, token);
        if (mediaController.getMetadata() == null) {
            finish();
            return;
        }
        MediaControllerCompat.setMediaController(FullScreenPlayerActivity.this, mediaController);
        mediaController.registerCallback(mCallback);
        PlaybackStateCompat state = mediaController.getPlaybackState();
        updatePlaybackState(state);
        MediaMetadataCompat metadata = mediaController.getMetadata();
        if (metadata != null) {
            onMetadataChanged(metadata);
            updateDuration(metadata);
        }
        updateProgress();
        if (state != null && (state.getState() == PlaybackStateCompat.STATE_PLAYING ||
                state.getState() == PlaybackStateCompat.STATE_BUFFERING)) {
            scheduleSeekbarUpdate();
        }
    }

    private void updateProgress() {
        if (mLastPlaybackState == null) {
            return;
        }
        long currentPosition = mLastPlaybackState.getPosition();
        if (mLastPlaybackState.getState() == PlaybackStateCompat.STATE_PLAYING) {
            // Calculate the elapsed time between the last position update and now and unless
            // paused, we can assume (delta * speed) + current position is approximately the
            // latest position. This ensure that we do not repeatedly call the getPlaybackState()
            // on MediaControllerCompat.
            long timeDelta = SystemClock.elapsedRealtime() -
                    mLastPlaybackState.getLastPositionUpdateTime();
            currentPosition += (int) timeDelta * mLastPlaybackState.getPlaybackSpeed();
        }
        progressSb.setProgress((int) currentPosition);
    }

    private void updatePlaybackState(PlaybackStateCompat state) {
        if (state == null) {
            return;
        }
        mLastPlaybackState = state;
        boolean playing = false;
        MediaControllerCompat controllerCompat = MediaControllerCompat.getMediaController(FullScreenPlayerActivity.this);
        switch (state.getState()) {
            case PlaybackStateCompat.STATE_PLAYING:
                scheduleSeekbarUpdate();
                playing = true;
                break;
            case PlaybackStateCompat.STATE_PAUSED:
            case PlaybackStateCompat.STATE_NONE:
            case PlaybackStateCompat.STATE_STOPPED:
                stopSeekbarUpdate();
                break;
            case PlaybackStateCompat.STATE_BUFFERING:
                stopSeekbarUpdate();
                break;
            default:
                LogHelper.d(TAG, "Unhandled state ", state.getState());
        }
        if (playing) {
            //播放状态
            playPauseIv.play();
            playPauseIv.setPlaying(true);
            if (coverAnimator.isPaused()) {
                coverAnimator.resume();
            } else {
                coverAnimator.cancel();
                coverAnimator.start();
            }
        } else {
            //暂停状态
            playPauseIv.pause();
            playPauseIv.setPlaying(false);
            coverAnimator.pause();
        }
    }

    private void onMetadataChanged(MediaMetadataCompat metadata) {
        LogHelper.d(TAG, "onMetadataChanged ", metadata);
        if (metadata == null) {
            return;
        }
        String title = metadata.getString(MediaMetadataCompat.METADATA_KEY_TITLE);
        String artist = metadata.getString(MediaMetadataCompat.METADATA_KEY_ARTIST);
        String source = metadata.getString(MusicProviderSource.CUSTOM_METADATA_TRACK_SOURCE);
        titleIv.setText(title);
        subTitleTv.setText(artist);
        getMusicInfo(source);
    }

    private void updateDuration(MediaMetadataCompat metadata) {
        if (metadata == null) {
            return;
        }
        LogHelper.d(TAG, "updateDuration called ");
        int duration = (int) metadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION);
        progressSb.setMax(duration);
        durationTv.setText(DateUtils.formatElapsedTime(duration / 1000));
    }

    private void playMedia() {
        MediaControllerCompat controller = MediaControllerCompat.getMediaController(FullScreenPlayerActivity.this);
        if (controller != null) {
            controller.getTransportControls().play();
        }
    }

    private void pauseMedia() {
        MediaControllerCompat controller = MediaControllerCompat.getMediaController(FullScreenPlayerActivity.this);
        if (controller != null) {
            controller.getTransportControls().pause();
        }
    }

    private void nextMedia() {
        MediaControllerCompat controller = MediaControllerCompat.getMediaController(FullScreenPlayerActivity.this);
        if (controller != null) {
            controller.getTransportControls().skipToNext();
        }
    }

    private void previousMedia() {
        MediaControllerCompat controller = MediaControllerCompat.getMediaController(FullScreenPlayerActivity.this);
        if (controller != null) {
            controller.getTransportControls().skipToPrevious();
        }
    }

    private void getMusicInfo(String source) {
        BaseApplication application = (BaseApplication) Utils.getApp();
        application.getAppComponent().repositoryManager()
                .obtainRetrofitService(UserService.class)
                .getMusicInfo(source)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ErrorHandleSubscriber<MusicSourceInfoBean>(application.getAppComponent().rxErrorHandler()) {
                    @Override
                    public void onNext(MusicSourceInfoBean musicSourceInfoBean) {
                        if (TextUtils.isEmpty(musicSourceInfoBean.getImgUrl())) {
                            return;
                        }
                        setPlayingBitmap(musicSourceInfoBean.getImgUrl());
                    }
                });
    }
}
