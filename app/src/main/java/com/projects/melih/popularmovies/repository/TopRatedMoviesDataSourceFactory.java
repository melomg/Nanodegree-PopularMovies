package com.projects.melih.popularmovies.repository;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;

import com.projects.melih.popularmovies.model.Movie;
import com.projects.melih.popularmovies.network.MovieService;
import com.projects.melih.popularmovies.network.responses.ResponseMovie;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.projects.melih.popularmovies.common.Constants.UNKNOWN_ERROR;

/**
 * Created by Melih Gültekin on 24.02.2018
 */

public class TopRatedMoviesDataSourceFactory implements DataSource.Factory<Integer, Movie> {
    private final MutableLiveData<PageKeyedMovieDataSource> sourceLiveData;
    private final MovieService networkService;

    public TopRatedMoviesDataSourceFactory(@NonNull MovieService networkService) {
        this.networkService = networkService;
        sourceLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<PageKeyedMovieDataSource> getSourceLiveData() {
        return sourceLiveData;
    }

    @Override
    public DataSource<Integer, Movie> create() {
        PageKeyedMovieDataSource source = new PageKeyedMovieDataSource(networkService);
        sourceLiveData.postValue(source);
        return source;
    }

    public static class PageKeyedMovieDataSource extends PageKeyedDataSource<Integer, Movie> {
        private final MutableLiveData<NetworkState> networkState = new MutableLiveData<>();
        private final MutableLiveData<NetworkState> initialLoad = new MutableLiveData<>();
        private final MovieService networkService;

        PageKeyedMovieDataSource(@NonNull MovieService networkService) {
            this.networkService = networkService;
        }

        public MutableLiveData<NetworkState> getNetworkState() {
            return networkState;
        }

        public MutableLiveData<NetworkState> getInitialLoad() {
            return initialLoad;
        }

        @Override
        public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Movie> callback) {
            Call<ResponseMovie> call = networkService.getTopRatedMovies(1);

            networkState.postValue(NetworkState.LOADING);
            initialLoad.postValue(NetworkState.LOADING);

            try {
                boolean success = false;
                final Response<ResponseMovie> response = call.execute();
                if (response.isSuccessful()) {
                    final ResponseMovie responseMovie = response.body();
                    if (responseMovie != null) {
                        final List<Movie> movies = responseMovie.getMovies();
                        if (movies != null) {
                            success = true;
                            callback.onResult(movies, 0, 2);
                        }
                    }
                }
                networkState.postValue(success ? NetworkState.LOADED : NetworkState.error(UNKNOWN_ERROR));
            } catch (IOException e) {
                final String message = e.getMessage();
                NetworkState error = NetworkState.error((message == null) ? UNKNOWN_ERROR : message);
                networkState.postValue(error);
                initialLoad.postValue(error);
            }
        }

        @Override
        public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Movie> callback) {
            // no-op
        }

        @Override
        public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Movie> callback) {
            networkState.postValue(NetworkState.LOADING);
            networkService.getTopRatedMovies(params.key).enqueue(
                    new Callback<ResponseMovie>() {
                        @Override
                        public void onResponse(@NonNull Call<ResponseMovie> call, @NonNull Response<ResponseMovie> response) {
                            boolean success = false;
                            if (response.isSuccessful()) {
                                final ResponseMovie body = response.body();
                                if (body != null) {
                                    final List<Movie> movies = body.getMovies();
                                    if ((movies != null) && !movies.isEmpty()) {
                                        success = true;
                                        callback.onResult(movies, body.getPage() + 1);
                                    }
                                }
                                networkState.postValue(success ? NetworkState.LOADED : NetworkState.error(UNKNOWN_ERROR));
                            } else {
                                networkState.postValue(NetworkState.error("error code: " + response.code()));
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ResponseMovie> call, @NonNull Throwable t) {
                            final String message = t.getMessage();
                            networkState.postValue(NetworkState.error((message == null) ? UNKNOWN_ERROR : message));
                        }
                    }
            );
        }
    }
}