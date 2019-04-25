package com.ming.live_player.presenter;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.ming.live_player.bean.MediaItem;
import com.ming.live_player.view.PlayerView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author ming
 * @version $Rev$
 * @des ${TODO}
 */
public class PlayerPresenterImp implements PlayerPresenter{
    private PlayerView playerView;
    private MediaItem mMediaItem;
   // private ArrayList<MediaItem> mediaItems;
    //Integer 标记存放目录位置，为listView的position
    private HashMap<Integer,ArrayList<MediaItem>> mediaMap;
    //目录不重复，两个HashMap互查实现
    private HashMap<String,Integer> setName;
    private int index=0;
    private static final String TAG=PlayerPresenterImp.class.getSimpleName();

    @Override
    public void getDataFromLocal() {
        //使用Rxjava管理线程
        Observable.create(new ObservableOnSubscribe<HashMap<Integer,ArrayList<MediaItem>>>() {
            @Override
            public void subscribe(ObservableEmitter<HashMap<Integer,ArrayList<MediaItem>>> emitter) {
                //通过内容提供者获取视频信息
                Log.d(TAG,"start getDataFromLocal");

                ContentResolver resolver=playerView.getContext().getContentResolver();
                Uri uri= MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                String[] objs={
                        MediaStore.Video.Media.DISPLAY_NAME,//视频文件在sdcard的名称
                        MediaStore.Video.Media.DURATION,//视频总时长
                        MediaStore.Video.Media.SIZE,//视频的文件大小
                        MediaStore.Video.Media.DATA,//视频的绝对地址
                        MediaStore.Video.Media.ARTIST,//歌曲的演唱者
                };
                Cursor cursor=resolver.query(uri,objs,null,null,null);
                if(cursor!=null){
                    Log.d(TAG, "cursor!=null"+cursor.getCount());
                    mediaMap=new HashMap<>();
                    setName=new HashMap<>();
                    while(cursor.moveToNext()){
                        //把所有信息存入MediaItem
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
                        File file = new File(data);
                        //找到前一级目录
                        String s=file.getParent();
                        String parentName=s.substring(s.lastIndexOf('/')+1);
                        mMediaItem.setParentName(parentName);
                        //查看目录是否相同
                        if(!setName.containsKey(parentName)){
                            ArrayList<MediaItem> mediaItems=new ArrayList<>();
                            mediaItems.add(mMediaItem);
                            setName.put(parentName,index);
                            mediaMap.put(index++,mediaItems);
                        }else{
                            //目录存在
                            mediaMap.get(setName.get(parentName))
                                    .add(mMediaItem);
                        }

                    }
                    cursor.close();
                }
                //主线程切换UI
                Log.e(TAG, "mediapackage"+mediaMap.size() );
                emitter.onNext(mediaMap);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HashMap<Integer, ArrayList<MediaItem>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HashMap<Integer, ArrayList<MediaItem>> mediaMap) {
                        playerView.showPackageList(mediaMap);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }



    @Override
    public void attachView(Object v) {
        this.playerView= (PlayerView) v;
    }

    @Override
    public void detachView() {
        this.playerView=null;
    }
}
