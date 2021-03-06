package com.ok.lab.magiclantern.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.ok.lab.magiclantern.adapters.ImageAdapter;
import com.ok.lab.magiclantern.data.MovieData;
import com.ok.lab.magiclantern.utils.JSONLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by olgakuklina on 2015-11-14.
 */
public class FetchUpcomingMovieTask extends AsyncTask<Integer, Void, ArrayList<MovieData>> {

    private static final String TAG = FetchUpcomingMovieTask.class.getSimpleName();
    private final FetchMovieListener fetchMovieListener;
    private final ImageAdapter adapter;
    private final String posterBaseUri;
    private final String apiKey;

    public FetchUpcomingMovieTask(ImageAdapter adapter, FetchMovieListener fetchMovieListener, String apiKey, String posterBaseUri) {
        this.adapter = adapter;
        this.fetchMovieListener = fetchMovieListener;
        this.posterBaseUri = posterBaseUri;
        this.apiKey = apiKey;
    }

    @Override
    protected void onPostExecute(ArrayList<MovieData> moviePosters) {
        super.onPostExecute(moviePosters);
        if (moviePosters != null) {
            for (MovieData res : moviePosters) {
                adapter.add(res);
            }
            adapter.notifyDataSetChanged();
            fetchMovieListener.onFetchCompleted();
        } else {
            fetchMovieListener.onFetchFailed();
        }
    }

    @Override
    protected ArrayList<MovieData> doInBackground(Integer... params) {
        ArrayList<MovieData> moviePosters = new ArrayList<>();
        try {
            JSONObject jObj = JSONLoader.load("/movie/upcoming" + "?page=" + params[0], apiKey);
            if (jObj == null) {
                Log.w(TAG, "Can not load the data from remote service");
                return null;
            }
            JSONArray movieArray = jObj.getJSONArray("results");
            for (int i = 0; i < movieArray.length(); i++) {
                JSONObject movie = movieArray.optJSONObject(i);
                String moviePoster = movie.getString("poster_path");
                int movieId = movie.getInt("id");
                String movieTitle = movie.getString("title");
                MovieData data = new MovieData(posterBaseUri + moviePoster, movieId, movieTitle);
                moviePosters.add(data);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error ", e);
        }
        return moviePosters;
    }
}
