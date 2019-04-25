package com.ming.live_player.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ming.live_player.IMusicPlayerService;
import com.ming.live_player.R;
import com.ming.live_player.adapter.PlayerListAdapter;
import com.ming.live_player.bean.MediaItem;
import com.ming.live_player.model.GetData;
import com.ming.live_player.model.LyricUtils;
import com.ming.live_player.model.SystemUtil;
import com.ming.live_player.service.MusicPlayerService;
import com.ming.live_player.view.custom.ShowLyric;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;

public class AudioPlayerActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_music_artist;
    private TextView tv_music_name;
    private SeekBar seekbar_audio;
    private TextView tv_music_time;
    private ShowLyric showLyric;
    private SystemUtil systemUtil;
    private boolean notification;
    private int position;
    private static final int PROGRESS=1;
    private static final int SHOW_lYRIC=2;
    private IMusicPlayerService service;
    private ImageButton ib_music_back;
    private ImageButton ib_music_exit;
    private ImageButton ib_playmode;
    private ImageButton ib_audio_pre;
    private ImageButton ib_audio_pause_start;
    private ImageButton ib_audio_next;
    private ImageButton ib_list;
    private Intent mIntent;
    private String lyricPath="mnt/sdcard/LyricTemp";
    private ArrayList<MediaItem> mediaItems;
    public static AudioPlayerActivity instance;
    private boolean isAddHeader;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case PROGRESS:
                    try {
                        int CurrentPosition=service.getCurrentPosition();
                        seekbar_audio.setProgress(CurrentPosition);
                        tv_music_time.setText(systemUtil.stringForTime(CurrentPosition)+"/"+systemUtil.stringForTime(service.getDuration()));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    handler.removeMessages(PROGRESS);
                    handler.sendEmptyMessageDelayed(PROGRESS,1000);
                case SHOW_lYRIC:
                    try {
                        int CurrentPosition=service.getCurrentPosition();
                        showLyric.setNextLyric(CurrentPosition);
                        handler.removeMessages(SHOW_lYRIC);
                        handler.sendEmptyMessage(SHOW_lYRIC);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    break;

            }

        }
    };

    private ServiceConnection con=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            service=IMusicPlayerService.Stub.asInterface(iBinder);
            Log.e("Service==", "onServiceConnected: "+service);
            if(service!=null){
                try {
                    if(!notification){
                        Log.e("SERVICE!=NULL","connected====");
                        service.openAudio(position);
                    }else {
                        showAudioPlayerView("显示页面");
                    }

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            try {
                if(service!=null){
                    service.stop();
                    service=null;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };
    private ListView lv_player_list;
    private LinearLayout ll_player_list_header;
    private ImageButton ib_list_exit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        bindAndStartService();

    }

    /**
     * 绑定方式启动服务
     */
    private void bindAndStartService() {
        mIntent = new Intent(this, MusicPlayerService.class);
        mIntent.setAction("com.ming.mingplayer_musicplayer");
        Log.e("service","开始绑定服务" );
        bindService(mIntent,con, Context.BIND_AUTO_CREATE);
        Log.e("service","绑定服务完成" );
        startService(mIntent);//多次调用不会启动多个服务，不至于实例化多个服务
        Log.e("service","开始服务" );
    }

    private void initData() {
        systemUtil = SystemUtil.getInstance(this);
        EventBus.getDefault().register( this );
        //是否是从状态栏进来的，默认为false
        notification =getIntent().getBooleanExtra("Notification",false);
        if(!notification){
            position =getIntent().getIntExtra("position",0);
            Log.e("Serviceposition","===="+ position);
        }

    }

    /**
     * 显示歌词
     */
    private void showLyric() {
        //解析歌词
        LyricUtils lyricUtils = new LyricUtils();

        try {
            String path = service.getAudioPath();//得到歌曲的绝对路径
            //传歌词文件
            ///mnt/sdcard/LyricTemp/*****.lrc
            path = path.substring(path.lastIndexOf("/"),path.lastIndexOf("."));
            File file = new File(lyricPath+path+".lrc");
            Log.e("lyricpath",lyricPath+path);
           /* if(!file.exists()){
                file = new File(path + ".txt");
            }*/
            lyricUtils.readLyricFile(file);//解析歌词
            showLyric.setLyrics(lyricUtils.getLyrics());

        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if(lyricUtils.isExistsLyric()){
            handler.sendEmptyMessage(SHOW_lYRIC);
        }

    }

    private void initView() {
        setContentView(R.layout.activity_audio_player);
        tv_music_artist = (TextView) findViewById(R.id.tv_artist);
        tv_music_name = (TextView) findViewById(R.id.tv_music_name);
        seekbar_audio =(SeekBar)findViewById(R.id.seekbar_audio);
        tv_music_time =(TextView)findViewById(R.id.tv_time);
        showLyric =(ShowLyric)findViewById(R.id.showlyric);
        ib_music_back =(ImageButton)findViewById(R.id.ib_music_back);
        ib_music_exit =(ImageButton)findViewById(R.id.ib_music_exit);
        ib_playmode =(ImageButton)findViewById(R.id.ib_playmode);
        ib_audio_pre =(ImageButton)findViewById(R.id.ib_audio_pre);
        ib_audio_pause_start =(ImageButton)findViewById(R.id.ib_audio_pause_start);
        ib_audio_next =(ImageButton)findViewById(R.id.ib_audio_next);
        ib_list =(ImageButton)findViewById(R.id.ib_list);
        lv_player_list =(ListView)findViewById(R.id.lv_player_list);
        ll_player_list_header =(LinearLayout)findViewById(R.id.ll_player_list_header);
        setListener();
    }

    private void setListener() {
        ib_music_back.setOnClickListener(this);
        ib_music_exit.setOnClickListener(this);
        ib_playmode.setOnClickListener(this);
        ib_audio_pre.setOnClickListener(this);
        ib_audio_pause_start.setOnClickListener(this);
        ib_audio_next.setOnClickListener(this);
        ib_list.setOnClickListener(this);
        seekbar_audio.setOnSeekBarChangeListener(new myAudioSeekbarListener());
    }

    /**
     * eventBus接收，显示主界面
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showAudioPlayerView (String event){
        /* Do something */
        try {
            handler.sendEmptyMessage(SHOW_lYRIC);
            String name=service.getName();
            tv_music_artist.setText(service.getArtist());
            tv_music_name.setText(name.substring(name.lastIndexOf("-")+1,name.lastIndexOf(".")));
            seekbar_audio.setMax(service.getDuration());
            handler.sendEmptyMessage(PROGRESS);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        showLyric();
        showPlaymode();
        Toast.makeText( this , event , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ib_music_back:
                finish();
                break;
            case R.id.ib_music_exit:
                showAlertDialog();
                break;
            case R.id.ib_playmode:
                setPlaymode();
                break;
            case R.id.ib_audio_pre:
                if(service != null){
                    try {
                        service.playpre();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.ib_audio_pause_start:
                if(service != null) {
                    try {
                        if (service.isPlaying()) {
                            //暂停
                            service.pause();
                            //按钮-播放
                            ib_audio_pause_start.setBackgroundResource(R.drawable.btn_video_pause_selector);
                        } else {
                            //播放
                            service.start();
                            //按钮-暂停
                            ib_audio_pause_start.setBackgroundResource(R.drawable.btn_video_start_selector);
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.ib_audio_next:
                if(service != null){
                    try {
                        service.playNext();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.ib_list:
                showPlayerList();
                break;
            case R.id.ib_list_exit:
                lv_player_list.setVisibility(View.GONE);
        }
    }

    /**
     * 点击显示歌词列表
     */
    private void showPlayerList() {
        lv_player_list.setVisibility(View.VISIBLE);
        mediaItems=GetData.getInstance().getDataFromLocal(this);
        if(!isAddHeader){
            //添加头部
            View headView = getLayoutInflater().inflate(R.layout.listview_header_music_list, null);
            ib_list_exit =(ImageButton)headView.findViewById(R.id.ib_list_exit);
            lv_player_list.addHeaderView(headView);
            ib_list_exit.setOnClickListener(this);
            isAddHeader=true;
        }
        PlayerListAdapter playerListAdapter=new PlayerListAdapter(this,mediaItems);
        lv_player_list.setAdapter(playerListAdapter);
        lv_player_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                try {
                    service.playMusic(position-1);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void showAlertDialog() {
       AlertDialog dialog=new AlertDialog.Builder(this).setTitle("提示").setMessage("确定退出音乐播放器？")
               .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       //解绑服务,并销毁
                       if(con != null){
                           unbindService(con);
                           con = null;
                       }
                       stopService(mIntent);
                       finish();
                   }
               })
               .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {

                   }
               }).create();
        dialog.show();
        //修改按键字体颜色
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE);
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
    }

    private void setPlaymode() {
        try {
            int playmode = service.getPlayMode();
            if(playmode==MusicPlayerService.REPEAT_NORMAL){
                playmode = MusicPlayerService.REPEAT_SINGLE;
            }else if(playmode == MusicPlayerService.REPEAT_SINGLE){
                playmode = MusicPlayerService.REPEAT_ALL;
            }else if(playmode ==MusicPlayerService.REPEAT_ALL){
                playmode = MusicPlayerService.REPEAT_NORMAL;
            }else{
                playmode = MusicPlayerService.REPEAT_NORMAL;
            }

            //在service中设置
            service.setPlayMode(playmode);

            //设置图片
            showPlaymode();

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置播放模式的图片
     */
    private void showPlaymode() {
        try {
            int playmode = service.getPlayMode();

            if(playmode==MusicPlayerService.REPEAT_NORMAL){
                ib_playmode.setBackgroundResource(R.drawable.btn_audio_playmode_normal_selector);
                Toast.makeText(AudioPlayerActivity.this, "顺序播放", Toast.LENGTH_SHORT).show();
            }else if(playmode == MusicPlayerService.REPEAT_SINGLE){
                ib_playmode.setBackgroundResource(R.drawable.btn_audio_playmode_single_selector);
                Toast.makeText(AudioPlayerActivity.this, "单曲循环", Toast.LENGTH_SHORT).show();
            }else if(playmode ==MusicPlayerService.REPEAT_ALL){
                ib_playmode.setBackgroundResource(R.drawable.btn_audio_playmode_all_selector);
                Toast.makeText(AudioPlayerActivity.this, "全部循环", Toast.LENGTH_SHORT).show();
            }else{
                ib_playmode.setBackgroundResource(R.drawable.btn_audio_playmode_normal_selector);
                Toast.makeText(AudioPlayerActivity.this, "顺序播放", Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private class myAudioSeekbarListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            if(b){
                try {
                    service.seekTo(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    public static void exitPlayer(){

    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister( this );
        handler.removeCallbacksAndMessages(null);
        //解绑服务
        if(con != null){
            unbindService(con);
            con = null;
        }
        Log.e("audioPlayer","onDestroy=======");
        super.onDestroy();
    }
}
