package com.ok.lab.magiclantern.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.ok.lab.magiclantern.R;
import com.ok.lab.magiclantern.asynctasks.OnMovieClickListener;

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
        Intent intent = new Intent(this, DetailsViewUniversalActivity.class)
                .putExtra(Intent.EXTRA_TEXT, movieId);
        startActivity(intent);
    }
}

