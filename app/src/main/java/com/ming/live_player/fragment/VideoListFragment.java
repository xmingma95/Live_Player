package com.ming.live_player.fragment;

import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.ming.live_player.R;
import com.ming.live_player.adapter.VideoItemAdapter;
import com.ming.live_player.bean.MediaItem;

import java.util.ArrayList;

/**
 * @author ming
 * @version $Rev$
 * @des 点击文件夹进入的视频列表
 */
public class VideoListFragment extends BaseFragment {

    private ListView lv_video_list;
    private ArrayList<MediaItem> mMediaItems;


    @Override
    protected View initView() {
        View view =View.inflate(mContext, R.layout.fragment_video_list,null);
        lv_video_list = (ListView) view.findViewById(R.id.lv_video_list);
        return view;
    }

    @Override
    protected void initData() {
        super.initData();
        mMediaItems= (ArrayList<MediaItem>) getArguments().getSerializable("videolist");
        Log.e("videolist","===="+mMediaItems.size());
        VideoItemAdapter videoItemAdapter=new VideoItemAdapter(mContext,mMediaItems,true);
        lv_video_list.setAdapter(videoItemAdapter);
        lv_video_list.deferNotifyDataSetChanged();

    }
}
