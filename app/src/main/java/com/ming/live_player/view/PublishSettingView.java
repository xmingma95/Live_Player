package com.ming.live_player.view;

import android.app.Activity;

/**
 * @author ming
 * @version $Rev$
 * @des ${TODO}
 */
public interface PublishSettingView extends BaseView {

    /**
     * 定位成功
     */
    void doLocationSuccess(String location);

    /**
     * 定位失败
     */
    void doLocationFailed();

    /**
     * 上传成功
     */
    void doUploadSuceess(String url);

    /**
     * 图片上传失败
     */
    void doUploadFailed();

    /**
     * 结束页面
     */
    void finishActivity();

    Activity getActivity();
    void  showMsg(String msg);
}
