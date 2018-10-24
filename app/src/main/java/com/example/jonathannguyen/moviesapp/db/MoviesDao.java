package com.example.jonathannguyen.moviesapp.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.jonathannguyen.moviesapp.api.model.Movies;

import java.util.List;

@Dao
public interface MoviesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Movies movie);

    @Query("DELETE FROM movies_table")
    void deleteAllMoviesFromFavourites();

    @Query("SELECT * from movies_table ORDER BY localId ASC")
    LiveData<List<Movies>> getAllMovies();

    @Query("DELETE FROM movies_table WHERE movies_table.id == :movieId")
    void deleteMovieFromFavourites(int movieId);

    @Query("SELECT * FROM movies_table WHERE movies_table.id == :movieId")
    Movies getMovieById(int movieId);
}
