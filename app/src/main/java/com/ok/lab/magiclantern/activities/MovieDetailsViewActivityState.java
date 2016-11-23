package com.ok.lab.magiclantern.activities;

import com.ok.lab.magiclantern.data.CastData;
import com.ok.lab.magiclantern.data.MovieDataContainer;
import com.ok.lab.magiclantern.data.ReviewData;
import com.ok.lab.magiclantern.data.TrailerData;

import java.util.List;

/**
 * Created by olgakuklina on 2015-09-10.
 */
public class MovieDetailsViewActivityState {
    private final List<TrailerData> trailerDatas;
    private final List<ReviewData> reviewDatas;
    private final List<CastData> castDatas;
    private final MovieDataContainer detailDatas;


    public MovieDetailsViewActivityState(List<TrailerData> trailerDatas, List<ReviewData> reviewDatas, List<CastData> castDatas, MovieDataContainer detailDatas) {
        this.trailerDatas = trailerDatas;
        this.reviewDatas = reviewDatas;
        this.castDatas = castDatas;
        this.detailDatas = detailDatas;
    }

    public List<TrailerData> getTrailerDatas() {
        return trailerDatas;
    }

    public List<ReviewData> getReviewDatas() {
        return reviewDatas;
    }

    public List<CastData> getCastDatas() {
        return castDatas;
    }

    public MovieDataContainer getDetailDatas() {
        return detailDatas;
    }
}
