package com.example.jonathannguyen.moviesapp.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.jonathannguyen.moviesapp.api.model.Genres;
import com.example.jonathannguyen.moviesapp.api.model.Movies;
import com.example.jonathannguyen.moviesapp.repository.FavouriteMoviesRepository;

import java.util.List;

public class FavouriteMoviesViewModel extends AndroidViewModel {
    FavouriteMoviesRepository favouriteMoviesRepository;
    LiveData<List<Movies>> mMovies;
    LiveData<List<Genres>> mGenres;
    private MutableLiveData<Integer> mLastPosition = new MutableLiveData<>();

    public FavouriteMoviesViewModel(@NonNull Application application) {
        super(application);
        favouriteMoviesRepository = new FavouriteMoviesRepository(application);
        getFavouriteMovies();
    }

    public void getFavouriteMovies(){
        mMovies = favouriteMoviesRepository.getAllMovies();
        mGenres = favouriteMoviesRepository.getAllGenres();
    }

    public List<Genres> getAllGenres(){return favouriteMoviesRepository.getAllGenres().getValue();}

    public LiveData<List<Genres>> getmGenres() {
        return mGenres;
    }

    public LiveData<List<Movies>> getmMovies() {
        return mMovies;
    }

    public LiveData<Integer> getmLastPosition(){ return mLastPosition; }


    public void setLastAdapterPosition(Integer position){
        mLastPosition.postValue(position);
    }
}
