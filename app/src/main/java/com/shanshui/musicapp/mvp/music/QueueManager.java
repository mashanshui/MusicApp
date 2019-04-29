package com.shanshui.musicapp.mvp.music;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;

import com.blankj.utilcode.util.SPUtils;
import com.shanshui.musicapp.mvp.AppConstant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author mashanshui
 * @date 2018-11-30
 * @desc TODO
 */
public class QueueManager {
    private static final String TAG = LogHelper.makeLogTag(QueueManager.class);

    private MusicProvider mMusicProvider;
    private MetadataUpdateListener mListener;
    private Resources mResources;

    // "Now playing" queue:
    //当前播放队列
    private List<MediaSessionCompat.QueueItem> mPlayingQueue;
    private int mCurrentIndex;
    private int mPlayMode = SPUtils.getInstance().getInt(AppConstant.ACTION_PLAY_MODE, AppConstant.PLAY_MODE_ORDER);

    /**
     * @param musicProvider 数据源提供者
     * @param resources     系统资源
     * @param listener      播放数据更新的回调接口
     */
    public QueueManager(@NonNull MusicProvider musicProvider,
                        @NonNull Resources resources,
                        @NonNull MetadataUpdateListener listener) {
        this.mMusicProvider = musicProvider;
        this.mListener = listener;
        this.mResources = resources;

        //mPlayingQueue是线程安全的
        mPlayingQueue = Collections.synchronizedList(new ArrayList<MediaSessionCompat.QueueItem>());
        mCurrentIndex = 0;
    }

    /**
     * 设置当前的队列索引值
     *
     * @param index
     */
    private void setCurrentQueueIndex(int index) {
        if (index >= 0 && index < mPlayingQueue.size()) {
            mCurrentIndex = index;
            mListener.onCurrentQueueIndexUpdated(mCurrentIndex);
        }
    }

    public boolean setCurrentQueueItem(long queueId) {
        // set the current index on queue from the queue Id:
        int index = QueueHelper.getMusicIndexOnQueue(mPlayingQueue, queueId);
        setCurrentQueueIndex(index);
        return index >= 0;
    }

    public boolean setCurrentQueueItem(String mediaId) {
        // set the current index on queue from the music Id:
        int index = QueueHelper.getMusicIndexOnQueue(mPlayingQueue, mediaId);
        setCurrentQueueIndex(index);
        return index >= 0;
    }

    public void setQueueSortMode(int sortMode) {
        mPlayMode = sortMode;
        switch (sortMode) {
            case AppConstant.PLAY_MODE_RANDOM:
                setCurrentQueue("Random music",
                        QueueHelper.getRandomQueue(mMusicProvider));
                break;
            case AppConstant.PLAY_MODE_ORDER:
                setCurrentQueue("Random music",
                        QueueHelper.getOrderQueue(mMusicProvider));
                break;
            case AppConstant.PLAY_MODE_LOOP:
                break;
            default:
                break;
        }
        updateMetadata();
    }

    public void setQueueFromMusic(String mediaId) {
        LogHelper.d(TAG, "setQueueFromMusic", mediaId);
        int index = QueueHelper.getMusicIndexOnQueue(mPlayingQueue, mediaId);
        if (mPlayingQueue.isEmpty() || index == -1) {
            setCurrentQueue("Normal music", QueueHelper.getPlayingQueue(mMusicProvider));
        }
        setCurrentQueueItem(mediaId);
        updateMetadata();
    }

    /**
     * 通过mCurrentIndex获取当前播放的音乐
     *
     * @return
     */
    public MediaSessionCompat.QueueItem getCurrentMusic() {
        if (!QueueHelper.isIndexPlayable(mCurrentIndex, mPlayingQueue)) {
            return null;
        }
        return mPlayingQueue.get(mCurrentIndex);
    }

    public int getCurrentQueueSize() {
        if (mPlayingQueue == null) {
            return 0;
        }
        return mPlayingQueue.size();
    }

    protected void setCurrentQueue(String title, List<MediaSessionCompat.QueueItem> newQueue) {
        setCurrentQueue(title, newQueue, null);
    }

    /**
     * 设置当前播放队列
     *
     * @param title
     * @param newQueue       新的播放队列
     * @param initialMediaId 初始的mediaId
     */
    protected void setCurrentQueue(String title, List<MediaSessionCompat.QueueItem> newQueue,
                                   String initialMediaId) {
        mPlayingQueue = newQueue;
        int index = 0;
        if (initialMediaId != null) {
            index = QueueHelper.getMusicIndexOnQueue(mPlayingQueue, initialMediaId);
        }
        mCurrentIndex = Math.max(index, 0);
        mListener.onQueueUpdated(title, newQueue);
    }

    /**
     * 更新媒体数据
     */
    public void updateMetadata() {
        MediaSessionCompat.QueueItem currentMusic = getCurrentMusic();
        if (currentMusic == null) {
            mListener.onMetadataRetrieveError();
            return;
        }
        final String musicId = currentMusic.getDescription().getMediaId();
        MediaMetadataCompat metadata = mMusicProvider.getMusic(musicId);
        if (metadata == null) {
            throw new IllegalArgumentException("Invalid musicId " + musicId);
        }

        mListener.onMetadataChanged(metadata);
    }

    /**
     * 按照传入的数量跳到队列该数量的位置后（若值为负数则向前跳）开始播放音乐
     *
     * @param amount
     * @return
     */
    public boolean skipQueuePosition(int amount) {
        if (mPlayMode == AppConstant.PLAY_MODE_LOOP) {
            return true;
        }
        int index = mCurrentIndex + amount;
        if (index < 0) {
            // 如果索引值跳到了第一首歌的索引之前，则会从第一首歌开始播放
            index = 0;
        } else {
            // 通过取余的方式，当索引跳过了最后一首音乐，则回到队列开始处继续计算最终的索引值
            //（通过这种方式实现了队列的循环）
            index %= mPlayingQueue.size();
        }
        if (!QueueHelper.isIndexPlayable(index, mPlayingQueue)) {
            LogHelper.e(TAG, "Cannot increment queue index by ", amount,
                    ". Current=", mCurrentIndex, " queue length=", mPlayingQueue.size());
            return false;
        }
        mCurrentIndex = index;
        return true;
    }

    public interface MetadataUpdateListener {
        void onMetadataChanged(MediaMetadataCompat metadata);//媒体数据变更时调用

        void onMetadataRetrieveError();//媒体数据检索失败时调用

        void onCurrentQueueIndexUpdated(int queueIndex);//当前播放索引变更时调用

        void onQueueUpdated(String title, List<MediaSessionCompat.QueueItem> newQueue);//当前播放队列变更时调用
    }
}
