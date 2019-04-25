package com.ming.live_player.presenter;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.ming.live_player.R;
import com.ming.live_player.model.SystemUtil;
import com.ming.live_player.view.PusherView;
import com.ming.live_player.view.custom.FilterDialogFragment;
import com.tencent.rtmp.ITXLivePushListener;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author ming
 * @version $Rev$
 * @des 发起直播页面P层
 */
public class PusherPresenterImp implements PusherPresenter, ITXLivePushListener, FilterDialogFragment.FilterCallback {
    private PusherView pusherView;
    private static final char[] DIGITS_LOWER =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private TXLivePusher mTXLivePusher;
    private TXCloudVideoView mTXCloudVideoView;

    private PopupWindow mSettingPopup;
    private boolean mFlashOn = false;
    private boolean isBeauty;
    private FilterDialogFragment mFilterDialogFragment;
    private SystemUtil systemUitl;

    private int mLocX;
    private int mLocY;

    /**
     * 这里应该从后端获取RTMP 推流URL，同时获得账号信息（头像，昵称等），由于没有做后台就不那么麻烦
     * 直接拼接
     * @param domain 域名
     * @param key  推流防盗链key
     * @param streamId 流ID
     * @param txTime 有效期
     * @return
     */
    @Override
    public void getPusherUrl(String domain,String key, String streamId, long txTime) {
        String input = new StringBuilder().
                append(key).
                append(streamId).
                append(Long.toHexString(txTime).toUpperCase()).toString();

        String txSecret = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            txSecret  = byteArrayToHexString(
                    messageDigest.digest(input.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String rtmpUrl= txSecret == null ? "" :
                new StringBuilder().
                        append("rtmp://").
                        append(domain).
                        append("/live/").
                        append(streamId).
                        append("?txSecret=").
                        append(txSecret).
                        append("&").
                        append("txTime=").
                        append(Long.toHexString(txTime).toUpperCase()).
                        toString();
        int errorCode=rtmpUrl.equals("")?1:0;
        pusherView.onGetPushUrl(rtmpUrl,errorCode);
    }

    private static String byteArrayToHexString(byte[] data) {
        char[] out = new char[data.length << 1];

        for (int i = 0, j = 0; i < data.length; i++) {
            out[j++] = DIGITS_LOWER[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS_LOWER[0x0F & data[i]];
        }
        return new String(out);
    }

    @Override
    public void startPusher(TXCloudVideoView videoView, TXLivePushConfig pusherConfig, String pushUrl) {
        if (mTXLivePusher == null) {
            mTXLivePusher = new TXLivePusher(pusherView.getContext());
            mTXLivePusher.setConfig(pusherConfig);
        }
        mTXCloudVideoView = videoView;
        mTXCloudVideoView.setVisibility(View.VISIBLE);
        mTXLivePusher.startCameraPreview(mTXCloudVideoView);
        mTXLivePusher.setPushListener(this);
        mTXLivePusher.startPusher(pushUrl);
    }

    @Override
    public void stopPusher() {
        if (mTXLivePusher != null) {
            mTXLivePusher.stopCameraPreview(false);
            mTXLivePusher.setPushListener(null);
            mTXLivePusher.stopPusher();
        }
    }

    @Override
    public void resumePusher() {
        if (mTXLivePusher != null) {
            Log.e("resumePusher","=====");
            mTXLivePusher.resumePusher();
         //   mTXLivePusher.startCameraPreview(mTXCloudVideoView);
           // mTXLivePusher.resumeBGM();
        }
    }

    @Override
    public void pausePusher() {
        if (mTXLivePusher != null) {
            //mTXLivePusher.pauseBGM();
            mTXLivePusher.pausePusher();
        }
    }

    /**
     *设置按钮
     */
    @Override
    public void showSettingPopupWindow(final View targetView, int[] locations) {
        targetView.setBackgroundResource(R.drawable.icon_setting_down);
        if (mSettingPopup == null) {
            View contentView = LayoutInflater.from(pusherView.getContext()).inflate(R.layout.live_host_setting, null);
            contentView.findViewById(R.id.ll_live_setting_flash).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mTXLivePusher.turnOnFlashLight(!mFlashOn)){
                        mFlashOn = !mFlashOn;
                        mSettingPopup.dismiss();
                    }else{
                        Toast.makeText(pusherView.getContext(),
                                "打开闪光灯失败:绝大部分手机不支持前置闪光灯!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            contentView.findViewById(R.id.ll_live_setting_changecamera).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSettingPopup.dismiss();
                    mTXLivePusher.switchCamera();
                }
            });

            contentView.findViewById(R.id.ll_live_setting_beauty).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSettingPopup.dismiss();

                    //style             磨皮风格：  0：光滑  1：自然  2：朦胧
                    //beautyLevel       磨皮等级： 取值为 0-9.取值为 0 时代表关闭美颜效果.默认值: 0,即关闭美颜效果.
                    //whiteningLevel    美白等级： 取值为 0-9.取值为 0 时代表关闭美白效果.默认值: 0,即关闭美白效果.
                    //ruddyLevel        红润等级： 取值为 0-9.取值为 0 时代表关闭美白效果.默认值: 0,即关闭美白效果.
                    if (isBeauty) {
                        //不开启美颜
                        mTXLivePusher.setBeautyFilter(0, 0,0,0);
                        isBeauty = !isBeauty;
                    } else {
                        if (!mTXLivePusher.setBeautyFilter(1,5,7, 3)) {
                            Toast.makeText(pusherView.getContext(), "手机不支持美颜效果", Toast.LENGTH_SHORT);
                        } else {
                            isBeauty = !isBeauty;
                        }
                    }
                    //                    mBeautyDialogFragment.show(mPusherView.getFragmentMgr(), "");

                }
            });

            contentView.findViewById(R.id.ll_live_setting_filter).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSettingPopup.dismiss();
                    mFilterDialogFragment.show(pusherView.getFragmentMgr(), "");
                }
            });


            mSettingPopup = new PopupWindow(contentView, systemUitl.dip2px(100),
                   systemUitl.dip2px(170));
            mSettingPopup.setFocusable(true);
            mSettingPopup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mLocX = locations[0] - (mSettingPopup.getWidth() - targetView
                    .getWidth()) / 2;
            mLocY = locations[1] - (mSettingPopup.getHeight());
            mSettingPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    targetView.setBackgroundResource(R.drawable.icon_setting_up);
                }
            });
        }
        mSettingPopup.showAtLocation(targetView, Gravity.NO_GRAVITY, mLocX, mLocY);


    }

    @Override
    public void attachView(Object v) {
        pusherView= (PusherView) v;
        mFilterDialogFragment = new FilterDialogFragment();
        mFilterDialogFragment.setFilterCallback(this);
        systemUitl =SystemUtil.getInstance(pusherView.getContext());
    }

    @Override
    public void detachView() {
        pusherView=null;
    }

    @Override
    public void onPushEvent(int i, Bundle bundle) {

    }

    @Override
    public void onNetStatus(Bundle bundle) {

    }

    /**
     * 设置滤镜回调
     */
    @Override
    public void setFilter(Bitmap filterBitmap) {
        mTXLivePusher.setFilter(filterBitmap);
    }
}
