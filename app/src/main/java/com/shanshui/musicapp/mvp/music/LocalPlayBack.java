package com.shanshui.musicapp.mvp.music;

import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.Utils;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.jess.arms.base.BaseApplication;
import com.jess.arms.integration.RepositoryManager;
import com.jess.arms.integration.RepositoryManager_Factory;
import com.shanshui.musicapp.app.utils.IToast;
import com.shanshui.musicapp.app.utils.RxUtils;
import com.shanshui.musicapp.mvp.AppConstant;
import com.shanshui.musicapp.mvp.model.api.service.UserService;
import com.shanshui.musicapp.mvp.model.bean.MusicSourceInfoBean;
import com.shanshui.musicapp.mvp.model.bean.SingerBean;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

import static com.google.android.exoplayer2.C.CONTENT_TYPE_MUSIC;
import static com.google.android.exoplayer2.C.USAGE_MEDIA;

/**
 * @author mashanshui
 * @date 2018-11-21
 * @desc TODO
 */
public class LocalPlayBack implements Playback {
    private static final String TAG = "LocalPlayBack";

    // The volume we set the media player to when we lose audio focus, but are
    // allowed to reduce the volume instead of stopping playback.
    //当音频失去焦点，且不需要停止播放，只需要减小音量时，我们设置的媒体播放器音量大小
    //例如微信的提示音响起，我们只需要减小当前音乐的播放音量即可
    public static final float VOLUME_DUCK = 0.2f;
    // The volume we set the media player when we have audio focus.
    //当我们获取音频焦点时设置的播放音量大小
    public static final float VOLUME_NORMAL = 1.0f;

    // we don't have audio focus, and can't duck (play at a low volume)
    //没有获取到音频焦点，也不允许duck状态
    private static final int AUDIO_NO_FOCUS_NO_DUCK = 0;
    // we don't have focus, but can duck (play at a low volume)
    //没有获取到音频焦点，但允许duck状态
    private static final int AUDIO_NO_FOCUS_CAN_DUCK = 1;
    // we have full audio focus
    //完全获取音频焦点
    private static final int AUDIO_FOCUSED = 2;

    private SimpleExoPlayer mExoPlayer;
    private Context context;
    private final MusicProvider mMusicProvider;
    private final ExoPlayerEventListener mEventListener = new ExoPlayerEventListener();
    // Whether to return STATE_NONE or STATE_STOPPED when mExoPlayer is null;
    private boolean mExoPlayerNullIsStopped = false;
    private Callback mCallback;
    private String mCurrentMediaId;
    private boolean mPlayOnFocusGain;
    //音乐播放还是准备
    private boolean mPrepare = true;
    //当前音频焦点的状态
    private int mCurrentAudioFocusState = AUDIO_NO_FOCUS_NO_DUCK;
    private final AudioManager mAudioManager;

