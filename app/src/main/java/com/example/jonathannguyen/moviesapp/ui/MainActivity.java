package com.example.jonathannguyen.moviesapp.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import com.example.jonathannguyen.moviesapp.R;
import com.example.jonathannguyen.moviesapp.ui.FavouriteMovies.FavouriteMoviesFragment;
import com.example.jonathannguyen.moviesapp.ui.PopularMovies.PopularMoviesFragment;
import com.example.jonathannguyen.moviesapp.ui.SearchMovies.SearchMoviesFragment;
import com.example.jonathannguyen.moviesapp.ui.Settings.SettingsActivity;

public class MainActivity extends AppCompatActivity implements
        PopularMoviesFragment.OnFragmentInteractionListener,
        SearchMoviesFragment.OnFragmentInteractionListener,
        FavouriteMoviesFragment.OnFragmentInteractionListener,
        SharedPreferences.OnSharedPreferenceChangeListener{
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        checkSettings();
        final Toolbar toolbar = findViewById(R.id.toolbar);
        final ViewPager viewPager = findViewById(R.id.viewpager);
        final BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        setSupportActionBar(toolbar);
        MoviesAppPagerAdapter pagerAdapter = new MoviesAppPagerAdapter(this,getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }
            @Override
            public void onPageSelected(int i) {
                switch (i){
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.nav_popular);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.nav_search);
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.nav_favourites);
                        break;
                        default :
                            bottomNavigationView.setSelectedItemId(R.id.nav_popular);
                            break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_popular:
                        viewPager.setCurrentItem(0);
                        return true;
                    case R.id.nav_search:
                        viewPager.setCurrentItem(1);
                        return true;
                    case R.id.nav_favourites:
                        viewPager.setCurrentItem(2);
                        return true;
                }
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this,SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void checkSettings(){
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        Log.d(MainActivity.class.toString(),
        String.valueOf(sharedPreferences.getBoolean(getResources().getString(R.string.notifications_key),getResources().getBoolean(R.bool.notifications_default_value))));

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key == getResources().getString(R.string.notifications_key)) {
            Log.d(MainActivity.class.toString(),
                    String.valueOf(sharedPreferences.getBoolean(getResources().getString(R.string.notifications_key), getResources().getBoolean(R.bool.notifications_default_value))));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }
}
