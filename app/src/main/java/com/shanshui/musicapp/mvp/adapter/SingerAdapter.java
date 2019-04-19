package com.shanshui.musicapp.mvp.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jess.arms.http.imageloader.glide.GlideArms;
import com.shanshui.musicapp.R;
import com.shanshui.musicapp.app.utils.MusicUtil;
import com.shanshui.musicapp.mvp.AppConstant;
import com.shanshui.musicapp.mvp.model.bean.SingerBean;

import java.util.List;

/**
 * @author mashanshui
 * @date 2019-01-17
 * @desc TODO
 */
public class SingerAdapter extends BaseQuickAdapter<SingerBean, BaseViewHolder> {

    public SingerAdapter(@Nullable List<SingerBean> data) {
        super(R.layout.recycler_singer_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SingerBean item) {
        helper.setText(R.id.tv_rank_num, String.valueOf(helper.getLayoutPosition()))
                .setText(R.id.tv_singer_name, item.getSingername())
                .setText(R.id.tv_hot_num, MusicUtil.formatNumToString(item.getHeat()))
                .setText(R.id.tv_fans_num, MusicUtil.formatNumToString(item.getFanscount()));
        GlideArms.with(mContext).load(MusicUtil.getImageUrl(item.getImgurl(), AppConstant.ONLINE_IMG_SIZE_120))
                .placeholder(R.drawable.ic_singer_default)
                .into((ImageView) helper.getView(R.id.iv_singer_icon));
    }
}