    public LocalPlayBack(Context context, MusicProvider musicProvider) {
        this.context = context.getApplicationContext();
        this.mMusicProvider = musicProvider;
        this.mAudioManager =
                (AudioManager) this.context.getSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    public void prepare(MediaSessionCompat.QueueItem item) {
        mPrepare = true;
        prepareSource(item);
    }

    private void prepareSource(MediaSessionCompat.QueueItem item) {
        mPlayOnFocusGain = true;
        tryToGetAudioFocus();
        String mediaId = item.getDescription().getMediaId();
        boolean mediaHasChanged = !TextUtils.equals(mediaId, mCurrentMediaId);
        if (mediaHasChanged) {
            mCurrentMediaId = mediaId;
        }
        if (mediaHasChanged || mExoPlayer == null) {
            releaseResources(false);
            MediaMetadataCompat track =
                    mMusicProvider.getMusic(item.getDescription().getMediaId());
            //获取音乐资源的路径
            String source = track.getString(MusicProviderSource.CUSTOM_METADATA_TRACK_SOURCE);
            if (TextUtils.equals(AppConstant.PROVIDE_LOCAL_MUSIC, MusicProvider.CURRENT_MUSIC_PROVIDE)) {
                playMusicByLocal(source);
            } else {
                playMusicByNetwork(source);
            }
        }
    }

    private void playSource(MediaSessionCompat.QueueItem item) {
        mPlayOnFocusGain = true;
        tryToGetAudioFocus();
        String mediaId = item.getDescription().getMediaId();
        boolean mediaHasChanged = !TextUtils.equals(mediaId, mCurrentMediaId);
        //暂停后点击播放
        if (!mediaHasChanged && getState() == PlaybackStateCompat.STATE_PAUSED) {
            pauseToPlay();
            return;
        }
        if (mediaHasChanged) {
            mCurrentMediaId = mediaId;
        }
        if (mediaHasChanged || mExoPlayer == null) {
            releaseResources(false);
            MediaMetadataCompat track =
                    mMusicProvider.getMusic(item.getDescription().getMediaId());
            //获取音乐资源的路径
            String source = track.getString(MusicProviderSource.CUSTOM_METADATA_TRACK_SOURCE);
            if (TextUtils.equals(AppConstant.PROVIDE_LOCAL_MUSIC, MusicProvider.CURRENT_MUSIC_PROVIDE)) {
                playMusicByLocal(source);
            } else {
                playMusicByNetwork(source);
            }
        }
    }

    private void pauseToPlay() {
        configurePlayerState();
    }

    private void playMusicByLocal(String source) {
        configurePlayer(source);
    }

    private void playMusicByNetwork(String source) {
        BaseApplication application = (BaseApplication) Utils.getApp();
        application.getAppComponent().repositoryManager()
                .obtainRetrofitService(UserService.class)
                .getMusicInfo(source)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ErrorHandleSubscriber<MusicSourceInfoBean>(application.getAppComponent().rxErrorHandler()) {
                    @Override
                    public void onNext(MusicSourceInfoBean musicSourceInfoBean) {
                        if (TextUtils.isEmpty(musicSourceInfoBean.getUrl())) {
                            IToast.showShort("VIP音乐敬请期待");
                            SystemClock.sleep(1000);
                            if (mCallback != null) {
                                mCallback.onCompletion();
                            }
                            return;
                        }
                        configurePlayer(musicSourceInfoBean.getUrl());
                    }
                });
    }

    private void configurePlayer(String source) {
        if (mExoPlayer == null) {
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(context),
                    new DefaultTrackSelector(), new DefaultLoadControl());
            mExoPlayer.addListener(mEventListener);
        }
        final AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(CONTENT_TYPE_MUSIC)
                .setUsage(USAGE_MEDIA)
                .build();
        mExoPlayer.setAudioAttributes(audioAttributes);

