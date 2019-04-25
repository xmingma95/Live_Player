package com.ming.live_player.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.VideoView;

/**
 * @author Admin
 * @version $Rev$
 * @des 自定义VideoView
 */
public class MyVideoView extends VideoView {

    public MyVideoView(Context context) {
        super(context);
    }

    public MyVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyVideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        System.out.println(MeasureSpec.getSize(widthMeasureSpec)+","+MeasureSpec.getSize(heightMeasureSpec));

        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),MeasureSpec.getSize(heightMeasureSpec));
    }

    public void setVideoSize(int videoWidth,int videoHeight){
        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = videoWidth;
        params.height = videoHeight;
        setLayoutParams(params);
    }
}
