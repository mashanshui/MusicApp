package com.shanshui.musicapp.mvp.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanshui.musicapp.R;
import com.shanshui.musicapp.mvp.model.DrawerList;

import java.util.List;

/**
 * @author mashanshui
 * @date 2018-11-02
 * @desc TODO
 */
public class DrawerAdapter extends BaseQuickAdapter<DrawerList, BaseViewHolder> {


    public DrawerAdapter(@Nullable List<DrawerList> data) {
        super(R.layout.item_drawer_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DrawerList item) {
        helper.setText(R.id.tv_item_menu, item.getResTitleId())
                .setBackgroundRes(R.id.iv_item_menu, item.getResIconId());
    }
}