        // Produces DataSource instances through which media data is loaded.
        //通过加载完毕的媒体数据生成数据源实例
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(
                        context, Util.getUserAgent(context, "uamp"), null);
        // Produces Extractor instances for parsing the media data.
        //生成Extractor实例用于解析媒体数据
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        // The MediaSource represents the media to be played.
        //将以上这些内容组合成MediaSource，然后传给播放器去播放
        MediaSource mediaSource =
                new ExtractorMediaSource(
                        Uri.parse(source), dataSourceFactory, extractorsFactory, null, null);
        // Prepares media to play (happens on background thread) and triggers
        // {@code onPlayerStateChanged} callback when the stream is ready to play.
        mExoPlayer.prepare(mediaSource);
        if (!mPrepare) {
            configurePlayerState();
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void stop(boolean notifyListeners) {
        giveUpAudioFocus();//放弃音频焦点
        releaseResources(true);
    }

    @Override
    public void setState(int state) {

    }

    @Override
    public int getState() {
        if (mExoPlayer == null) {
            return mExoPlayerNullIsStopped
                    ? PlaybackStateCompat.STATE_STOPPED
                    : PlaybackStateCompat.STATE_NONE;
        }
        switch (mExoPlayer.getPlaybackState()) {
            case Player.STATE_IDLE:
                return PlaybackStateCompat.STATE_PAUSED;
            case Player.STATE_BUFFERING:
                return PlaybackStateCompat.STATE_BUFFERING;//缓冲
            case Player.STATE_READY:
                return mExoPlayer.getPlayWhenReady()
                        ? PlaybackStateCompat.STATE_PLAYING
                        : PlaybackStateCompat.STATE_PAUSED;
            case Player.STATE_ENDED:
                return PlaybackStateCompat.STATE_PAUSED;
            default:
                return PlaybackStateCompat.STATE_NONE;
        }
    }

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public boolean isPlaying() {
        return mPlayOnFocusGain || (mExoPlayer != null && mExoPlayer.getPlayWhenReady());
    }

    @Override
    public long getCurrentStreamPosition() {
        return mExoPlayer != null ? mExoPlayer.getCurrentPosition() : 0;
    }

    @Override
    public void updateLastKnownStreamPosition() {

    }

    @Override
    public void play(MediaSessionCompat.QueueItem item) {
        mPrepare = false;
        playSource(item);
//        configurePlayerState();
    }

    @Override
    public void pause() {
        // Pause player and cancel the 'foreground service' state.
        if (mExoPlayer != null) {
            mExoPlayer.setPlayWhenReady(false);
        }
        // While paused, retain the player instance, but give up audio focus.
        releaseResources(false);
    }

    @Override
    public void seekTo(long position) {
        if (mExoPlayer != null) {
            mExoPlayer.seekTo(position);
        }
    }

    @Override
    public void setCurrentMediaId(String mediaId) {
        this.mCurrentMediaId = mediaId;
    }

    @Override
    public String getCurrentMediaId() {
        return mCurrentMediaId;
    }

    @Override
    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    private class ExoPlayerEventListener implements Player.EventListener {

        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest) {

        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

        }

        @Override
        public void onLoadingChanged(boolean isLoading) {

        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            switch (playbackState) {
                case Player.STATE_IDLE:
                case Player.STATE_BUFFERING:
                case Player.STATE_READY:
                    if (mCallback != null) {
                        mCallback.onPlaybackStatusChanged(getState());
                    }
                    break;
                case Player.STATE_ENDED:
                    // The media player finished playing the current song.
                    if (mCallback != null) {
                        mCallback.onCompletion();
                    }
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {

        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            final String what;
            switch (error.type) {
                case ExoPlaybackException.TYPE_SOURCE:
                    what = error.getSourceException().getMessage();
                    break;
                case ExoPlaybackException.TYPE_RENDERER:
                    what = error.getRendererException().getMessage();
                    break;
                case ExoPlaybackException.TYPE_UNEXPECTED:
                    what = error.getUnexpectedException().getMessage();
                    break;
                default:
                    what = "Unknown: " + error;
            }
            Log.e(TAG, "onPlayerError: " + what);
            if (mCallback != null) {
                mCallback.onError("ExoPlayer error " + what);
            }
        }

        @Override
        public void onPositionDiscontinuity() {

        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

        }
    }

    /**
     * 请求音频焦点成功之后监听其状态的Listener
     * AUDIOFOCUS_GAIN 得到音频焦点时触发的状态，请求得到的音频焦点一般会长期占有
     * AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK 失去音频焦点时触发的状态，在该状态的时候不需要暂停音频，但是我们需要降低音频的声音
     * AUDIOFOCUS_LOSS_TRANSIENT 失去音频焦点时触发的状态，但是该状态不会长时间保持，此时我们应该暂停音频，且当重新获取音频焦点的时候继续播放
     * AUDIOFOCUS_LOSS 失去音频焦点时触发的状态，且这个状态有可能会长期保持，此时应当暂停音频并释放音频相关的资源
     */
    private final AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int focusChange) {
                    LogHelper.d(TAG, "onAudioFocusChange. focusChange=", focusChange);
                    switch (focusChange) {
                        case AudioManager.AUDIOFOCUS_GAIN:
                            mCurrentAudioFocusState = AUDIO_FOCUSED;
                            break;
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                            // Audio focus was lost, but it's possible to duck (i.e.: play quietly)
                            mCurrentAudioFocusState = AUDIO_NO_FOCUS_CAN_DUCK;
                            break;
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                            // Lost audio focus, but will gain it back (shortly), so note whether
                            // playback should resume
                            mCurrentAudioFocusState = AUDIO_NO_FOCUS_NO_DUCK;
                            mPlayOnFocusGain = mExoPlayer != null && mExoPlayer.getPlayWhenReady();
                            break;
                        case AudioManager.AUDIOFOCUS_LOSS:
                            // Lost audio focus, probably "permanently"
                            mCurrentAudioFocusState = AUDIO_NO_FOCUS_NO_DUCK;
                            break;
                        default:
                            break;
                    }

                    if (mExoPlayer != null) {
                        // Update the player state based on the change
                        configurePlayerState();
                    }
                }
            };


