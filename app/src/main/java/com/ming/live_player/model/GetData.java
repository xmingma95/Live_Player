package com.ming.live_player.model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.ming.live_player.bean.MediaItem;

import java.util.ArrayList;

/**
 * @author ming
 * @version $Rev$
 * @des 提供播放数据
 */
public class GetData {

    private volatile static GetData instance;
    private ArrayList<MediaItem> mediaItems;
    private MediaItem mMediaItem;

    public static GetData getInstance(){
        if(instance==null){
            synchronized (GetData.class){
                if(instance==null){
                    instance=new GetData();
                }
            }
        }
        return instance;
    }

    /**
     * 获取视频/音频信息
     */

    public  ArrayList<MediaItem> getDataFromLocal(final Context context) {
        mediaItems =new ArrayList<>();
        Thread thread=new Thread(){
            @Override
            public void run() {
                super.run();
                //通过内容提供者获取视频信息
                ContentResolver resolver=context.getContentResolver();
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
                        Log.e("MusicList:","cursor!=null====");
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
                        Log.e("MusicList:",mediaItems.get(0).getName());
                    }
                    cursor.close();
                }
            }
        };
        thread.start();
        try {
            //等待线程结束
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mediaItems;
    }

}
