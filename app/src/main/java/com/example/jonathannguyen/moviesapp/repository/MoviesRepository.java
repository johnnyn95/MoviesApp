package com.example.jonathannguyen.moviesapp.repository;

import android.app.Application;
import android.util.Log;

import com.example.jonathannguyen.moviesapp.api.model.GenresResponse;
import com.example.jonathannguyen.moviesapp.api.OnGetGenresCallback;
import com.example.jonathannguyen.moviesapp.api.OnGetMoviesCallback;
import com.example.jonathannguyen.moviesapp.api.model.MoviesResponse;
import com.example.jonathannguyen.moviesapp.api.TheMovieDbService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesRepository {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String LANGUAGE = "en-US";
    private static final String API_KEY = "e5c29049ee97d4ff4783f528930be86e";
    public static MoviesRepository repository;
    private TheMovieDbService theMovieDbService;

    public MoviesRepository(Application application,TheMovieDbService api){
        theMovieDbService = api;
    }

    public static MoviesRepository getInstance(Application application){
        if(repository == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            repository = new MoviesRepository(application,retrofit.create(TheMovieDbService.class));
        }
        return repository;
    }

    public void getPopularMovies(final OnGetMoviesCallback callback){
        theMovieDbService.getPopular(API_KEY,LANGUAGE,1)
                .enqueue(new Callback<MoviesResponse>() {
                    @Override
                    public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                        if(response.isSuccessful()){
                            MoviesResponse moviesResponse = response.body();
                            if(moviesResponse != null && moviesResponse.getMovies() != null) {
                                callback.onSuccess(moviesResponse.getMovies());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }
                    @Override
                    public void onFailure(Call<MoviesResponse> call, Throwable t) {
                        Log.d(MoviesRepository.class.toString(),"Failed to get movies");
                        callback.onError();
                    }
                });
    }

    public void getGenres(final OnGetGenresCallback callback){
        theMovieDbService.getGenres(API_KEY,LANGUAGE)
                .enqueue(new Callback<GenresResponse>() {
                    @Override
                    public void onResponse(Call<GenresResponse> call, Response<GenresResponse> response) {
                        if(response.isSuccessful()){
                            GenresResponse genresResponse = response.body();
                            if(genresResponse != null && genresResponse.getGenres() != null) {
                                callback.onSuccess(genresResponse.getGenres());
                            } else {
                                callback.onError();
                            }
                        } else {
                            Log.d(MoviesRepository.class.toString(),"Failed to get genres");
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<GenresResponse> call, Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getPopularMoviesNextPage(final OnGetMoviesCallback callback,int page){
        theMovieDbService.getPopular(API_KEY,LANGUAGE,page)
                .enqueue(new Callback<MoviesResponse>() {
                    @Override
                    public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                        if(response.isSuccessful()){
                            MoviesResponse moviesResponse = response.body();
                            if(moviesResponse != null && moviesResponse.getMovies() != null) {
                                callback.onSuccess(moviesResponse.getMovies());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }
                    @Override
                    public void onFailure(Call<MoviesResponse> call, Throwable t) {
                        Log.d(MoviesRepository.class.toString(),"Failed to get movies");
                        callback.onError();
                    }
                });
    }
}
