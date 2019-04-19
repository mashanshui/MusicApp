package com.shanshui.musicapp.mvp.model.bean;

import com.shanshui.musicapp.mvp.model.entity.Response2;
import com.shanshui.musicapp.mvp.model.entity.ResponseListObject;

/**
 * @author mashanshui
 * @date 2018-11-07
 * @desc TODO
 */
public class ChartsBean extends Response2<ResponseListObject<ChartsOutBean>> {
    /**
     * songname : 门丽 - 擦干所有的泪
     */

    private String songname;
    /**
     * albumname : 蔡依林 - Ugly Beauty
     */

    private String albumname;

    public String getSongname() {
        return songname;
    }

    public void setSongname(String songname) {
        this.songname = songname;
    }

    public String getAlbumname() {
        return albumname;
    }

    public void setAlbumname(String albumname) {
        this.albumname = albumname;
    }
}
