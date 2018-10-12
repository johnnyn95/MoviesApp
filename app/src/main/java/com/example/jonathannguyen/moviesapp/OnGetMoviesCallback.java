package com.example.jonathannguyen.moviesapp;

import java.util.List;

public interface OnGetMoviesCallback {
    void onSuccess(List<Movies> movies);
    void onError();
}
