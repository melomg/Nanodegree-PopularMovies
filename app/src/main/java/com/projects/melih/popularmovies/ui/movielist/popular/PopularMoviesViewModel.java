package com.projects.melih.popularmovies.ui.movielist.popular;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.projects.melih.popularmovies.common.CollectionUtils;
import com.projects.melih.popularmovies.common.Utils;
import com.projects.melih.popularmovies.model.Movie;
import com.projects.melih.popularmovies.network.MovieAPI;
import com.projects.melih.popularmovies.network.MovieService;
import com.projects.melih.popularmovies.network.NetworkState;
import com.projects.melih.popularmovies.network.responses.ResponseMovie;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.projects.melih.popularmovies.common.Constants.UNKNOWN_ERROR;

/**
 * Created by Melih Gültekin on 1.03.2018
 */

public class PopularMoviesViewModel extends AndroidViewModel {
    private static final int FIRST_PAGE = 0;
    private final MovieService movieService;
    private final MutableLiveData<Integer> page;
    private final MutableLiveData<NetworkState> networkState;
    private final MutableLiveData<NetworkState> refreshState;
    private MutableLiveData<Integer> totalPage;
    private MutableLiveData<List<Movie>> list;
    private Call<ResponseMovie> call;

    public PopularMoviesViewModel(@NonNull Application application) {
        super(application);
        page = new MutableLiveData<>();
        page.setValue(FIRST_PAGE);
        networkState = new MutableLiveData<>();
        refreshState = new MutableLiveData<>();
        movieService = MovieAPI.getMovieService();
    }

    LiveData<List<Movie>> getPagedList() {
        return list;
    }

    LiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    LiveData<NetworkState> getRefreshState() {
        return refreshState;
    }

    synchronized void sortByPopular(boolean isFirstPage) {
        if (list == null) {
            list = new MutableLiveData<>();
        }
        if (!Utils.isNetworkConnected(getApplication().getApplicationContext())) {
            list.postValue(list.getValue());
            networkState.postValue(NetworkState.NO_NETWORK);
            refreshState.postValue(NetworkState.LOADED);
        } else {
            if (isFirstPage) {
                list.setValue(new ArrayList<>());
                page.setValue(FIRST_PAGE);
            }
            //noinspection ConstantConditions
            if ((totalPage == null) || (page.getValue() < totalPage.getValue())) {
                final Integer currentPage = page.getValue();
                if (currentPage != null) {
                    page.setValue(currentPage + 1);
                }
                callPopularMovies(page.getValue());
            }
        }
    }

    private void callPopularMovies(final int page) {
        refreshState.postValue(NetworkState.LOADING);
        call = movieService.getPopularMovies(page);
        call.enqueue(new Callback<ResponseMovie>() {
            @Override
            public void onResponse(@NonNull Call<ResponseMovie> call, @NonNull Response<ResponseMovie> response) {
                boolean success = false;
                if (response.isSuccessful()) {
                    final ResponseMovie body = response.body();
                    if (body != null) {
                        final List<Movie> movies = body.getMovies();
                        if (totalPage == null) {
                            totalPage = new MutableLiveData<>();
                        }
                        totalPage.setValue(body.getTotalPages());
                        if (CollectionUtils.isNotEmpty(movies)) {
                            success = true;
                            List<Movie> allMovies = list.getValue();
                            if (allMovies == null) {
                                allMovies = new ArrayList<>();
                            }
                            allMovies.addAll(movies);
                            list.postValue(allMovies);
                        }
                    }
                    refreshState.postValue(NetworkState.LOADED);
                    if (!success) {
                        networkState.postValue(NetworkState.error(UNKNOWN_ERROR));
                    }
                } else {
                    networkState.postValue(NetworkState.error("error code: " + response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseMovie> call, @NonNull Throwable t) {
                final String message = t.getMessage();
                networkState.postValue(NetworkState.error((message == null) ? UNKNOWN_ERROR : message));
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        call.cancel();
    }
}