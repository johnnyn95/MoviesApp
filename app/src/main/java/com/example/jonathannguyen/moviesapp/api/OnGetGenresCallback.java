package com.example.jonathannguyen.moviesapp.api;

import com.example.jonathannguyen.moviesapp.api.model.Genres;

import java.util.List;

public interface OnGetGenresCallback {
    void onSuccess(List<Genres> genres);
    void onError();
}
