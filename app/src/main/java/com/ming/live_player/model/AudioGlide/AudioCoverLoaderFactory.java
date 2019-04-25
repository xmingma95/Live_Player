package com.ming.live_player.model.AudioGlide;


import android.support.annotation.NonNull;

import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;

import java.io.InputStream;


/**
 * @author ming
 * @version $Rev$
 * @des  定义一个ModelLoaderFactory的实现类用于生成ModelLoader的实例
 */
public class AudioCoverLoaderFactory implements ModelLoaderFactory<AudioCover,InputStream> {


    @NonNull
    @Override
    public ModelLoader<AudioCover, InputStream> build(@NonNull MultiModelLoaderFactory multiFactory) {
        return new AudioModuleLoader();
    }

    @Override
    public void teardown() {

    }
}

