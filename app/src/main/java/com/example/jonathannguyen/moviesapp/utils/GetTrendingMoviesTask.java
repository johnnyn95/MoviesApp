package com.example.jonathannguyen.moviesapp.utils;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.jonathannguyen.moviesapp.R;
import com.example.jonathannguyen.moviesapp.api.OnGetMoviesCallback;
import com.example.jonathannguyen.moviesapp.api.model.Movies;
import com.example.jonathannguyen.moviesapp.repository.MoviesRepositoryApi;
import com.example.jonathannguyen.moviesapp.repository.MoviesRepositoryDb;

import java.util.List;
import java.util.Random;

public class GetTrendingMoviesTask {
    public static final String ACTION_GET_TRENDING_MOVIES = "get-trending-movies";
    public static final String ACTION_ADD_TO_FAVOURITES_TRENDING_MOVIES = "add-to-favourites-trending-movies";
    public static final String ACTION_TURN_OFF_NOTIFICATIONS_TRENDING_MOVIES = "turn-off-notifications-trending-movies";
    static MoviesRepositoryApi moviesRepositoryApi;
    static MoviesRepositoryDb moviesRepositoryDb;
    static Movies movie;

    public GetTrendingMoviesTask(Application application){
        moviesRepositoryApi = moviesRepositoryApi.getInstance(application);
        moviesRepositoryDb = moviesRepositoryDb.getInstance(application);
    }

    public static void executeTask(Application application, String action){
        switch (action){
            case ACTION_GET_TRENDING_MOVIES :
                getTrendingMovie(application);
                break;
            case ACTION_ADD_TO_FAVOURITES_TRENDING_MOVIES :
                addMovieToFavourites(application,movie);
                NotificationManager notificationManager = (NotificationManager) application.getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancelAll();
                break;
            case ACTION_TURN_OFF_NOTIFICATIONS_TRENDING_MOVIES :
                turnOffNotifications(application);
                notificationManager = (NotificationManager) application.getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancelAll();
                break;
        }
    }

    private static Movies getRandomMovie(List<Movies> movies){
        if(movies != null){
        Random random = new Random();
        int randomNum = random.nextInt(movies.size());
        return movies.get(randomNum);
        } else {
            return null;
        }
    }

    private static void getTrendingMovie(final Application application){
        AsyncTask<Void,Void,List<Movies>> getTrendingMoviesAsyncTask = new AsyncTask<Void, Void, List<Movies>>() {
            List<Movies> result;
            Random random = new Random();
            int randomNum = random.nextInt(5);
            @Override
            protected List<Movies> doInBackground(Void... voids) {
                moviesRepositoryApi = moviesRepositoryApi.getInstance(application);
                moviesRepositoryDb = moviesRepositoryDb.getInstance(application);
                moviesRepositoryApi.getPopularMoviesNextPage(new OnGetMoviesCallback() {
                    @Override
                    public void onSuccess(List<Movies> movies) {
                        Movies randomMovie = getRandomMovie(movies);
                        while(movie == null) {
                            if (!moviesRepositoryDb.checkIfMovieIsInFavourites(randomMovie)) {
                                movie = randomMovie;
                                NotificationUtils.notifyUser(application.getBaseContext(), movie);
                            } else {
                                randomMovie = getRandomMovie(movies);
                            }
                        }
                    }

                    @Override
                    public void onError() {
                        Log.d(GetTrendingMoviesTask.class.toString(),"Failed to fetch movie from Trending service!");
                    }
                },randomNum);
                return result;
            }
        };
        getTrendingMoviesAsyncTask.execute();
    }

    private static void addMovieToFavourites(final Application application, final Movies movie){
        AsyncTask<Void,Void,Void> addToFavouritesAsyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                moviesRepositoryDb = moviesRepositoryDb.getInstance(application);
                moviesRepositoryDb.addMovieToFavourites(movie);
                return null;
            }
        };
        addToFavouritesAsyncTask.execute();
    }

    private static void turnOffNotifications(final Application application){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application.getBaseContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(application.getResources().getString(R.string.notifications_key),false);
        editor.commit();
    }

}
