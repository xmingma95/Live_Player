package com.ming.live_player.presenter;

import com.ming.live_player.bean.MediaItem;

import java.util.ArrayList;

/**
 * @author ming
 * @version $Rev$
 * @des ${TODO}
 */
public interface VideoListPresenter extends BasePresenter {
    void startPlayerVideo(ArrayList<MediaItem> mediaItems,int position);
}
