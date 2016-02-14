package com.example.android.magiclantern.activities;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.android.magiclantern.R;
import com.example.android.magiclantern.asynctasks.OnMovieClickListener;
import com.example.android.magiclantern.fragments.DetailsViewUniversalActivityFragment;
import com.example.android.magiclantern.fragments.PopularMoviesUniversalActivityFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMovieClickListener {

    private static final String SHARED_PREF_NAME = "com.example.android.magiclantern.magic.lantern";
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().collapseActionView();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences prefs = MainActivity.this.getSharedPreferences(SHARED_PREF_NAME, 0);

                        SharedPreferences.Editor e = prefs.edit();
                        e.putString("pref_sorting", "favorites"); // save "value" to the SharedPreferences
                        e.commit();
                        PopularMoviesUniversalActivityFragment newFragment = new PopularMoviesUniversalActivityFragment();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        getSupportActionBar().setTitle("Favorites");

                        transaction.replace(R.id.main_fragment, newFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                });
            }
        });
        Log.v(TAG, "MainActivity:savedInstanceState=" + savedInstanceState);

        if (savedInstanceState == null) {
            PopularMoviesUniversalActivityFragment newFragment = new PopularMoviesUniversalActivityFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            transaction.replace(R.id.main_fragment, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        SharedPreferences prefs = this.getSharedPreferences(SHARED_PREF_NAME, 0);
        String prefSortOrder = prefs.getString("pref_sorting", getString(R.string.pref_sort_default));
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu menu = navigationView.getMenu();
        switch (prefSortOrder) {
            case "popularity.desc":
                menu.getItem(1).setChecked(true);
                getSupportActionBar().setTitle("Popular");
                break;
            case "upcoming":
                menu.getItem(3).setChecked(true);
                getSupportActionBar().setTitle("Upcoming");
                break;
            case "top_rated":
                menu.getItem(2).setChecked(true);
                getSupportActionBar().setTitle("Top rated");
                break;
            case "current.desc":
                menu.getItem(0).setChecked(true);
                getSupportActionBar().setTitle("In Theatres");
                break;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.now_playing) {

            SharedPreferences prefs = this.getSharedPreferences(SHARED_PREF_NAME, 0);

            SharedPreferences.Editor e = prefs.edit();
            e.putString("pref_sorting", "current.desc"); // save "value" to the SharedPreferences
            e.commit();

            PopularMoviesUniversalActivityFragment newFragment = new PopularMoviesUniversalActivityFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            transaction.replace(R.id.main_fragment, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();

            getSupportActionBar().setTitle("In Theatres");

        } else if (id == R.id.top_rated) {
            SharedPreferences prefs = this.getSharedPreferences(SHARED_PREF_NAME, 0);

            SharedPreferences.Editor e = prefs.edit();
            e.putString("pref_sorting", "top_rated"); // save "value" to the SharedPreferences
            e.commit();

            PopularMoviesUniversalActivityFragment newFragment = new PopularMoviesUniversalActivityFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            transaction.replace(R.id.main_fragment, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();

            getSupportActionBar().setTitle("Top rated");

        } else if (id == R.id.popular) {

            SharedPreferences prefs = this.getSharedPreferences(SHARED_PREF_NAME, 0);

            SharedPreferences.Editor e = prefs.edit();
            e.putString("pref_sorting", "popularity.desc"); // save "value" to the SharedPreferences
            e.commit();
            PopularMoviesUniversalActivityFragment newFragment = new PopularMoviesUniversalActivityFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            transaction.replace(R.id.main_fragment, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();

            getSupportActionBar().setTitle("Popular");

        } else if (id == R.id.upcoming) {
            SharedPreferences prefs = this.getSharedPreferences(SHARED_PREF_NAME, 0);

            SharedPreferences.Editor e = prefs.edit();
            e.putString("pref_sorting", "upcoming"); // save "value" to the SharedPreferences
            e.commit();
            PopularMoviesUniversalActivityFragment newFragment = new PopularMoviesUniversalActivityFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            transaction.replace(R.id.main_fragment, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();

            getSupportActionBar().setTitle("Upcoming");

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMovieClick(int movieId) {
        Log.v(TAG, "onMovieClick movieId = " + movieId);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.tablet_popular_movies_fragment_layout);
        if (layout == null) {
            Intent intent = new Intent(this, DetailsViewUniversalActivity.class)
                    .putExtra(Intent.EXTRA_TEXT, movieId);
            startActivity(intent);
        } else {
            DetailsViewUniversalActivityFragment fragment = (DetailsViewUniversalActivityFragment) getFragmentManager().findFragmentById(R.id.tablet_details_fragment);
            fragment.clearState();
            fragment.fetchMovieData(movieId);
            layout.setVisibility(View.VISIBLE);

        }
    }
}
