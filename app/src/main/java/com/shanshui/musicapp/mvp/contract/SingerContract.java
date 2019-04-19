package com.shanshui.musicapp.mvp.contract;

import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;
import com.shanshui.musicapp.mvp.model.bean.SingerBean;
import com.shanshui.musicapp.mvp.model.bean.SingerCategoryBean;

import java.util.List;

import io.reactivex.Observable;


public interface SingerContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void requestSuccess(List<SingerBean> list);

        void requestFailure();

        int getPage();

        int getLength();

        void updateSingerCategory(List<SingerCategoryBean> categoryBeans);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<SingerBean> getSingerList(int page, int length);
        Observable<SingerCategoryBean> getSingerCategory();
    }
}
