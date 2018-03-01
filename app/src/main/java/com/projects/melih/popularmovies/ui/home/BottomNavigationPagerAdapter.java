package com.projects.melih.popularmovies.ui.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.projects.melih.popularmovies.ui.movie.PopularMoviesFragment;
import com.projects.melih.popularmovies.ui.movie.TopRatedMoviesFragment;

/**
 * Created by Melih Gültekin on 1.03.2018
 */

public class BottomNavigationPagerAdapter extends FragmentStatePagerAdapter {
    public static final int BY_POPULAR = 0;
    public static final int BY_TOP_RATED = 1;
    private static final int TAB_COUNT = 2;

    BottomNavigationPagerAdapter(FragmentManager childFragmentManager) {
        super(childFragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case BY_TOP_RATED:
                fragment = TopRatedMoviesFragment.newInstance();
                break;
            case BY_POPULAR:
            default:
                fragment = PopularMoviesFragment.newInstance();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }
}