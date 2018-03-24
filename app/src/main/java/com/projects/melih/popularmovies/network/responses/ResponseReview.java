package com.projects.melih.popularmovies.network.responses;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.projects.melih.popularmovies.model.Review;

import java.util.ArrayList;

/**
 * Created by Melih Gültekin on 17.03.2018
 */

@SuppressWarnings("unused")
public class ResponseReview {
    @SerializedName("id")
    private long id;

    @SerializedName("page")
    private int page;

    @SerializedName("total_results")
    private long totalResults;

    @SerializedName("total_pages")
    private int totalPages;

    @Nullable
    @SerializedName("results")
    private ArrayList<Review> reviews;

    public long getId() {
        return id;
    }

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
    public ArrayList<Review> getReviews() {
        return reviews;
    }
}