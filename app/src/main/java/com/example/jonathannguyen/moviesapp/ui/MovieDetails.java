package com.example.jonathannguyen.moviesapp.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.jonathannguyen.moviesapp.R;
import com.example.jonathannguyen.moviesapp.api.model.Genres;
import com.example.jonathannguyen.moviesapp.api.model.Movies;

import java.util.ArrayList;
import java.util.List;

public class MovieDetails extends AppCompatActivity {

    private MoviesViewModel moviesViewModel;

    private static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w780";
    private static String YOUTUBE_VIDEO_URL = "http://www.youtube.com/watch?v=%s";
    private static String YOUTUBE_THUMBNAIL_URL = "http://img.youtube.com/vi/%s/0.jpg";

    private ImageView movieBackdrop;
    private TextView movieTitle;
    private TextView movieGenres;
    private TextView movieOverview;
    private TextView movieOverviewLabel;
    private TextView movieReleaseDate;
    private RatingBar movieRating;
    private LinearLayout movieTrailers;
    private LinearLayout movieReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        initUI();
        moviesViewModel = ViewModelProviders.of(this).get(MoviesViewModel.class);
        moviesViewModel.getmMovieDetails().observe(this, new Observer<Movies>() {
            @Override
            public void onChanged(@Nullable Movies movies) {
                setMovie(moviesViewModel.getmMovieDetails().getValue());
            }
        });
        moviesViewModel.getMovieDetails(getIntent().getIntExtra(getString(R.string.EXTRA_MOVIE_ID),0));

    }

    private void initUI() {
        movieBackdrop = findViewById(R.id.movieDetailsBackdrop);
        movieTitle = findViewById(R.id.movieDetailsTitle);
        movieGenres = findViewById(R.id.movieDetailsGenres);
        movieOverview = findViewById(R.id.movieDetailsOverview);
        movieOverviewLabel = findViewById(R.id.summaryLabel);
        movieReleaseDate = findViewById(R.id.movieDetailsReleaseDate);
        movieRating = findViewById(R.id.movieDetailsRating);
        movieTrailers = findViewById(R.id.movieTrailers);
        movieReviews = findViewById(R.id.movieReviews);
    }

    private void setMovie(Movies movie) {
        movieTitle.setText(movie.getTitle());
        movieOverviewLabel.setVisibility(View.VISIBLE);
        movieOverview.setText(movie.getOverview());
        movieRating.setVisibility(View.VISIBLE);
        movieRating.setRating(movie.getRating() / 2);
        movieReleaseDate.setText(movie.getReleaseDate());
        List<String> currentGenres = new ArrayList<>();
        for (Genres genre : movie.getGenres()) {
            currentGenres.add(genre.getName());
        }
        movieGenres.setText(TextUtils.join(", ", currentGenres));
        if (!isFinishing()) {
            Glide.with(MovieDetails.this)
                    .load(IMAGE_BASE_URL + movie.getBackdrop())
                    .apply(RequestOptions.placeholderOf(R.color.colorAccent))
                    .into(movieBackdrop);
        }
    }
}
