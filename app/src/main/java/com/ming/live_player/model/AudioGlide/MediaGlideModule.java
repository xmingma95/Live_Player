package com.ming.live_player.model.AudioGlide;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;

/**
 * @author ming
 * @version $Rev$
 * @des 将自定义加载配置到GlideModule中
 */
@GlideModule
public class MediaGlideModule extends AppGlideModule {
    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        super.registerComponents(context, glide, registry);
        registry.append(
                AudioCover.class,
                InputStream.class,
                new AudioCoverLoaderFactory());
    }

}
