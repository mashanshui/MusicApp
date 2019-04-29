package com.shanshui.musicapp.mvp.adapter;

import android.support.annotation.Nullable;
import android.support.v4.media.MediaDescriptionCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.shanshui.musicapp.R;
import com.shanshui.musicapp.mvp.model.bean.MusicBean;
import com.shanshui.musicapp.mvp.model.bean.MusicListPositionMessageBean;

import java.util.List;

/**
 * @author mashanshui
 * @date 2018-11-05
 * @desc TODO
 */
public class MusicListAdapter extends BaseQuickAdapter<MusicBean, BaseViewHolder> {
    public static final int MUSIC_ITEM_0 = 0;
    public static final int MUSIC_ITEM_1 = 1;
    public static final int MUSIC_ITEM_2 = 2;
    private MusicListPositionMessageBean mLastPosition = null;
    private int mCurrentPosition = 0;

    public MusicListAdapter(@Nullable List<MusicBean> data) {
        super(data);
        setMultiTypeDelegate(new MultiTypeDelegate<MusicBean>() {
            @Override
            protected int getItemType(MusicBean musicBean) {
                return musicBean.getItemType();
            }
        });
        getMultiTypeDelegate()
                .registerItemType(MUSIC_ITEM_0, R.layout.recycler_music_item0)
                .registerItemType(MUSIC_ITEM_1, R.layout.recycler_music_item1)
                .registerItemType(MUSIC_ITEM_2, R.layout.recycler_music_item2);
    }

    @Override
    protected void convert(BaseViewHolder helper, MusicBean item) {
        MediaDescriptionCompat mediaDescription = item.getMetadata();
        switch (helper.getItemViewType()) {
            case MUSIC_ITEM_0:
                helper.setText(R.id.tv_artist, mediaDescription.getSubtitle())
                        .setText(R.id.tv_song_name, mediaDescription.getTitle())
                        .addOnClickListener(R.id.iv_download);
                break;
            case MUSIC_ITEM_1:
                helper.setText(R.id.tv_music_name, mediaDescription.getTitle())
                        .setText(R.id.tv_music_singer, mediaDescription.getSubtitle());
                break;
            case MUSIC_ITEM_2:
                helper.setText(R.id.tv_artist, mediaDescription.getSubtitle())
                        .setText(R.id.tv_song_name, mediaDescription.getTitle());
                break;
            default:
                break;
        }
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    public void setCurrentPosition(int mCurrentPosition) {
        if (mLastPosition != null) {
            getItem(mLastPosition.getPosition()).setItemType(mLastPosition.getItemType());
            notifyItemChanged(mLastPosition.getPosition());
        }
        this.mCurrentPosition = mCurrentPosition;
        MusicBean musicBean = getItem(mCurrentPosition);

        MusicListPositionMessageBean messageBean = new MusicListPositionMessageBean();
        messageBean.setItemType(musicBean.getItemType());
        messageBean.setPosition(mCurrentPosition);
        mLastPosition = messageBean;

        musicBean.setItemType(MusicListAdapter.MUSIC_ITEM_2);
        getData().set(mCurrentPosition, musicBean);
        notifyItemChanged(mCurrentPosition);
    }
}
