package com.example.jonathannguyen.moviesapp.ui.PopularMovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jonathannguyen.moviesapp.R;
import com.example.jonathannguyen.moviesapp.api.model.Movies;
import com.example.jonathannguyen.moviesapp.ui.MovieDetails.MovieDetails;
import com.example.jonathannguyen.moviesapp.ui.MoviesAdapter;
import com.example.jonathannguyen.moviesapp.ui.MoviesAdapterOnClickHandler;

import java.util.List;

public class PopularMoviesFragment extends Fragment implements MoviesAdapterOnClickHandler {
    PopularMoviesViewModel popularMoviesViewModel;
    MoviesAdapter adapter = new MoviesAdapter(this);
    RecyclerView recyclerView;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    private OnFragmentInteractionListener mListener;

    public PopularMoviesFragment() { }

    public static PopularMoviesFragment newInstance() {
        PopularMoviesFragment fragment = new PopularMoviesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_popular_movies, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = getView().findViewById(R.id.recyclerview);
        final FloatingActionButton fab = getView().findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popularMoviesViewModel.setLastAdapterPosition(0);
                popularMoviesViewModel.getPopularMovies();
                fab.hide();
                Snackbar snackbar = Snackbar.make(view, R.string.refresh_popular_movies, Snackbar.LENGTH_SHORT)
                        .setAction("Action", null);
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorSuccess));
                snackbar.show();
                recyclerView.smoothScrollToPosition(0);
            }
        });

        recyclerView.setAdapter(adapter);
        if(recyclerView.getLayoutManager() == null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            recyclerView.setLayoutManager(getLinearLayoutManager(recyclerView));
        }
        popularMoviesViewModel = ViewModelProviders.of(this).get(PopularMoviesViewModel.class);

        popularMoviesViewModel.getmMovies().observe(this, new Observer<List<Movies>>(){
            @Override
            public void onChanged(@Nullable List<Movies> movies) {
                adapter.setMovies(movies);
                adapter.setAllGenres(popularMoviesViewModel.getmGenres().getValue());
                recyclerView.setAdapter(adapter);

                if(popularMoviesViewModel.getmLastPosition().getValue() != null){
                    recyclerView.scrollToPosition(popularMoviesViewModel.getmLastPosition().getValue());
                }

            }
        });

        popularMoviesViewModel.getmLastPosition().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                recyclerView.scrollToPosition(integer);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fab.getVisibility() == View.VISIBLE) {
                    fab.hide();
                } else if (dy < 0 && fab.getVisibility() != View.VISIBLE) {
                    fab.show();
                }
                if(dy > 0)
                {
                    visibleItemCount = getLinearLayoutManager(recyclerView).getChildCount();
                    totalItemCount = getLinearLayoutManager(recyclerView).getItemCount();
                    pastVisiblesItems = getLinearLayoutManager(recyclerView).findFirstVisibleItemPosition();

                    if (loading)
                    {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            loading = false;
                            popularMoviesViewModel.getPopularMoviesNextPage();
                            popularMoviesViewModel.setLastAdapterPosition(getLinearLayoutManager(recyclerView).findFirstVisibleItemPosition());
                        }
                    }
                    loading = true;
                }
            }
        });
        popularMoviesViewModel.getPopularMovies();

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
    public void addToFavourites(Movies movie) {
        if(!popularMoviesViewModel.checkIfMovieIsInFavourites(movie)){
            popularMoviesViewModel.addMovieToFavourites(movie);
            Snackbar snackbar = Snackbar.make(getView(), R.string.movie_added, Snackbar.LENGTH_SHORT)
                    .setAction("Action", null);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorSuccess));
            snackbar.show();
        } else {
            Snackbar snackbar = Snackbar.make(getView(), R.string.movie_duplicate, Snackbar.LENGTH_SHORT)
                    .setAction("Action", null);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
            snackbar.show();
        }
    }

    @Override
    public void movieDetails(Movies movie) {
        Intent intent = new Intent(getActivity(),MovieDetails.class);
        intent.putExtra(getString(R.string.EXTRA_MOVIE_ID),movie.getId());
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
        popularMoviesViewModel.setLastAdapterPosition(getLinearLayoutManager(recyclerView).findFirstVisibleItemPosition());
    }

    private LinearLayoutManager getLinearLayoutManager(RecyclerView recyclerView){
        if(recyclerView != null)
        return (LinearLayoutManager) recyclerView.getLayoutManager();
        else
            return null;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
