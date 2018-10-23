package com.example.jonathannguyen.moviesapp.utils;

import android.arch.persistence.room.TypeConverter;

import com.example.jonathannguyen.moviesapp.api.model.Genres;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Converters {
    @TypeConverter
    public List<Integer> gettingListFromString(String genreIds) {
        Type listType = new TypeToken<List<Integer>>() {}.getType();
        return new Gson().fromJson(genreIds, listType);
    }

    @TypeConverter
    public List<Genres> gettingGenresListFromString(String genresString) {
        Type listType = new TypeToken<List<Genres>>() {}.getType();
        return new Gson().fromJson(genresString, listType);
    }

    @TypeConverter
    public String writingStringFromGenresList(List<Genres> genres) {
        Gson gson = new Gson();
        String json = gson.toJson(genres);
        return json;
    }

    @TypeConverter
    public String writingStringFromList(List<Integer> list) {

        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
