package com.shanshui.musicapp.mvp.model.entity;

import com.google.gson.annotations.SerializedName;

/**
 * @author 张继淮
 * @date 2017/9/28
 * @description 网络请求返回类型类
 */

public class Response<T> {
    /**
     * JS_CSS_DATE : 20130320
     * kg_domain : http://m.kugou.com
     * src : http://downmobile.kugou.com/promote/package/download/channel=6
     * fr : null
     * ver : v3
     * rank : {}
     * __Tpl : rank/list.html
     */

    private int JS_CSS_DATE;
    private String kg_domain;
    private String src;
    private String fr;
    private String ver;
    @SerializedName(value = "rank",alternate = {"plist","data"})
    private T rank;
    private String __Tpl;

    public int getJS_CSS_DATE() {
        return JS_CSS_DATE;
    }

    public void setJS_CSS_DATE(int JS_CSS_DATE) {
        this.JS_CSS_DATE = JS_CSS_DATE;
    }

    public String getKg_domain() {
        return kg_domain;
    }

    public void setKg_domain(String kg_domain) {
        this.kg_domain = kg_domain;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getFr() {
        return fr;
    }

    public void setFr(String fr) {
        this.fr = fr;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public T getRank() {
        return rank;
    }

    public void setRank(T rank) {
        this.rank = rank;
    }

    public String get__Tpl() {
        return __Tpl;
    }

    public void set__Tpl(String __Tpl) {
        this.__Tpl = __Tpl;
    }
}
