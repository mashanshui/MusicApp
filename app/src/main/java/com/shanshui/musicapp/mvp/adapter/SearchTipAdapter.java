package com.shanshui.musicapp.mvp.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanshui.musicapp.R;
import com.shanshui.musicapp.mvp.model.bean.SearchTipBean;

import java.util.List;

/**
 * @author mashanshui
 * @date 2019-04-22
 * @desc TODO
 */
public class SearchTipAdapter extends BaseQuickAdapter<SearchTipBean, BaseViewHolder> {

    public SearchTipAdapter(@Nullable List<SearchTipBean> data) {
        super(R.layout.recycler_search_tip,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SearchTipBean item) {
        helper.setText(R.id.tv_search_tip, item.getKeyword());
    }
}
