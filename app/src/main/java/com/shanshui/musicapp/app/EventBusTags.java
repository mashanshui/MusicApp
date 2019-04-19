package com.shanshui.musicapp.app;

import org.simple.eventbus.EventBus;

/**
 * ================================================
 * 放置 {@link EventBus} 的 Tag, 便于检索
 *
 * @see <a href="https://github.com/JessYanCoding/MVPArms/wiki#3.5">EventBusTags wiki 官方文档</a>
 * Created by MVPArmsTemplate
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public interface EventBusTags {
    /**
     * 切换播放源
     */
    String switchMusicProvide = "Switch_Music_Provide";

    /**
     * 切换播放源完成
     */
    String switchMusicProvideSuccess = "Switch_Music_Provide_Success";

    /**
     * 播放全部音乐
     */
    String playAllMusic = "playAllMusic";
}
