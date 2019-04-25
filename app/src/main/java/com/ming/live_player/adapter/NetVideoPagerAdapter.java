package com.ming.live_player.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ming.live_player.R;
import com.ming.live_player.bean.MediaItem;

import java.util.ArrayList;

/**
 * @author Admin
 * @version $Rev$
 * @des 网络视频列表
 */
public class NetVideoPagerAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<MediaItem> mMediaItems;
    private ViewHolder mViewHolder;

    public NetVideoPagerAdapter(Context context, ArrayList<MediaItem> mediaItems) {
        this.context=context;
        this.mMediaItems=mediaItems;

    }

    @Override
    public int getCount() {
        return mMediaItems.size();
    }

    @Override
    public Object getItem(int i) {
        return mMediaItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=View.inflate(context, R.layout.net_video_item,null);
            mViewHolder=new ViewHolder();
            mViewHolder.tv_desc=(TextView)convertView.findViewById(R.id.tv_desc);
            mViewHolder.tv_name=(TextView)convertView.findViewById(R.id.tv_name);
            mViewHolder.iv_image_url=(ImageView)convertView.findViewById(R.id.iv_image_url);
            convertView.setTag(mViewHolder);
        }else{
            mViewHolder= (ViewHolder) convertView.getTag();
        }
        MediaItem mediaItem=mMediaItems.get(position);
        mViewHolder.tv_name.setText(mediaItem.getName());
        mViewHolder.tv_desc.setText(mediaItem.getDesc());
        Glide.with(context).load(mediaItem.getImageUrl()).into(mViewHolder.iv_image_url);
        return convertView;
    }

    static class ViewHolder{
        TextView tv_name;
        TextView tv_desc;
        ImageView iv_image_url;
    }

}
