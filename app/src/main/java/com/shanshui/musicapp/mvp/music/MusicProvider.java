package com.shanshui.musicapp.mvp.music;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;

import com.shanshui.musicapp.mvp.AppConstant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author mashanshui
 * @date 2018-11-30
 * @desc TODO
 */
public class MusicProvider {
    private static final String TAG = LogHelper.makeLogTag(MusicProvider.class);
    public static String CURRENT_MUSIC_PROVIDE;
    private MusicProviderSource mSource;

    private final ConcurrentMap<String, MutableMediaMetadata> mMusicListById;

    enum State {
        NON_INITIALIZED, INITIALIZING, INITIALIZED
    }

    private volatile State mCurrentState = State.NON_INITIALIZED;

    public interface Callback {
        void onMusicCatalogReady(boolean success);
    }

    public MusicProvider() {
        this(new LocalMusicProviderSource(AppConstant.PROVIDE_LOCAL_MUSIC));
    }

    public MusicProvider(MusicProviderSource source) {
        mSource = source;
        mMusicListById = new ConcurrentHashMap<>();
        CURRENT_MUSIC_PROVIDE = source.getProvideName();
    }

    /**
     * @param source 切换播放源
     */
    public void switchMusicProvider(MusicProviderSource source) {
        mSource = source;
        CURRENT_MUSIC_PROVIDE = source.getProvideName();
        mMusicListById.clear();
        mCurrentState = State.NON_INITIALIZED;
    }

    /**
     * @return 存储了所有音乐的列表的迭代器
     */
    public Iterable<MediaMetadataCompat> getMusicIterable() {
        if (mCurrentState != State.INITIALIZED) {
            return Collections.emptyList();
        }
        List<MediaMetadataCompat> list = new ArrayList<>(mMusicListById.size());
        for (MutableMediaMetadata mutableMetadata : mMusicListById.values()) {
            list.add(mutableMetadata.metadata);
        }
        return list;
    }

    /**
     * Get an iterator over a shuffled collection of all songs
     * 获取 存储了所有音乐的列表 随机打乱顺序后的迭代器
     */
    public Iterable<MediaMetadataCompat> getShuffledMusicIterable() {
        if (mCurrentState != State.INITIALIZED) {
            return Collections.emptyList();
        }
        List<MediaMetadataCompat> shuffled = new ArrayList<>(mMusicListById.size());
        for (MutableMediaMetadata mutableMetadata : mMusicListById.values()) {
            shuffled.add(mutableMetadata.metadata);
        }
        Collections.shuffle(shuffled);//打乱列表的顺序
        return shuffled;
    }

    /**
     * 获取 存储了所有音乐的列表 正常顺序顺序的迭代器
     */
    public Iterable<MediaMetadataCompat> getOrderMusicIterable() {
        if (mCurrentState != State.INITIALIZED) {
            return Collections.emptyList();
        }
        List<MediaMetadataCompat> shuffled = new ArrayList<>(mMusicListById.size());
        for (MutableMediaMetadata mutableMetadata : mMusicListById.values()) {
            shuffled.add(mutableMetadata.metadata);
        }
        return shuffled;
    }

    /**
     * Very basic implementation of a search that filter music tracks with title containing
     * the given query.
     */
    public List<MediaMetadataCompat> searchMusicBySongTitle(String query) {
        return searchMusic(MediaMetadataCompat.METADATA_KEY_TITLE, query);
    }

    /**
     * Very basic implementation of a search that filter music tracks with album containing
     * the given query.
     */
    public List<MediaMetadataCompat> searchMusicByAlbum(String query) {
        return searchMusic(MediaMetadataCompat.METADATA_KEY_ALBUM, query);
    }

    /**
     * Very basic implementation of a search that filter music tracks with artist containing
     * the given query.
     */
    public List<MediaMetadataCompat> searchMusicByArtist(String query) {
        return searchMusic(MediaMetadataCompat.METADATA_KEY_ARTIST, query);
    }

