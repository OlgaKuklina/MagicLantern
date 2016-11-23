package com.ok.lab.magiclantern.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import com.ok.lab.magiclantern.R;

/**
 * Created by olgakuklina on 2015-09-01.
 */
public class PopularMoviesSettingsFragment extends PreferenceFragment {
    private static final String SHARED_PREF_NAME = "com.ok.lab.magiclantern";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager preferenceManager = getPreferenceManager();
        preferenceManager.setSharedPreferencesName(SHARED_PREF_NAME);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.popular_movies_preferences);
    }
}
