package com.example.jonathannguyen.moviesapp.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jonathannguyen.moviesapp.R;
import com.example.jonathannguyen.moviesapp.api.model.Genres;
import com.example.jonathannguyen.moviesapp.api.model.Movies;


import java.util.List;


public class FavouriteMoviesFragment extends Fragment implements FavouriteMoviesAdapterOnClickHandler{
    FavouriteMoviesViewModel favouriteMoviesViewModel;
    FavouriteMoviesAdapter adapter = new FavouriteMoviesAdapter(this);
    RecyclerView recyclerView;

    private OnFragmentInteractionListener mListener;

    public FavouriteMoviesFragment() { }

    public static FavouriteMoviesFragment newInstance(String param1, String param2) {
        FavouriteMoviesFragment fragment = new FavouriteMoviesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favourite_movies, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = getView().findViewById(R.id.recyclerview_favourites);
        recyclerView.setAdapter(adapter);
        if(recyclerView.getLayoutManager() == null)
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        favouriteMoviesViewModel = ViewModelProviders.of(this).get(FavouriteMoviesViewModel.class);
        favouriteMoviesViewModel.getmMovies().observe(this, new Observer<List<Movies>>() {
            @Override
            public void onChanged(@Nullable List<Movies> movies) {
                adapter.setMovies(movies);
                adapter.setAllGenres(favouriteMoviesViewModel.getmGenres().getValue());
                recyclerView.setAdapter(adapter);
            }
        });

        favouriteMoviesViewModel.getmLastPosition().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                recyclerView.scrollToPosition(favouriteMoviesViewModel.getmLastPosition().getValue());
            }
        });
        favouriteMoviesViewModel.getFavouriteMovies();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void removeFromFavourites(Movies movie) {
        favouriteMoviesViewModel.removeMovieFromFavourites(movie);
        adapter.setMovies(favouriteMoviesViewModel.getmMovies().getValue());
        adapter.setAllGenres(favouriteMoviesViewModel.getmGenres().getValue());
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(getLinearLayoutManager(recyclerView).findFirstVisibleItemPosition());
    }

    @Override
    public void movieDetails(Movies movie) {
        Intent intent = new Intent(getActivity(),MovieDetails.class);
        intent.putExtra(getString(R.string.EXTRA_MOVIE_ID),movie.getId());
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        favouriteMoviesViewModel.setLastAdapterPosition(getLinearLayoutManager(recyclerView).findFirstVisibleItemPosition());
    }

    private LinearLayoutManager getLinearLayoutManager(RecyclerView recyclerView){
        return (LinearLayoutManager) recyclerView.getLayoutManager();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
