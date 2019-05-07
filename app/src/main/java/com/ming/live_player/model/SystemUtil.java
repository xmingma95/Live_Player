package com.ming.live_player.model;

import android.content.Context;
import android.provider.Settings;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

/**
 * @author ming
 * @version $Rev$
 * @des 系统工具类，时间，亮度等
 */
public class SystemUtil {

    private static SystemUtil instance=null;
    private Context context;
    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;

    private SystemUtil(Context context){
        //避免内存泄漏
        this.context=context.getApplicationContext();
        // 转换成字符串的时间
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
    }

    /**
     * 懒汉模式单例
     */
    public static SystemUtil getInstance(Context context){
        if(instance==null){
            synchronized(SystemUtil.class){
                if(instance==null){
                    instance=new SystemUtil(context);
                }
            }

        }
        return  instance;
    }


    public String getSystemTime() {
        SimpleDateFormat format=new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        return format.format(new Date());
    }

    public int getSystemBrightness() {
        int systemBrightness = 0;
        try {
            systemBrightness = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return systemBrightness;
    }

    /**
     * 把毫秒转换成：1:20:30这里形式
     * @param timeMs
     * @return
     */
    public String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;

        int minutes = (totalSeconds / 60) % 60;

        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds)
                    .toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }


    /**
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
     */
    public  int dip2px(float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public int px2dip(float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


}
