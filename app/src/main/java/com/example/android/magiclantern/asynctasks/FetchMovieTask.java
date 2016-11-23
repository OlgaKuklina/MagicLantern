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
 * Created by olgakuklina on 2015-08-29.
 */
public class FetchMovieTask extends AsyncTask<Integer, Void, ArrayList<MovieData>> {

    private static final String TAG = FetchMovieTask.class.getSimpleName();
    private final FetchMovieListener fetchListener;
    private final ImageAdapter adapter;
    private final String sortOrder;
    private final String posterBaseUri;
    private final String apiKey;

    public FetchMovieTask(ImageAdapter adapter, String sortOrder, FetchMovieListener fetchListener, String apiKey, String posterBaseUri) {
        this.posterBaseUri = posterBaseUri;
        this.fetchListener = fetchListener;
        this.sortOrder = sortOrder;
        this.adapter = adapter;
        this.apiKey = apiKey;
    }

    @Override
    protected ArrayList<MovieData> doInBackground(Integer... params) {
        ArrayList<MovieData> moviePosters = new ArrayList<>();
        try {

            JSONObject jObj = JSONLoader.load("/discover/movie?sort_by=" + sortOrder + "&page=" + params[0], apiKey);
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

    @Override
    protected void onPostExecute(ArrayList<MovieData> moviePosters) {
        super.onPostExecute(moviePosters);
        if (moviePosters != null) {
            for (MovieData res : moviePosters) {
                adapter.add(res);
            }
            adapter.notifyDataSetChanged();
            fetchListener.onFetchCompleted();
        } else {
            fetchListener.onFetchFailed();
        }

    }
}

