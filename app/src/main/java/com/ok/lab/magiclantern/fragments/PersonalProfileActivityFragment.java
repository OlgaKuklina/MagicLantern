package com.ok.lab.magiclantern.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ok.lab.magiclantern.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class PersonalProfileActivityFragment extends Fragment {

    public PersonalProfileActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_personal_profile, container, false);
    }
}
