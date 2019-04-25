package com.ming.live_player.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.ming.live_player.pager.LiveDetailPager;

import java.util.ArrayList;

/**
 * @author ming
 * @version $Rev$
 * @des ${TODO}
 */
public class MyLiveViewPagerAdapter extends PagerAdapter {
    private ArrayList<LiveDetailPager> pagers;
    private String[] titles;
    public MyLiveViewPagerAdapter(ArrayList<LiveDetailPager> pagers,String[] titles){
        this.pagers=pagers;
        this.titles=titles;
    }

    @Override
    public int getCount() {
        return pagers.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view==o;
    }

    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        LiveDetailPager livePager=pagers.get(position);
        View view=livePager.rootView;
        container.addView(view);
        livePager.initData();
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
