package com.projects.melih.popularmovies.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.projects.melih.popularmovies.model.Movie;
import com.projects.melih.popularmovies.network.MovieAPI;
import com.projects.melih.popularmovies.network.responses.ResponseMovie;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Melih Gültekin on 18.02.2018
 */

public class SharedViewModel extends ViewModel {
    private MutableLiveData<List<Movie>> movies;
    private Call<ResponseMovie> callPopularMovies;
    private Call<ResponseMovie> callTopRatedMovies;

    public LiveData<List<Movie>> getMovies() {
        if (movies == null) {
            movies = new MutableLiveData<>();
        }
        return movies;
    }

    public void sortByPopular() {
        callPopularMovies = MovieAPI.getMovieService().getPopularMovies();
        callPopularMovies.enqueue(new Callback<ResponseMovie>() {
            @Override
            public void onResponse(@NonNull Call<ResponseMovie> call, @NonNull Response<ResponseMovie> response) {
                final ResponseMovie body = response.body();
                if (body != null) {
                    movies.setValue(body.getMovies());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseMovie> call, @NonNull Throwable t) {
                //TODO show error
            }
        });
    }

    public void sortByTopRated() {
        callTopRatedMovies = MovieAPI.getMovieService().getTopRatedMovies();
        callTopRatedMovies.enqueue(new Callback<ResponseMovie>() {
            @Override
            public void onResponse(@NonNull Call<ResponseMovie> call, @NonNull Response<ResponseMovie> response) {
                final ResponseMovie body = response.body();
                if (body != null) {
                    movies.setValue(body.getMovies());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseMovie> call, @NonNull Throwable t) {
                //TODO show error
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (callPopularMovies != null) {
            callPopularMovies.cancel();
        }
        if (callTopRatedMovies != null) {
            callTopRatedMovies.cancel();
        }
    }
}
