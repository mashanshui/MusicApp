package com.shanshui.musicapp.mvp.music;

import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

/**
 * @author mashanshui
 * @date 2018-11-21
 * @desc TODO
 */
public class PlaybackManager implements Playback.Callback {
    private static final String TAG = "PlaybackManager";

    private MediaSessionCallback mediaSessionCallback;
    private Playback mPlayback;
    private PlaybackServiceCallback mServiceCallback;
    private QueueManager mQueueManager;
    private MusicProvider mMusicProvider;

    public PlaybackManager(Playback mPlayback, PlaybackServiceCallback mServiceCallback, MusicProvider musicProvider, QueueManager queueManager) {
        this.mQueueManager = queueManager;
        this.mServiceCallback = mServiceCallback;
        this.mPlayback = mPlayback;
        this.mMusicProvider = musicProvider;
        mPlayback.setCallback(this);
        mediaSessionCallback = new MediaSessionCallback();
    }

    public MediaSessionCompat.Callback getMediaSessionCallback() {
        return mediaSessionCallback;
    }

    /**
     * 响应控制器指令的回调
     */
    private class MediaSessionCallback extends MediaSessionCompat.Callback {

        @Override
        public void onPrepare() {
            Log.e(TAG, "onPrepare: " );
            if (mQueueManager.getCurrentMusic() == null) {
                mQueueManager.setRandomQueue();
            }
            handPrepareRequest();
        }

        /**
         * 响应MediaController.getTransportControls().play
         */
        @Override
        public void onPlay() {
            Log.e(TAG, "onPlay");
            if (mQueueManager.getCurrentMusic() == null) {
                mQueueManager.setRandomQueue();
            }
            handlePlayRequest();
        }

        /**
         * 响应MediaController.getTransportControls().playFromUri
         *
         * @param uri
         * @param extras
         */
        @Override
        public void onPlayFromUri(Uri uri, Bundle extras) {
            Log.e(TAG, "onPlayFromUri");
        }

        @Override
        public void onPlayFromSearch(String query, Bundle extras) {
        }

        @Override
        public void onPlayFromMediaId(String mediaId, Bundle extras) {
            Log.e(TAG, "onPlayFromMediaId: " + mediaId);
            if (mQueueManager.getCurrentMusic() == null) {
                mQueueManager.setRandomQueue();
            }
            mQueueManager.setQueueFromMusic(mediaId);
            handlePlayRequest();
        }

        //设置到指定进度时触发
        //通过MediaControllerCompat.getTransportControls().seekTo(position)触发
        @Override
        public void onSeekTo(long position) {
            LogHelper.d(TAG, "onSeekTo:", position);
            mPlayback.seekTo((int) position);
        }

        /**
         * 响应MediaController.getTransportControls().onPause
         */
        @Override
        public void onPause() {
            Log.e(TAG, "onPause");
            handlePauseRequest();
        }

        //停止播放时触发
        //通过MediaControllerCompat.getTransportControls().stop()触发
        @Override
        public void onStop() {
            LogHelper.d(TAG, "stop. current state=" + mPlayback.getState());
            handleStopRequest(null);
        }

        //跳到下一首时触发
        //通过MediaControllerCompat.getTransportControls().skipToNext()触发
        @Override
        public void onSkipToNext() {
            LogHelper.d(TAG, "skipToNext");
            if (mQueueManager.skipQueuePosition(1)) {
                handlePlayRequest();
            } else {
                handleStopRequest("Cannot skip");
            }
            mQueueManager.updateMetadata();
        }

        //跳到上一首时触发
        //通过MediaControllerCompat.getTransportControls().skipToPrevious()触发
        @Override
        public void onSkipToPrevious() {
            if (mQueueManager.skipQueuePosition(-1)) {
                handlePlayRequest();
            } else {
                handleStopRequest("Cannot skip");
            }
            mQueueManager.updateMetadata();
        }
    }

    /**
     * 应用初始化时默认加载的歌曲
     */
    private void handPrepareRequest() {
        MediaSessionCompat.QueueItem currentMusic = mQueueManager.getCurrentMusic();
        if (currentMusic != null) {
            mPlayback.prepare(currentMusic);
        }
    }

