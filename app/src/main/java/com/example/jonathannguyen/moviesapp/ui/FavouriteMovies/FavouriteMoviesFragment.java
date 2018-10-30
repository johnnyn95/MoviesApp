package com.example.jonathannguyen.moviesapp.ui.FavouriteMovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.jonathannguyen.moviesapp.R;
import com.example.jonathannguyen.moviesapp.api.model.Genres;
import com.example.jonathannguyen.moviesapp.api.model.Movies;
import com.example.jonathannguyen.moviesapp.ui.MovieDetails.MovieDetails;


import java.util.List;

public class FavouriteMoviesFragment extends Fragment implements FavouriteMoviesAdapterOnClickHandler{
    FavouriteMoviesViewModel favouriteMoviesViewModel;
    FavouriteMoviesAdapter adapter = new FavouriteMoviesAdapter(this);
    RecyclerView recyclerView;

    private OnFragmentInteractionListener mListener;


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_favourites,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.action_removeAll:
                favouriteMoviesViewModel.removeAllMoviesFromFavourites();
                Snackbar snackbar = Snackbar.make(getView(), R.string.all_movies_removed, Snackbar.LENGTH_SHORT)
                        .setAction("Action", null);
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorSuccess));
                snackbar.show();

        }
        return super.onOptionsItemSelected(item);
    }

    public FavouriteMoviesFragment() { }

    public static FavouriteMoviesFragment newInstance(String param1, String param2) {
        FavouriteMoviesFragment fragment = new FavouriteMoviesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
        if(recyclerView.getLayoutManager() == null)
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        favouriteMoviesViewModel = ViewModelProviders.of(this).get(FavouriteMoviesViewModel.class);
        favouriteMoviesViewModel.getmMovies().observe(this, new Observer<List<Movies>>() {
            @Override
            public void onChanged(@Nullable List<Movies> movies) {
                adapter.setMovies(movies);
                recyclerView.setAdapter(adapter);
                if(favouriteMoviesViewModel.getmLastPosition().getValue() != null) {
                recyclerView.scrollToPosition(favouriteMoviesViewModel.getmLastPosition().getValue());
                }

            }
        });
        favouriteMoviesViewModel.getmGenres().observe(this, new Observer<List<Genres>>() {
            @Override
            public void onChanged(@Nullable List<Genres> genres) {
                adapter.setAllGenres(genres);
                if(favouriteMoviesViewModel.getmLastPosition().getValue() != null) {
                    recyclerView.scrollToPosition(favouriteMoviesViewModel.getmLastPosition().getValue());
                }
            }
        });

        favouriteMoviesViewModel.getmLastPosition().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                recyclerView.scrollToPosition(integer);
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
        Snackbar snackbar = Snackbar.make(getView(), R.string.movie_removed, Snackbar.LENGTH_SHORT)
                .setAction("Action", null);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
        snackbar.show();
        favouriteMoviesViewModel.setLastAdapterPosition(getLinearLayoutManager(recyclerView).findFirstVisibleItemPosition());
    }

    @Override
    public void movieDetails(Movies movie) {
        Intent intent = new Intent(getActivity(),MovieDetails.class);
        intent.putExtra(getString(R.string.EXTRA_MOVIE_ID),movie.getId());
        intent.putExtra(getString(R.string.EXTRA_CALLED_FROM_FAVOURITES),true);
        startActivity(intent);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(getLinearLayoutManager(recyclerView) != null)
            favouriteMoviesViewModel.setLastAdapterPosition(getLinearLayoutManager(recyclerView).findFirstVisibleItemPosition());
    }

    private LinearLayoutManager getLinearLayoutManager(RecyclerView recyclerView){
        return (LinearLayoutManager) recyclerView.getLayoutManager();
    }

}
