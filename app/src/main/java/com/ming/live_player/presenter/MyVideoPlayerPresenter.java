package com.ming.live_player.presenter;

/**
 * @author ming
 * @version $Rev$
 * @des 视频播放器P层
 */
public interface MyVideoPlayerPresenter extends BasePresenter{
    void playVideo(int position);
    void updateVoice(int progress);
    void updateBrightness(int progress);
}
