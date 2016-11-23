package com.ok.lab.magiclantern.activities;

import android.app.Activity;
import android.os.Bundle;

import com.ok.lab.magiclantern.fragments.PopularMoviesSettingsFragment;

/**
 * Created by olgakuklina on 2015-09-01.
 */
public class PopularMoviesSettingsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new PopularMoviesSettingsFragment())
                .commit();
    }
}