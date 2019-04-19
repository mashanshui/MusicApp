package com.shanshui.musicapp.mvp.model.bean;

import com.shanshui.musicapp.mvp.model.entity.ResponseListObject;

/**
 * @author mashanshui
 * @date 2019-01-19
 * @desc TODO
 */
public class SingerCategoryBean extends ResponseListObject<SingerCategoryBean> {

    /**
     * classid : 88
     * classname : 热门歌手
     * imgurl : http://mobileimg.kugou.com/billImage/150/26-11.jpg
     */

    private int classid;
    private String classname;
    private String imgurl;

    public int getClassid() {
        return classid;
    }

    public void setClassid(int classid) {
        this.classid = classid;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }
}
