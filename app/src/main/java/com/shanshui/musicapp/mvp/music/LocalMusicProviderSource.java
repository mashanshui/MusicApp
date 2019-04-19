package com.shanshui.musicapp.mvp.music;

import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.media.MediaMetadataCompat;
import android.util.Log;

import com.blankj.utilcode.util.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author mashanshui
 * @date 2018-11-13
 * @desc TODO
 */
public class LocalMusicProviderSource implements MusicProviderSource {
    private static final String TAG = "LocalMusicProviderSourc";
    private String provideName;
    private List<MediaMetadataCompat> mData = new ArrayList<>();

    public LocalMusicProviderSource(String provideName) {
        this.provideName = provideName;
    }

    @Override
    public String getProvideName() {
        return provideName;
    }

    public LocalMusicProviderSource(String provideName, List<MediaMetadataCompat> mData) {
        this.provideName = provideName;
        this.mData = mData;
    }

    public void add(String title, String album, String artist, String source,
                    String iconUrl, long durationMs) {
        String id = String.valueOf(source.hashCode());

        //noinspection ResourceType
        mData.add(new MediaMetadataCompat.Builder()
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
                .build());
    }

    @Override
    public Iterator<MediaMetadataCompat> iterator() {
//        if (mData.isEmpty()) {
//            loadMusic();
//        }
        return mData.iterator();
    }

    private void loadMusic() {
        Cursor cursor = Utils.getApp().getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Audio.Media.SIZE + ">80000", null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (cursor == null) {
            return;
        }

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();
            long id = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media._ID));               //音乐id
            String title = cursor.getString((cursor
                    .getColumnIndex(MediaStore.Audio.Media.TITLE)));            //音乐标题
            String artist = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ARTIST));            //艺术家
            String album = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ALBUM));            //艺术家
            int duration = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DURATION));          //时长
            long size = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media.SIZE));              //文件大小
            String url = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DATA));              //文件路径
            int isMusic = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));          //是否为音乐
            if (isMusic != 0) {     //只把音乐添加到集合当中
                add(title, album, artist, url, null, duration);
            }
        }
        cursor.close();
    }
}
