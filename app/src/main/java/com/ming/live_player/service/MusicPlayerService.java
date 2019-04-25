package com.ming.live_player.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.ming.live_player.IMusicPlayerService;
import com.ming.live_player.R;
import com.ming.live_player.activity.AudioPlayerActivity;
import com.ming.live_player.bean.MediaItem;
import com.ming.live_player.model.CacheUtils;
import com.ming.live_player.model.GetData;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;

public class MusicPlayerService extends Service {
    private ArrayList<MediaItem> mediaItems;
    private MediaItem mMediaItem;
    private int position;
    private MediaPlayer mediaPlayer;
    private String TAG="MusicPlayerService";
    String id = "live_player_music";
    String name="live_player";
    private GetData getData;

    /**
     * 顺序播放
     */
    public static final int REPEAT_NORMAL = 1;
    /**
     * 单曲循环
     */
    public static final int REPEAT_SINGLE = 2;
    /**
     * 全部循环
     */
    public static final int REPEAT_ALL = 3;

    /**
     * 播放模式
     */
    private int playMode = REPEAT_NORMAL;

    private String MODE="LIVE_PLAYER_MODE";

    @Override
    public void onCreate() {
        super.onCreate();
        playMode = CacheUtils.getPlaymode(this,MODE);
        //获取音乐列表

        getData=GetData.getInstance();
        mediaItems=getData.getDataFromLocal(this);
    }

    public MusicPlayerService() {
    }

    /**
     * 绑定AIDL
     */
    private IMusicPlayerService.Stub stub =new IMusicPlayerService.Stub(){
        //千万别忘了实例化
        MusicPlayerService service=MusicPlayerService.this;
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void openAudio(int position) throws RemoteException {
            service.openAudio(position);
        }

        @Override
        public void start() throws RemoteException {
            service.start();
        }

        @Override
        public void pause() throws RemoteException {
            service.pause();
        }

        @Override
        public void stop() throws RemoteException {
            service.stop();
        }

        @Override
        public int getCurrentPosition() throws RemoteException {
            return service.getCurrentPosition();
        }

        @Override
        public int getDuration() throws RemoteException {
            return service.getDuration();
        }

        @Override
        public String getArtist() throws RemoteException {
            return service.getArtist();
        }

        @Override
        public String getAudioPath() throws RemoteException {
            return service.getAudioPath();
        }

        @Override
        public String getName() throws RemoteException {
            return service.getName();
        }

        @Override
        public void playNext() throws RemoteException {
            service.playNext();
        }

        @Override
        public void playpre() throws RemoteException {
            service.playpre();
        }

        @Override
        public void playMusic(int position) throws RemoteException {
            service.playMusic(position);
        }

        @Override
        public void setPlayMode(int playMode) throws RemoteException {
            service.setPlayMode(playMode);
        }

        @Override
        public int getPlayMode() throws RemoteException {
            return service.getPlayMode();
        }

        @Override
        public void seekTo(int progress) throws RemoteException {
            mediaPlayer.seekTo(progress);
        }

        @Override
        public boolean isPlaying() throws RemoteException {
            return service.isPlaying();
        }


    };



