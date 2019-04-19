package com.shanshui.musicapp.mvp.presenter;

import android.app.Application;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import javax.inject.Inject;

import com.jess.arms.utils.PermissionUtil;
import com.shanshui.musicapp.app.utils.MusicUtil;
import com.shanshui.musicapp.mvp.contract.MineContract;
import com.shanshui.musicapp.mvp.model.bean.LocalMusicBean;
import com.shanshui.musicapp.mvp.model.bean.MusicBean;
import com.shanshui.musicapp.mvp.model.bean.MusicListBean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


@FragmentScope
public class MinePresenter extends BasePresenter<MineContract.Model, MineContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;
    private ThreadPoolExecutor threadPoolExecutor;

    @Inject
    public MinePresenter(MineContract.Model model, MineContract.View rootView) {
        super(model, rootView);
        threadPoolExecutor = new ThreadPoolExecutor(2, 4, 3, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(3),
                new ThreadPoolExecutor.DiscardOldestPolicy());
    }

    public void requestFilePermission() {
        PermissionUtil.externalStorage(new PermissionUtil.RequestPermission() {
            @Override
            public void onRequestPermissionSuccess() {
                mRootView.updateList(MusicUtil.switchLocMusicToMetadata(loadLocalMusic()));
//                threadPoolExecutor.execute(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                });
            }

            @Override
            public void onRequestPermissionFailure(List<String> permissions) {
                mRootView.showMessage("拒绝权限无法查找本地音乐");
            }

            @Override
            public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {
            }
        }, mRootView.getRxPermissions(), mErrorHandler);
    }

    private List<LocalMusicBean> loadLocalMusic() {
        List<LocalMusicBean> list = new ArrayList<>();
        Cursor cursor = mRootView.getActivity().getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Audio.Media.SIZE + ">80000", null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();
            LocalMusicBean mp3Info = new LocalMusicBean();
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
                mp3Info.setTitle(title);
                mp3Info.setAlbum(album);
                mp3Info.setArtist(artist);
                mp3Info.setSource(url);
                mp3Info.setDurationMs(duration);
                list.add(mp3Info);
            }
            Log.e(TAG, "loadLocalMusic: " + mp3Info.toString());
        }
        cursor.close();
        return list;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }
}
