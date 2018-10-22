package com.example.jonathannguyen.moviesapp.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.example.jonathannguyen.moviesapp.api.OnGetReviewsCallback;
import com.example.jonathannguyen.moviesapp.api.OnGetTrailersCallback;
import com.example.jonathannguyen.moviesapp.api.model.GenresResponse;
import com.example.jonathannguyen.moviesapp.api.OnGetGenresCallback;
import com.example.jonathannguyen.moviesapp.api.OnGetMoviesCallback;
import com.example.jonathannguyen.moviesapp.api.model.Movies;
import com.example.jonathannguyen.moviesapp.api.model.MoviesResponse;
import com.example.jonathannguyen.moviesapp.api.TheMovieDbService;
import com.example.jonathannguyen.moviesapp.api.OnGetMovieCallback;
import com.example.jonathannguyen.moviesapp.api.model.ReviewsResponse;
import com.example.jonathannguyen.moviesapp.api.model.Trailers;
import com.example.jonathannguyen.moviesapp.api.model.TrailersResponse;
import com.example.jonathannguyen.moviesapp.db.MoviesDao;
import com.example.jonathannguyen.moviesapp.db.MoviesRoomDatabase;

import java.util.List;

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

    public void getMovie(int movieId, final OnGetMovieCallback callback) {
        theMovieDbService.getMovie(movieId, API_KEY, LANGUAGE)
                .enqueue(new Callback<Movies>() {
                    @Override
                    public void onResponse(Call<Movies> call, Response<Movies> response) {
                        if (response.isSuccessful()) {
                            Movies movie = response.body();
                            if (movie != null) {
                                callback.onSuccess(movie);
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }
                    @Override
                    public void onFailure(Call<Movies> call, Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getReviews(int movieId, final OnGetReviewsCallback callback){
        theMovieDbService.getReviews(movieId,API_KEY,LANGUAGE)
                .enqueue(new Callback<ReviewsResponse>() {
                    @Override
                    public void onResponse(Call<ReviewsResponse> call, Response<ReviewsResponse> response) {
                        if(response.isSuccessful()){
                            ReviewsResponse reviewsResponse = response.body();
                            if(reviewsResponse != null && reviewsResponse.getReviews() != null){
                                callback.onSuccess(reviewsResponse.getReviews());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }
                    @Override
                    public void onFailure(Call<ReviewsResponse> call, Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getTrailers(int movieId, final OnGetTrailersCallback callback) {
        theMovieDbService.getTrailers(movieId,API_KEY, LANGUAGE)
                .enqueue(new Callback<TrailersResponse>() {
                    @Override
                    public void onResponse(Call<TrailersResponse> call, Response<TrailersResponse> response) {
                        if (response.isSuccessful()) {
                            TrailersResponse trailerResponse = response.body();
                            if (trailerResponse != null && trailerResponse.getTrailers() != null) {
                                callback.onSuccess(trailerResponse.getTrailers());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }
                    @Override
                    public void onFailure(Call<TrailersResponse> call, Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getSearchMovies(String query,final OnGetMoviesCallback callback){
        theMovieDbService.getSearchMovies(API_KEY,LANGUAGE,1,query).enqueue(new Callback<MoviesResponse>() {
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
                Log.d(MoviesRepository.class.toString(),"Failed to get search movies");
                callback.onError();
            }
        });
    }

    public void getSearchMoviesNextPage(String query,Integer page,final OnGetMoviesCallback callback){
        theMovieDbService.getSearchMovies(API_KEY,LANGUAGE,page,query).enqueue(new Callback<MoviesResponse>() {
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
                Log.d(MoviesRepository.class.toString(),"Failed to get search movies");
                callback.onError();
            }
        });
    }

}
