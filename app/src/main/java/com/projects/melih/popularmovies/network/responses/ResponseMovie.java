package com.projects.melih.popularmovies.network.responses;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.projects.melih.popularmovies.model.Movie;

import java.util.List;

/**
 * Created by Melih Gültekin on 20.02.2018
 */

public class ResponseMovie {
    @SerializedName("page")
    private int page;

    @SerializedName("total_results")
    private long totalResults;

    @SerializedName("total_pages")
    private int totalPages;

    @Nullable
    @SerializedName("results")
    private List<Movie> movies;

    public int getPage() {
        return page;
    }

    public long getTotalResults() {
        return totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    @Nullable
    public List<Movie> getMovies() {
        return movies;
    }
}
