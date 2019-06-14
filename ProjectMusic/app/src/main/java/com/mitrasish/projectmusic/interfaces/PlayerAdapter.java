package com.mitrasish.projectmusic.interfaces;

import android.net.Uri;

public interface PlayerAdapter {

    void loadMedia(Uri mediaUri);

    void release();

    boolean isPlaying();

    void play();

    void reset(Uri mediaUri);

    void pause();

    void initializeProgressCallback();

    void seekTo(int position);



}
