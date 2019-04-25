package com.ming.live_player.model.AudioGlide;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelLoader;

import java.io.InputStream;

/**
 * @author ming
 * @version $Rev$
 * @des 定义一个ModelLoader的实现类包装DataFetcher
 */
public class AudioModuleLoader implements ModelLoader<AudioCover, InputStream>{
    @Nullable
    @Override
    public LoadData<InputStream> buildLoadData(@NonNull AudioCover audioCover, int width, int height, @NonNull Options options) {
        return new LoadData<>(new AudioCoverSignature(audioCover.path), new AudioCoverFetcher(audioCover));
    }

    @Override
    public boolean handles(@NonNull AudioCover audioCover) {
        return true;
    }
}
