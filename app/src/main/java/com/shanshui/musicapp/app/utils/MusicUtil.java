package com.shanshui.musicapp.app.utils;

import android.support.v4.media.MediaMetadataCompat;

import com.shanshui.musicapp.mvp.adapter.MusicListAdapter;
import com.shanshui.musicapp.mvp.model.bean.LocalMusicBean;
import com.shanshui.musicapp.mvp.model.bean.MusicBean;
import com.shanshui.musicapp.mvp.model.bean.MusicListBean;
import com.shanshui.musicapp.mvp.music.MusicProviderSource;
import com.shanshui.musicapp.mvp.music.RemoteMusicProviderSource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mashanshui
 * @date 2019-01-19
 * @desc TODO
 */
public class MusicUtil {
    /**
     * @param oldUrl    转换前的图片url
     * @param imageSize 转换后图片的大小
     * @return 转换图片地址
     */
    public static String getImageUrl(String oldUrl, int imageSize) {
        return oldUrl.replace("{size}", String.valueOf(imageSize));
    }

    /**
     * @param num
     * @return 转换数字为以万为单位的字符串
     */
    public static String formatNumToString(int num) {
        if (num < 10000) {
            return String.valueOf(num);
        }
        BigDecimal bigDecimal = new BigDecimal(num);
        // 转换为万元（除以10000）
        BigDecimal decimal = bigDecimal.divide(new BigDecimal("10000"));
        // 保留两位小数
        DecimalFormat formater = new DecimalFormat("0.0");
        // 四舍五入
        formater.setRoundingMode(RoundingMode.HALF_UP);    // 5000008.89
        // 格式化完成之后得出结果
        return formater.format(decimal) + "万";
    }

    /**
     * @param oldMusicList
     * @return 将服务器返回的数据转化为MediaMetadataCompat格式的数据
     */
    public static List<MediaMetadataCompat> switchNetMusicToMetadata(List<MusicListBean> oldMusicList) {
        List<MediaMetadataCompat> newMusicList = new ArrayList<>();
        for (int i = 0; i < oldMusicList.size(); i++) {
            MusicListBean bean = oldMusicList.get(i);
            newMusicList.add(getMediaMetadata(getSongName(bean.getFilename())
                    , ""
                    , getSingerName(bean.getFilename(), bean.getRemark())
                    , bean.getHash()
                    , ""
                    , getMusicDuration(bean.getDuration())));
        }
        return newMusicList;
    }

    /**
     * @param oldMusicList
     * @return 将本地数据转化为MediaMetadataCompat格式的数据
     */
    public static List<MediaMetadataCompat> switchLocMusicToMetadata(List<LocalMusicBean> oldMusicList) {
        List<MediaMetadataCompat> newMusicList = new ArrayList<>();
        for (int i = 0; i < oldMusicList.size(); i++) {
            LocalMusicBean bean = oldMusicList.get(i);
            newMusicList.add(getMediaMetadata(bean.getTitle()
                    , bean.getAlbum()
                    , bean.getArtist()
                    , bean.getSource()
                    , ""
                    , bean.getDurationMs()));
        }
        return newMusicList;
    }

    /**
     * @param title
     * @param album
     * @param artist
     * @param source
     * @param iconUrl
     * @param durationMs
     * @return 拼接数据
     */
    public static MediaMetadataCompat getMediaMetadata(String title, String album, String artist, String source,
                                                       String iconUrl, long durationMs) {
        String id = String.valueOf(source.hashCode());

        //noinspection ResourceType
        return new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, id)
                .putString(MusicProviderSource.CUSTOM_METADATA_TRACK_SOURCE, source)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, album)
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, artist)
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, artist)
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, durationMs)
//                .putString(MediaMetadataCompat.METADATA_KEY_GENRE, genre)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, iconUrl)
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, title)
//                .putLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, trackNumber)
//                .putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, totalTrackCount)
                .build();
    }

    /**
     * @param filename
     * @return 获取歌曲的名字
     */
    private static String getSongName(String filename) {
        return filename.substring(filename.lastIndexOf("-") + 1).trim();
    }

    /**
     * @param filename
     * @param remark
     * @return 获取歌手名字
     */
    public static String getSingerName(String filename, String remark) {
        return filename.substring(0, filename.lastIndexOf("-") + 2) + remark;
    }

    /**
     * @param duration
     * @return 获取歌曲的时长
     */
    public static long getMusicDuration(long duration) {
        return duration * 1000;
    }

    /**
     * @param oldMusicList
     * @return 将MediaMetadataCompat格式的数据转化为MusicBean
     */
    public static List<MusicBean> switchMusicToBean(List<MediaMetadataCompat> oldMusicList) {
        List<MusicBean> newMusicList = new ArrayList<>();
        for (int i = 0; i < oldMusicList.size(); i++) {
            MediaMetadataCompat bean = oldMusicList.get(i);
            MusicBean musicBean = new MusicBean(MusicListAdapter.MUSIC_ITEM_1);
            musicBean.setMetadata(bean.getDescription());
            newMusicList.add(musicBean);
        }
        return newMusicList;
    }

}
