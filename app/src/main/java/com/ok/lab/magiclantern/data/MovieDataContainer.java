package com.ok.lab.magiclantern.data;

/**
 * Created by olgakuklina on 2015-09-10.
 */
public class MovieDataContainer extends MovieData {

    public final String plot;
    public final String year;
    public final Integer duration;
    public final Double voteaverage;
    public final String backdrop_path;
    public final String original_language;

    public MovieDataContainer(String moviePoster, int movieId, String title, String plot, String year, Integer duration, Double voteaverage, String backgroundPath, String originalLanguage) {
        super(moviePoster, movieId, title);
        this.plot = plot;
        this.year = year;
        this.duration = duration;
        this.voteaverage = voteaverage;
        this.backdrop_path = backgroundPath;
        this.original_language = originalLanguage;

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

    public String getOriginalLanguage() {
        return original_language;
    }

}
