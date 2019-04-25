// IMusicPlayerService.aidl
package com.ming.live_player;

// Declare any non-default types here with import statements

interface IMusicPlayerService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */

    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    void openAudio(int position);

    void start();

    void pause();

    void stop();

    int getCurrentPosition();

    int getDuration();

    String getArtist();

    String getAudioPath();

    String getName();

    void playNext();

    void playpre();

    void playMusic(int position);

    void setPlayMode(int playMode);

    int getPlayMode();

    void seekTo(int progress);

    boolean isPlaying();


    }
