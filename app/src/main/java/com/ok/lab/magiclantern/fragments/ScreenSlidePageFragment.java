package com.ok.lab.magiclantern.fragments;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ok.lab.magiclantern.R;

/**
 * Created by olgakuklina on 2016-12-04.
 */

public class ScreenSlidePageFragment extends Fragment{


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            ViewGroup rootView = (ViewGroup) inflater.inflate(
                    R.layout.fragment_screen_slide_page, container, false);

            return rootView;
        }


}
