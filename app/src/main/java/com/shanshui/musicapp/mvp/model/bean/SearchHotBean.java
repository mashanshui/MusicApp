package com.shanshui.musicapp.mvp.model.bean;

/**
 * @author mashanshui
 * @date 2019-04-19
 * @desc TODO
 */
public class SearchHotBean {

    /**
     * sort : 1
     * keyword : 独家首发
     * jumpurl : https://m2.service.kugou.com/yueku/category/html/index.html?areaid=30
     */

    private int sort;
    private String keyword;
    private String jumpurl;

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getJumpurl() {
        return jumpurl;
    }

    public void setJumpurl(String jumpurl) {
        this.jumpurl = jumpurl;
    }
}
