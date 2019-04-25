package com.ming.live_player.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ming.live_player.R;
import com.ming.live_player.bean.MediaItem;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author ming
 * @version $Rev$
 * @des 视频文件夹的Adapter
 */
public class VideoParentAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<MediaItem> mMediaItems;
    private HashMap<Integer, ArrayList<MediaItem>> mediaMap;
    private ViewHolder mViewHolder;
    public VideoParentAdapter(Context context, HashMap<Integer, ArrayList<MediaItem>> mediaMap) {
        this.context=context;
        this.mediaMap=mediaMap;
    }

    @Override
    public int getCount() {
        return mediaMap.size();
    }

    @Override
    public Object getItem(int i) {
        return mediaMap.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if(convertView==null){
            convertView=View.inflate(context, R.layout.listview_item_package,null);
            mViewHolder=new ViewHolder();
            mViewHolder.tv_package_name=(TextView)convertView.findViewById(R.id.tv_package_name);
            convertView.setTag(mViewHolder);
        }else{
            mViewHolder= (ViewHolder) convertView.getTag();
        }
        Log.e("madeaMap",mediaMap.toString());
        if(mediaMap.get(0)!=null){
            mViewHolder.tv_package_name.setText(mediaMap.get(position).get(0).getParentName());
        }


        return convertView;
    }

    static class ViewHolder{
        TextView tv_package_name;
    }
}
