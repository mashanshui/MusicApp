package com.shanshui.musicapp.mvp.model.entity;

/**
 * @author mashanshui
 * @date 2019-01-17
 * @desc TODO
 */
public class Response2<T> {

    /**
     * status : 1
     * error :
     * data : {}
     * errcode : 0
     */

    private int status;
    private String error;
    private T data;
    private int errcode;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
