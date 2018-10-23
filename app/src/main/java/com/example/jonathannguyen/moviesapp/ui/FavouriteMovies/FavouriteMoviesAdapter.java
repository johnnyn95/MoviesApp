package com.example.jonathannguyen.moviesapp.ui.FavouriteMovies;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.jonathannguyen.moviesapp.R;
import com.example.jonathannguyen.moviesapp.api.model.Genres;
import com.example.jonathannguyen.moviesapp.api.model.Movies;

import java.util.ArrayList;
import java.util.List;

public class FavouriteMoviesAdapter extends RecyclerView.Adapter<FavouriteMoviesAdapter.MovieViewHolder>  {
    private List<Movies> movies;
    private List<Genres> allGenres;
    private String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w500";
    private final FavouriteMoviesAdapterOnClickHandler moviesAdapterOnClickHandler;

    public FavouriteMoviesAdapter(FavouriteMoviesAdapterOnClickHandler moviesAdapterOnClickHandler){
        this.moviesAdapterOnClickHandler = moviesAdapterOnClickHandler;
    }

    public void setMovies(List<Movies> movies){
        this.movies = movies;
    }

    public void setAllGenres(List<Genres> allGenres) {
        this.allGenres = allGenres;
    }

    @Override
    public FavouriteMoviesAdapter.MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)  {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.favourite_movies_list_item,viewGroup,false);
        return new FavouriteMoviesAdapter.MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteMoviesAdapter.MovieViewHolder movieViewHolder, int i){
        movieViewHolder.bind(movies.get(i));

    }

    @Override
    public int getItemCount() {
        if(movies != null ){
            return movies.size();
        } else return 0;
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView releaseDate;
        TextView title;
        TextView rating;
        TextView genres;
        ImageView poster;
        ImageView delete;

        public MovieViewHolder(View itemView) {
            super(itemView);
            releaseDate = itemView.findViewById(R.id.item_movie_release_date);
            title = itemView.findViewById(R.id.item_movie_title);
            rating = itemView.findViewById(R.id.item_movie_rating);
            genres = itemView.findViewById(R.id.item_movie_genre);
            poster = itemView.findViewById(R.id.item_movie_poster);
            delete = itemView.findViewById(R.id.item_movie_delete);
            delete.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        public void bind(Movies movie){
            releaseDate.setText(movie.getReleaseDate().split("-")[0]);
            title.setText(movie.getTitle());
            rating.setText(String.valueOf(movie.getRating()));
            genres.setText(getGenres(movie.getGenreIds()));
            Glide.with(itemView).load(IMAGE_BASE_URL + movie.getPosterPath())
                    .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                    .into(poster);
        }

        private String getGenres(List<Integer> genreIds) {
            if(allGenres != null) {
                List<String> movieGenres = new ArrayList<>();
                for (Integer genreId : genreIds) {
                    for (Genres genre : allGenres) {
                        if (genre.getId() == genreId) {
                            movieGenres.add(genre.getName());
                            break;
                        }
                    }
                }
                return TextUtils.join(", ", movieGenres);
            } else {
                Log.d(FavouriteMoviesAdapter.class.toString(),"Failed to fetch genres");
                return "";
            }
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Movies movie = movies.get(position);
            if(movie != null) {
                if (v == delete) {
                    moviesAdapterOnClickHandler.removeFromFavourites(movie);
                    movies.remove(position);
                    notifyItemRemoved(position);
                } else {
                    moviesAdapterOnClickHandler.movieDetails(movie);
                }
            }
        }
    }
}
