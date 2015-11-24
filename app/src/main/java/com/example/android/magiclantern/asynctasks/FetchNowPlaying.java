package com.example.android.magiclantern.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.android.magiclantern.adapters.ImageAdapter;
import com.example.android.magiclantern.data.MovieData;
import com.example.android.magiclantern.utils.JSONLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by olgakuklina on 2015-11-14.
 */
public class FetchNowPlaying extends AsyncTask<Integer, Void, ArrayList<MovieData>> {
    private static final String POSTER_BASE_URI = "http://image.tmdb.org/t/p/w185";
    private static final String TAG = FetchNowPlaying.class.getSimpleName();
    private final ImageAdapter adapter;
    private final FetchMovieListener fetchMovieListener;

    public FetchNowPlaying(ImageAdapter adapter, FetchMovieListener fetchMovieListener) {
        this.adapter = adapter;
        this.fetchMovieListener = fetchMovieListener;
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

            JSONObject jObj = JSONLoader.load("/movie/now_playing" + "?page=" + params[0]);
            if(jObj == null) {
                Log.w(TAG, "Can not load the data from remote service");
                return null;
            }
            Log.v(TAG, "page:" + jObj.getInt("page") + "params[0] =" + params[0]);
            JSONArray movieArray = jObj.getJSONArray("results");
            Log.v(TAG, "length:" + movieArray.length());
            for (int i = 0; i < movieArray.length(); i++) {
                JSONObject movie = movieArray.optJSONObject(i);
                String moviePoster = movie.getString("poster_path");
                int movieId = movie.getInt("id");
                MovieData data = new MovieData(POSTER_BASE_URI + moviePoster, movieId);
                moviePosters.add(data);
                Log.v(TAG, "moviePoster = " + moviePoster);
            }

        } catch (JSONException e) {
            Log.e(TAG, "Error ", e);
        }
        return moviePosters;
    }
}
