package com.shanshui.musicapp.mvp.contract;

import android.app.Activity;
import android.support.v4.media.MediaMetadataCompat;

import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;
import com.shanshui.musicapp.mvp.model.bean.LocalMusicBean;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;


public interface MineContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        //申请权限
        RxPermissions getRxPermissions();
        Activity getActivity();

        void updateList(List<MediaMetadataCompat> datas);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {

    }
}
