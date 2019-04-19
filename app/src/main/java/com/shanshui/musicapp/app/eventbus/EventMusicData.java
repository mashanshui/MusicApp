package com.shanshui.musicapp.app.eventbus;

import android.support.v4.media.MediaMetadataCompat;

import com.shanshui.musicapp.mvp.AppConstant;

import java.util.List;

/**
 * @author mashanshui
 * @date 2019-03-26
 * @desc TODO
 */
public class EventMusicData {
    public int musicType;

    public String provideName;

    public List<MediaMetadataCompat> mData;

    public EventMusicData(String provideName, List<MediaMetadataCompat> mData) {
        this.provideName = provideName;
        this.mData = mData;
        musicType = AppConstant.NETWORK_MUSIC;
    }

    public EventMusicData(int musicType, String provideName, List<MediaMetadataCompat> mData) {
        this.musicType = musicType;
        this.provideName = provideName;
        this.mData = mData;
    }
}
