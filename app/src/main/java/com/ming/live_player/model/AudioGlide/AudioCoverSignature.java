package com.ming.live_player.model.AudioGlide;

import com.bumptech.glide.load.Key;

import java.io.File;
import java.security.MessageDigest;

/**
 * @author ming
 * @version $Rev$
 * @des 定义一个Key的实现类，用于实现第一步的Model中的数据的签名用于区分缓存
 */
public class AudioCoverSignature implements Key {
    private final File file;
    private StringBuilder stringBuilder;

    public AudioCoverSignature(String path) {
        this.file = new File(path);
        stringBuilder = new StringBuilder();
    }

    @Override public void updateDiskCacheKey(MessageDigest messageDigest) {
        stringBuilder.append(file.lastModified()).append(file.getAbsolutePath());
        byte[] bs = stringBuilder.toString().getBytes();
        messageDigest.update(bs, 0, bs.length);
    }

}
