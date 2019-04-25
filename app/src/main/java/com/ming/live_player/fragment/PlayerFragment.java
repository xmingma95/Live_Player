package com.ming.live_player.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ming.live_player.R;
import com.ming.live_player.activity.MainActivity;
import com.ming.live_player.activity.VideoListActivity;
import com.ming.live_player.adapter.VideoParentAdapter;
import com.ming.live_player.bean.MediaItem;
import com.ming.live_player.presenter.PlayerPresenterImp;
import com.ming.live_player.view.PlayerView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author ming
 * @version $Rev$
 * @des 本地视频主页面
 */
public class PlayerFragment extends BaseFragment implements PlayerView {

    private static final String TAG=PlayerFragment.class.getSimpleName();
    private ListView lv_video;
    private ProgressBar pb_video_loading;
    private PlayerPresenterImp mPlayerPresenterImp;
    private TextView tv_find_no;
    private RelativeLayout rl_player;
    private VideoListFragment videoListFragment;
    private MainActivity mActivity;
    private HashMap<Integer,ArrayList<MediaItem>> mediaMap;

    @Override
    protected View initView() {
        View view =View.inflate(mContext, R.layout.fragment_player,null);
        lv_video =(ListView)view.findViewById(R.id.lv_video);
        tv_find_no =(TextView)view.findViewById(R.id.tv_find_no);
        rl_player =(RelativeLayout)view.findViewById(R.id.rl_player);
        pb_video_loading =(ProgressBar)view.findViewById(R.id.pb_video_loading);
        mPlayerPresenterImp=new PlayerPresenterImp();
        mPlayerPresenterImp.attachView(this);

        return view;
    }

    @Override
    protected void initData() {
        super.initData();
        mActivity= (MainActivity) mContext;
        mPlayerPresenterImp.getDataFromLocal();


    }

    @Override
    public void setListener() {
        lv_video.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Log.e(TAG,"onClick"+position);
                //传递播放列表，记得序列化
                Bundle bundle = new Bundle();
                bundle.putSerializable("videolist",mediaMap.get(position));
                Intent intent=new Intent(mContext,VideoListActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
//
            }
        });
    }

    @Override
    public void noContent() {
        tv_find_no.setVisibility(View.VISIBLE);
    }

    @Override
    public void showPackageList(HashMap<Integer,ArrayList<MediaItem>> mediaMap) {
        //显示视频文件夹
        if(mediaMap!=null&&mediaMap.size()>0){
            this.mediaMap=mediaMap;
            VideoParentAdapter videoParentAdapter=new VideoParentAdapter(mContext,mediaMap);
            lv_video.setAdapter(videoParentAdapter);
            dismissLoading();
            setListener();
        }else {
            dismissLoading();
            noContent();
        }

    }

    @Override
    public void showLoading() {
        pb_video_loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissLoading() {
        pb_video_loading.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlayerPresenterImp.detachView();
    }
}
