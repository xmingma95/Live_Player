package com.ming.live_player.presenter;

import android.view.View;

import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.ui.TXCloudVideoView;

/**
 * @author ming
 * @version $Rev$
 * @des ${TODO}
 */
public interface PusherPresenter extends BasePresenter {

    void getPusherUrl(String domain,String key, String streamId, long txTime);

    void startPusher(TXCloudVideoView videoView, TXLivePushConfig pusherConfig, String pushUrl);

    void stopPusher();

    void resumePusher();

    void pausePusher();

    void showSettingPopupWindow(View targetView, int[] locations);
}
