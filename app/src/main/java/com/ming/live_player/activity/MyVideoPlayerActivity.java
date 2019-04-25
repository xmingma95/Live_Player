package com.ming.live_player.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ming.live_player.R;
import com.ming.live_player.bean.MediaItem;
import com.ming.live_player.model.SystemUtil;
import com.ming.live_player.presenter.MyVideoPlayerPresenterImp;
import com.ming.live_player.view.MyVideoPlayerView;
import com.ming.live_player.view.MyVideoView;
import com.ming.live_player.view.custom.VerticalSeekBar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class MyVideoPlayerActivity extends AppCompatActivity implements
                                            View.OnClickListener,MyVideoPlayerView {
    private static final int PROGRESS = 1;
    private static final int HIDEMEDIACONTROLLER = 2;
    private final static int VOICEHIDE=3;
    private final static int BRIGHTNESSHIDE=4;

    private static final int DEFAULT_SCREEN=1;
    private static final int FULL_SCREEN=2;
    private MyVideoView videoView;
    private Uri uri;
    private LinearLayout llTop;
    private TextView tvTopName;
    private ImageView ivBattery;
    private VerticalSeekBar seekbarVoice;
    private LinearLayout ll_voice;
    private LinearLayout ll_brightness;
    private VerticalSeekBar seekbarBrightness;
    private TextView tvCurrentTime;
    private SeekBar seekbarVideo;
    private TextView tvTotalTime;
    private Button btnExit;
    private Button btnVideoPre;
    private Button btnPauseStart;
    private Button btnVideoNext;
    private Button btnVideoSwitchScreen;
    private SystemUtil systemUtil;
    private TextView tvSystemTime;
    private ArrayList<MediaItem> mediaItems;
    private int position;

    private RelativeLayout mc_controller;
    private int videoWidth;
    private int videoHeight;
    private int screenWidth;
    private int screenHeight;

    private boolean isShowMediaController;
    private boolean isFullScreen;

    private AudioManager am;
    private int currentVoice;
    private int maxVoice;
    private float startY;
    private float startX;
    private int mVoice;
    private int toughRange;
    private int maxScreen;
    private float endY;
    private float endX;

    private MyVideoPlayerPresenterImp mMyVideoPlayerPresenterImp;
    private MyReceiver myReceiver;
    //手势识别器
    private GestureDetector detector;
    private MyHandler myHandler;
    private int brightnessNext;
    private int brightnessNow;
    private int duration;
    private int curVideoPosition;
    private boolean isChangingProgress;
    private boolean isChangeVoice;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        setVideoListener();
        getData();

        initData();
        setData();
    }

    @Override
    protected void onDestroy() {
        if(myReceiver!=null){
            unregisterReceiver(myReceiver);
            myReceiver=null;
        }
        mMyVideoPlayerPresenterImp.detachView();
        super.onDestroy();
    }

    /**
     * 获取序列化视频列表
     */
    private void getData() {
        uri=getIntent().getData();
        mediaItems= (ArrayList<MediaItem>) getIntent().getSerializableExtra("videolist");
        position=getIntent().getIntExtra("position",0);
    }


    /**
     * 设置数据，包括按钮状态，进度条
     */
    private void setData() {
        if(mediaItems!=null&&mediaItems.size()>0){
            MediaItem mediaitem=mediaItems.get(position);
            updateTopName(mediaitem.getName());
            videoView.setVideoPath(mediaitem.getData());
        }else if(uri!=null){
            updateTopName(uri.toString());
            videoView.setVideoURI(uri);
        }else{
            Toast.makeText(MyVideoPlayerActivity.this,"没有数据",Toast.LENGTH_SHORT).show();
        }
        //设置按钮状态
        setButtonState();
        seekbarVoice.setMax(maxVoice);
        seekbarVoice.setProgress(currentVoice);
        seekbarBrightness.setMax(255);
        brightnessNow=systemUtil.getSystemBrightness();
        seekbarBrightness.setProgress(brightnessNow);

    }

    /**
     * 初始化数据，注册电量广播,手势识别
     */
    private void initData() {
        mMyVideoPlayerPresenterImp=new MyVideoPlayerPresenterImp(mediaItems,uri);
        mMyVideoPlayerPresenterImp.attachView(this);
        systemUtil=SystemUtil.getInstance(this);
        myReceiver=new MyReceiver();
        myHandler=new MyHandler(this);
        IntentFilter batteryFilter=new IntentFilter();
        batteryFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(myReceiver,batteryFilter);
        hideMediaController();

        //手势识别器初始化
        detector=new GestureDetector(this,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {

                return super.onSingleTapUp(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                //setFullScreenAndDefault();
                super.onLongPress(e);

            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                startAndPause();
                return super.onDoubleTap(e);

            }
        });

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        maxScreen=Math.max(screenWidth,screenHeight);
        toughRange =Math.min(screenWidth,screenHeight);
        am = (AudioManager) getSystemService(AUDIO_SERVICE);
        currentVoice = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVoice = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }


    private void startAndPause() {
        if(videoView.isPlaying()){
            //暂停播放
            videoView.pause();
            btnPauseStart.setBackgroundResource(R.drawable.btn_video_pause_selector);
        }else {
            //开始播放
            videoView.start();
            btnPauseStart.setBackgroundResource(R.drawable.btn_video_start_selector);
        }
    }


    /**
     * 自定义视频播放监听
     */
    private void setVideoListener() {
        videoView.setOnPreparedListener(new MyPreparedListener());

        videoView.setOnErrorListener(new MyErrorListener());

        videoView.setOnCompletionListener(new MyCompletionListener());

        seekbarVideo.setOnSeekBarChangeListener(new MySeekBarChangeListener());

        seekbarVoice.setOnSeekBarChangeListener(new MyVoiceSeekBarChangeListener());
    }

    //电量变化时接收广播
    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level=intent.getIntExtra("level",0);
            //设置电量
            setBattery(level);
        }
    }

    //设置电量
    private void setBattery(int level) {
        if(level<=0){
            ivBattery.setImageResource(R.drawable.ic_battery_0);
        }else if(level<=10){
            ivBattery.setImageResource(R.drawable.ic_battery_10);
        }else if(level<=20){
            ivBattery.setImageResource(R.drawable.ic_battery_20);
        }else if(level<=40){
            ivBattery.setImageResource(R.drawable.ic_battery_40);
        }else if(level<=60){
            ivBattery.setImageResource(R.drawable.ic_battery_60);
        }else if(level<=80){
            ivBattery.setImageResource(R.drawable.ic_battery_80);
        }else if(level<=100){
            ivBattery.setImageResource(R.drawable.ic_battery_100);
        }else{
            ivBattery.setImageResource(R.drawable.ic_battery_100);
        }
    }

    @Override
    public void updateTopName(String name) {
        tvTopName.setText(name);
    }

    /**
     * 设置播放下一个和上一个按钮的状态
     */
    @Override
    public void setButtonState() {
        if(mediaItems!=null&&mediaItems.size()>0){
            if(mediaItems.size()==1){
                setEnable(false);
            }else if(mediaItems.size()==2){
                if(position==0){
                    btnVideoPre.setBackgroundResource(R.drawable.btn_pre_gray);
                    btnVideoPre.setEnabled(false);
                    btnVideoNext.setBackgroundResource(R.drawable.btn_video_next_selector);
                    btnVideoNext.setEnabled(true);
                }else{
                    btnVideoNext.setBackgroundResource(R.drawable.btn_next_gray);
                    btnVideoNext.setEnabled(false);
                    btnVideoPre.setBackgroundResource(R.drawable.btn_video_pre_selector);
                    btnVideoPre.setEnabled(true);
                }
            }else {
                if(position==0){
                    btnVideoPre.setBackgroundResource(R.drawable.btn_pre_gray);
                    btnVideoPre.setEnabled(false);
                    btnVideoNext.setBackgroundResource(R.drawable.btn_video_next_selector);
                    btnVideoNext.setEnabled(true);
                }else if(position==mediaItems.size()-1){
                    btnVideoNext.setBackgroundResource(R.drawable.btn_next_gray);
                    btnVideoNext.setEnabled(false);
                    btnVideoPre.setBackgroundResource(R.drawable.btn_video_pre_selector);
                    btnVideoPre.setEnabled(true);
                }else{
                    setEnable(true);
                }
            }
        }else if(uri!=null){
            setEnable(false);
        }
    }

    @Override
    public void setEnable(boolean isEnable) {
        if(isEnable){
            btnVideoNext.setBackgroundResource(R.drawable.btn_video_next_selector);
            btnVideoNext.setEnabled(true);
            btnVideoPre.setBackgroundResource(R.drawable.btn_video_pre_selector);
            btnVideoPre.setEnabled(true);
        }else{
            btnVideoNext.setBackgroundResource(R.drawable.btn_next_gray);
            btnVideoNext.setEnabled(false);
            btnVideoPre.setBackgroundResource(R.drawable.btn_pre_gray);
            btnVideoPre.setEnabled(false);
        }
    }

    @Override
    public void setFullScreenAndDefault() {
        if(isFullScreen){
            System.out.println("默认===");
            setVideoType(DEFAULT_SCREEN);
        }else{
            System.out.println("全屏=====");
            setVideoType(FULL_SCREEN);
        }
    }

    private void setVideoType(int default_screen) {
        switch (default_screen){
            //全屏
            case FULL_SCREEN:
                videoView.setVideoSize(screenWidth,screenHeight);
                isFullScreen=true;
                break;
            //默认
            case DEFAULT_SCREEN:
                //视频的宽和高
                int mVideoWidth=videoWidth;
                int mVideoHeight=videoHeight;
                //屏幕的宽和高
                int width = screenWidth;
                int height = screenHeight;
                if ( mVideoWidth * height  < width * mVideoHeight ) {
                    width = height * mVideoWidth / mVideoHeight;
                } else if ( mVideoWidth * height  > width * mVideoHeight ) {
                    height = width * mVideoHeight / mVideoWidth;
                }
                videoView.setVideoSize(width,height);
                isFullScreen=false;
                break;
            default:
                break;
        }
    }


    @Override
    public void showMediaController() {
        mc_controller.setVisibility(View.VISIBLE);
        isShowMediaController=false;
    }

    @Override
    public void hideMediaController() {
        mc_controller.setVisibility(View.GONE);
        isShowMediaController=true;
    }

    @Override
    public void setPath(String path) {
        videoView.setVideoPath(path);
    }

    @Override
    public void showSeekbar(boolean isVoice) {
        if(isVoice){
            ll_voice.setVisibility(View.VISIBLE);
        }else{
            ll_brightness.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void hideSeekbar(boolean isVoice) {
        if(isVoice){
            myHandler.sendEmptyMessageDelayed(VOICEHIDE,3000);
        }else{
            myHandler.sendEmptyMessageDelayed(BRIGHTNESSHIDE,3000);
        }
    }

    @Override
    public void setSeekbar(int progress, boolean isVoice) {
        if(isVoice){
            seekbarVoice.setProgress(progress);
            currentVoice=progress;
        }else{
            seekbarBrightness.setProgress(progress);
            brightnessNow=progress;
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void dismissLoading() {

    }

    @Override
    public Context getContext() {
        return this;
    }

    private class MyPreparedListener implements MediaPlayer.OnPreparedListener {
        @Override
        public void onPrepared(MediaPlayer mp) {
            //获取视频宽和高
            videoWidth = mp.getVideoWidth();
            videoHeight = mp.getVideoHeight();
            videoView.start();
            //获取视频总时长，关联总长度
            duration = videoView.getDuration();
            seekbarVideo.setMax(duration);
            tvTotalTime.setText(systemUtil.stringForTime(duration));
            //进度条更新
            myHandler.sendEmptyMessage(PROGRESS);
            setVideoType(DEFAULT_SCREEN);

        }
    }

    private class MyErrorListener implements MediaPlayer.OnErrorListener {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            Toast.makeText(MyVideoPlayerActivity.this,"播放出错啦",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private class  MyCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
             mMyVideoPlayerPresenterImp.playVideo(++position);
            Toast.makeText(MyVideoPlayerActivity.this,"播放完成啦",Toast.LENGTH_SHORT).show();
        }
    }

    private class MySeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(fromUser){
                videoView.seekTo(progress);
            }

        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
           myHandler.removeMessages(HIDEMEDIACONTROLLER);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            myHandler.sendEmptyMessageDelayed(HIDEMEDIACONTROLLER,3000);
        }
    }

    private class MyVoiceSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                mMyVideoPlayerPresenterImp.updateVoice(progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
             myHandler.removeMessages(HIDEMEDIACONTROLLER);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            myHandler.sendEmptyMessageDelayed(HIDEMEDIACONTROLLER,4000);
        }
    }

    /**
     *触屏调节声音.亮度
     */
   @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(isShowMediaController){
                    //显示
                    showMediaController();
                    myHandler.sendEmptyMessageDelayed(HIDEMEDIACONTROLLER,3000);
                }else {
                    //隐藏
                    hideMediaController();
                    myHandler.removeMessages(HIDEMEDIACONTROLLER);
                }
                startX=event.getX();
                startY = event.getY();
                curVideoPosition = videoView.getCurrentPosition();
                //当前的声音
                mVoice =am.getStreamVolume(AudioManager.STREAM_MUSIC);
                brightnessNext = brightnessNow;
                myHandler.removeMessages(HIDEMEDIACONTROLLER);
                break;
            case MotionEvent.ACTION_MOVE:
                endY =event.getY();
                endX =event.getX();
                //x轴yz轴起点在左上角
                float distanceY= startY - endY;
                float distanceX= endX - startX;
                if(Math.abs(distanceY)*1.2>=Math.abs(distanceX)&&!isChangingProgress){
                    isChangeVoice=true;
                    float deltaVoiceY=(distanceY/toughRange)*maxVoice;
                    float deltaBrightnessY=(distanceY/toughRange)*255;
                    //最终声音
                    int voice = (int) Math.min(Math.max(mVoice+deltaVoiceY,0),maxVoice);
                    int brightness= (int) Math.min(Math.max(brightnessNext +deltaBrightnessY,0),255);
                    if(deltaVoiceY != 0){
                        if(startX>=maxScreen/2){
                            mMyVideoPlayerPresenterImp.updateVoice(voice);
                        }else{
                            mMyVideoPlayerPresenterImp.updateBrightness(brightness);
                        }
                    }
                }else if(!isChangeVoice){
                    //滑动进度
                    isChangingProgress=true;
                    float deltaVideoX=(distanceX/maxScreen)*duration;
                    int videoProgress= (int) Math.min(Math.max(curVideoPosition +deltaVideoX,0),duration);
                    videoView.seekTo(videoProgress);
                    showMediaController();
                    seekbarVideo.setProgress(videoProgress);
                }
                break;
            case MotionEvent.ACTION_UP:
                isChangingProgress=false;
                isChangeVoice=false;
                myHandler.sendEmptyMessageDelayed(HIDEMEDIACONTROLLER,3000);
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode ==KeyEvent.KEYCODE_VOLUME_DOWN){
            currentVoice --;
            mMyVideoPlayerPresenterImp.updateVoice(currentVoice);
            return true;
        }else if(keyCode==KeyEvent.KEYCODE_VOLUME_UP){
            currentVoice ++;
            mMyVideoPlayerPresenterImp.updateVoice(currentVoice);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void initViews() {
        setContentView(R.layout.activity_videoplayer);
        videoView =(MyVideoView)findViewById(R.id.video_view);
        llTop = (LinearLayout)findViewById( R.id.ll_top);
        tvTopName = (TextView)findViewById( R.id.tv_top_name );
        ivBattery = (ImageView)findViewById( R.id.iv_battery );

        seekbarBrightness =(VerticalSeekBar)findViewById(R.id.seekbar_brightness);
        seekbarVoice = (VerticalSeekBar)findViewById( R.id.seekbar_voice );
        ll_voice =(LinearLayout)findViewById(R.id.ll_voice);
        ll_brightness =(LinearLayout)findViewById(R.id.ll_brightness);
        tvCurrentTime = (TextView)findViewById( R.id.tv_current_time );
        seekbarVideo = (SeekBar)findViewById( R.id.seekbar_video );
        tvTotalTime = (TextView)findViewById( R.id.tv_total_time );
        tvSystemTime = (TextView)findViewById( R.id.tv_system_time);
        btnExit = (Button)findViewById( R.id.btn_exit );
        btnVideoPre = (Button)findViewById( R.id.btn_video_pre );
        btnPauseStart = (Button)findViewById( R.id.btn_pause_start );
        btnVideoNext = (Button)findViewById( R.id.btn_video_next );
        btnVideoSwitchScreen = (Button)findViewById( R.id.btn_video_switch_screen );
        mc_controller =(RelativeLayout)findViewById(R.id.mc_controller);

        btnExit.setOnClickListener( this );
        btnVideoPre.setOnClickListener( this );
        btnPauseStart.setOnClickListener( this );
        btnVideoNext.setOnClickListener( this );
        btnVideoSwitchScreen.setOnClickListener( this );
    }

    @Override
    public void onClick(View v) {
        if ( v == btnExit ) {
            finish();
        } else if ( v == btnVideoPre ) {
            // 播放上一个视频
            mMyVideoPlayerPresenterImp.playVideo(--position);
        } else if ( v == btnPauseStart ) {
            startAndPause();
        } else if ( v == btnVideoNext ) {
            // 播放下一个视频
           mMyVideoPlayerPresenterImp.playVideo(++position);
        } else if ( v == btnVideoSwitchScreen ) {
            setFullScreenAndDefault();
        }
    }

    /**
     * 静态类避免内存泄漏
     */
    private static class MyHandler extends Handler {
        WeakReference<MyVideoPlayerActivity> mWeakReference;
        public MyHandler(MyVideoPlayerActivity activity)
        {
            mWeakReference= new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg)
        {
            final MyVideoPlayerActivity activity=mWeakReference.get();
            if(activity!=null)
            {
                if (msg.what == HIDEMEDIACONTROLLER) {
                    activity.hideMediaController();
                } else if(msg.what==PROGRESS){
                    int currentPosition=activity.videoView.getCurrentPosition();
                    //播放进度和时间
                    activity.seekbarVideo.setProgress(currentPosition);
                    activity.tvCurrentTime.setText(activity.systemUtil.stringForTime(currentPosition));
                    //系统时间
                    activity.tvSystemTime.setText(activity.systemUtil.getSystemTime());
                    activity.myHandler.removeMessages(PROGRESS);
                    activity.myHandler.sendEmptyMessageDelayed(PROGRESS,1000);
                }else if(msg.what==VOICEHIDE){
                    activity.ll_voice.setVisibility(View.GONE);
                }else if(msg.what==BRIGHTNESSHIDE){
                    activity.ll_brightness.setVisibility(View.GONE);
                }
            }
        }

    }

}
