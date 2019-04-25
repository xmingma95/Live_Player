package com.ming.live_player.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.ming.live_player.R;
import com.ming.live_player.adapter.MyLiveViewPagerAdapter;
import com.ming.live_player.pager.LiveDetailPager;

import java.util.ArrayList;

/**
 * @author ming
 * @version $Rev$
 * @des 观看直播主页面
 */
public class LiveFragment extends BaseFragment {

    private TabLayout tl_live;
    private ViewPager vp_live_detail;
    private ArrayList<LiveDetailPager> pagers;
    private String[] titles=new String[]{"热门","最新","搞笑","娱乐","热门","最新","搞笑","娱乐","新闻","潮流"};
    @Override
    protected View initView() {
        View view =View.inflate(mContext, R.layout.fragment_live,null);
        Log.e("fragment","success======");
        tl_live =(TabLayout)view.findViewById(R.id.tl_live);
        vp_live_detail =(ViewPager)view.findViewById(R.id.vp_live_detail);
        return view;
    }

    @Override
    protected void initData() {
        super.initData();
        pagers=new ArrayList<>();
        for (int i = 0; i <10; i++) {
            pagers.add(new LiveDetailPager(mContext,i));
        }
        vp_live_detail.setAdapter(new MyLiveViewPagerAdapter(pagers,titles));
        tl_live.setupWithViewPager(vp_live_detail);
    }
}
