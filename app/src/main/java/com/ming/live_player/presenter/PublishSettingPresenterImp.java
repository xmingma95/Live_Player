package com.ming.live_player.presenter;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;

import com.ming.live_player.activity.LivePublisherActivity;
import com.ming.live_player.model.Constants;
import com.ming.live_player.model.LocationMgr;
import com.ming.live_player.model.OtherUtils;
import com.ming.live_player.view.PublishSettingView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author ming
 * @version $Rev$
 * @des 直播设置P层实现
 */
public class PublishSettingPresenterImp implements PublishSettingPresenter{

    private PublishSettingView publishSettingView;
    public static final int PICK_IMAGE_CAMERA = 100;
    public static final int PICK_IMAGE_LOCAL = 200;
    public static final int CROP_CHOOSE = 10;
    private boolean mUploading = false;
    private Calendar calendar=Calendar.getInstance();
    private Uri imgUriOri;
    private File file;
    private String imgPathOri;
    private Uri outputUri;

    @Override
    public boolean checkPublishPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissions = new ArrayList<>();
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)) {
                permissions.add(Manifest.permission.CAMERA);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE)) {
                permissions.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (permissions.size() != 0) {
                ActivityCompat.requestPermissions(activity,
                        permissions.toArray(new String[0]),
                        Constants.WRITE_PERMISSION_REQ_CODE);
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean checkScrRecordPermission() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    @Override
    public Uri cropImage(Uri uri,int requestCode) {
        outputUri=null;
        if(requestCode==PICK_IMAGE_LOCAL){
            File file =createUriImageFile();
            //真实的 uri
            outputUri = Uri.fromFile(file);
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        //都是放uri
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 750);
        intent.putExtra("aspectY", 550);
        intent.putExtra("outputX", 750);
        intent.putExtra("outputY", 550);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        //选择拍照时，outpurUri==null;
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri==null?uri:outputUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        List<ResolveInfo> resInfoList = publishSettingView.getActivity().getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            publishSettingView.getActivity().grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        publishSettingView.getActivity().startActivityForResult(intent, CROP_CHOOSE);
        Log.e("cropUri","===="+uri);
        return outputUri==null?uri:outputUri;
    }



    @Override
    public void doPublish(String title, int liveType, String location, int bitrateType, boolean isRecord) {
        //trim避免空格字符串
        if (TextUtils.isEmpty(title)) {
            publishSettingView.showMsg("请输入非空直播标题");
        } else if (OtherUtils.getCharacterNum(title) > Constants.TV_TITLE_MAX_LEN) {
            publishSettingView.showMsg("直播标题过长 ,最大长度为" + Constants.TV_TITLE_MAX_LEN / 2);
        } else if (mUploading) {
            publishSettingView.showMsg("请等待封面上传完成");
        } else if (!OtherUtils.isNetworkAvailable(publishSettingView.getContext())) {
            publishSettingView.showMsg("当前网络环境不能发布直播");
        } else {
            if (liveType == Constants.RECORD_TYPE_SCREEN) {
                //录屏直播
               publishSettingView.showMsg("screen live");

            } else {
                //摄像头直播
                publishSettingView.showMsg("camera live");
             //   LivePublisherActivity.invoke(mPublishSettOtherUtilsingView.getActivity(), title, location, isRecord, bitrateType);
            }
            Intent intent = new Intent(publishSettingView.getActivity(), LivePublisherActivity.class);
            intent.putExtra(Constants.ROOM_TITLE,
                    TextUtils.isEmpty(title) ? "默认标题": title);
            intent.putExtra(Constants.USER_ID, "65536");
            intent.putExtra(Constants.USER_NICK, "test");
            intent.putExtra(Constants.USER_LOC, location);
            intent.putExtra(Constants.IS_RECORD, isRecord);
            intent.putExtra(Constants.BITRATE, bitrateType);
            publishSettingView.getActivity().startActivity(intent);
        }
    }

    public LocationMgr.OnLocationListener getLocationListener() {
        return mOnLocationListener;
    }


    private LocationMgr.OnLocationListener mOnLocationListener = new LocationMgr.OnLocationListener() {

        @Override
        public void onLocationChanged(int code, double lat1, double long1, String location) {
            if (0 == code) {
                publishSettingView.doLocationSuccess(location);
//                UserInfoMgr.getInstance().setLocation(location, lat1, long1, new IUserInfoMgrListener() {
//                    @Override
//                    public void OnQueryUserInfo(int error, String errorMsg) {
//                    }
//
//                    @Override
//                    public void OnSetUserInfo(int error, String errorMsg) {
//                        if (0 != error) {
//                            mPublishSettingView.showMsg("设置位置失败" + errorMsg);
//                        }
//                    }
//                });
            } else {
                publishSettingView.doLocationFailed();
            }
        }

    };
    @Override
    public void doLocation() {
        if (LocationMgr.checkLocationPermission(publishSettingView.getActivity())) {
            boolean success = LocationMgr.getMyLocation(publishSettingView.getActivity(), mOnLocationListener);
            if (!success) {
                publishSettingView.doLocationFailed();
            }
        }

    }

    @Override
    public Uri pickImage(boolean mPermission, int type) {
        //fileUri = null;
        if (!mPermission) {
            publishSettingView.showMsg("权限不足");
            return null;
        }
        switch (type) {

            case PICK_IMAGE_CAMERA:
               Intent intent_photo = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                file =createUriImageFile();
                if (file != null) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        imgUriOri = Uri.fromFile(file);
                    } else {
                        imgUriOri = FileProvider.getUriForFile(publishSettingView.getContext(),
                                "com.live.player.provider", file);
                    }
                    intent_photo.putExtra(MediaStore.EXTRA_OUTPUT, imgUriOri);
                   publishSettingView.getActivity().startActivityForResult(intent_photo, PICK_IMAGE_CAMERA);
                }
                break;
            case PICK_IMAGE_LOCAL:
                Intent intent_album = new Intent(Intent.ACTION_PICK);
                intent_album.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                publishSettingView.getActivity().startActivityForResult(intent_album, PICK_IMAGE_LOCAL);
                break;

        }
        Log.e("Uri",imgUriOri+"");
        return imgUriOri;
    }

    private File createUriImageFile() {
        String imgNameOri = "HomePic_" + new SimpleDateFormat( "yyyyMMdd_HHmmss")
                .format(new Date());
        File pictureDirOri = new File(publishSettingView.getActivity()
                .getExternalFilesDir( Environment.DIRECTORY_PICTURES)
                .getAbsolutePath() + "/OriPicture");
        if (!pictureDirOri.exists()) {
            pictureDirOri.mkdirs();
        }
        File image = null;
        try {
            image = File.createTempFile(
                    imgNameOri, /* prefix */
                    ".jpg", /* suffix */
                    pictureDirOri /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        imgPathOri = image.getAbsolutePath();
        return image;

    }

    @Override
    public void doUploadPic(String path) {

    }

    @Override
    public void attachView(Object v) {
        publishSettingView= (PublishSettingView) v;
    }

    @Override
    public void detachView() {
        publishSettingView=null;
    }
}
