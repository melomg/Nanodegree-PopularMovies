package com.projects.melih.popularmovies.ui.movielist;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projects.melih.popularmovies.R;
import com.projects.melih.popularmovies.network.NetworkState;

/**
 * Created by Melih Gültekin on 17.02.2018
 */

public class FavoriteMoviesFragment extends BaseMovieListFragment {

    private PopularMoviesViewModel model;

    public static FavoriteMoviesFragment newInstance() {
        return new FavoriteMoviesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        model = ViewModelProviders.of(this).get(PopularMoviesViewModel.class);

        model.sortByPopular(false);
        model.getPagedList().observe(this, allMovies -> {
            if (allMovies != null) {
                adapter.submitMovies(allMovies);
            }
        });
        model.getNetworkState().observe(this, networkState -> {
            if (networkState == NetworkState.NO_NETWORK) {
                showToast(R.string.network_error);
            } else if (networkState != null) {
                showToast(networkState.getErrorMessage());
            }
        });
        model.getRefreshState().observe(this, networkState -> binding.swipeRefresh.setRefreshing(networkState == NetworkState.LOADING));
        binding.swipeRefresh.setOnRefreshListener(() -> {
            super.onRefreshListener();
            model.sortByPopular(true);
        });
        return view;
    }

    @Override
    protected void onLoadMore() {
        model.sortByPopular(false);
    }
}