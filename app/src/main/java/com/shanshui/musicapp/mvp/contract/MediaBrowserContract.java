package com.shanshui.musicapp.mvp.contract;

import android.support.v4.media.MediaMetadataCompat;

import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;
import com.shanshui.musicapp.mvp.model.bean.MusicListBean;
import com.shanshui.musicapp.mvp.model.entity.Response2;
import com.shanshui.musicapp.mvp.model.entity.ResponseListObject;

import java.util.List;

import io.reactivex.Observable;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 01/22/2019 22:14
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public interface MediaBrowserContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        int getPage();

        void setPage(int page);

        int getLength();

        void handleSuccess(List<MediaMetadataCompat> musicList);

        void handleFailure();
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<Response2<ResponseListObject<MusicListBean>>> getChartsMusicList(String rank, int page, int length);

        Observable<Response2<ResponseListObject<MusicListBean>>> getSongSheetMusicList(String specialId, int page, int length);

        Observable<Response2<ResponseListObject<MusicListBean>>> getSingerMusicList(String singerId, int page, int length);
    }
}
