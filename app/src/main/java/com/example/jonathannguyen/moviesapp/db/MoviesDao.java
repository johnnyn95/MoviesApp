package com.example.jonathannguyen.moviesapp.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.support.annotation.MainThread;

import com.example.jonathannguyen.moviesapp.api.model.Movies;

import java.util.List;

@Dao
public interface MoviesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Movies movie);

    @Query("DELETE FROM movies_table")
    void deleteAll();

    @Query("SELECT * from movies_table ORDER BY localId ASC")
    LiveData<List<Movies>> getAllMovies();
}
