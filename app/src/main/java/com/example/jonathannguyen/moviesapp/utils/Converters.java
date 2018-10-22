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
        List<Integer> list = new ArrayList<>();

        String[] array = genreIds.split(",");

        for (String s : array) {
            if (!s.isEmpty()) {
                list.add(Integer.parseInt(s));
            }
        }
        return list;
    }

    @TypeConverter
    public String fromGenresList(List<Genres> genres) {
        if (genres == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Genres>>() {}.getType();
        String json = gson.toJson(genres, type);
        return json;
    }

    @TypeConverter
    public List<Genres> toGenresList(String genresString) {
        if (genresString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Genres>>() {
        }.getType();
        List<Genres> genresList = gson.fromJson(genresString, type);
        return genresList;
    }

    @TypeConverter
    public String writingStringFromList(List<Integer> list) {
        String genreIds = "";
        for (int i : list) {
            genreIds += "," + i;
        }
        return genreIds;
    }
}
