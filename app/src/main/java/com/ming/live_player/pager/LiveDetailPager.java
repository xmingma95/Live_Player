package com.ming.live_player.pager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ming.live_player.R;
import com.ming.live_player.activity.MyVideoPlayerActivity;
import com.ming.live_player.adapter.NetVideoPagerAdapter;
import com.ming.live_player.base.BasePager;
import com.ming.live_player.bean.MediaItem;
import com.ming.live_player.model.OkHttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Response;

/**
 * @author ming
 * @version $Rev$
 * @des 本来这里应该是一个直播列表，但是后台现在没做了，就写一个网络视频列表好了
 */
public class LiveDetailPager extends BasePager {

    private int position;
    private ListView lv_net_video;
    private TextView tv_netvideo_content;
    private ProgressBar pb_video_loading;

    /**
     * 装数据集合
     */
    private ArrayList<MediaItem> mediaItems;

    private NetVideoPagerAdapter adapter;
    /**
     * 网络视频的联网地址
     */
    private static  final  String NET_URL = "http://api.m.mtime.cn/PageSubArea/TrailerList.api";

    public LiveDetailPager(Context context,int position) {
        super(context);
        this.position=position;
    }

    @Override
    protected View initView() {
        View view=View.inflate(context,R.layout.pager_live,null);
        lv_net_video =(ListView)view.findViewById(R.id.lv_net_video);
        tv_netvideo_content =(TextView)view.findViewById(R.id.tv_netvideo_no);
        pb_video_loading =(ProgressBar)view.findViewById(R.id.pb_video_loading);
        lv_net_video.setOnItemClickListener(new MyNetOnItemClickListener());

        return view;
    }

    @Override
    public void initData() {
        super.initData();
 //       mTextView.setText("我是直播页面，"+position);
        getDataFromNet();
    }

    private void getDataFromNet() {
        OkHttpUtil okHttpUtil=OkHttpUtil.getInstance();
        okHttpUtil.getDataAsynFromNet(NET_URL, new OkHttpUtil.MyNetCall() {
            @Override
            public void success(okhttp3.Call call, Response response) throws IOException {
                if(response.body()!=null){
                    String json=response.body().string();
                    System.out.println(json);
                    processData(json);
                }
            }

            @Override
            public void failed(okhttp3.Call call, IOException e) {
                Log.e("net===","网络连接失败");
            }
        });

    }

    private void processData(final String json) {
        System.out.println("processData");
        Observable.create(new ObservableOnSubscribe<ArrayList<MediaItem>>() {
            @Override
            public void subscribe(ObservableEmitter<ArrayList<MediaItem>> emitter) {
                mediaItems= parseJson(json);
                emitter.onNext(mediaItems);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<MediaItem>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ArrayList<MediaItem> mediaItems) {
                        showData(mediaItems);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void showData(ArrayList<MediaItem> mediaItems) {
        this.mediaItems=mediaItems;
        //设置适配器
        if(mediaItems != null && mediaItems.size() >0){
            //有数据
            //设置适配器
            adapter = new NetVideoPagerAdapter(context,mediaItems);
            lv_net_video.setAdapter(adapter);
            //把文本隐藏
            tv_netvideo_content.setVisibility(View.GONE);
        }else{
            //没有数据
            //文本显示
            tv_netvideo_content.setVisibility(View.VISIBLE);
        }
        //ProgressBar隐藏
        pb_video_loading.setVisibility(View.GONE);

    }

    private ArrayList<MediaItem> parseJson(String json) {
        System.out.println("处理json"+json);
        ArrayList<MediaItem> mediaItems=new ArrayList<>();
        try {
            JSONObject jsonObject=new JSONObject(json);
            JSONArray jsonArray=jsonObject.optJSONArray("trailers");
            if(jsonArray!=null&&jsonArray.length()>0){
                for (int i = 0; i <jsonArray.length() ; i++) {
                    JSONObject jsonObjectItem = (JSONObject) jsonArray.get(i);
                    if(jsonObjectItem!=null){
                        MediaItem mediaItem = new MediaItem();
                        String name=jsonObjectItem.optString("movieName");//标题
                        mediaItem.setName(name);
                        String videoTitle=jsonObjectItem.optString("videoTitle");//文字描述
                        mediaItem.setDesc(videoTitle);
                        String imageUrl=jsonObjectItem.optString("coverImg");//图片地址
                        mediaItem.setImageUrl(imageUrl);
                        String hightUrl = jsonObjectItem.optString("hightUrl");//视频地址
                        mediaItem.setData(hightUrl);
                        mediaItems.add(mediaItem);
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mediaItems;
    }

    private class MyNetOnItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            //3.传递列表数据-对象-序列化
            Intent intent = new Intent(context,MyVideoPlayerActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("videolist",mediaItems);
            intent.putExtras(bundle);
            intent.putExtra("position",i);
            context.startActivity(intent);
        }
    }
}
