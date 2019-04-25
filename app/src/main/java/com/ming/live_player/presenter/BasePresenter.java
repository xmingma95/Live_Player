package com.ming.live_player.presenter;

/**
 * @author ming
 * @version $Rev$
 * @des ${TODO}
 */
public interface BasePresenter<T> {
    void attachView(T v);
    void detachView();

}
