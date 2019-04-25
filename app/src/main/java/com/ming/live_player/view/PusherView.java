package com.ming.live_player.view;

import android.app.FragmentManager;

/**
 * @des 直播页面
 */
public interface PusherView extends BaseView {
    /**
     * @param pushUrl
     * @param errorCode 0表示成功 1表示失败
     */
    void onGetPushUrl(String pushUrl, int errorCode);
    FragmentManager getFragmentMgr();
    void finish();
}
