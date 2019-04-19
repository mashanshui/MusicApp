package com.shanshui.musicapp.mvp.model.bean;

import com.shanshui.musicapp.mvp.model.entity.Response;
import com.shanshui.musicapp.mvp.model.entity.ResponseList;
import com.shanshui.musicapp.mvp.model.entity.ResponseListObject;
import com.shanshui.musicapp.mvp.model.entity.ResponsePListObject;

/**
 * @author mashanshui
 * @date 2019-01-16
 * @desc TODO
 */
public class SongSheetBean extends Response<ResponsePListObject<ResponseListObject<SongSheetBean>>> {

    /**
     * recommendfirst : 1
     * specialname : 想在薄情的世界，做一个深情的人
     * intro : 能够给予我们爱的人，心中盛装着对世界的万般深情。那些陪着我们走过最不堪岁月的人，想必也就是最爱你的人。在爱的面前，无需掩饰。  突然想做一个温暖而深情的人，就像彩虹下的飞鸟乘着微风去远方觅食，就像邮局里的信件寄托着浓浓的情意，就像大海上的灯塔守候第一缕晨光出现。
     * suid : 1270245579
     * is_selected : 0
     * selected_reason :
     * slid : 0
     * trans_param : {"special_tag":0}
     * publishtime : 2018-11-20 00:00:00
     * singername :
     * verified : 0
     * user_type : 1
     * user_avatar : http://imge.kugou.com/kugouicon/165/20180711/20180711152617926552.jpg
     * imgurl : http://imge.kugou.com/soft/collection/{size}/20181231/20181231180504225868.jpg
     * collectcount : 24
     * specialid : 565136
     * username : 墨小宝
     * ugc_talent_review : 1
     * playcount : 707875
     */

    private int recommendfirst;
    private String specialname;
    private String intro;
    private int suid;
    private int is_selected;
    private String selected_reason;
    private int slid;
    private TransParamBean trans_param;
    private String publishtime;
    private String singername;
    private int verified;
    private int user_type;
    private String user_avatar;
    private String imgurl;
    private int collectcount;
    private int specialid;
    private String username;
    private int ugc_talent_review;
    private int playcount;

    public int getRecommendfirst() {
        return recommendfirst;
    }

    public void setRecommendfirst(int recommendfirst) {
        this.recommendfirst = recommendfirst;
    }

    public String getSpecialname() {
        return specialname;
    }

    public void setSpecialname(String specialname) {
        this.specialname = specialname;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public int getSuid() {
        return suid;
    }

    public void setSuid(int suid) {
        this.suid = suid;
    }

    public int getIs_selected() {
        return is_selected;
    }

    public void setIs_selected(int is_selected) {
        this.is_selected = is_selected;
    }

    public String getSelected_reason() {
        return selected_reason;
    }

    public void setSelected_reason(String selected_reason) {
        this.selected_reason = selected_reason;
    }

    public int getSlid() {
        return slid;
    }

    public void setSlid(int slid) {
        this.slid = slid;
    }

    public TransParamBean getTrans_param() {
        return trans_param;
    }

    public void setTrans_param(TransParamBean trans_param) {
        this.trans_param = trans_param;
    }

    public String getPublishtime() {
        return publishtime;
    }

    public void setPublishtime(String publishtime) {
        this.publishtime = publishtime;
    }

    public String getSingername() {
        return singername;
    }

    public void setSingername(String singername) {
        this.singername = singername;
    }

    public int getVerified() {
        return verified;
    }

    public void setVerified(int verified) {
        this.verified = verified;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public int getCollectcount() {
        return collectcount;
    }

    public void setCollectcount(int collectcount) {
        this.collectcount = collectcount;
    }

    public int getSpecialid() {
        return specialid;
    }

    public void setSpecialid(int specialid) {
        this.specialid = specialid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUgc_talent_review() {
        return ugc_talent_review;
    }

    public void setUgc_talent_review(int ugc_talent_review) {
        this.ugc_talent_review = ugc_talent_review;
    }

    public int getPlaycount() {
        return playcount;
    }

    public void setPlaycount(int playcount) {
        this.playcount = playcount;
    }

    public static class TransParamBean {
        /**
         * special_tag : 0
         */

        private int special_tag;

        public int getSpecial_tag() {
            return special_tag;
        }

        public void setSpecial_tag(int special_tag) {
            this.special_tag = special_tag;
        }
    }
}
