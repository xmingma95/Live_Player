package com.ming.live_player.view;

import com.ming.live_player.bean.MediaItem;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author ming
 * @version $Rev$
 * @des ${TODO}
 */
public interface PlayerView extends BaseView {
    void setListener();
    void noContent();
    void showPackageList(HashMap<Integer,ArrayList<MediaItem>> mediaMap);
}
