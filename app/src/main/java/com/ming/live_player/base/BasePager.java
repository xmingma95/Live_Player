package com.ming.live_player.base;

import android.content.Context;
import android.view.View;

/**
 * @author ming
 * @version $Rev$
 * @des viewpager 基类
 */
public abstract class BasePager {
    public final Context context;

    public View rootView;

    public BasePager(Context context) {
        this.context = context;
        rootView=initView();
    }

    protected abstract View initView();

    public void initData(){

    }


}
