package com.example.jonathannguyen.moviesapp.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.jonathannguyen.moviesapp.api.model.Movies;

@Database(entities = {Movies.class},version = 2)
public abstract class MoviesRoomDatabase extends RoomDatabase {
    public abstract MoviesDao moviesDao();

    private static volatile MoviesRoomDatabase INSTANCE;

    public static MoviesRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MoviesRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MoviesRoomDatabase.class, "movies_database").fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
