package com.example.jonathannguyen.moviesapp.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.jonathannguyen.moviesapp.api.model.Genres;

import java.util.List;

@Dao
public interface GenresDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Genres> genres);

    @Query("SELECT * FROM genres_table")
    LiveData<List<Genres>> getAllGenres();

    @Query("SELECT * FROM genres_table WHERE genres_table.id == :genreId")
    Genres getGenreName(int genreId);
}

