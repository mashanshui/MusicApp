package com.shanshui.musicapp.mvp.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanshui.musicapp.R;
import com.shanshui.musicapp.mvp.model.bean.SearchHotBean;

import java.util.List;

/**
 * @author mashanshui
 * @date 2019-04-19
 * @desc TODO
 */
public class SearchHotAdapter extends BaseQuickAdapter<SearchHotBean, BaseViewHolder> {

    public SearchHotAdapter(@Nullable List<SearchHotBean> data) {
        super(R.layout.recycler_search_hot, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SearchHotBean item) {
        helper.setText(R.id.btn_search_hot, item.getKeyword())
                .addOnClickListener(R.id.btn_search_hot);
    }
}
