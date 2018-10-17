package com.example.jonathannguyen.moviesapp.api;

import com.example.jonathannguyen.moviesapp.api.model.Trailers;

import java.util.List;

public interface OnGetTrailersCallback {
    void onSuccess(List<Trailers> trailers);

    void onError();
}
