package com.example.jonathannguyen.moviesapp.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
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
import com.example.jonathannguyen.moviesapp.api.model.Reviews;
import com.example.jonathannguyen.moviesapp.api.model.Trailers;

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
    private TextView trailersLabel;
    private TextView reviewsLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        if(isNetworkAvailable()) {
            Log.d(MovieDetails.class.toString(), "onCreate: there is internet ");
        } else {
            Log.d(MovieDetails.class.toString(), "onCreate: there is NO internet ");

        }
        setupToolbar();
        initUI();
        moviesViewModel = ViewModelProviders.of(this).get(MoviesViewModel.class);
        moviesViewModel.getmMovieDetails().observe(this, new Observer<Movies>() {
            @Override
            public void onChanged(@Nullable Movies movies) {
                setMovie(moviesViewModel.getmMovieDetails().getValue());
            }
        });
        moviesViewModel.getmTrailers().observe(this, new Observer<List<Trailers>>(){
            @Override
            public void onChanged(@Nullable List<Trailers> trailers) {
                setMovieTrailers(trailers);
            }
        });
        moviesViewModel.getmReviews().observe(this, new Observer<List<Reviews>>() {
            @Override
            public void onChanged(@Nullable List<Reviews> reviews) {
                setMovieReviews(reviews);
            }
        });
        moviesViewModel.getMovieDetails(getIntent().getIntExtra(getString(R.string.EXTRA_MOVIE_ID),0));
        moviesViewModel.getMovieTrailers(getIntent().getIntExtra(getString(R.string.EXTRA_MOVIE_ID),0));
        moviesViewModel.getMovieReviews(getIntent().getIntExtra(getString(R.string.EXTRA_MOVIE_ID),0));
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
        trailersLabel = findViewById(R.id.trailersLabel);
        reviewsLabel = findViewById(R.id.reviewsLabel);
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
                    .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                    .into(movieBackdrop);
        }
        trailersLabel.setVisibility(View.VISIBLE);
        movieTrailers.removeAllViews();


    }
    private void setMovieTrailers(List<Trailers> trailers){
        for (final Trailers trailer : trailers) {
            View parent = getLayoutInflater().inflate(R.layout.thumbnail_trailer, movieTrailers, false);
            ImageView thumbnail = parent.findViewById(R.id.thumbnail);
            thumbnail.requestLayout();
            thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showTrailer(String.format(YOUTUBE_VIDEO_URL, trailer.getKey()));
                }
            });
            Glide.with(MovieDetails.this)
                    .load(String.format(YOUTUBE_THUMBNAIL_URL, trailer.getKey()))
                    .apply(RequestOptions.placeholderOf(R.color.colorPrimary).centerCrop())
                    .into(thumbnail);
            movieTrailers.addView(parent);
        }
    }
    private void setMovieReviews(List<Reviews> reviews){
        reviewsLabel.setVisibility(View.VISIBLE);
        movieReviews.removeAllViews();
        for (Reviews review : reviews) {
            View parent = getLayoutInflater().inflate(R.layout.reviews, movieReviews, false);
            TextView author = parent.findViewById(R.id.reviewAuthor);
            TextView content = parent.findViewById(R.id.reviewContent);
            author.setText(review.getAuthor());
            content.setText(review.getContent());
            movieReviews.addView(parent);
        }
    }
    private void showTrailer(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
    private void setupToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
