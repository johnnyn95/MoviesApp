package com.example.jonathannguyen.moviesapp.ui.FavouriteMovies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.jonathannguyen.moviesapp.api.model.Genres;
import com.example.jonathannguyen.moviesapp.api.model.Movies;
import com.example.jonathannguyen.moviesapp.repository.MoviesRepositoryDb;

import java.util.List;

public class FavouriteMoviesViewModel extends AndroidViewModel {
    MoviesRepositoryDb moviesRepositoryDb;
    LiveData<List<Movies>> mMovies;
    LiveData<List<Genres>> mGenres;
    private MutableLiveData<Integer> mLastPosition = new MutableLiveData<>();

    public FavouriteMoviesViewModel(@NonNull Application application) {
        super(application);
        moviesRepositoryDb = new MoviesRepositoryDb(application);
        getFavouriteMovies();
    }

    public void getFavouriteMovies(){
        mMovies = moviesRepositoryDb.getAllMovies();
        mGenres = moviesRepositoryDb.getAllGenres();
    }

    public LiveData<List<Genres>> getmGenres() { return mGenres;
    }

    public LiveData<List<Movies>> getmMovies() { return mMovies;
    }

    public LiveData<Integer> getmLastPosition() { return mLastPosition; }

    public void setLastAdapterPosition(Integer position){
        mLastPosition.postValue(position);
    }

    public void removeMovieFromFavourites(Movies movie){
        moviesRepositoryDb.removeMovieFromFavourites(movie);
    }

    public void removeAllMoviesFromFavourites() {
    moviesRepositoryDb.removeAllMoviesFromFavourites();}
}
