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
import com.projects.melih.popularmovies.repository.TopRatedMoviesDataSourceFactory;

/**
 * Created by Melih Gültekin on 1.03.2018
 */

class TopRatedMoviesViewModel extends ViewModel {
    private final TopRatedMoviesDataSourceFactory topRatedMoviesSourceFactory = new TopRatedMoviesDataSourceFactory(MovieAPI.getMovieService());

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

    void sortByTopRated(boolean shouldRefresh) {
        if (shouldRefresh) {
            final TopRatedMoviesDataSourceFactory.PageKeyedMovieDataSource value = topRatedMoviesSourceFactory.getSourceLiveData().getValue();
            if (value != null) {
                value.invalidate();
            }
        } else if (pagedList == null) {
            pagedList = new LivePagedListBuilder<>(topRatedMoviesSourceFactory, Constants.PAGE_SIZE).build();
            networkState = Transformations.switchMap(topRatedMoviesSourceFactory.getSourceLiveData(), TopRatedMoviesDataSourceFactory.PageKeyedMovieDataSource::getNetworkState);
            refreshState = Transformations.switchMap(topRatedMoviesSourceFactory.getSourceLiveData(), TopRatedMoviesDataSourceFactory.PageKeyedMovieDataSource::getInitialLoad);
        }
    }
}