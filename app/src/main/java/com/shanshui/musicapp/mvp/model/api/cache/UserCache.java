package com.shanshui.musicapp.mvp.model.api.cache;

import com.shanshui.musicapp.mvp.model.bean.ChartsBean;
import com.shanshui.musicapp.mvp.model.bean.SingerBean;
import com.shanshui.musicapp.mvp.model.bean.SingerCategoryBean;
import com.shanshui.musicapp.mvp.model.bean.SongSheetBean;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.rx_cache2.EvictProvider;
import io.rx_cache2.LifeCache;
import io.rx_cache2.Reply;

/**
 * @author mashanshui
 * @date 2018-11-07
 * @desc TODO
 */
public interface UserCache {

    @LifeCache(duration = 1, timeUnit = TimeUnit.HOURS)
    Observable<Reply<ChartsBean>> getMusicCharts(Observable<ChartsBean> chartsBean, EvictProvider evictProvider);

    @LifeCache(duration = 1, timeUnit = TimeUnit.HOURS)
    Observable<Reply<SongSheetBean>> getSongSheet(Observable<SongSheetBean> songSheetBean, EvictProvider evictProvider);

    @LifeCache(duration = 1, timeUnit = TimeUnit.HOURS)
    Observable<Reply<SingerCategoryBean>> getSingerCategory(Observable<SingerCategoryBean> singerBean, EvictProvider evictProvider);

    @LifeCache(duration = 1, timeUnit = TimeUnit.HOURS)
    Observable<Reply<SingerBean>> getSingerList(Observable<SingerBean> singerBean, EvictProvider evictProvider);
}