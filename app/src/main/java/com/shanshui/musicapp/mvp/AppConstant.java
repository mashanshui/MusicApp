package com.shanshui.musicapp.mvp;

/**
 * @author mashanshui
 * @date 2018-11-08
 * @desc TODO
 */
public final class AppConstant {
    private AppConstant() {
    }

    /**
     * 图片可用尺寸
     */
    public static final int ONLINE_IMG_SIZE_80 = 80;
    public static final int ONLINE_IMG_SIZE_100 = 100;
    public static final int ONLINE_IMG_SIZE_120 = 120;
    public static final int ONLINE_IMG_SIZE_150 = 150;
    public static final int ONLINE_IMG_SIZE_160 = 160;
    public static final int ONLINE_IMG_SIZE_200 = 200;
    public static final int ONLINE_IMG_SIZE_240 = 240;
    public static final int ONLINE_IMG_SIZE_260 = 260;
    public static final int ONLINE_IMG_SIZE_400 = 400;
    public static final int ONLINE_IMG_SIZE_480 = 480;
    public static final int ONLINE_IMG_SIZE_720 = 720;

    /**
     * statusBarAlpha
     */
    public static final int DEFAULT_STATUS_BAR_ALPHA = 40;

    /**
     * 分页默认数量
     */
    public static final int PAGE_SIZE = 30;
    public static final String EXTRA_CURRENT_MEDIA_DESCRIPTION =
            "com.shanshui.musicapp.CURRENT_MEDIA_DESCRIPTION";

    /**
     * 本地音乐提供者
     */
    public static final String PROVIDE_LOCAL_MUSIC = "PROVIDE_LOCAL_MUSIC";

    /**
     * 网络音乐
     */
    public static final int NETWORK_MUSIC = 0;

    /**
     * 本地音乐
     */
    public static final int LOCAL_MUSIC = 1;

    /**
     * 保存搜索历史数据
     */
    public static final String PREF_USER_SEARCH_HISTORY = "PREF_USER_SEARCH_HISTORY";

    /**
     * 自定义行为——》播放模式
     */
    public static final String ACTION_PLAY_MODE = "ACTION_PLAY_MODE";

    /**
     * 播放模式——》顺序播放
     */
    public static final int PLAY_MODE_ORDER = 0;

    /**
     * 播放模式——》单曲循环
     */
    public static final int PLAY_MODE_LOOP = 1;

    /**
     * 播放模式——》随机播放
     */
    public static final int PLAY_MODE_RANDOM = 2;

}
