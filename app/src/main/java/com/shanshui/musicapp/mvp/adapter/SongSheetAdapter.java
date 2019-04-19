package com.shanshui.musicapp.mvp.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jess.arms.http.imageloader.glide.GlideArms;
import com.shanshui.musicapp.R;
import com.shanshui.musicapp.app.utils.MusicUtil;
import com.shanshui.musicapp.mvp.AppConstant;
import com.shanshui.musicapp.mvp.model.bean.SongSheetBean;

import java.util.List;

/**
 * @author mashanshui
 * @date 2019-01-16
 * @desc TODO
 */
public class SongSheetAdapter extends BaseQuickAdapter<SongSheetBean, BaseViewHolder> {

    public SongSheetAdapter(@Nullable List<SongSheetBean> data) {
        super(R.layout.recycler_song_sheet_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SongSheetBean item) {
        helper.setText(R.id.tv_play_count, MusicUtil.formatNumToString(item.getPlaycount()))
                .setText(R.id.tv_creater_user, item.getUsername())
                .setText(R.id.tv_introduction, item.getSpecialname());
        GlideArms.with(mContext).load(MusicUtil.getImageUrl(item.getImgurl(), AppConstant.ONLINE_IMG_SIZE_400))
                .placeholder(R.drawable.default_cover)
                .into((ImageView) helper.getView(R.id.iv_cover));
    }
}
