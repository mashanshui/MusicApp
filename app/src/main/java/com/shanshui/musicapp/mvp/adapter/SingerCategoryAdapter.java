package com.shanshui.musicapp.mvp.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.shanshui.musicapp.R;
import com.shanshui.musicapp.mvp.model.bean.SingerCategoryBean;

import java.util.List;

/**
 * @author mashanshui
 * @date 2019-01-20
 * @desc TODO
 */
public class SingerCategoryAdapter extends BaseQuickAdapter<SingerCategoryBean, BaseViewHolder> {

    public SingerCategoryAdapter(@Nullable List<SingerCategoryBean> data) {
        super(R.layout.recycler_singer_category_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SingerCategoryBean item) {
        QMUIRoundButton button = helper.getView(R.id.btn_singer_category);
        button.setChangeAlphaWhenPress(true);
        button.setText(item.getClassname());
        helper.addOnClickListener(R.id.btn_singer_category);
    }
}
