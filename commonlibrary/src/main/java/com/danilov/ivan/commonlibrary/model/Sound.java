package com.danilov.ivan.commonlibrary.model;

import java.io.Serializable;

/**
 * Created by Ivan Danilov on 17.06.2016.
 * Email: i.danilov@globus-ltd.com
 */
public class Sound implements Serializable {

    private String author;
    private String trackName;
    private boolean isPlay;

    public Sound(String author, String trackName, boolean isPlay) {
        this.author = author;
        this.trackName = trackName;
        this.isPlay = isPlay;
    }

    public Sound() {
    }

    public String getAuthor() {
        return author;
    }

    public String getTrackName() {
        return trackName;
    }

    public boolean isPlay() {
        return isPlay;
    }

    public void setPlay(boolean play) {
        isPlay = play;
    }
}
