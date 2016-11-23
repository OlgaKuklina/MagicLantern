package com.ok.lab.magiclantern.data;

/**
 * Created by olgakuklina on 2015-08-30.
 */
public class MovieData {
    private final String moviePoster;
    private final int movieId;
    private final String title;


    public MovieData(String moviePoster, int movieId, String movieTitle) {
        this.moviePoster = moviePoster;
        this.movieId = movieId;
        this.title = movieTitle;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public int getMovieId() {
        return movieId;
    }

    public String getTitle() {
        return title;
    }
}
