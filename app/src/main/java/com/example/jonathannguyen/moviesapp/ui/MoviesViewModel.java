package com.example.jonathannguyen.moviesapp.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.example.jonathannguyen.moviesapp.api.OnGetReviewsCallback;
import com.example.jonathannguyen.moviesapp.api.OnGetTrailersCallback;
import com.example.jonathannguyen.moviesapp.api.model.Genres;
import com.example.jonathannguyen.moviesapp.api.model.Movies;
import com.example.jonathannguyen.moviesapp.api.OnGetMovieCallback;
import com.example.jonathannguyen.moviesapp.api.model.Reviews;
import com.example.jonathannguyen.moviesapp.api.model.Trailers;
import com.example.jonathannguyen.moviesapp.repository.FavouriteMoviesRepository;
import com.example.jonathannguyen.moviesapp.repository.MoviesRepository;
import com.example.jonathannguyen.moviesapp.api.OnGetGenresCallback;
import com.example.jonathannguyen.moviesapp.api.OnGetMoviesCallback;
import com.example.jonathannguyen.moviesapp.api.TheMovieDbService;

import java.util.ArrayList;
import java.util.List;

public class MoviesViewModel extends AndroidViewModel  {
    private MutableLiveData<List<Movies>> mMovies = new MutableLiveData<>();
    private MutableLiveData<List<Genres>> mGenres  = new MutableLiveData<>();
    private MutableLiveData<Integer> mLastPosition = new MutableLiveData<>();
    private MutableLiveData<Movies> mMovieDetails = new MutableLiveData<>();
    private MutableLiveData<List<Trailers>> mTrailers = new MutableLiveData<>();
    private MutableLiveData<List<Reviews>> mReviews = new MutableLiveData<>();

    private MoviesRepository moviesRepository;
    private FavouriteMoviesRepository favouriteMoviesRepository;
    private TheMovieDbService api;
    int currentPopularPage = 1;

    public MoviesViewModel(Application application){
        super(application);
        moviesRepository = new MoviesRepository(application,api);
        favouriteMoviesRepository = new FavouriteMoviesRepository(application);
    }

    public void getPopularMovies(){
        moviesRepository = MoviesRepository.getInstance(getApplication());
        moviesRepository.getPopularMovies(new OnGetMoviesCallback() {
            @Override
            public void onSuccess(List<Movies> movies) {
                if(movies != null)
                mMovies.postValue(movies);
            }

            @Override
            public void onError() {
                Log.d(MoviesRepository.class.toString(),"Failed to fetch movies");
            }
        });
        moviesRepository.getGenres(new OnGetGenresCallback() {
            @Override
            public void onSuccess(List<Genres> genres) {
                if(genres != null)
                    mGenres.postValue(genres);
                    favouriteMoviesRepository.getInstance(getApplication());
                    favouriteMoviesRepository.insertGenres(genres);
            }

            @Override
            public void onError() {
                Log.d(MoviesRepository.class.toString(),"Failed to fetch genres");
            }
        });
    }

    public void getPopularMoviesNextPage(){
        currentPopularPage++;
        moviesRepository = MoviesRepository.getInstance(getApplication());
        moviesRepository.getPopularMoviesNextPage(new OnGetMoviesCallback() {
            @Override
            public void onSuccess(List<Movies> movies) {
                if(movies != null) {
                    List<Movies> newList = new ArrayList<Movies>(getmMovies().getValue());
                    newList.addAll(movies);
                    mMovies.postValue(newList);
                }
            }

            @Override
            public void onError() {
                Log.d(MoviesRepository.class.toString(),"Failed to fetch movies");
            }
        },currentPopularPage);
    }

    public void getMovieDetails(int movieId){
        moviesRepository = MoviesRepository.getInstance(getApplication());
        moviesRepository.getMovie(movieId, new OnGetMovieCallback() {
            @Override
            public void onSuccess(Movies movie) {
                mMovieDetails.postValue(movie);
            }

            @Override
            public void onError() {
                Log.d(MoviesRepository.class.toString(),"Failed to fetch movie");
            }
        });
    }

    public void getMovieTrailers(int movieId){
        moviesRepository = MoviesRepository.getInstance(getApplication());
        moviesRepository.getTrailers(movieId, new OnGetTrailersCallback() {
            @Override
            public void onSuccess(List<Trailers> trailers) {
                mTrailers.postValue(trailers);
            }

            @Override
            public void onError() {
                Log.d(MoviesRepository.class.toString(),"Failed to fetch trailers");
            }
        });

    }

    public void getMovieReviews(int movieId){
        moviesRepository = MoviesRepository.getInstance(getApplication());
        moviesRepository.getReviews(movieId, new OnGetReviewsCallback() {
            @Override
            public void onSuccess(List<Reviews> reviews) {
                mReviews.postValue(reviews);
            }

            @Override
            public void onError() {
                Log.d(MoviesRepository.class.toString(),"Failed to fetch reviews");
            }
        });

    }

    public LiveData<List<Movies>> getmMovies(){ return mMovies; }

    public LiveData<List<Genres>> getmGenres(){ return mGenres;}

    public LiveData<Integer> getmLastPosition(){ return mLastPosition; }

    public LiveData<Movies> getmMovieDetails(){ return mMovieDetails; }

    public LiveData<List<Trailers>> getmTrailers(){ return mTrailers;}

    public MutableLiveData<List<Reviews>> getmReviews(){ return mReviews; }

    public void addMovieToFavourites(Movies movie){
         favouriteMoviesRepository.getInstance(getApplication());
         favouriteMoviesRepository.addMovieToFavourites(movie);

    }

    public void setLastAdapterPosition(Integer position){ mLastPosition.postValue(position); }
}
