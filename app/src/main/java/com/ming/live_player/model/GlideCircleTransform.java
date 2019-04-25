package com.ming.live_player.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

/**
 * @description: Glide图像裁剪
 *
 */
public class GlideCircleTransform extends BitmapTransformation {
    public GlideCircleTransform(Context context){

    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return OtherUtils.createCircleImage(toTransform, 0);
    }



    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }
}
