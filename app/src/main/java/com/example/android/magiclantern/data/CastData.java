package com.example.android.magiclantern.data;

/**
 * Created by olgakuklina on 2016-11-20.
 */

public class CastData {

    private String actorName;
    private String pictureIdPath;

    public CastData(String actorName, String pictureIdPath) {
        this.actorName = actorName;
        this.pictureIdPath = pictureIdPath;
    }

    public String getReviewAuthor() {
        return actorName;
    }

    public String getimagePath() {
        return pictureIdPath;
    }
}
