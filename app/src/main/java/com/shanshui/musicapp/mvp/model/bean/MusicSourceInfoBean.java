package com.shanshui.musicapp.mvp.model.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author mashanshui
 * @date 2019-04-01
 * @desc TODO
 */
public class MusicSourceInfoBean {

    /**
     * fileHead : 100
     * q : 0
     * extra : {"320filesize":7998693,"sqfilesize":23222557,"sqhash":"CAC59E48D58853BF40BB6158F2F5B0C5","128hash":"CB7EE97F4CC11C4EA7A1FA4B516A5D97","320hash":"47F63F15A7C048829FA796BC7F74E62B","128filesize":3198974}
     * fileSize : 3198974
     * choricSinger : 李玉刚
     * album_img : http://imge.kugou.com/stdmusic/{size}/20161109/20161109171040932108.jpg
     * topic_remark :
     * url : http://fs.open.kugou.com/931cd37fe49bca40dcbab9bdbee4b110/5ca1b929/G078/M08/18/17/jg0DAFgi6G-AKqsqADDP_nSW5F4051.mp3
     * time : 1554103101
     * trans_param : {"roaming_astrict":0,"pay_block_tpl":1,"musicpack_advance":0,"display_rate":0,"display":0,"cid":21448131}
     * albumid : 1820033
     * singerName : 李玉刚
     * topic_url :
     * extName : mp3
     * songName : 刚好遇见你
     * singerHead :
     * hash : CB7EE97F4CC11C4EA7A1FA4B516A5D97
     * intro :
     * req_hash : CB7EE97F4CC11C4EA7A1FA4B516A5D97
     * imgUrl : http://singerimg.kugou.com/uploadpic/softhead/{size}/20181224/20181224183453372.jpg
     * album_audio_id : 40238103
     * area_code : 1
     * ctype : 1009
     * error :
     * status : 1
     * stype : 11323
     * bitRate : 128
     * 320privilege : 10
     * 128privilege : 8
     * singerId : 2018
     * fileName : 李玉刚 - 刚好遇见你
     * errcode : 0
     * privilege : 8
     * sqprivilege : 10
     * mvhash :
     * timeLength : 200
     */

    private int fileHead;
    private int q;
    private ExtraBean extra;
    private int fileSize;
    private String choricSinger;
    private String album_img;
    private String topic_remark;
    private String url;
    private int time;
    private TransParamBean trans_param;
    private int albumid;
    private String singerName;
    private String topic_url;
    private String extName;
    private String songName;
    private String singerHead;
    private String hash;
    private String intro;
    private String req_hash;
    private String imgUrl;
    private int album_audio_id;
    private String area_code;
    private int ctype;
    private String error;
    private int status;
    private int stype;
    private int bitRate;
    @SerializedName("320privilege")
    private int _$320privilege;
    @SerializedName("128privilege")
    private int _$128privilege;
    private int singerId;
    private String fileName;
    private int errcode;
    private int privilege;
    private int sqprivilege;
    private String mvhash;
    private int timeLength;

    public int getFileHead() {
        return fileHead;
    }

    public void setFileHead(int fileHead) {
        this.fileHead = fileHead;
    }

    public int getQ() {
        return q;
    }

    public void setQ(int q) {
        this.q = q;
    }

    public ExtraBean getExtra() {
        return extra;
    }

