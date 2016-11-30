package com.ok.lab.magiclantern.data;

import java.util.List;

/**
 * Created by olgakuklina on 2015-09-10.
 */
public class MovieDataContainer extends MovieData {

    public final String plot;
    public final String year;
    public final Integer duration;
    public final Double vote_average;
    public final String backdrop_path;
    public final String original_language;
    public final List<String> original_countries;
    public final List<String> genres;
    public final String status;
    public final String imdb_uri;
    public final List<String> production_companies;



    public MovieDataContainer(String moviePoster, int movieId, String title, String plot, String year, Integer duration, Double vote_average, String backgroundPath, String originalLanguage, List<String> original_countries, List<String> genres, String status, String imdb_uri, List<String> production_companies) {
        super(moviePoster, movieId, title);
        this.plot = plot;
        this.year = year;
        this.duration = duration;
        this.vote_average = vote_average;
        this.backdrop_path = backgroundPath;
        this.original_language = originalLanguage;
        this.original_countries = original_countries;
        this.genres = genres;
        this.status = status;
        this.imdb_uri = imdb_uri;
        this.production_companies = production_companies;
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

    public Double getVote_average() {
        return vote_average;
    }

    public String getbackgroundPath() {
        return backdrop_path;
    }

    public String getOriginalLanguage() {
        return original_language;
    }

    public List<String> getOriginalCountries() {
        return original_countries;
    }

    public List<String> getGenres() { return genres; }

    public String getStatus() { return status; }

    public String getImdb_uri() { return imdb_uri; }

    public List<String> getProduction_companies() {
        return production_companies;
    }
}
