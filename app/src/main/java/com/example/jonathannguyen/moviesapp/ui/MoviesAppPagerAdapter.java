package com.example.jonathannguyen.moviesapp.ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.jonathannguyen.moviesapp.R;

public class MoviesAppPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;

    public MoviesAppPagerAdapter(Context context, FragmentManager fragmentManager){
        super(fragmentManager);
        this.mContext = context;
    }
    @Override
    public Fragment getItem(int i) {
        switch(i){
            case 0:
                return new PopularMoviesFragment();
            case 1:
                return new SearchMoviesFragment();
            case 2:
                return new FavouriteMoviesFragment();
                default:
                    return new PopularMoviesFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int position){
        switch(position){
            case 0:
                return mContext.getString(R.string.fragment_popular);
            case 1:
                return mContext.getString(R.string.fragment_search);
            case 2:
                return mContext.getString(R.string.fragment_favourites);
            default:
                return null;
        }
    }
}