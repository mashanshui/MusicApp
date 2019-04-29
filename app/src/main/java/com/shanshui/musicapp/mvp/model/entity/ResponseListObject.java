package com.shanshui.musicapp.mvp.model.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author mashanshui
 * @date 2019-01-16
 * @desc TODO
 */
public class ResponseListObject<T> {


    /**
     * timestamp : 1547641681
     * info : []
     * total : 4399
     */

    private int timestamp;
    private int total;
    @SerializedName(value = "info", alternate = {"list","data"})
    private List<T> info;

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getInfo() {
        return info;
    }

    public void setInfo(List<T> info) {
        this.info = info;
    }
}
