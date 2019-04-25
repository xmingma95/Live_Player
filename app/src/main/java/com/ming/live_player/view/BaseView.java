package com.ming.live_player.view;

import android.content.Context;

/**
 * @author ming
 * @version 1.0
 * @des view层基本接口
 */
public interface BaseView {
    /**
     * 网络加载时显示
     */
    void showLoading();
    void dismissLoading();

    Context getContext();

}
