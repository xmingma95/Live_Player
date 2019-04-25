package com.ming.live_player.adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.ming.live_player.R;
import com.ming.live_player.bean.MediaItem;
import com.ming.live_player.model.AudioGlide.AudioCover;
import com.ming.live_player.model.AudioGlide.GlideApp;
import com.ming.live_player.model.SystemUtil;

import java.util.ArrayList;

/**
 * @author ming
 * @version $Rev$
 * @des 本地视频列表和音乐列表的Adapter
 */
public class VideoItemAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<MediaItem> mMediaItems;
    private ViewHolder mViewHolder;
    private SystemUtil systemUtils;
    private Boolean isVideo;
    private int width;
    private int height;

    public VideoItemAdapter(Context context, ArrayList<MediaItem> mediaItems, Boolean isVideo) {
        this.context = context;
        mMediaItems = mediaItems;
        systemUtils=SystemUtil.getInstance(context);
        width=systemUtils.dip2px(80);
        height=systemUtils.dip2px(60);
        this.isVideo=isVideo;
    }

    @Override
    public int getCount() {
        Log.e("Videoadatper","count"+mMediaItems.size());
        return mMediaItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mMediaItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=View.inflate(context, R.layout.listview_item_video,null);
            mViewHolder = new ViewHolder();
            mViewHolder.tv_video_name=(TextView)convertView.findViewById(R.id.tv_video_name);
            mViewHolder.tv_video_time=(TextView)convertView.findViewById(R.id.tv_video_time);
            mViewHolder.tv_video_data=(TextView)convertView.findViewById(R.id.tv_video_data);
            mViewHolder.iv_video_logo=(ImageView)convertView.findViewById(R.id.iv_video_logo);
            convertView.setTag(mViewHolder);
        }else {
            mViewHolder= (ViewHolder) convertView.getTag();
        }

        MediaItem mediaItem=mMediaItems.get(position);
        mViewHolder.tv_video_name.setText(mediaItem.getName());
        mViewHolder.tv_video_time.setText(systemUtils.stringForTime((int) mediaItem.getDuration()));
        mViewHolder.tv_video_data.setText(Formatter.formatFileSize(context,mediaItem.getSize()));
       // glide显示视频缩略图
        if(isVideo){
            GlideApp.with(context)
                    .load(mediaItem.getData())
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    //设置圆角，不变形
                    .transform(new CenterCrop(),new RoundedCorners(20))
                    .placeholder(R.drawable.video_default)
                    .error(R.drawable.video_default)
                    .into(mViewHolder.iv_video_logo);
        }else {
            //设置专辑封面
            GlideApp.with(context)
                    //使用Model类包装数据即可
                    .load(new AudioCover(mediaItem.getData()))
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    //设置圆角，不变形
                    .transform(new CenterCrop(),new RoundedCorners(20))
                    .placeholder(R.drawable.music_default)
                    .error(R.drawable.music_default)
                    .into(mViewHolder.iv_video_logo);
        }

        return convertView;
    }

    static class ViewHolder{
        ImageView iv_video_logo;
        TextView tv_video_name;
        TextView tv_video_time;
        TextView tv_video_data;
    }


}
