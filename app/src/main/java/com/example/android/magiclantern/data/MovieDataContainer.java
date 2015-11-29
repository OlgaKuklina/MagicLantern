package com.example.android.magiclantern.data;

/**
 * Created by olgakuklina on 2015-09-10.
 */
public class MovieDataContainer extends MovieData {

    public final String title;
    public final String plot;
    public final String year;
    public final Integer duration;
    public final Double voteaverage;
    public final String backdrop_path;

    public MovieDataContainer(String moviePoster, int movieId, String title, String plot, String year, Integer duration, Double voteaverage, String backgroundPath) {
        super(moviePoster, movieId);
        this.title = title;
        this.plot = plot;
        this.year = year;
        this.duration = duration;
        this.voteaverage = voteaverage;
        this.backdrop_path = backgroundPath;

    }

    public String getTitle() {
        return title;
    }

    public String getPlot() {
        return plot;
    }

    public String getYear() {
        return year;
    }

    public Integer getDuration() {
        return duration;
    }

    public Double getVoteaverage() {
        return voteaverage;
    }

    public String getbackgroundPath() {
        return backdrop_path;
    }

}
