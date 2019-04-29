package com.shanshui.musicapp.mvp.contract;

import android.content.Context;

import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;
import com.shanshui.musicapp.mvp.model.bean.MusicListBean;
import com.shanshui.musicapp.mvp.model.bean.SearchHotBean;
import com.shanshui.musicapp.mvp.model.entity.Response2;
import com.shanshui.musicapp.mvp.model.entity.ResponseListObject;

import java.util.List;

import io.reactivex.Observable;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/19/2019 14:20
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public interface SearchMusicPreviewContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void updateSearchHot(List<SearchHotBean> data);

        Context getContext();

        void updateSearchHistory(List<String> stringList);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<Response2<ResponseListObject<SearchHotBean>>> getSearchHotData();
    }
}
