package com.shanshui.musicapp.mvp.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanshui.musicapp.R;

import java.util.List;

/**
 * @author mashanshui
 * @date 2019-04-19
 * @desc TODO
 */
public class SearchHistoryAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public SearchHistoryAdapter(@Nullable List<String> data) {
        super(R.layout.recycler_search_history, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_name, item);
    }
}
