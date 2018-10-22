package com.example.jonathannguyen.moviesapp.api.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.example.jonathannguyen.moviesapp.utils.Converters;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@TypeConverters({Converters.class})
@Entity(tableName = "genres_table")
public class Genres {

    @PrimaryKey
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
