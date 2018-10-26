package com.example.jonathannguyen.moviesapp.api.model;

import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;

import com.example.jonathannguyen.moviesapp.utils.Converters;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@TypeConverters({Converters.class})
public class MoviesTrendingResponse {
    @SerializedName("page")
    @Expose
    private int page;
    @SerializedName("results")
    @Expose
    private List<Movies> movies;
    @SerializedName("total_pages")
    @Expose
    private int totalPages;
    @SerializedName("total_results")
    @Expose
    private int totalResults;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Movies> getMovies() {
        return movies;
    }

    public void setMovies(List<Movies> movies) {
        this.movies = movies;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

}
