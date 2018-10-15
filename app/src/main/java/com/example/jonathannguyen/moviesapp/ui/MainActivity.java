package com.example.jonathannguyen.moviesapp.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

public class MainActivity extends AppCompatActivity implements MoviesAdapterOnClickHandler {
    private MoviesViewModel moviesViewModel;
    MoviesAdapter adapter = new MoviesAdapter(this);
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerview);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moviesViewModel.getPopularMovies();
                Snackbar.make(view, R.string.refresh_popular_movies, Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        moviesViewModel = new MoviesViewModel(getApplication());
        moviesViewModel = ViewModelProviders.of(this).get(MoviesViewModel.class);
        moviesViewModel.getmMovies().observe(this, new Observer<List<Movies>>(){
            @Override
            public void onChanged(@Nullable List<Movies> movies) {
                adapter.setMovies(movies);
                adapter.setAllGenres(moviesViewModel.getmGenres().getValue());
                recyclerView.setAdapter(adapter);
                if(moviesViewModel.getmLastPosition().getValue() != null){
                recyclerView.scrollToPosition(moviesViewModel.getmLastPosition().getValue());
                }

            }
        });
        moviesViewModel.getmLastPosition().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                recyclerView.scrollToPosition(moviesViewModel.getmLastPosition().getValue());
            }
        });
        moviesViewModel.getPopularMovies();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        moviesViewModel.getLastAdapterPosition(layoutManager.findFirstVisibleItemPosition());
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
    public void addToFavourites(Movies movie) {
        // TODO add movie to favourites db
        Log.d("fav",movie.getTitle());
    }

    @Override
    public void movieDetails(Movies movie) {
        // TODO open new activity with movie details
        Log.d("fav movie details",movie.getTitle());
    }
}
