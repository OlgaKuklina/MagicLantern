package com.example.android.magiclantern.activities;

import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.android.magiclantern.R;
import com.example.android.magiclantern.fragments.DetailsViewUniversalActivityFragment;
import com.example.android.magiclantern.fragments.PopularMoviesUniversalActivityFragment;


public class DetailsViewUniversalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String SHARED_PREF_NAME = "com.example.android.magiclantern.magic.lantern";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_view_universal);
        setTitle(R.string.title_activity_details_view_universal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                fab.setRippleColor(getResources().getColor(R.color.colorPrimary));
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences prefs = DetailsViewUniversalActivity.this.getSharedPreferences(SHARED_PREF_NAME, 0);

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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


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
        getMenuInflater().inflate(R.menu.menu_details_view_universal, menu);
        DetailsViewUniversalActivityFragment fragment = (DetailsViewUniversalActivityFragment) getFragmentManager().findFragmentById(R.id.fragment);
        fragment.onCreateOptionsMenu(menu);
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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }
}
