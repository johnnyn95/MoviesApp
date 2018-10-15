package com.example.jonathannguyen.moviesapp.ui;

import com.example.jonathannguyen.moviesapp.api.model.Movies;

public interface MoviesAdapterOnClickHandler {
    void addToFavourites(Movies movie);
    void movieDetails(Movies movie);
}
