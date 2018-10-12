package com.example.jonathannguyen.moviesapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TheMovieDbService {
    @GET("movie/popular")
    Call<MoviesResponse> getPopular(@Query("api_key") String apiKey,
                                    @Query("language") String language,
                                    @Query("page") int page);
    @GET("genre/movie/list")
    Call<GenresResponse> getGenres(
            @Query("api_key") String apiKey,
            @Query("language") String language);

    @GET("search/movie")
    Call<MoviesResponse> getSearchMovies(@Query("api_key") String apiKey,
                                         @Query("language") String language,
                                         @Query("page") int page,
                                         @Query("query") String searchQuery);
}
