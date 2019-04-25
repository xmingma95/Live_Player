package com.ming.live_player.presenter;

import android.app.Activity;
import android.net.Uri;

/**
 * @author ming
 * @version $Rev$
 * @des 直播发布设置页面
 */
public interface PublishSettingPresenter extends BasePresenter{


    /**
     * 检查推流权限
     */
    boolean checkPublishPermission(Activity activity);

    /**
     * 检查录制权限
     */
    boolean checkScrRecordPermission();

    /**
     * 截取图片
     */
     Uri cropImage(Uri uri,int requestCode);

    /**
     * 开始直播
     */
    void doPublish(String title, int liveType, String location, int bitrateType, boolean isRecord);

    /**
     * 直播定位
     */
    void doLocation();

    /**
     * 选择图片方式：相册、相机
     */
    Uri pickImage(boolean mPermission, int type);

    /**
     * 上传图片
     */
    void doUploadPic(String path);
}
