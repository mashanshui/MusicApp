package com.shanshui.musicapp.mvp.model.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author mashanshui
 * @date 2018-11-07
 * @desc TODO
 */
public class ResponseList<T> {

    /**
     * total : 48
     * page : 1
     * pagesize : 30
     * timestamp : 1541573428
     * list : {}
     */

    private int total;
//    private int page;
    private int pagesize;
    private int timestamp;

    @SerializedName(value = "list",alternate = {"info"})
    private List<T> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }
}
