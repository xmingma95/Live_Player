package com.ming.live_player.model;

        import android.content.Context;
        import android.content.SharedPreferences;

/**
 * 作用：缓存工具类
 */
public class CacheUtils {

    /**
     * 保持播放模式
     */
    public static void putPlaymode(Context context,String key,int values){
        SharedPreferences sharedPreferences = context.getSharedPreferences("live_player",Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(key,values).apply();

    }

    /**
     *得到播放模式
     */
    public static int getPlaymode(Context context,String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences("live_player",Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, 1);
    }

    /**
     * 保持数据
     */
    public static  void putString(Context context,String key,String values){
        SharedPreferences sharedPreferences = context.getSharedPreferences("live_player",Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(key,values).apply();
    }

    /**
     * 得到缓存的数据
     */
    public static String getString(Context context,String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences("live_player",Context.MODE_PRIVATE);
        return  sharedPreferences.getString(key,"");
    }


}
