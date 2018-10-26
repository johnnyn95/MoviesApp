package com.example.jonathannguyen.moviesapp.utils;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.example.jonathannguyen.moviesapp.api.OnGetMoviesCallback;
import com.example.jonathannguyen.moviesapp.api.model.Movies;
import com.example.jonathannguyen.moviesapp.repository.MoviesRepositoryApi;

import java.util.List;
import java.util.Random;

public class GetTrendingMoviesTask {
    public static final String ACTION_GET_TRENDING_MOVIES = "get-trending-movies";
    static MoviesRepositoryApi moviesRepositoryApi;
    static List<Movies> trendingMovies;

    public GetTrendingMoviesTask(Application application){
        moviesRepositoryApi = moviesRepositoryApi.getInstance(application);
    }
    public void executeTask(Application application,String action){
        if(action == ACTION_GET_TRENDING_MOVIES){
            AsyncTask<Void,Void,List<Movies>> getTrendingMoviesAsyncTask = new AsyncTask<Void, Void, List<Movies>>() {
                List<Movies> result;
                @Override
                protected List<Movies> doInBackground(Void... voids) {
                    moviesRepositoryApi.getPopularMovies(new OnGetMoviesCallback() {
                        @Override
                        public void onSuccess(List<Movies> movies) {
                            trendingMovies = movies;
                            Log.d("PLSWORK",getRandomMovie(trendingMovies).getTitle());
                        }

                        @Override
                        public void onError() {
                            Log.d("PLSWORK","DONT");
                        }
                    });
                    return result;
                }
            };
            getTrendingMoviesAsyncTask.execute();
        }
    }

    public static Movies getRandomMovie(List<Movies> movies){
        if(movies != null){
        Random random = new Random();
        int randomNum = random.nextInt(movies.size());
        return movies.get(randomNum);
        } else {
            return null;
        }
    }

}
