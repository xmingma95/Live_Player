package com.ming.live_player.presenter;

import android.content.Intent;
import android.os.Bundle;

import com.ming.live_player.activity.MyVideoPlayerActivity;
import com.ming.live_player.bean.MediaItem;
import com.ming.live_player.view.VideoListView;

import java.util.ArrayList;

/**
 * @author ming
 * @version $Rev$
 * @des 视频列表P层
 */
public class VideoListPresenterImp implements VideoListPresenter {
    private VideoListView videoListView;
    private ArrayList<MediaItem> mediaItems;

    /**
     * 调用视频播放器播放视频
     * @param position 视频列表中的位置
     */
    @Override
    public void startPlayerVideo(ArrayList<MediaItem> mediaItems,int position) {
        //传递播放列表，序列化
        Intent intent=new Intent(videoListView.getContext(), MyVideoPlayerActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("videolist",mediaItems);
        intent.putExtras(bundle);
        intent.putExtra("position",position);
        videoListView.getContext().startActivity(intent);
    }

    @Override
    public void attachView(Object v) {
        this.videoListView= (VideoListView) v;
    }

    @Override
    public void detachView() {
        this.videoListView=null;
    }
}
