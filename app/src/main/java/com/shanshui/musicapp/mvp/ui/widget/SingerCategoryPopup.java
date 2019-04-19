package com.shanshui.musicapp.mvp.ui.widget;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;

import com.shanshui.musicapp.R;
import com.shanshui.musicapp.mvp.adapter.SingerCategoryAdapter;
import com.shanshui.musicapp.mvp.model.bean.SingerCategoryBean;

import java.util.List;

import razerdp.basepopup.BasePopupWindow;

/**
 * @author mashanshui
 * @date 2019-01-20
 * @desc TODO
 */
public class SingerCategoryPopup extends BasePopupWindow {
    private List<SingerCategoryBean> singerCategoryBeans;
    private SingerCategoryAdapter adapter;

    public SingerCategoryPopup(Context context,List<SingerCategoryBean> singerCategoryBeans) {
        super(context, true);
        this.singerCategoryBeans = singerCategoryBeans;
        delayInit();
    }

    @Override
    public View onCreateContentView() {
        View view = createPopupById(R.layout.popup_singer_category);
        RecyclerView recyclerView = view.findViewById(R.id.rv_popup_singer);
        adapter = new SingerCategoryAdapter(singerCategoryBeans);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(adapter);
        return view;
    }

    // 以下为可选代码（非必须实现）
    // 返回作用于PopupWindow的show和dismiss动画，本库提供了默认的几款动画，这里可以自由实现
    @Override
    protected Animation onCreateShowAnimation() {
        return getDefaultScaleAnimation(true);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getDefaultScaleAnimation(false);
    }
}
