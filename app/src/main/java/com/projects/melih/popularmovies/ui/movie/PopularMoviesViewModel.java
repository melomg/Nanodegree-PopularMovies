package com.projects.melih.popularmovies.ui.movie;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.projects.melih.popularmovies.common.Constants;
import com.projects.melih.popularmovies.model.Movie;
import com.projects.melih.popularmovies.network.MovieAPI;
import com.projects.melih.popularmovies.repository.NetworkState;
import com.projects.melih.popularmovies.repository.PopularMoviesDataSourceFactory;

/**
 * Created by Melih Gültekin on 1.03.2018
 */

class PopularMoviesViewModel extends ViewModel {
    private final PopularMoviesDataSourceFactory popularMoviesSourceFactory = new PopularMoviesDataSourceFactory(MovieAPI.getMovieService());

    private LiveData<PagedList<Movie>> pagedList;
    private LiveData<NetworkState> networkState;
    private LiveData<NetworkState> refreshState;

    LiveData<PagedList<Movie>> getPagedList() {
        return pagedList;
    }

    LiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    LiveData<NetworkState> getRefreshState() {
        return refreshState;
    }

    void sortByPopular(boolean shouldRefresh) {
        if (shouldRefresh) {
            final PopularMoviesDataSourceFactory.PageKeyedMovieDataSource value = popularMoviesSourceFactory.getSourceLiveData().getValue();
            if (value != null) {
                value.invalidate();
            }
        } else if (pagedList == null) {
            pagedList = new LivePagedListBuilder<>(popularMoviesSourceFactory, Constants.PAGE_SIZE).build();
            networkState = Transformations.switchMap(popularMoviesSourceFactory.getSourceLiveData(), PopularMoviesDataSourceFactory.PageKeyedMovieDataSource::getNetworkState);
            refreshState = Transformations.switchMap(popularMoviesSourceFactory.getSourceLiveData(), PopularMoviesDataSourceFactory.PageKeyedMovieDataSource::getInitialLoad);
        }
    }
}