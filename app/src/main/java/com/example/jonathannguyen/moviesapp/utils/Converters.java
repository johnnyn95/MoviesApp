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
    Gson gson = new Gson();

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
    public List<Genres> stringToSomeObjectList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Genres>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public String someObjectListToString(List<Genres> someObjects) {
        return gson.toJson(someObjects);
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
