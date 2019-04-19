package com.shanshui.musicapp.mvp.music;

import android.support.v4.media.MediaMetadataCompat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author mashanshui
 * @date 2019-01-22
 * @desc TODO
 */
public class RemoteMusicProviderSource implements MusicProviderSource {
    private static final String TAG = "RemoteMusicProviderSour";
    private String provideName;
    private List<MediaMetadataCompat> mData;

    public RemoteMusicProviderSource(String provideName, List<MediaMetadataCompat> mData) {
        this.provideName = provideName;
        this.mData = mData;
    }

    @Override
    public Iterator<MediaMetadataCompat> iterator() {
        return mData.iterator();
    }

    @Override
    public String getProvideName() {
        return provideName;
    }

}
