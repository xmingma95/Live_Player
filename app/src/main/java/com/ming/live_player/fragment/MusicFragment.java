package com.ming.live_player.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ming.live_player.R;
import com.ming.live_player.activity.AudioPlayerActivity;
import com.ming.live_player.adapter.VideoItemAdapter;
import com.ming.live_player.bean.MediaItem;

import java.util.ArrayList;

/**
 * @author ming
 * @version $Rev$
 * @des 音乐主页面
 */
public class MusicFragment extends BaseFragment {

    private ListView lv_video;
    private TextView tv__video_content;
    private ProgressBar pb_video_loading;
    private MediaItem mMediaItem;
    private ArrayList<MediaItem> mediaItems;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(mediaItems!=null&&mediaItems.size()>0){
                VideoItemAdapter musicAdapter=new VideoItemAdapter(mContext,mediaItems,false);
                lv_video.setAdapter(musicAdapter);
                tv__video_content.setVisibility(View.GONE);
            }else {
                tv__video_content.setVisibility(View.VISIBLE);
                tv__video_content.setText("没有发现音频……");
            }
            pb_video_loading.setVisibility(View.GONE);

        }
    };

    @Override
    protected View initView() {
        System.out.println("进入本地视频");
        View view =View.inflate(mContext, R.layout.fragment_music,null);
        lv_video =(ListView)view.findViewById(R.id.lv_video);
        tv__video_content =(TextView)view.findViewById(R.id.tv_video_content);
        pb_video_loading =(ProgressBar)view.findViewById(R.id.pb_video_loading);
        lv_video.setOnItemClickListener(new MyOnItemClickListener());
        return view;

    }

    @Override
    protected void initData() {
        super.initData();
        getDataFromLocal();

    }

    public void getDataFromLocal() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                //通过内容提供者获取视频信息
                mediaItems=new ArrayList<>();
                ContentResolver resolver=mContext.getContentResolver();
                Uri uri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                String[] objs={
                        MediaStore.Audio.Media.DISPLAY_NAME,//视频文件在sdcard的名称
                        MediaStore.Audio.Media.DURATION,//视频总时长
                        MediaStore.Audio.Media.SIZE,//视频的文件大小
                        MediaStore.Audio.Media.DATA,//视频的绝对地址
                        MediaStore.Audio.Media.ARTIST,//歌曲的演唱者
                };
                Cursor cursor=resolver.query(uri,objs,null,null,null);
                if(cursor!=null){
                    while(cursor.moveToNext()){
                        mMediaItem=new MediaItem();
                        String name=cursor.getString(0);
                        mMediaItem.setName(name);

                        long duration=cursor.getLong(1);
                        mMediaItem.setDuration(duration);

                        long size=cursor.getLong(2);
                        mMediaItem.setSize(size);

                        String data=cursor.getString(3);
                        mMediaItem.setData(data);

                        String artist=cursor.getString(4);
                        mMediaItem.setArtist(artist);
                        mediaItems.add(mMediaItem);

                    }
                    cursor.close();
                }
                handler.sendEmptyMessage(10);
            }

        }.start();

    }


    private class MyOnItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Intent intent=new Intent(mContext, AudioPlayerActivity.class);
            Log.e("position","==="+position);
            intent.putExtra("position",position);
            startActivity(intent);


        }
    }
}
