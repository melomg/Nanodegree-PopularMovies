package com.projects.melih.popularmovies.network;

import com.projects.melih.popularmovies.network.responses.ResponseMovie;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Melih Gültekin on 18.02.2018
 */

public interface MovieService {
    @GET("movie/popular")
    Call<ResponseMovie> getPopularMovies(@Query("page") int page);

    @GET("movie/top_rated")
    Call<ResponseMovie> getTopRatedMovies(@Query("page") int page);
}