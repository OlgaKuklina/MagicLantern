package com.example.android.magiclantern.activities;

import com.example.android.magiclantern.data.MovieDataContainer;
import com.example.android.magiclantern.data.ReviewData;
import com.example.android.magiclantern.data.TrailerData;

import java.util.List;

/**
 * Created by olgakuklina on 2015-09-10.
 */
public class MovieDetailsViewActivityState {
    private final List<TrailerData> trailerDatas;
    private final List<ReviewData> reviewDatas;
    private final MovieDataContainer detailDatas;

    public MovieDetailsViewActivityState(List<TrailerData> trailerDatas, List<ReviewData> reviewDatas, MovieDataContainer detailDatas) {
        this.trailerDatas = trailerDatas;
        this.reviewDatas = reviewDatas;
        this.detailDatas = detailDatas;
    }

    public List<TrailerData> getTrailerDatas() {
        return trailerDatas;
    }

    public List<ReviewData> getReviewDatas() {
        return reviewDatas;
    }

    public MovieDataContainer getDetailDatas() {
        return detailDatas;
    }
}
