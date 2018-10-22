package com.example.jonathannguyen.moviesapp.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.jonathannguyen.moviesapp.api.TheMovieDbService;
import com.example.jonathannguyen.moviesapp.api.model.Genres;
import com.example.jonathannguyen.moviesapp.api.model.Movies;
import com.example.jonathannguyen.moviesapp.db.GenresDao;
import com.example.jonathannguyen.moviesapp.db.GenresRoomDatabase;
import com.example.jonathannguyen.moviesapp.db.MoviesDao;
import com.example.jonathannguyen.moviesapp.db.MoviesRoomDatabase;
import com.example.jonathannguyen.moviesapp.ui.FavouriteMoviesViewModel;

import java.util.List;
import java.util.ListIterator;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FavouriteMoviesRepository {
    private MoviesDao mMoviesDao;
    private GenresDao mGenresDao;
    public static FavouriteMoviesRepository repository;

    public FavouriteMoviesRepository(Application application){
        MoviesRoomDatabase moviesRoomDatabase = MoviesRoomDatabase.getDatabase(application);
        GenresRoomDatabase genresRoomDatabase = GenresRoomDatabase.getDatabase(application);
        mMoviesDao = moviesRoomDatabase.moviesDao();
        mGenresDao = genresRoomDatabase.genresDao();
    }

    public static FavouriteMoviesRepository getInstance(Application application){
        if(repository == null) {
            repository = new FavouriteMoviesRepository(application);
        }
        return repository;
    }

    public LiveData<List<Genres>>  getAllGenres(){
        return mGenresDao.getAllGenres();
    }

    public LiveData<List<Movies>> getAllMovies(){
        return mMoviesDao.getAllMovies();
    }

    public void addMovieToFavourites(Movies movie){
        new insertFavouriteMovie(mMoviesDao).execute(movie);
    }

    public void insertGenres(List<Genres> genres){
        new insertAllGenres(mGenresDao).execute(genres);
    }

    private static class insertFavouriteMovie extends AsyncTask<Movies, Void, Void> {
        private MoviesDao mAsyncTaskDao;
        insertFavouriteMovie(MoviesDao dao) { mAsyncTaskDao = dao; }

        @Override
        protected Void doInBackground(final Movies... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class insertAllGenres extends AsyncTask<List<Genres>,Void ,Void>{
        private GenresDao mAsyncTaskDao;
        insertAllGenres(GenresDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Void doInBackground(final List<Genres>... params) {
            for(int i = 0;i < params.length;i++){
                mAsyncTaskDao.insert(params[i].get(i));
            }
            return null;
        }
    }

    private static class insertGenres extends AsyncTask<Genres,Void ,Void>{
        private GenresDao mAsyncTaskDao;
        insertGenres(GenresDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Void doInBackground(final Genres... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}