    /**
     * 处理播放音乐的请求
     */
    public void handlePlayRequest() {
        mServiceCallback.onPlaybackStart();
        MediaSessionCompat.QueueItem currentMusic = mQueueManager.getCurrentMusic();
        mPlayback.play(currentMusic);
    }

    /**
     * Handle a request to pause music
     * 处理暂停音乐的请求
     */
    public void handlePauseRequest() {
        if (mPlayback.isPlaying()) {
            mPlayback.pause();
            mServiceCallback.onPlaybackStop();
        }
    }

    /**
     * Handle a request to stop music
     * 处理停止音乐的请求
     *
     * @param withError Error message in case the stop has an unexpected cause. The error
     *                  message will be set in the PlaybackState and will be visible to
     *                  MediaController clients.
     */
    public void handleStopRequest(String withError) {
        mPlayback.stop(true);
        mServiceCallback.onPlaybackStop();
        updatePlaybackState(withError);
    }


    /**
     * Update the current media player state, optionally showing an error message.
     * 更新当前媒体播放器的状态，可选择是否显示错误信息
     *
     * @param error 如果不为null，错误信息将呈现给用户.
     */
    public void updatePlaybackState(String error) {
        LogHelper.d(TAG, "updatePlaybackState, playback state=" + mPlayback.getState());
        long position = PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN;
        if (mPlayback != null && mPlayback.isConnected()) {
            position = mPlayback.getCurrentStreamPosition();
        }

        //noinspection ResourceType
        PlaybackStateCompat.Builder stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(getAvailableActions());

//        setCustomAction(stateBuilder);
        int state = mPlayback.getState();

        // If there is an error message, send it to the playback state:
        if (error != null) {
            // Error states are really only supposed to be used for errors that cause playback to
            // stop unexpectedly and persist until the user takes action to fix it.
            stateBuilder.setErrorMessage(error);
            state = PlaybackStateCompat.STATE_ERROR;
        }
        //noinspection ResourceType
        stateBuilder.setState(state, position, 1.0f, SystemClock.elapsedRealtime());

        // Set the activeQueueItemId if the current index is valid.
        //如果当前索引是有效的
        MediaSessionCompat.QueueItem currentMusic = mQueueManager.getCurrentMusic();
        if (currentMusic != null) {
            stateBuilder.setActiveQueueItemId(currentMusic.getQueueId());
        }

        mServiceCallback.onPlaybackStateUpdated(stateBuilder.build());

        if (state == PlaybackStateCompat.STATE_PLAYING ||
                state == PlaybackStateCompat.STATE_PAUSED) {
            mServiceCallback.onNotificationRequired();
        }
    }

    //获取所有可用的动作命令
    private long getAvailableActions() {
        long actions =
                PlaybackStateCompat.ACTION_PLAY_PAUSE |
                        PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID |
                        PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH |
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                        PlaybackStateCompat.ACTION_SKIP_TO_NEXT;
        if (mPlayback.isPlaying()) {
            actions |= PlaybackStateCompat.ACTION_PAUSE;
        } else {
            actions |= PlaybackStateCompat.ACTION_PLAY;
        }
        return actions;
    }

    @Override
    public void onCompletion() {
        //当音乐播放器播完了当前歌曲，则继续播放下一首
        if (mQueueManager.skipQueuePosition(1)) {
            handlePlayRequest();
            mQueueManager.updateMetadata();
        } else {
            // If skipping was not possible, we stop and release the resources:
            //若不可能跳到下一首音乐进行播放，则停止并释放资源
            handleStopRequest(null);
        }
    }

    @Override
    public void onPlaybackStatusChanged(int state) {
        updatePlaybackState(null);
    }

    @Override
    public void onError(String error) {
        updatePlaybackState(error);
    }

    @Override
    public void setCurrentMediaId(String mediaId) {
        mQueueManager.setQueueFromMusic(mediaId);
    }

    public interface PlaybackServiceCallback {
        void onPlaybackStart();

        void onNotificationRequired();

        void onPlaybackStop();

        void onPlaybackStateUpdated(PlaybackStateCompat newState);
    }
}
