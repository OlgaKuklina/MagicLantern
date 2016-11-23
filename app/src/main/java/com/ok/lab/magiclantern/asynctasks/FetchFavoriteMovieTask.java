package com.ok.lab.magiclantern.asynctasks;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.ok.lab.magiclantern.adapters.ImageAdapter;
import com.ok.lab.magiclantern.data.MovieData;

import java.util.ArrayList;

import static com.ok.lab.magiclantern.data.FavoriteMoviesContract.FavoriteMovieColumn.COLUMN_NAME_MOVIE_ID;
import static com.ok.lab.magiclantern.data.FavoriteMoviesContract.FavoriteMovieColumn.COLUMN_NAME_TITLE;
import static com.ok.lab.magiclantern.data.FavoriteMoviesContract.FavoriteMovieColumn.COLUMN_POSTER_PATH;

/**
 * Created by olgakuklina on 2015-09-10.
 */
public class FetchFavoriteMovieTask extends AsyncTask<Void, Void, ArrayList<MovieData>> {

    private static final String TAG = FetchFavoriteMovieTask.class.getSimpleName();
    private static final Uri URI = Uri.parse("content://com.ok.lab.magiclantern.provider/favorite");

    private final String posterBaseUri;
    private final ImageAdapter adapter;
    private final ContentResolver contentResolver;

    public FetchFavoriteMovieTask(ImageAdapter adapter, ContentResolver contentResolver, String posterBaseUri) {
        this.adapter = adapter;
        this.contentResolver = contentResolver;
        this.posterBaseUri = posterBaseUri;
    }

    @Override
    protected ArrayList<MovieData> doInBackground(Void... params) {
        ArrayList<MovieData> moviePosters = new ArrayList<>();
        final Cursor cursor = contentResolver.query(URI, new String[]{COLUMN_NAME_MOVIE_ID, COLUMN_POSTER_PATH, COLUMN_NAME_TITLE}, null, null, null);

        if (cursor != null && cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                MovieData data = new MovieData(posterBaseUri + cursor.getString(1), cursor.getInt(0), cursor.getString(2));
                moviePosters.add(data);
            }
        } else {
            Log.d(TAG, "Cursor is empty");
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


