package com.example.jonathannguyen.moviesapp.api;

import com.example.jonathannguyen.moviesapp.api.model.Movies;

public interface OnGetMovieCallback {
    void onSuccess(Movies movie);
    void onError();
}
