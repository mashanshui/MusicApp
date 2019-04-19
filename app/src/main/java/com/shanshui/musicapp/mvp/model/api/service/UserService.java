/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.shanshui.musicapp.mvp.model.api.service;

import com.shanshui.musicapp.mvp.model.bean.ChartsBean;
import com.shanshui.musicapp.mvp.model.bean.MusicListBean;
import com.shanshui.musicapp.mvp.model.bean.MusicSourceInfoBean;
import com.shanshui.musicapp.mvp.model.bean.SingerBean;
import com.shanshui.musicapp.mvp.model.bean.SingerCategoryBean;
import com.shanshui.musicapp.mvp.model.bean.SongSheetBean;
import com.shanshui.musicapp.mvp.model.entity.Response2;
import com.shanshui.musicapp.mvp.model.entity.ResponseListObject;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;


/**
 * ================================================
 * 展示 {@link Retrofit#create(Class)} 中需要传入的 ApiService 的使用方式
 * 存放关于用户的一些 API
 * <p>
 * Created by JessYan on 08/05/2016 12:05
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public interface UserService {

    /**
     * @return 获取音乐排行榜分类列表
     */
//    @GET("rank/list&json=true")
    @GET("http://mobilecdnbj.kugou.com/api/v3/rank/list?apiver=6&withsong=1&showtype=2&plat=0&parentid=0&area_code=1&version=9068")
    Observable<ChartsBean> getMusicCharts();

    /**
     * @return 获取音乐排行榜下的歌曲列表
     */
    @GET("http://mobilecdngz.kugou.com/api/v3/rank/song?volid=36080&area_code=1&version=9068&plat=0")
    Observable<Response2<ResponseListObject<MusicListBean>>> getMusicChartsDetail(@Query("rankid") String rankid, @Query("page") int page, @Query("pagesize") int pagesize);

    /**
     * @param page
     * @return 获取歌单列表
     */
    @GET("plist/index&json=true")
    Observable<SongSheetBean> getSongSheet(@Query("page") int page);

    /**
     * @return 获取歌单列表下的歌曲列表
     */
    @GET("http://mobilecdn.kugou.com/api/v3/special/song?with_cover=1&plat=0&area_code=1&version=9068")
    Observable<Response2<ResponseListObject<MusicListBean>>> getSongSheetDetail(@Query("specialid") String specialid, @Query("page") int page, @Query("pagesize") int pagesize);


    /**
     * @return 获取歌手标签
     */
    @GET("singer/class&json=true")
    Observable<SingerCategoryBean> getSingerCategory();

    /**
     * @return 获取歌手排行榜
     */
    @GET("http://mobilecdnbj.kugou.com/api/v5/singer/list?showtype=1&sextype=0&version=9068&sort=1&plat=0&musician=0&type=0")
    Observable<SingerBean> getSingerList(@Query("page") int page, @Query("pagesize") int pageSize);

    /**
     * @return 获取歌手列表下的歌曲列表
     */
    @GET("http://mobilecdn.kugou.com/api/v3/singer/song?plat=0&sorttype=2&area_code=1&version=9068")
    Observable<Response2<ResponseListObject<MusicListBean>>> getSingerDetail(@Query("singerid") String singerid, @Query("page") int page, @Query("pagesize") int pagesize);


    /**
     * @param hash
     * @return 获取歌曲的播放信息（包括播放url）
     */
    @GET("app/i/getSongInfo.php?cmd=playInfo")
    Observable<MusicSourceInfoBean> getMusicInfo(@Query("hash") String hash);
}