    public void setExtra(ExtraBean extra) {
        this.extra = extra;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public String getChoricSinger() {
        return choricSinger;
    }

    public void setChoricSinger(String choricSinger) {
        this.choricSinger = choricSinger;
    }

    public String getAlbum_img() {
        return album_img;
    }

    public void setAlbum_img(String album_img) {
        this.album_img = album_img;
    }

    public String getTopic_remark() {
        return topic_remark;
    }

    public void setTopic_remark(String topic_remark) {
        this.topic_remark = topic_remark;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public TransParamBean getTrans_param() {
        return trans_param;
    }

    public void setTrans_param(TransParamBean trans_param) {
        this.trans_param = trans_param;
    }

    public int getAlbumid() {
        return albumid;
    }

    public void setAlbumid(int albumid) {
        this.albumid = albumid;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getTopic_url() {
        return topic_url;
    }

    public void setTopic_url(String topic_url) {
        this.topic_url = topic_url;
    }

    public String getExtName() {
        return extName;
    }

    public void setExtName(String extName) {
        this.extName = extName;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSingerHead() {
        return singerHead;
    }

    public void setSingerHead(String singerHead) {
        this.singerHead = singerHead;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getReq_hash() {
        return req_hash;
    }

    public void setReq_hash(String req_hash) {
        this.req_hash = req_hash;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getAlbum_audio_id() {
        return album_audio_id;
    }

    public void setAlbum_audio_id(int album_audio_id) {
        this.album_audio_id = album_audio_id;
    }

    public String getArea_code() {
        return area_code;
    }

    public void setArea_code(String area_code) {
        this.area_code = area_code;
    }

    public int getCtype() {
        return ctype;
    }

    public void setCtype(int ctype) {
        this.ctype = ctype;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStype() {
        return stype;
    }

    public void setStype(int stype) {
        this.stype = stype;
    }

    public int getBitRate() {
        return bitRate;
    }

    public void setBitRate(int bitRate) {
        this.bitRate = bitRate;
    }

    public int get_$320privilege() {
        return _$320privilege;
    }

    public void set_$320privilege(int _$320privilege) {
        this._$320privilege = _$320privilege;
    }

    public int get_$128privilege() {
        return _$128privilege;
    }

    public void set_$128privilege(int _$128privilege) {
        this._$128privilege = _$128privilege;
    }

    public int getSingerId() {
        return singerId;
    }

    public void setSingerId(int singerId) {
        this.singerId = singerId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public int getPrivilege() {
        return privilege;
    }

    public void setPrivilege(int privilege) {
        this.privilege = privilege;
    }

    public int getSqprivilege() {
        return sqprivilege;
    }

    public void setSqprivilege(int sqprivilege) {
        this.sqprivilege = sqprivilege;
    }

    public String getMvhash() {
        return mvhash;
    }

    public void setMvhash(String mvhash) {
        this.mvhash = mvhash;
    }

    public int getTimeLength() {
        return timeLength;
    }

    public void setTimeLength(int timeLength) {
        this.timeLength = timeLength;
    }

    public static class ExtraBean {
        /**
         * 320filesize : 7998693
         * sqfilesize : 23222557
         * sqhash : CAC59E48D58853BF40BB6158F2F5B0C5
         * 128hash : CB7EE97F4CC11C4EA7A1FA4B516A5D97
         * 320hash : 47F63F15A7C048829FA796BC7F74E62B
         * 128filesize : 3198974
         */

        @SerializedName("320filesize")
        private int _$320filesize;
        private int sqfilesize;
        private String sqhash;
        @SerializedName("128hash")
        private String _$128hash;
        @SerializedName("320hash")
        private String _$320hash;
        @SerializedName("128filesize")
        private int _$128filesize;

        public int get_$320filesize() {
            return _$320filesize;
        }

        public void set_$320filesize(int _$320filesize) {
            this._$320filesize = _$320filesize;
        }

        public int getSqfilesize() {
            return sqfilesize;
        }

        public void setSqfilesize(int sqfilesize) {
            this.sqfilesize = sqfilesize;
        }

        public String getSqhash() {
            return sqhash;
        }

        public void setSqhash(String sqhash) {
            this.sqhash = sqhash;
        }

        public String get_$128hash() {
            return _$128hash;
        }

        public void set_$128hash(String _$128hash) {
            this._$128hash = _$128hash;
        }

        public String get_$320hash() {
            return _$320hash;
        }

        public void set_$320hash(String _$320hash) {
            this._$320hash = _$320hash;
        }

        public int get_$128filesize() {
            return _$128filesize;
        }

        public void set_$128filesize(int _$128filesize) {
            this._$128filesize = _$128filesize;
        }
    }

    public static class TransParamBean {
        /**
         * roaming_astrict : 0
         * pay_block_tpl : 1
         * musicpack_advance : 0
         * display_rate : 0
         * display : 0
         * cid : 21448131
         */

        private int roaming_astrict;
        private int pay_block_tpl;
        private int musicpack_advance;
        private int display_rate;
        private int display;
        private int cid;

        public int getRoaming_astrict() {
            return roaming_astrict;
        }

        public void setRoaming_astrict(int roaming_astrict) {
            this.roaming_astrict = roaming_astrict;
        }

        public int getPay_block_tpl() {
            return pay_block_tpl;
        }

        public void setPay_block_tpl(int pay_block_tpl) {
            this.pay_block_tpl = pay_block_tpl;
        }

        public int getMusicpack_advance() {
            return musicpack_advance;
        }

        public void setMusicpack_advance(int musicpack_advance) {
            this.musicpack_advance = musicpack_advance;
        }

        public int getDisplay_rate() {
            return display_rate;
        }

        public void setDisplay_rate(int display_rate) {
            this.display_rate = display_rate;
        }

        public int getDisplay() {
            return display;
        }

        public void setDisplay(int display) {
            this.display = display;
        }

        public int getCid() {
            return cid;
        }

        public void setCid(int cid) {
            this.cid = cid;
        }
    }
}
