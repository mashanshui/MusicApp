package com.shanshui.musicapp.mvp.model.bean;

import android.support.v4.media.MediaDescriptionCompat;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * @author mashanshui
 * @date 2018-11-05
 * @desc TODO
 */
public class MusicBean {
    private int itemType;

    public MusicBean(int itemType) {
        this.itemType = itemType;
    }

    private MediaDescriptionCompat metadata;

    public MediaDescriptionCompat getMetadata() {
        return metadata;
    }

    public void setMetadata(MediaDescriptionCompat metadata) {
        this.metadata = metadata;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public int getItemType() {
        return itemType;
    }
}
