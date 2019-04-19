package com.shanshui.musicapp.mvp.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jess.arms.http.imageloader.glide.GlideArms;
import com.shanshui.musicapp.R;
import com.shanshui.musicapp.app.utils.MusicUtil;
import com.shanshui.musicapp.mvp.AppConstant;
import com.shanshui.musicapp.mvp.model.bean.ChartsBean;
import com.shanshui.musicapp.mvp.model.bean.ChartsOutBean;

import java.util.List;

/**
 * @author mashanshui
 * @date 2018-11-07
 * @desc TODO
 */
public class ChartsAdapter extends BaseQuickAdapter<ChartsOutBean, BaseViewHolder> {

    public ChartsAdapter(@Nullable List<ChartsOutBean> data) {
        super(R.layout.recycler_charts_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ChartsOutBean item) {
        List<ChartsBean> songInfo = item.getSonginfo();
        String song1 = TextUtils.isEmpty(songInfo.get(0).getSongname()) ? songInfo.get(0).getAlbumname() : songInfo.get(0).getSongname();
        String song2 = TextUtils.isEmpty(songInfo.get(1).getSongname()) ? songInfo.get(1).getAlbumname() : songInfo.get(1).getSongname();
        String song3 = TextUtils.isEmpty(songInfo.get(2).getSongname()) ? songInfo.get(2).getAlbumname() : songInfo.get(2).getSongname();
        helper.setText(R.id.title, item.getRankname())
                .setText(R.id.tv_music_1, "1." + song1)
                .setText(R.id.tv_music_2, "2." + song2)
                .setText(R.id.tv_music_3, "3." + song3);
        GlideArms.with(mContext).load(MusicUtil.getImageUrl(item.getImgurl(), AppConstant.ONLINE_IMG_SIZE_400))
                .placeholder(R.drawable.default_cover)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into((ImageView) helper.getView(R.id.iv_cover));
    }
}
