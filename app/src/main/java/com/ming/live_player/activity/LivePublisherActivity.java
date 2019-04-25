package com.ming.live_player.activity;

import android.animation.ObjectAnimator;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ming.live_player.R;
import com.ming.live_player.model.Constants;
import com.ming.live_player.model.OtherUtils;
import com.ming.live_player.presenter.PusherPresenterImp;
import com.ming.live_player.view.PusherView;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.util.Timer;
import java.util.TimerTask;

public class LivePublisherActivity extends AppCompatActivity implements View.OnClickListener
                                                           ,PusherView {

    private TXCloudVideoView video_view;
    private ImageView iv_head_icon;
    private TextView tv_broadcasting_time;
    private ImageView iv_record_ball;
    private TextView tv_host_name;
    private TextView tv_member_counts;
    private RecyclerView rv_user_avatar;
    private Button btn_message_input;
    private Button btn_close;
    private Button btn_setting;
    private String mUserId;
    private String mPushUrl;
    private String mTitle;
    private String mCoverPicUrl;
    private String mHeadPicUrl;
    private String mNickName;
    private String mLocation;
    private boolean mIsRecord;
    private ObjectAnimator mObjAnim;
    private TXLivePushConfig mTXPushConfig;
    private PusherPresenterImp mPusherPresenterImp;
    private Timer mBroadcastTimer;
    private BroadcastTimerTask mBroadcastTimerTask;
    private int[] mSettingLocation = new int[2];
    //播放时间
    private long mSecond = 0;
    private boolean mPasuing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initView();
        initData();
    }


    private void initData() {
        getDataFormIntent();
        tv_member_counts.setText("0");
        btn_close.setOnClickListener(this);
        btn_message_input.setOnClickListener(this);
        btn_setting.setOnClickListener(this);
        recordAnmination();
        if (video_view!= null) {
            video_view.disableLog(false);
        }
        mTXPushConfig= new TXLivePushConfig();
        mPusherPresenterImp=new PusherPresenterImp();
        mPusherPresenterImp.attachView(this);
        //推流有效期为24小时
        long txTime=System.currentTimeMillis()/1000+86400;
        //获取推流地址
        mPusherPresenterImp.getPusherUrl(Constants.DOMAIN,Constants.RTMPKEY, Constants.STREAMID, txTime);

    }


    /**
     * 红点渐变
     */
    private void recordAnmination() {
        mObjAnim = ObjectAnimator.ofFloat(iv_record_ball, "alpha", 1.0f, 0f, 1.0f);
        mObjAnim.setDuration(1000);
        mObjAnim.setRepeatCount(-1);
        mObjAnim.start();
    }

    private void initView() {
        setContentView(R.layout.activity_live_publisher);
        video_view =(TXCloudVideoView)findViewById(R.id.video_view);
        iv_head_icon =(ImageView)findViewById(R.id.iv_head_icon);
        tv_broadcasting_time =(TextView)findViewById(R.id.tv_broadcasting_time);
        iv_record_ball =(ImageView)findViewById(R.id.iv_record_ball);
        tv_host_name =(TextView)findViewById(R.id.tv_host_name);
        tv_member_counts =(TextView)findViewById(R.id.tv_member_counts);
        rv_user_avatar =(RecyclerView)findViewById(R.id.rv_user_avatar);
        btn_message_input =(Button)findViewById(R.id.btn_message_input);
        btn_close =(Button)findViewById(R.id.btn_close);
        btn_setting =(Button)findViewById(R.id.btn_setting);

    }

    private void getDataFormIntent() {
        Intent intent = getIntent();
        mUserId = intent.getStringExtra(Constants.USER_ID);
       // mPushUrl = intent.getStringExtra(Constants.PUBLISH_URL);
        mTitle = intent.getStringExtra(Constants.ROOM_TITLE);
       // mCoverPicUrl = intent.getStringExtra(Constants.COVER_PIC);
       // mHeadPicUrl = intent.getStringExtra(Constants.USER_HEADPIC);
        mNickName = intent.getStringExtra(Constants.USER_NICK);
        mLocation = intent.getStringExtra(Constants.USER_LOC);
        mIsRecord = intent.getBooleanExtra(Constants.IS_RECORD, false);
    }

    private void startPublish() {
        mTXPushConfig.setAutoAdjustBitrate(false);
        mTXPushConfig.setVideoResolution(TXLiveConstants.VIDEO_RESOLUTION_TYPE_540_960);
        mTXPushConfig.setVideoBitrate(1000);
        mTXPushConfig.setVideoFPS(20);
        Log.i("startPublish", "startPublish: MANUFACTURER " + Build.MANUFACTURER + " model:" + Build.MODEL);

        //切后台推流图片
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pause_publish, options);
        mTXPushConfig.setPauseImg(bitmap);
        mPusherPresenterImp.startPusher(video_view, mTXPushConfig, mPushUrl);

        if (mBroadcastTimer == null) {
            mBroadcastTimer = new Timer(true);
            mBroadcastTimerTask = new BroadcastTimerTask();
            mBroadcastTimer.schedule(mBroadcastTimerTask, 1000, 1000);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_close:
               // stopPublish();
                finish();
                break;
            case R.id.btn_message_input:
             //   showInputMsgDialog();
                break;
            case R.id.btn_setting:
                //setting坐标
                mPusherPresenterImp.showSettingPopupWindow(btn_setting, mSettingLocation);
                break;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (mSettingLocation[0] == 0 && mSettingLocation[1] == 0) {
            btn_setting.getLocationOnScreen(mSettingLocation);
        }
    }

    @Override
    public void onGetPushUrl(String pushUrl, int errorCode) {
        mPushUrl = pushUrl;
        if (errorCode == 0) {
            startPublish();
        } else {
            Toast.makeText(this,"push url is empty",Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public FragmentManager getFragmentMgr() {

        return  getFragmentManager();
    }

    @Override
    protected void onResume() {
        super.onResume();
        video_view.onResume();
        if (mPasuing) {
            mPasuing = false;
            mPusherPresenterImp.resumePusher();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
        mPasuing = true;
        video_view.onPause();
        mPusherPresenterImp.pausePusher();
     //   mPusherPresenterImp.stopPusher();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("onDestroy","=======");
        video_view.onDestroy();
        mPusherPresenterImp.stopPusher();
        if (mObjAnim != null) {
            mObjAnim.cancel();
        }
        if (mBroadcastTimerTask != null) {
            mBroadcastTimerTask.cancel();
            mBroadcastTimer.cancel();
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

    class BroadcastTimerTask extends TimerTask {

        @Override
        public void run() {
            mSecond++;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tv_broadcasting_time.setText(OtherUtils.formattedTime(mSecond));
                }
            });
        }
    }
}
