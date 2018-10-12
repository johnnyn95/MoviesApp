package com.example.jonathannguyen.moviesapp;

import java.util.List;

public interface OnGetGenresCallback {
    void onSuccess(List<Genres> genres);
    void onError();
}
