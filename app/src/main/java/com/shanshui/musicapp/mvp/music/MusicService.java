package com.shanshui.musicapp.mvp.music;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.shanshui.musicapp.app.EventBusTags;
import com.shanshui.musicapp.app.eventbus.EventMusicData;
import com.shanshui.musicapp.mvp.AppConstant;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.List;

public class MusicService extends MediaBrowserServiceCompat implements PlaybackManager.PlaybackServiceCallback {
    private static final String TAG = "MusicService";

    private MediaSessionCompat mSession;
    private PlaybackStateCompat mPlaybackState;
    private MusicProvider mMusicProvider;
    private PlaybackManager playbackManager;

    public MusicService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        mMusicProvider = new MusicProvider();
        QueueManager queueManager = new QueueManager(mMusicProvider, getResources(), new QueueManager.MetadataUpdateListener() {
            @Override
            public void onMetadataChanged(MediaMetadataCompat metadata) {
                mSession.setMetadata(metadata);
            }

            @Override
            public void onMetadataRetrieveError() {

            }

            @Override
            public void onCurrentQueueIndexUpdated(int queueIndex) {
//                playbackManager.handlePlayRequest();
            }

            @Override
            public void onQueueUpdated(String title, List<MediaSessionCompat.QueueItem> newQueue) {
                mSession.setQueue(newQueue);
                mSession.setQueueTitle(title);
            }
        });
        playbackManager = new PlaybackManager(new LocalPlayBack(MusicService.this, mMusicProvider)
                , this, mMusicProvider, queueManager);
        mSession = new MediaSessionCompat(this, "MusicService");
        mSession.setCallback(playbackManager.getMediaSessionCallback());//设置回调
        mSession.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        //设置token后会触发MediaBrowserCompat.ConnectionCallback的回调方法
        //表示MediaBrowser与MediaBrowserService连接成功
        setSessionToken(mSession.getSessionToken());
        playbackManager.updatePlaybackState(null);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String s, int i, @Nullable Bundle bundle) {
        return new BrowserRoot("root", null);
    }

    @Override
    public void onLoadChildren(@NonNull final String parentMediaId, @NonNull final Result<List<MediaBrowserCompat.MediaItem>> result) {
        LogHelper.d(TAG, "OnLoadChildren: parentMediaId=", parentMediaId);
        if (mMusicProvider.isInitialized()) {
            // if music library is ready, return immediately
            //如果音乐库已经准备好了，立即将数据发送至客户端
            result.sendResult(mMusicProvider.getChildren(parentMediaId, getResources()));
        } else {
            // otherwise, only return results when the music library is retrieved
            //音乐数据检索完毕后返回结果
            result.detach();
            mMusicProvider.retrieveMediaAsync(new MusicProvider.Callback() {
                //完成音乐加载后的回调
                @Override
                public void onMusicCatalogReady(boolean success) {
                    result.sendResult(mMusicProvider.getChildren(parentMediaId, getResources()));
                }
            });
        }
    }

    @Override
    public void onPlaybackStart() {
        mSession.setActive(true);
    }

    @Override
    public void onNotificationRequired() {

    }

    @Override
    public void onPlaybackStop() {
        mSession.setActive(false);
    }

    @Override
    public void onPlaybackStateUpdated(PlaybackStateCompat newState) {
        mSession.setPlaybackState(newState);
    }

    @Subscriber(tag = EventBusTags.switchMusicProvide)
    public void switchMusicProvide(EventMusicData musicData) {
        if (musicData.musicType == AppConstant.NETWORK_MUSIC) {
            mMusicProvider.switchMusicProvider(new RemoteMusicProviderSource(musicData.provideName, musicData.mData));
        } else {
            mMusicProvider.switchMusicProvider(new LocalMusicProviderSource(musicData.provideName, musicData.mData));
        }
        mMusicProvider.retrieveMediaAsync(new MusicProvider.Callback() {
            @Override
            public void onMusicCatalogReady(boolean success) {
                EventBus.getDefault().post("", EventBusTags.switchMusicProvideSuccess);
            }
        });
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
