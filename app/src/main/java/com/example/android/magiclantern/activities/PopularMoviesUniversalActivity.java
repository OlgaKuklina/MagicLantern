package com.example.android.magiclantern.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.example.android.magiclantern.R;
import com.example.android.magiclantern.asynctasks.OnMovieClickListener;

public class PopularMoviesUniversalActivity extends Activity implements OnMovieClickListener {
    private static final String TAG = PopularMoviesUniversalActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies_universal);
        setTitle(R.string.title_activity_popular_movies_universal);
    }

    @Override
    public void onMovieClick(int movieId) {
        Log.v(TAG, "onMovieClick movieId = " + movieId);
        RelativeLayout layout = null;
        if (layout == null) {
            Intent intent = new Intent(this, DetailsViewUniversalActivity.class)
                    .putExtra(Intent.EXTRA_TEXT, movieId);
            startActivity(intent);
        } else {
//            DetailsViewUniversalActivityFragment fragment = (DetailsViewUniversalActivityFragment) getFragmentManager().findFragmentById(R.id.tablet_details_fragment);
//            fragment.clearState();
//            fragment.fetchMovieData(movieId);
//            layout.setVisibility(View.VISIBLE);

        }
    }
}
