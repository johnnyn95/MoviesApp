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
import com.example.jonathannguyen.moviesapp.utils.CheckSettings;
import com.example.jonathannguyen.moviesapp.utils.GetTrendingMoviesIntentService;
import com.example.jonathannguyen.moviesapp.utils.GetTrendingMoviesTask;
import com.example.jonathannguyen.moviesapp.utils.Scheduler;

public class MainActivity extends AppCompatActivity implements
        PopularMoviesFragment.OnFragmentInteractionListener,
        SearchMoviesFragment.OnFragmentInteractionListener,
        FavouriteMoviesFragment.OnFragmentInteractionListener,
        SharedPreferences.OnSharedPreferenceChangeListener{
    SharedPreferences sharedPreferences;
    CheckSettings checkSettings;
    Toolbar toolbar;
    ViewPager viewPager;
    BottomNavigationView bottomNavigationView;
    MoviesAppPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkSettings();
        initUI();
        setSupportActionBar(toolbar);
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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key == getResources().getString(R.string.notifications_key)) {
            checkSettings.getInstance(getApplication());
            checkSettings.setNOTIFICATIONS(sharedPreferences.getBoolean(getResources().getString(R.string.notifications_key), getResources().getBoolean(R.bool.notifications_default_value)));
        }
        if(key == getResources().getString(R.string.language_key)){
            checkSettings.getInstance(getApplication());
            checkSettings.setLANGUAGE(sharedPreferences.getString(getResources().getString(R.string.language_key),getResources().getString(R.string.language_default_value)));
        }
        Intent restartApp = new Intent(this,MainActivity.class);
        startActivity(restartApp);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    private void checkSettings(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        String language = String.valueOf(sharedPreferences.getString(getResources().getString(R.string.language_key),getResources().getString(R.string.language_default_value)));
        checkSettings = new CheckSettings(getApplication(),language);
        if(checkSettings.getNOTIFICATIONS()){
             //Intent startNotificationsIntent = new Intent(this,GetTrendingMoviesIntentService.class);
             //startNotificationsIntent.setAction(GetTrendingMoviesTask.ACTION_GET_TRENDING_MOVIES);
             //startService(startNotificationsIntent);
            Scheduler.scheduleOnNetworkReminder(this);
        }
    }

    private void initUI(){
        toolbar = findViewById(R.id.toolbar);
        viewPager = findViewById(R.id.viewpager);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        pagerAdapter = new MoviesAppPagerAdapter(this,getSupportFragmentManager());
    }
}
