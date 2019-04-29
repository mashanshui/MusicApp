package com.shanshui.musicapp.mvp.model.bean;

/**
 * @author mashanshui
 * @date 2019-04-22
 * @desc TODO
 */
public class SearchTipBean {

    /**
     * songcount : 480
     * searchcount : 0
     * keyword : 歌曲
     */

    private int songcount;
    private int searchcount;
    private String keyword;

    public int getSongcount() {
        return songcount;
    }

    public void setSongcount(int songcount) {
        this.songcount = songcount;
    }

    public int getSearchcount() {
        return searchcount;
    }

    public void setSearchcount(int searchcount) {
        this.searchcount = searchcount;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