    /**
     * 尝试获取音频焦点
     * requestAudioFocus(OnAudioFocusChangeListener l, int streamType, int durationHint)
     * OnAudioFocusChangeListener l：音频焦点状态监听器
     * int streamType：请求焦点的音频类型
     * int durationHint：请求焦点音频持续性的指示
     * AUDIOFOCUS_GAIN：指示申请得到的音频焦点不知道会持续多久，一般是长期占有
     * AUDIOFOCUS_GAIN_TRANSIENT：指示要申请的音频焦点是暂时性的，会很快用完释放的
     * AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK：指示要申请的音频焦点是暂时性的，同时还指示当前正在使用焦点的音频可以继续播放，只是要“duck”一下（降低音量）
     */
    private void tryToGetAudioFocus() {
        LogHelper.d(TAG, "tryToGetAudioFocus");
        int result =
                mAudioManager.requestAudioFocus(
                        mOnAudioFocusChangeListener,
                        AudioManager.STREAM_MUSIC,
                        AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            mCurrentAudioFocusState = AUDIO_FOCUSED;
        } else {
            mCurrentAudioFocusState = AUDIO_NO_FOCUS_NO_DUCK;
        }
    }

    /**
     * Reconfigures the player according to audio focus settings and starts/restarts it. This method
     * starts/restarts the ExoPlayer instance respecting the current audio focus state. So if we
     * have focus, it will play normally; if we don't have focus, it will either leave the player
     * paused or set it to a low volume, depending on what is permitted by the current focus
     * settings.
     * 根据音频焦点的设置重新配置播放器 以及 启动/重新启动 播放器。调用这个方法 启动/重新启动 播放器实例取决于当前音频焦点的状态。
     * 因此如果我们持有音频焦点，则正常播放音频；如果我们失去音频焦点，播放器将暂停播放或者设置为低音量，这取决于当前焦点设置允许哪种设置
     */
    private void configurePlayerState() {
        LogHelper.d(TAG, "configurePlayerState. mCurrentAudioFocusState=", mCurrentAudioFocusState);
        if (mCurrentAudioFocusState == AUDIO_NO_FOCUS_NO_DUCK) {
            // We don't have audio focus and can't duck, so we have to pause
            pause();
        } else {

            if (mCurrentAudioFocusState == AUDIO_NO_FOCUS_CAN_DUCK) {
                // We're permitted to play, but only if we 'duck', ie: play softly
                mExoPlayer.setVolume(VOLUME_DUCK);
            } else {
                mExoPlayer.setVolume(VOLUME_NORMAL);
            }

            // If we were playing when we lost focus, we need to resume playing.
            if (mPlayOnFocusGain) {
                //播放的过程中因失去焦点而暂停播放，短暂暂停之后仍需要继续播放时会进入这里执行相应的操作
                mExoPlayer.setPlayWhenReady(true);
                mPlayOnFocusGain = false;
            }
        }
    }

    /**
     * 放弃音频焦点
     */
    private void giveUpAudioFocus() {
        LogHelper.d(TAG, "giveUpAudioFocus");
        //申请放弃音频焦点
        if (mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener)
                == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            //AudioManager.AUDIOFOCUS_REQUEST_GRANTED 申请成功
            mCurrentAudioFocusState = AUDIO_NO_FOCUS_NO_DUCK;
        }
    }


    /**
     * Releases resources used by the service for playback, which is mostly just the WiFi lock for
     * local playback. If requested, the ExoPlayer instance is also released.
     * 释放Service用于播放的资源，这个资源主要是指本地播放时的wifi锁。
     * 如有必要，也可以将ExoPlayer实例的资源也释放掉（由传参releasePlayer决定）
     *
     * @param releasePlayer 指示播放器是否应该被释放
     */
    private void releaseResources(boolean releasePlayer) {

        // Stops and releases player (if requested and available).
        if (releasePlayer && mExoPlayer != null) {
            mExoPlayer.release();
            mExoPlayer.removeListener(mEventListener);
            mExoPlayer = null;
            mExoPlayerNullIsStopped = true;
            mPlayOnFocusGain = false;
        }
    }

}
