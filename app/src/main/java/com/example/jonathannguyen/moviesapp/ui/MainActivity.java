package com.example.jonathannguyen.moviesapp.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


import com.example.jonathannguyen.moviesapp.R;
import com.example.jonathannguyen.moviesapp.ui.FavouriteMovies.FavouriteMoviesFragment;
import com.example.jonathannguyen.moviesapp.ui.PopularMovies.PopularMoviesFragment;
import com.example.jonathannguyen.moviesapp.ui.SearchMovies.SearchMoviesFragment;

public class MainActivity extends AppCompatActivity implements
        PopularMoviesFragment.OnFragmentInteractionListener,
        SearchMoviesFragment.OnFragmentInteractionListener,
        FavouriteMoviesFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
