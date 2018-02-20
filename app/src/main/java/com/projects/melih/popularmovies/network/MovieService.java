package com.projects.melih.popularmovies.network;

import com.projects.melih.popularmovies.network.responses.ResponseMovie;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Melih Gültekin on 18.02.2018
 */

public interface MovieService {
    @GET("movie/popular")
    Call<ResponseMovie> getPopularMovies();

    @GET("movie/top_rated")
    Call<ResponseMovie> getTopRatedMovies();
}