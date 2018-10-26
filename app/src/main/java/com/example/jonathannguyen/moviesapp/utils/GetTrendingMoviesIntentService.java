package com.example.jonathannguyen.moviesapp.utils;

import android.app.Application;
import android.app.IntentService;
import android.content.Intent;

public class GetTrendingMoviesIntentService extends IntentService {

    public GetTrendingMoviesIntentService() {
        super("GetTrendingMoviesIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        GetTrendingMoviesTask getTrendingMoviesTask = new GetTrendingMoviesTask(getApplication());
        getTrendingMoviesTask.executeTask(getApplication(),GetTrendingMoviesTask.ACTION_GET_TRENDING_MOVIES);
    }
}
