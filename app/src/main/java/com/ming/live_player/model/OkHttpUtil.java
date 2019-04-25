package com.ming.live_player.model;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author ming
 * @des okhttp的封装类
 */
public class OkHttpUtil {
    private static final byte[] LOCKER = new byte[0];
    private OkHttpClient mOkHttpClient;
    private static OkHttpUtil mInstance;

    private OkHttpUtil() {
        OkHttpClient.Builder ClientBuilder=new OkHttpClient.Builder();
        ClientBuilder.readTimeout(10, TimeUnit.SECONDS);//读取超时
        ClientBuilder.connectTimeout(10, TimeUnit.SECONDS);//连接超时
        ClientBuilder.writeTimeout(10, TimeUnit.SECONDS);//写入超时
        mOkHttpClient=ClientBuilder.build();
    }

    /**
     * 单例模式获取OkHttpUtil
     */
    public static OkHttpUtil getInstance() {
        if (mInstance == null) {
            synchronized (LOCKER) {
                if (mInstance == null) {
                    mInstance = new OkHttpUtil();
                }
            }
        }
        return mInstance;
     }

     /**
      * 异步GET请求
      * */
    public  void getDataAsynFromNet(String url, final MyNetCall myNetCall) {
        //1 构造Request
        Request.Builder builder = new Request.Builder();
        Request request=builder.get().url(url).build();
        //2 将Request封装为Call
        Call call = mOkHttpClient.newCall(request);
        //3 执行Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                myNetCall.failed(call,e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                myNetCall.success(call,response);
            }
        });
    }

    public interface MyNetCall{
        void success(Call call, Response response) throws IOException;
        void failed(Call call, IOException e);
    }

}
