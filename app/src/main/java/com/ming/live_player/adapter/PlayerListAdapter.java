package com.ming.live_player.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ming.live_player.R;
import com.ming.live_player.bean.MediaItem;

import java.util.ArrayList;

/**
 * @author ming
 * @version $Rev$
 * @des ${TODO}
 */
public class PlayerListAdapter extends BaseAdapter {
    private ArrayList<MediaItem> mediaItems;
    private Context context;
    private ViewHolder mViewHolder;
    public PlayerListAdapter(Context context,ArrayList<MediaItem> mediaItems){
        this.mediaItems=mediaItems;
        this.context=context;
    }

    @Override
    public int getCount() {
        return mediaItems.size();
    }

    @Override
    public Object getItem(int i) {
        return mediaItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if(convertView==null){
            convertView=View.inflate(context, R.layout.listview_item_music,null);
            mViewHolder=new ViewHolder();
            mViewHolder.tv_list_music_name=convertView.findViewById(R.id.tv_list_music_name);
            convertView.setTag(mViewHolder);
        }
        mViewHolder= (ViewHolder) convertView.getTag();
        mViewHolder.tv_list_music_name.setText(mediaItems.get(position).getName());
        return convertView;
    }

    class ViewHolder{
        TextView tv_list_music_name;
    }

}
