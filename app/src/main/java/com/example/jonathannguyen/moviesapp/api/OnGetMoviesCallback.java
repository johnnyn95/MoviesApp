package com.example.jonathannguyen.moviesapp.api;

import com.example.jonathannguyen.moviesapp.api.model.Movies;

import java.util.List;

public interface OnGetMoviesCallback {
    void onSuccess(List<Movies> movies);
    void onError();
}
