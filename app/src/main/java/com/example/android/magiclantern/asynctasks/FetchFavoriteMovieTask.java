package com.example.android.magiclantern.asynctasks;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.magiclantern.adapters.ImageAdapter;
import com.example.android.magiclantern.data.MovieData;

import java.util.ArrayList;

import static com.example.android.magiclantern.data.FavoriteMoviesContract.FavoriteMovieColumn.COLUMN_NAME_MOVIE_ID;
import static com.example.android.magiclantern.data.FavoriteMoviesContract.FavoriteMovieColumn.COLUMN_POSTER_PATH;

/**
 * Created by olgakuklina on 2015-09-10.
 */
public class FetchFavoriteMovieTask extends AsyncTask<Void, Void, ArrayList<MovieData>> {
    private static final Uri URI = Uri.parse("content://com.example.android.magiclantern.provider/favorite");
    private static final String POSTER_BASE_URI = "http://image.tmdb.org/t/p/w185";

    private static final String TAG = FetchFavoriteMovieTask.class.getSimpleName();
    private final ImageAdapter adapter;
    private final ContentResolver contentResolver;

    public FetchFavoriteMovieTask(ImageAdapter adapter, ContentResolver contentResolver) {
        this.adapter = adapter;
        this.contentResolver = contentResolver;
    }

    @Override
    protected ArrayList<MovieData> doInBackground(Void... params) {
        ArrayList<MovieData> moviePosters = new ArrayList<>();

        final Cursor cursor = contentResolver.query(URI, new String[]{COLUMN_NAME_MOVIE_ID, COLUMN_POSTER_PATH}, null, null, null);
        Log.d(TAG, "Cursor = " + cursor.getCount());
        if (cursor != null && cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                MovieData data = new MovieData(POSTER_BASE_URI + cursor.getString(1), cursor.getInt(0));
                moviePosters.add(data);
                Log.v(TAG, "moviePoster = " + data);
            }
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
        }

    }
}


