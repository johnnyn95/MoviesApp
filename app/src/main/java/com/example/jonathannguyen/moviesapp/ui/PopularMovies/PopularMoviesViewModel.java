package com.example.jonathannguyen.moviesapp.ui.PopularMovies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.jonathannguyen.moviesapp.api.OnGetReviewsCallback;
import com.example.jonathannguyen.moviesapp.api.OnGetTrailersCallback;
import com.example.jonathannguyen.moviesapp.api.model.Genres;
import com.example.jonathannguyen.moviesapp.api.model.Movies;
import com.example.jonathannguyen.moviesapp.api.OnGetMovieCallback;
import com.example.jonathannguyen.moviesapp.api.model.Reviews;
import com.example.jonathannguyen.moviesapp.api.model.Trailers;
import com.example.jonathannguyen.moviesapp.repository.MoviesRepositoryDb;
import com.example.jonathannguyen.moviesapp.repository.MoviesRepositoryApi;
import com.example.jonathannguyen.moviesapp.api.OnGetGenresCallback;
import com.example.jonathannguyen.moviesapp.api.OnGetMoviesCallback;
import com.example.jonathannguyen.moviesapp.api.TheMovieDbService;
import com.example.jonathannguyen.moviesapp.utils.CheckSettings;

import java.util.ArrayList;
import java.util.List;

public class PopularMoviesViewModel extends AndroidViewModel  {
    private MutableLiveData<List<Movies>> mMovies = new MutableLiveData<>();
    private MutableLiveData<List<Genres>> mGenres  = new MutableLiveData<>();
    private MutableLiveData<Integer> mLastPosition = new MutableLiveData<>();
    private MutableLiveData<Movies> mMovieDetails = new MutableLiveData<>();
    private MutableLiveData<List<Trailers>> mTrailers = new MutableLiveData<>();
    private MutableLiveData<List<Reviews>> mReviews = new MutableLiveData<>();

    private MoviesRepositoryApi moviesRepositoryApi;
    private MoviesRepositoryDb moviesRepositoryDb;
    private CheckSettings checkSettings;
    private TheMovieDbService api;
    int currentPopularPage = 1;

    public PopularMoviesViewModel(Application application){
        super(application);
        checkSettings = checkSettings.getInstance(application);
        moviesRepositoryApi = new MoviesRepositoryApi(application,api,checkSettings.getLANGUAGE());
        moviesRepositoryDb = new MoviesRepositoryDb(application);
    }

    public void getPopularMovies(){
        moviesRepositoryApi = MoviesRepositoryApi.getInstance(getApplication());
        moviesRepositoryApi.getPopularMovies(new OnGetMoviesCallback() {
            @Override
            public void onSuccess(List<Movies> movies) {
                if(movies != null)
                mMovies.postValue(movies);
            }

            @Override
            public void onError() {
                Log.d(MoviesRepositoryApi.class.toString(),"Failed to fetch movies");
            }
        });
        moviesRepositoryApi.getGenres(new OnGetGenresCallback() {
            @Override
            public void onSuccess(List<Genres> genres) {
                if(genres != null)
                    mGenres.postValue(genres);
                    moviesRepositoryDb.getInstance(getApplication());
                    moviesRepositoryDb.insertGenres(genres);
            }

            @Override
            public void onError() {
                Log.d(MoviesRepositoryApi.class.toString(),"Failed to fetch genres");
            }
        });
    }

    public void getPopularMoviesNextPage(){
        currentPopularPage++;
        moviesRepositoryApi = MoviesRepositoryApi.getInstance(getApplication());
        moviesRepositoryApi.getPopularMoviesNextPage(new OnGetMoviesCallback() {
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
                Log.d(MoviesRepositoryApi.class.toString(),"Failed to fetch movies");
            }
        },currentPopularPage);
    }

    public void getMovieDetails(int movieId){
        moviesRepositoryApi = MoviesRepositoryApi.getInstance(getApplication());
        moviesRepositoryApi.getMovie(movieId, new OnGetMovieCallback() {
            @Override
            public void onSuccess(Movies movie) {
                mMovieDetails.postValue(movie);
            }

            @Override
            public void onError() {
                Log.d(MoviesRepositoryApi.class.toString(),"Failed to fetch movie");
            }
        });
    }

    public void getMovieTrailers(int movieId){
        moviesRepositoryApi = MoviesRepositoryApi.getInstance(getApplication());
        moviesRepositoryApi.getTrailers(movieId, new OnGetTrailersCallback() {
            @Override
            public void onSuccess(List<Trailers> trailers) {
                mTrailers.postValue(trailers);
            }

            @Override
            public void onError() {
                Log.d(MoviesRepositoryApi.class.toString(),"Failed to fetch trailers");
            }
        });

    }

    public void getMovieReviews(int movieId){
        moviesRepositoryApi = MoviesRepositoryApi.getInstance(getApplication());
        moviesRepositoryApi.getReviews(movieId, new OnGetReviewsCallback() {
            @Override
            public void onSuccess(List<Reviews> reviews) {
                mReviews.postValue(reviews);
            }

            @Override
            public void onError() {
                Log.d(MoviesRepositoryApi.class.toString(),"Failed to fetch reviews");
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
         moviesRepositoryDb.getInstance(getApplication());
         moviesRepositoryDb.addMovieToFavourites(movie);
    }

    public boolean checkIfMovieIsInFavourites(Movies movie){
        moviesRepositoryDb.getInstance(getApplication());
        return moviesRepositoryDb.checkIfMovieIsInFavourites(movie);
    }

    public void setLastAdapterPosition(Integer position){ mLastPosition.postValue(position); }
}
