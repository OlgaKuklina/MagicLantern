package com.example.android.magiclantern.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.example.android.magiclantern.R;

/**
 * Created by olgakuklina on 2015-09-01.
 */
public class PopularMoviesSettingsFragment extends PreferenceFragment {
    private static final String SHARED_PREF_NAME = "com.example.android.magiclantern.magic.lantern";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager preferenceManager = getPreferenceManager();
        preferenceManager.setSharedPreferencesName(SHARED_PREF_NAME);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.popular_movies_preferences);
    }
}
