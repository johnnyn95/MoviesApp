package com.example.jonathannguyen.moviesapp.ui;

import com.example.jonathannguyen.moviesapp.api.model.Movies;

public interface FavouriteMoviesAdapterOnClickHandler {
    void removeFromFavourites(Movies movie);
    void movieDetails(Movies movie);
}