    /**
     * Very basic implementation of a search that filter music tracks with a genre containing
     * the given query.
     */
    public List<MediaMetadataCompat> searchMusicByGenre(String query) {
        return searchMusic(MediaMetadataCompat.METADATA_KEY_GENRE, query);
    }

    private List<MediaMetadataCompat> searchMusic(String metadataField, String query) {
        if (mCurrentState != State.INITIALIZED) {
            return Collections.emptyList();
        }
        ArrayList<MediaMetadataCompat> result = new ArrayList<>();
        query = query.toLowerCase(Locale.US);
        for (MutableMediaMetadata track : mMusicListById.values()) {
            if (track.metadata.getString(metadataField).toLowerCase(Locale.US)
                    .contains(query)) {
                result.add(track.metadata);
            }
        }
        return result;
    }

    /**
     * Return the MediaMetadataCompat for the given musicID.
     *
     * @param musicId The unique, non-hierarchical music ID.
     */
    public MediaMetadataCompat getMusic(String musicId) {
        return mMusicListById.containsKey(musicId) ? mMusicListById.get(musicId).metadata : null;
    }

    /**
     * 重新设置当前musicId的信息
     */
    public void setMusic(String musicId, MediaMetadataCompat item) {
        mMusicListById.put(musicId, new MutableMediaMetadata(musicId, item));
    }

    public boolean isInitialized() {
        return mCurrentState == State.INITIALIZED;
    }

    /**
     * Get the list of music tracks from a server and caches the track information
     * for future reference, keying tracks by musicId and grouping by genre.
     * 从服务端获取音乐路径列表，以及缓存列表数据以便将来直接引用
     * 使用musicId作为列表的关键字并将音乐按类型分组
     */
    public void retrieveMediaAsync(final Callback callback) {
        if (mCurrentState == State.INITIALIZED) {
            if (callback != null) {
                // Nothing to do, execute callback immediately
                callback.onMusicCatalogReady(true);
            }
            return;
        }

        //在单独的线程中异步加载音乐目录
        new AsyncTask<Void, Void, State>() {
            @Override
            protected State doInBackground(Void... params) {
                retrieveMedia();
                return mCurrentState;
            }

            @Override
            protected void onPostExecute(State current) {
                if (callback != null) {
                    callback.onMusicCatalogReady(current == State.INITIALIZED);
                }
            }
        }.execute();
    }

    /**
     * 通过MusicProviderSource的迭代器检索Media资源
     */
    private synchronized void retrieveMedia() {
        try {
            if (mCurrentState == State.NON_INITIALIZED) {
                mCurrentState = State.INITIALIZING;
                Iterator<MediaMetadataCompat> tracks = mSource.iterator();
                while (tracks.hasNext()) {
                    MediaMetadataCompat item = tracks.next();
                    String musicId = item.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID);
                    mMusicListById.put(musicId, new MutableMediaMetadata(musicId, item));
                }
                mCurrentState = State.INITIALIZED;
            }
        } finally {
            if (mCurrentState != State.INITIALIZED) {
                // Something bad happened, so we reset state to NON_INITIALIZED to allow
                // retries (eg if the network connection is temporary unavailable)
                mCurrentState = State.NON_INITIALIZED;
            }
        }
    }

    public List<MediaBrowserCompat.MediaItem> getChildren(String mediaId, Resources resources) {
        List<MediaBrowserCompat.MediaItem> mediaItems = new ArrayList<>();
        for (Map.Entry<String, MutableMediaMetadata> entry : mMusicListById.entrySet()) {
            MutableMediaMetadata mutableMediaMetadata = entry.getValue();
            mediaItems.add(createMediaItem(mutableMediaMetadata.metadata));
        }
        return mediaItems;
    }

    private MediaBrowserCompat.MediaItem createMediaItem(MediaMetadataCompat metadata) {
//        MediaMetadataCompat copy = new MediaMetadataCompat.Builder(metadata)
//                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, hierarchyAwareMediaID)
//                .build();
        return new MediaBrowserCompat.MediaItem(metadata.getDescription(),
                MediaBrowserCompat.MediaItem.FLAG_PLAYABLE);

    }
}
