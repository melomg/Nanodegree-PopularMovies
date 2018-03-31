package com.projects.melih.popularmovies.ui.movielist.favorite;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projects.melih.popularmovies.R;
import com.projects.melih.popularmovies.components.GridAutoFitLayoutManager;
import com.projects.melih.popularmovies.databinding.FragmentMovieListBinding;
import com.projects.melih.popularmovies.model.Movie;
import com.projects.melih.popularmovies.network.NetworkState;
import com.projects.melih.popularmovies.ui.base.BaseFragment;
import com.projects.melih.popularmovies.ui.moviedetail.MovieDetailFragment;
import com.projects.melih.popularmovies.ui.movielist.MovieListener;

/**
 * Created by Melih Gültekin on 25.03.2018
 */

public class FavoriteMoviesFragment extends BaseFragment {
    private FragmentMovieListBinding binding;
    private FavoriteMoviesViewModel model;
    private FavoriteListAdapter adapter;

    public static FavoriteMoviesFragment newInstance() {
        return new FavoriteMoviesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_list, container, false);
        //noinspection ConstantConditions
        model = ViewModelProviders.of(getActivity()).get(FavoriteMoviesViewModel.class);

        model.fetchData();
        model.getFavoriteList().observe(this, allFavoriteMovies -> {
            showListView();
            if (allFavoriteMovies != null) {
                adapter.swapCursor(allFavoriteMovies);
            }
        });
        model.getNetworkState().observe(this, networkState -> {
            if (networkState == NetworkState.FAILED) {
                showToast(R.string.unknown_error);
            } else if (networkState == NetworkState.EMPTY) {
                showEmptyView();
            }
        });
        model.getRefreshState().observe(this, networkState -> binding.swipeRefresh.setRefreshing(networkState == NetworkState.LOADING));
        binding.swipeRefresh.setOnRefreshListener(() -> model.fetchData());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.swipeRefresh.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);

        adapter = new FavoriteListAdapter(context, new MovieListener() {
            @Override
            public void onFavoriteDelete(Movie movie) {
                model.deleteFavorite(movie);
            }

            @Override
            public void onItemClick(Movie movie) {
                navigationListener.addFragment(MovieDetailFragment.newInstance(movie, false));
            }
        });

        GridAutoFitLayoutManager layoutManager = new GridAutoFitLayoutManager(context, R.dimen.list_item_width);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setHasFixedSize(false);
        binding.recyclerView.setAdapter(adapter);
    }

    private void showEmptyView() {
        binding.emptyView.setText(context.getString(R.string.empty_favorite_list));
        binding.emptyView.setVisibility(View.VISIBLE);
        binding.recyclerView.setVisibility(View.GONE);
    }

    private void showListView() {
        binding.emptyView.setVisibility(View.GONE);
        binding.recyclerView.setVisibility(View.VISIBLE);
    }
}