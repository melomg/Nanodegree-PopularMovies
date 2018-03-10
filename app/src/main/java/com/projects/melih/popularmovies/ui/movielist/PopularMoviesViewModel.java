package com.projects.melih.popularmovies.ui.movielist;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import com.projects.melih.popularmovies.common.Constants;
import com.projects.melih.popularmovies.common.Utils;
import com.projects.melih.popularmovies.model.Movie;
import com.projects.melih.popularmovies.network.MovieAPI;
import com.projects.melih.popularmovies.network.NetworkState;
import com.projects.melih.popularmovies.repository.PopularMoviesDataSourceFactory;

/**
 * Created by Melih Gültekin on 1.03.2018
 */

class PopularMoviesViewModel extends AndroidViewModel {
    private final PopularMoviesDataSourceFactory popularMoviesSourceFactory = new PopularMoviesDataSourceFactory(MovieAPI.getMovieService());

    private LiveData<PagedList<Movie>> pagedList;
    private MutableLiveData<NetworkState> networkState;
    private LiveData<NetworkState> refreshState;

    public PopularMoviesViewModel(@NonNull Application application) {
        super(application);
    }

    LiveData<PagedList<Movie>> getPagedList() {
        return pagedList;
    }

    MutableLiveData<NetworkState> getNetworkState() {
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
            networkState = (MutableLiveData<NetworkState>) Transformations.switchMap(popularMoviesSourceFactory.getSourceLiveData(), PopularMoviesDataSourceFactory.PageKeyedMovieDataSource::getNetworkState);
            refreshState = Transformations.switchMap(popularMoviesSourceFactory.getSourceLiveData(), PopularMoviesDataSourceFactory.PageKeyedMovieDataSource::getInitialLoad);
        }
        if (!Utils.isNetworkConnected(getApplication().getApplicationContext())) {
            networkState.postValue(NetworkState.NO_NETWORK);
        }
    }
}