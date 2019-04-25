package com.ming.live_player.view;

/**
 * @author ming
 * @version $Rev$
 * @des 视频播放器V层
 */
public interface MyVideoPlayerView extends BaseView{
    void updateTopName(String name);
    void setEnable(boolean isEnable);
    void setButtonState();
    void setFullScreenAndDefault();
    void showMediaController();
    void hideMediaController();
    void setPath(String path);
    //声音和亮度
    void showSeekbar(boolean isVoice);
    void hideSeekbar(boolean isVoice);
    void setSeekbar(int progress,boolean isVoice);
}
