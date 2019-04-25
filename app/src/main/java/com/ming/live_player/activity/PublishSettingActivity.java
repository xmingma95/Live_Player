package com.ming.live_player.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ming.live_player.R;
import com.ming.live_player.model.Constants;
import com.ming.live_player.model.LocationMgr;
import com.ming.live_player.model.OtherUtils;
import com.ming.live_player.presenter.PublishSettingPresenterImp;
import com.ming.live_player.view.PublishSettingView;
import com.ming.live_player.view.custom.CustomSwitch;

import java.io.IOException;

/**
 * 发布直播的设置页面
 */
public class PublishSettingActivity extends AppCompatActivity implements PublishSettingView,
        View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private EditText tvTitle;
    private TextView btn_cancel;
    private TextView tv_pic_tip;
    private ImageView cover;
    private TextView btn_publish;
    private TextView address;
    private CustomSwitch btn_record;
    private CustomSwitch btn_lbs;
    private TextView tv_record;
    private RadioGroup rg_record_type;
    private RadioGroup rg_bitrate;
    private RelativeLayout rl_bitrate;
    private Dialog mPicChsDialog;
    private boolean mPermission = false;

    private PublishSettingPresenterImp publishSettingPresenterImp;

    private int mRecordType = Constants.RECORD_TYPE_CAMERA;
    private int mBitrateType = Constants.BITRATE_NORMAL;

    private Uri fileUri, cropUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

    }

    private void initView() {
        setContentView(R.layout.activity_publish_setting);
        tvTitle =(EditText)findViewById(R.id.live_title);
        btn_cancel =(TextView)findViewById(R.id.btn_cancel);
        tv_pic_tip =(TextView)findViewById(R.id.tv_pic_tip);
        cover =(ImageView)findViewById(R.id.cover);
        btn_publish =(TextView)findViewById(R.id.btn_publish);
        address =(TextView)findViewById(R.id.address);
        btn_record =(CustomSwitch)findViewById(R.id.btn_record);
        btn_lbs =(CustomSwitch)findViewById(R.id.btn_lbs);
        tv_record =(TextView)findViewById(R.id.tv_record);
        rg_record_type =(RadioGroup)findViewById(R.id.rg_record_type);
        rg_bitrate =(RadioGroup)findViewById(R.id.rg_bitrate);
        rl_bitrate =(RelativeLayout)findViewById(R.id.rl_bitrate);
        initPhotoDialog();
        initData();
        setListener();

    }

    private void setListener() {
        rg_record_type.setOnCheckedChangeListener(this);
        rg_bitrate.setOnCheckedChangeListener(this);
        cover.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_publish.setOnClickListener(this);
        btn_lbs.setOnClickListener(this);
        btn_record.setOnClickListener(this);
    }

    private void initData() {
        publishSettingPresenterImp= new PublishSettingPresenterImp();
        publishSettingPresenterImp.attachView(this);
        mPermission =publishSettingPresenterImp.checkPublishPermission(this);
        Log.e("pression","====="+mPermission);
        cover.setImageResource(R.drawable.publish_background);
    }

    /**
     * 图片选择 对话框（拍照，相册选择，取消）
     */
    private void initPhotoDialog() {
        mPicChsDialog = new Dialog(this, R.style.floag_dialog);
        mPicChsDialog.setContentView(R.layout.dialog_pic_choose);
        WindowManager windowManager = getWindowManager();
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        Window dlgwin = mPicChsDialog.getWindow();
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.BOTTOM);
        lp.width = (int) (dm.widthPixels); //设置宽度
        mPicChsDialog.getWindow().setAttributes(lp);
        mPicChsDialog.findViewById(R.id.chos_camera).setOnClickListener(this);
        mPicChsDialog.findViewById(R.id.pic_lib).setOnClickListener(this);
        mPicChsDialog.findViewById(R.id.dialog_btn_cancel).setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Log.e("resultok",fileUri+"");
            switch (requestCode) {
                case PublishSettingPresenterImp.PICK_IMAGE_CAMERA:
                    cropUri =publishSettingPresenterImp.cropImage(fileUri,requestCode);
                    break;
                case PublishSettingPresenterImp.PICK_IMAGE_LOCAL:
                    cropUri =publishSettingPresenterImp.cropImage(data.getData(),requestCode);
                    break;
                case PublishSettingPresenterImp.CROP_CHOOSE:
                    tv_pic_tip.setVisibility(View.GONE);
                    Log.e("cropuri2","==="+cropUri);
                    Glide.with(this).load(cropUri).into(cover);
                 //   publishSettingPresenterImp.doUploadPic(cropUri.getPath());
                    break;
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.LOCATION_PERMISSION_REQ_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (!LocationMgr.getMyLocation(this,publishSettingPresenterImp.getLocationListener())) {
                        address.setText("定位失败");
                        btn_lbs.setChecked(false, false);
                    }
                }
                break;
            case Constants.WRITE_PERMISSION_REQ_CODE:
                for (int ret : grantResults) {
                    if (ret != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                mPermission = true;
                break;
            default:
                break;
        }
    }

    @Override
    public void doLocationSuccess(String location) {
        address.setText(location);
    }

    @Override
    public void doLocationFailed() {
        address.setText("定位失败");
        btn_lbs.setChecked(false, false);
    }

    @Override
    public void doUploadSuceess(String url) {
        Glide.with(this).load(url).into(cover);
    }

    @Override
    public void doUploadFailed() {
        showMsg("封面上传失败");
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void showMsg(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.btn_publish:
                String location = address.getText().toString().equals("定位失败") ||
                        address.getText().toString().equals("正在定位中") ?
                        "不显示地理位置" : address.getText().toString();
                publishSettingPresenterImp.doPublish(tvTitle.getText().toString().trim(), mRecordType, location, mBitrateType, false);
                break;
            case R.id.cover:
                mPicChsDialog.show();
                break;
            case R.id.btn_lbs:
                if (btn_lbs.getChecked()) {
                    btn_lbs.setChecked(false, true);
                    address.setText("不显示地理位置");
                } else {
                    btn_lbs.setChecked(true, true);
                    address.setText("正在定位中");
                    publishSettingPresenterImp.doLocation();
                }
                break;
            case R.id.btn_record:
                if (btn_record.getChecked()) {
                    btn_record.setChecked(false, true);
                    tv_record.setText("不进行录制");
                } else {
                    btn_record.setChecked(true, true);
                    tv_record.setText("进行录制");
                }
                break;
            case R.id.chos_camera:
                fileUri = publishSettingPresenterImp.pickImage(mPermission, PublishSettingPresenterImp.PICK_IMAGE_CAMERA);
                mPicChsDialog.dismiss();
                break;
            case R.id.pic_lib:
                publishSettingPresenterImp.pickImage(mPermission, PublishSettingPresenterImp.PICK_IMAGE_LOCAL);
                mPicChsDialog.dismiss();
                break;
            case R.id.dialog_btn_cancel:
                mPicChsDialog.dismiss();
                break;
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.rb_bitrate_slow:
                mBitrateType = Constants.BITRATE_SLOW;
                break;
            case R.id.rb_bitrate_normal:
                mBitrateType = Constants.BITRATE_NORMAL;
                break;
            case R.id.rb_bitrate_fast:
                mBitrateType = Constants.BITRATE_FAST;
                break;
            case R.id.rb_record_camera:
                mRecordType = Constants.RECORD_TYPE_CAMERA;
                rl_bitrate.setVisibility(View.GONE);
                break;
            case R.id.rb_record_screen:
                if (!publishSettingPresenterImp.checkScrRecordPermission()) {
                    showMsg("当前安卓系统版本过低，仅支持5.0及以上系统");
                    rg_record_type.check(R.id.rb_record_camera);
                    return;
                }
                try {
                    OtherUtils.checkFloatWindowPermission(PublishSettingActivity.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                rl_bitrate.setVisibility(View.VISIBLE);
                mRecordType = Constants.RECORD_TYPE_SCREEN;
                break;
            default:
                break;
        }
    }
}
