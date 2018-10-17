package com.example.jonathannguyen.moviesapp.api;

import com.example.jonathannguyen.moviesapp.api.model.Reviews;

import java.util.List;

public interface OnGetReviewsCallback {
    void onSuccess(List<Reviews> reviews);

    void onError();
}
