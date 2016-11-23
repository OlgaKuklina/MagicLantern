package com.example.android.magiclantern.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.magiclantern.R;
import com.example.android.magiclantern.data.TrailerData;
import com.example.android.magiclantern.fragments.DetailsViewUniversalActivityFragment;

import java.util.List;


public class DetailsViewUniversalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = DetailsViewUniversalActivity.class.getSimpleName();
    private static final String SHARED_PREF_NAME = "com.example.android.magiclantern.magic.lantern";
    private static final String SHARE_URI_PREFIX = "http://www.themoviedb.org/movie/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // getLayoutInflater().setFactory(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_view_universal);
        setTitle(R.string.title_activity_details_view_universal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu menu = navigationView.getMenu();
        menu.getItem(0).setEnabled(false);
        menu.getItem(1).setEnabled(false);
        menu.getItem(2).setEnabled(false);
        menu.getItem(3).setEnabled(false);
        menu.getItem(4).setEnabled(false);
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
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        switch (id) {
            case R.id.nav_share:
                Log.v(TAG, "nav_share");
                String linkUri = SHARE_URI_PREFIX + getIntent().getIntExtra(Intent.EXTRA_TEXT, -1);
                shareIntent.putExtra(Intent.EXTRA_TEXT, linkUri);
                startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.action_share)));
                break;
            case R.id.nav_send_trailer:
                DetailsViewUniversalActivityFragment fragment = (DetailsViewUniversalActivityFragment) getFragmentManager().findFragmentById(R.id.fragment);
                List<TrailerData> data = fragment.getTrailerData();
                if (data != null && !data.isEmpty()) {
                    String trailerUri = data.get(0).getTrailerUri().toString();
                    shareIntent.putExtra(Intent.EXTRA_TEXT, trailerUri);
                    startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.action_send_trailer)));
                }
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
