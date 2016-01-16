package com.example.android.magiclantern.data;

/**
 * Created by olgakuklina on 2015-08-30.
 */
public class MovieData {
    private final String moviePoster;
    private final int movieId;
    private final int page;

    public MovieData(String moviePoster, int movieId, int page) {
        this.moviePoster = moviePoster;
        this.movieId = movieId;
        this.page = page;
    }
    public MovieData(String moviePoster, int movieId ) {
        this.moviePoster = moviePoster;
        this.movieId = movieId;
        page = -1;
    }
    public String getMoviePoster() {
        return moviePoster;
    }

    public int getMovieId() {
        return movieId;
    }
    public int getMoviePage() {
        return page;
    }
}
