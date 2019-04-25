package com.ming.live_player.presenter;

import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.ming.live_player.activity.MyVideoPlayerActivity;
import com.ming.live_player.bean.MediaItem;
import com.ming.live_player.view.MyVideoPlayerView;

import java.util.ArrayList;

/**
 * @author ming
 * @version $Rev$
 * @des 播放功能P层实现
 */
public class MyVideoPlayerPresenterImp implements MyVideoPlayerPresenter {
    private MyVideoPlayerView myVideoPlayerView;
    private ArrayList<MediaItem> mediaItems;
    private AudioManager am;
    private Uri uri;
    public MyVideoPlayerPresenterImp(ArrayList<MediaItem> mediaItems,Uri uri) {
        this.mediaItems=mediaItems;
        this.uri=uri;
    }

    @Override
    public void playVideo(int position) {
        if(mediaItems!=null&&mediaItems.size()>0){
            if(position>=0){
                MediaItem mediaitem=mediaItems.get(position);
                Log.e("playerInMyVideo",mediaitem.toString());
                myVideoPlayerView.updateTopName(mediaitem.getName());
                myVideoPlayerView.setPath(mediaitem.getData());
                myVideoPlayerView.setButtonState();
            }
        }else if(uri!=null){
            myVideoPlayerView.setEnable(false);
        }
    }

    @Override
    public void updateVoice(int progress) {
        myVideoPlayerView.showSeekbar(true);
        am.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
        myVideoPlayerView.setSeekbar(progress,true);
        myVideoPlayerView.hideSeekbar(true);
    }

    @Override
    public void updateBrightness(int progress) {
        myVideoPlayerView.showSeekbar(false);
        changeAppBrightness(progress);
        myVideoPlayerView.setSeekbar(progress,false);
        myVideoPlayerView.hideSeekbar(false);

    }

    private void changeAppBrightness(int brightness) {
        Window window = ((MyVideoPlayerActivity)myVideoPlayerView.getContext()).getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        if (brightness == -1) {
            lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
        } else {
            lp.screenBrightness = (brightness <= 0 ? 1 : brightness) / 255f;
        }
        window.setAttributes(lp);
    }




    @Override
    public void attachView(Object v) {
        this.myVideoPlayerView= (MyVideoPlayerView) v;
        am = (AudioManager) myVideoPlayerView.getContext().getSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    public void detachView() {
        this.myVideoPlayerView=null;
    }
}
