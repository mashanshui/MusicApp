package com.shanshui.musicapp.mvp.model.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author mashanshui
 * @date 2019-01-19
 * @desc TODO
 */
public class ChartsOutBean {


    /**
     * rankid : 6666
     * ranktype : 2
     * jump_url : http://clickc.admaster.com.cn/c/a121793,b3159003,c281,i13834,m101,8a2,8b2,h
     * id : 1
     * children : []
     * banner7url : http://imge.kugou.com/mcommon/{size}/20190118/20190118110643880962.jpg
     * intro : 数据来源：酷狗
     * 排序方式：按歌曲搜索播放量的涨幅排序
     * 更新周期：每天
     * 动感十足、颜值开挂！
     * 飙升的人生不需要解释！
     * 广汽传祺GA4品牌魅力家轿，
     * 和你一起为梦想加速！
     * update_frequency : 每天
     * custom_type : 0
     * imgurl : http://imge.kugou.com/mcommon/{size}/20190118/20190118110641691638.jpg
     * haschildren : 0
     * songinfo : [{"songname":"宇多田ヒカル、Skrillex - Face My Fears (English Version)【王国之心3游戏主题曲】"},{"songname":"门丽 - 擦干所有的泪"},{"songname":"吴青峰 - 我们 (Live)"}]
     * bannerurl : http://imge.kugou.com/mcommonbanner/{size}/20190118/20190118110645602640.jpg
     * jump_title : 广汽传祺-为梦想提速，赢新年大礼！
     * rankname : 酷狗飙升榜
     * isvol : 1
     */

    private int rankid;
    private int ranktype;
    private String jump_url;
    private int id;
    private String banner7url;
    private String intro;
    private String update_frequency;
    private int custom_type;
    private String imgurl;
    private int haschildren;
    private String bannerurl;
    private String jump_title;
    private String rankname;
    private int isvol;
    private List<?> children;
    @SerializedName(value = "songinfo", alternate = {"albuminfo"})
    private List<ChartsBean> songinfo;

    public int getRankid() {
        return rankid;
    }

    public void setRankid(int rankid) {
        this.rankid = rankid;
    }

    public int getRanktype() {
        return ranktype;
    }

    public void setRanktype(int ranktype) {
        this.ranktype = ranktype;
    }

    public String getJump_url() {
        return jump_url;
    }

    public void setJump_url(String jump_url) {
        this.jump_url = jump_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBanner7url() {
        return banner7url;
    }

    public void setBanner7url(String banner7url) {
        this.banner7url = banner7url;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getUpdate_frequency() {
        return update_frequency;
    }

    public void setUpdate_frequency(String update_frequency) {
        this.update_frequency = update_frequency;
    }

    public int getCustom_type() {
        return custom_type;
    }

    public void setCustom_type(int custom_type) {
        this.custom_type = custom_type;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public int getHaschildren() {
        return haschildren;
    }

    public void setHaschildren(int haschildren) {
        this.haschildren = haschildren;
    }

    public String getBannerurl() {
        return bannerurl;
    }

    public void setBannerurl(String bannerurl) {
        this.bannerurl = bannerurl;
    }

    public String getJump_title() {
        return jump_title;
    }

    public void setJump_title(String jump_title) {
        this.jump_title = jump_title;
    }

    public String getRankname() {
        return rankname;
    }

    public void setRankname(String rankname) {
        this.rankname = rankname;
    }

    public int getIsvol() {
        return isvol;
    }

    public void setIsvol(int isvol) {
        this.isvol = isvol;
    }

    public List<?> getChildren() {
        return children;
    }

    public void setChildren(List<?> children) {
        this.children = children;
    }

    public List<ChartsBean> getSonginfo() {
        return songinfo;
    }

    public void setSonginfo(List<ChartsBean> songinfo) {
        this.songinfo = songinfo;
    }
}
