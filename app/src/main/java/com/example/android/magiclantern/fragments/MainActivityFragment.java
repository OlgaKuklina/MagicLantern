package com.example.android.magiclantern.fragments;

import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.android.magiclantern.R;
import com.example.android.magiclantern.adapters.ImageAdapter;
import com.example.android.magiclantern.asynctasks.FetchMovieListener;
import com.example.android.magiclantern.asynctasks.FetchNowPlaying;
import com.example.android.magiclantern.asynctasks.OnMovieClickListener;

/**
 * Created by olgakuklina on 2015-11-14.
 */
public class MainActivityFragment extends Fragment {

    private static final String TAG = MainActivityFragment.class.getSimpleName();

    private ImageAdapter adapter;
    private GridView gridview;


    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ImageAdapter(getActivity());
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_popular_movies_universal, container, false);

        gridview = (GridView) view.findViewById(R.id.gridview);
        if (getActivity() instanceof OnMovieClickListener) {
            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    int movieId = (int) adapter.getItemId(position);
                    ((OnMovieClickListener) getActivity()).onMovieClick(movieId);
                }
            });
        }
        gridview.setAdapter(adapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();


        boolean isConnected = checkInternetConnection();
        Log.v(TAG, "Network is" + isConnected);
        if (!isConnected ) {
            Log.e(TAG, "Network is not available");

            Toast toast = Toast.makeText(getActivity().getApplicationContext(), R.string.network_not_available_message, Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        NowPlayingMovieViewScrollListener fetchMovieListener = new NowPlayingMovieViewScrollListener();
//        FetchNowPlaying task = new FetchNowPlaying(adapter, fetchMovieListener);
//                task.execute();
            gridview.setOnScrollListener(fetchMovieListener);
    }


    private boolean checkInternetConnection() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    private class NowPlayingMovieViewScrollListener implements AbsListView.OnScrollListener, FetchMovieListener {
        private static final int PAGE_SIZE = 20;
        private boolean loadingState = false;


        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (firstVisibleItem + visibleItemCount >= totalItemCount) {

                if (!loadingState) {
                    FetchNowPlaying fetchMovieTask = new FetchNowPlaying(adapter, this);
                    fetchMovieTask.execute(totalItemCount / PAGE_SIZE + 1);
                    loadingState = true;

                }
            }

        }


        @Override
        public void onFetchCompleted() {
            loadingState = false;

        }

        @Override
        public void onFetchFailed() {
            loadingState = false;
            Toast.makeText(getActivity(), R.string.popular_movies_activity_text_conection_error, Toast.LENGTH_LONG).show();
        }
    }
}


