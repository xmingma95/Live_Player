package com.ming.live_player.model.AudioGlide;

import android.media.MediaMetadataRetriever;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author ming
 * @version $Rev$
 * @des 定义一个DataFetcher的实现类(关键代码-处理封面的获取并回调给Glide)
 */
public class AudioCoverFetcher implements DataFetcher<InputStream> {

    private final AudioCover model;
    private MediaMetadataRetriever mRetriever;

    public AudioCoverFetcher(AudioCover model) {
        this.model = model;
    }


    @Override
    public void loadData(@NonNull Priority priority, @NonNull DataCallback<? super InputStream> callback) {
        mRetriever = new MediaMetadataRetriever();
        try {
            mRetriever.setDataSource(model.path);
            byte[] picture = mRetriever.getEmbeddedPicture();

            if (picture != null) {
                callback.onDataReady(new ByteArrayInputStream(picture));
                Log.e("DataFether","picture not null");
            } else {
                callback.onLoadFailed(new Exception("load audio cover fail"));
                Log.e("DataFether","picture null");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void cleanup() {
        mRetriever.release();
    }

    @Override
    public void cancel() {

    }

    @NonNull
    @Override
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }

    @NonNull
    @Override
    public DataSource getDataSource() {
        return DataSource.LOCAL;
    }
}
