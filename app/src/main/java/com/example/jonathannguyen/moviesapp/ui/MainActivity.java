package com.example.jonathannguyen.moviesapp.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.jonathannguyen.moviesapp.api.model.Movies;
import com.example.jonathannguyen.moviesapp.R;

import java.util.List;

public class MainActivity extends AppCompatActivity implements
        PopularMoviesFragment.OnFragmentInteractionListener,
        SearchMoviesFragment.OnFragmentInteractionListener,
        FavouriteMoviesFragment.OnFragmentInteractionListener {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerview);
        setSupportActionBar(toolbar);

        final ViewPager viewPager = findViewById(R.id.viewpager);
        MoviesAppPagerAdapter pagerAdapter = new MoviesAppPagerAdapter(this,getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        //TabLayout tabLayout = findViewById(R.id.tablayout);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