    /**
     * 绑定方式启动服务时，返回AIDL
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }

    private void openAudio(int position){
        synchronized (this){
            this.position=position;
            if(mediaItems!=null&&mediaItems.size()>0){
                mMediaItem=mediaItems.get(position);

                if(mediaPlayer!=null){
                    // mediaPlayer.release();
                    mediaPlayer.reset();
                }
                try {
                    mediaPlayer=new MediaPlayer();
                    mediaPlayer.setOnPreparedListener(new MyPreparedListener());
                    mediaPlayer.setOnCompletionListener(new MyComepletionListener());
                    mediaPlayer.setOnErrorListener(new MyOnErrorListener());
                    mediaPlayer.setDataSource(mMediaItem.getData());
                    mediaPlayer.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(this,"没有数据",Toast.LENGTH_SHORT).show();
            }
        }


    }

    private NotificationManager mManager;

    //播放
    private void start(){
        mediaPlayer.start();
        mManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
         Intent intent=new Intent(this, AudioPlayerActivity.class);
        intent.putExtra("Notification",true);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = null;
        NotificationChannel mChannel;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            //android 8.0以上需要设置渠道
            mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW);
            mManager.createNotificationChannel(mChannel);
            notification = new Notification.Builder(this,"default")
                            .setChannelId(id)
                            .setSmallIcon(R.drawable.music_default)
                            .setContentIntent(pendingIntent)
                             //设置通知标题
                            .setContentTitle("Live_Player")
                            //设置通知内容
                            .setContentText("正在播放"+mediaItems.get(position).getName())
                            //设置通知时间，默认为系统发出通知的时间，通常不用设置
                            .build();
        }else{
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,"default")
                    .setSmallIcon(R.drawable.music_default)
                    .setContentIntent(pendingIntent)
                    //设置通知标题
                    .setContentTitle("Live_Player")
                    //设置通知内容
                    .setContentText("正在播放"+mediaItems.get(position).getName())
                    //设置通知时间，默认为系统发出通知的时间，通常不用设置
                    .setWhen(System.currentTimeMillis());
            notification=mBuilder.build();
        }
        mManager.notify(1,notification);

    }

    //暂停
    private void pause(){
        mediaPlayer.pause();
    }
    //停止
    private void stop(){
        mManager.cancel(1);
        mediaPlayer.stop();
    }

    //得到当前的播放进度
    private int getCurrentPosition(){
        return mediaPlayer.getCurrentPosition();
    }


    //得到当前的总时长
    private int getDuration(){
        return mediaPlayer.getDuration();
    }

    //得到当前的艺术家
    private String getArtist(){
        return mMediaItem.getArtist();
    }

    //得到歌曲路径
    private String getAudioPath(){
        return mMediaItem.getData();
    }

    //得到歌曲名字
    private String getName(){
        return mMediaItem.getName();
    }

    //播放下一个
    private void playNext(){
        //1.根据当前的播放模式，设置下一个的位置
        setPosition(true);
        //2.根据当前的播放模式和下标位置去播放音频
        openAudio(position);
    }

    /**
     * 播放上一曲
     */
    private void playpre(){
        //1.根据当前的播放模式，设置上一个的位置
        setPosition(false);
        //2.根据当前的播放模式和下标位置去播放音频
        openAudio(position);
    }


    /**
     * 播放指定位置
     */
    private void playMusic(int position) {
        this.position=position;
        //根据当前的播放模式和下标位置去播放音频
        openAudio(position);
    }

    private void setPosition(boolean isNext) {
        int playmode = getPlayMode();
        if(playmode == MusicPlayerService.REPEAT_SINGLE||playmode==MusicPlayerService.REPEAT_ALL){
            position=isNext?position+1:position-1;
            //列表循环的情况
            if(position < 0){
                position = mediaItems.size()-1;
            }else if(position>=mediaItems.size()){
                position=0;
            }
        }else{
            position=isNext?position+1:position-1;
            if(position<0){
                position=0;
            }else if(position>=mediaItems.size()){
                position=mediaItems.size()-1;
            }
        }
    }

    //设置播放模式
    private void setPlayMode(int playMode){
        this.playMode = playMode;
        CacheUtils.putPlaymode(this,MODE,playMode);
        if(this.playMode ==MusicPlayerService.REPEAT_SINGLE){
            //单曲循环播放-不会触发播放完成的回调
            mediaPlayer.setLooping(true);
        }else{
            //不循环播放
            mediaPlayer.setLooping(false);
        }


    }

    private int getPlayMode(){
        return playMode;
    }

    /**
     * 是否在播放音频
     * @return
     */
    private boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }

    private class MyPreparedListener implements MediaPlayer.OnPreparedListener {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            EventBus.getDefault().post( "我发射了");
            start();
        }
    }


    private class MyComepletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            playNext();
        }
    }

    private class MyOnErrorListener implements MediaPlayer.OnErrorListener {
        @Override
        public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
            playNext();
            return true;
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("Service","onUnbind====");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.e("Service","onDestroy");
        stop();
        super.onDestroy();
    }
}
