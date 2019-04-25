package com.ming.live_player.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.ming.live_player.R;
import com.ming.live_player.adapter.VideoItemAdapter;
import com.ming.live_player.bean.MediaItem;
import com.ming.live_player.presenter.VideoListPresenterImp;
import com.ming.live_player.view.VideoListView;

import java.util.ArrayList;

public class VideoListActivity extends AppCompatActivity implements VideoListView {

    private ImageButton ib_title_back;
    private TextView tv_title;
    private ListView lv_video_list;
    private ArrayList<MediaItem> mediaItems;
    private VideoListPresenterImp videoListPresenterImp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();

    }

    private void initData() {
        mediaItems= (ArrayList<MediaItem>) getIntent().getSerializableExtra("videolist");
        if(mediaItems!=null){
            //设置标题栏
            tv_title.setText(mediaItems.get(0).getParentName());
        }
        VideoItemAdapter videoItemAdapter=
                new VideoItemAdapter(this,mediaItems,true);
        lv_video_list.setAdapter(videoItemAdapter);
        ib_title_back.setVisibility(View.VISIBLE);
        //设置返回键和listview的点击事件
        setListener();

    }

    private void setListener() {
        ib_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        lv_video_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                videoListPresenterImp.startPlayerVideo(mediaItems,position);
            }
        });
    }

    private void initView() {
        setContentView(R.layout.activity_videolist);
        ib_title_back =(ImageButton)findViewById(R.id.ib_title_back);
        tv_title =(TextView)findViewById(R.id.tv_title);
        lv_video_list =(ListView)findViewById(R.id.lv_video_list);
        //绑定P层
        videoListPresenterImp=new VideoListPresenterImp();
        videoListPresenterImp.attachView(this);
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
    protected void onDestroy() {
        super.onDestroy();
        videoListPresenterImp.detachView();
    }
}
