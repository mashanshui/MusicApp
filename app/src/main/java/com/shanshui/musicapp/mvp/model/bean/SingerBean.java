package com.shanshui.musicapp.mvp.model.bean;

import com.shanshui.musicapp.mvp.model.entity.Response2;
import com.shanshui.musicapp.mvp.model.entity.ResponseListObject;

/**
 * @author mashanshui
 * @date 2019-01-17
 * @desc TODO
 */
public class SingerBean extends Response2<ResponseListObject<SingerBean>> {

    /**
     * heatoffset : 0
     * sortoffset : 80
     * singername : 周杰伦
     * intro :
     * songcount : 0
     * imgurl : http://singerimg.kugou.com/uploadpic/softhead/{size}/20180515/20180515002522714.jpg
     * albumcount : 0
     * mvcount : 0
     * singerid : 3520
     * heat : 235574
     * fanscount : 6532165
     * is_settled : 0
     */

    private int heatoffset;
    private int sortoffset;
    private String singername;
    private String intro;
    private int songcount;
    private String imgurl;
    private int albumcount;
    private int mvcount;
    private int singerid;
    private int heat;
    private int fanscount;
    private int is_settled;

    public int getHeatoffset() {
        return heatoffset;
    }

    public void setHeatoffset(int heatoffset) {
        this.heatoffset = heatoffset;
    }

    public int getSortoffset() {
        return sortoffset;
    }

    public void setSortoffset(int sortoffset) {
        this.sortoffset = sortoffset;
    }

    public String getSingername() {
        return singername;
    }

    public void setSingername(String singername) {
        this.singername = singername;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public int getSongcount() {
        return songcount;
    }

    public void setSongcount(int songcount) {
        this.songcount = songcount;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public int getAlbumcount() {
        return albumcount;
    }

    public void setAlbumcount(int albumcount) {
        this.albumcount = albumcount;
    }

    public int getMvcount() {
        return mvcount;
    }

    public void setMvcount(int mvcount) {
        this.mvcount = mvcount;
    }

    public int getSingerid() {
        return singerid;
    }

    public void setSingerid(int singerid) {
        this.singerid = singerid;
    }

    public int getHeat() {
        return heat;
    }

    public void setHeat(int heat) {
        this.heat = heat;
    }

    public int getFanscount() {
        return fanscount;
    }

    public void setFanscount(int fanscount) {
        this.fanscount = fanscount;
    }

    public int getIs_settled() {
        return is_settled;
    }

    public void setIs_settled(int is_settled) {
        this.is_settled = is_settled;
    }
}
