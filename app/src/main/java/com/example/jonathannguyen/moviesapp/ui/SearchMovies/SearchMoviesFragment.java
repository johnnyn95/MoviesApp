package com.example.jonathannguyen.moviesapp.ui.SearchMovies;

import android.app.Activity;
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
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.jonathannguyen.moviesapp.R;
import com.example.jonathannguyen.moviesapp.api.model.Movies;
import com.example.jonathannguyen.moviesapp.ui.MovieDetails;
import com.example.jonathannguyen.moviesapp.ui.MoviesAdapter;
import com.example.jonathannguyen.moviesapp.ui.MoviesAdapterOnClickHandler;

import java.util.List;

public class SearchMoviesFragment extends Fragment implements MoviesAdapterOnClickHandler {
    private SearchMoviesViewModel searchMoviesViewModel;
    MoviesAdapter adapter = new MoviesAdapter(this);
    RecyclerView recyclerView;
    EditText searchQuery;
    FloatingActionButton fab;
    AppBarLayout appBarLayout;
    ImageButton clearSearchQuery;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private OnFragmentInteractionListener mListener;

    public SearchMoviesFragment() { }

    public static SearchMoviesFragment newInstance() {
        SearchMoviesFragment fragment = new SearchMoviesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_search_movies, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = getView().findViewById(R.id.recyclerview_search);
        searchQuery = getView().findViewById(R.id.search_query);
        clearSearchQuery = getView().findViewById(R.id.clear_search_query);
        appBarLayout = getView().findViewById(R.id.appBarLayout);
        fab = getView().findViewById(R.id.fab_search);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(searchQuery.getText().toString() != "" && !TextUtils.isEmpty(searchQuery.getText().toString())){
                    searchMoviesViewModel.getSearchMovies(searchQuery.getText().toString());
                    fab.hide();
                    appBarLayout.setExpanded(false,true);
                    hideKeyboardFrom(getContext(),view);
                    if(searchMoviesViewModel.getmLastPosition().getValue() != null){
                        recyclerView.scrollToPosition(searchMoviesViewModel.getmLastPosition().getValue());
                    }
                } else {
                    Snackbar snackbar = Snackbar
                            .make(view, R.string.search_query_empty, Snackbar.LENGTH_SHORT)
                            .setAction("Action", null);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                    snackbar.show();
                }
            }
        });
        clearSearchQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchQuery.setText("");
            }
        });
        recyclerView.setAdapter(adapter);
        if(recyclerView.getLayoutManager() == null){
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        searchMoviesViewModel = ViewModelProviders.of(this).get(SearchMoviesViewModel.class);
        searchMoviesViewModel.getmMovies().observe(this, new Observer<List<Movies>>(){
            @Override
            public void onChanged(@Nullable List<Movies> movies) {
                adapter.setMovies(movies);
                adapter.setAllGenres(searchMoviesViewModel.getmGenres().getValue());
                recyclerView.setAdapter(adapter);
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
                    recyclerView.setPadding(0,0,0,0);
                } else if (dy < 0 && fab.getVisibility() != View.VISIBLE) {
                    fab.show();
                    recyclerView.setPadding(0,20,0,0);
                    appBarLayout.setExpanded(true,true);
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
                            searchMoviesViewModel.getSearchMoviesNextPage(searchQuery.getText().toString());
                            searchMoviesViewModel.setLastAdapterPosition(getLinearLayoutManager(recyclerView).findFirstVisibleItemPosition());
                        }
                    }
                    loading = true;
                }
            }
        });
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
        searchMoviesViewModel.addMovieToFavourites(movie);
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
        searchMoviesViewModel.setLastAdapterPosition(getLinearLayoutManager(recyclerView).findFirstVisibleItemPosition());
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
    private LinearLayoutManager getLinearLayoutManager(RecyclerView recyclerView){
        return (LinearLayoutManager) recyclerView.getLayoutManager();
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
