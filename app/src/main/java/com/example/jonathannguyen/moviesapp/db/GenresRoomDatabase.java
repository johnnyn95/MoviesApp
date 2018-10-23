package com.example.jonathannguyen.moviesapp.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.jonathannguyen.moviesapp.api.model.Genres;

@Database(entities = {Genres.class},version = 1)
public abstract class GenresRoomDatabase extends RoomDatabase {
    public abstract GenresDao genresDao();

    private static volatile  GenresRoomDatabase INSTANCE;

    public static GenresRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (GenresRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            GenresRoomDatabase.class, "genres_database").fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
