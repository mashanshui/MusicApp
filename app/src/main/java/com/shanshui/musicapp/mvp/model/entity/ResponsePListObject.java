package com.shanshui.musicapp.mvp.model.entity;

/**
 * @author mashanshui
 * @date 2019-01-16
 * @desc TODO
 */
public class ResponsePListObject<T> {
    /**
     * list : {"timestamp":1547641681,"info":[],"total":4399}
     * pagesize : 30
     */
    private int pagesize;
    private T list;

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public T getList() {
        return list;
    }

    public void setList(T list) {
        this.list = list;
    }
}
