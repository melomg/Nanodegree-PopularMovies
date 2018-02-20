package com.projects.melih.popularmovies.model;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Melih Gültekin on 17.02.2018
 */

public class Movie {
    @SerializedName("adult")
    private boolean isAdult;

    @Nullable
    @SerializedName("backdrop_path")
    private String backdropPath;

    @Nullable
    @SerializedName("genre_ids")
    private List<Long> genreIds;

    @SerializedName("id")
    private long id;

    @Nullable
    @SerializedName("original_language")
    private String originalLanguage;

    @Nullable
    @SerializedName("original_title")
    private String originalTitle;

    @Nullable
    @SerializedName("overview")
    private String overview;

    @SerializedName("popularity")
    private double popularity;

    @Nullable
    @SerializedName("poster_path")
    private String posterPath;

    @Nullable
    @SerializedName("release_date")
    private String releaseDate;

    @Nullable
    @SerializedName("title")
    private String title;

    @SerializedName("video")
    private boolean isVideo;

    @SerializedName("vote_average")
    private double voteAverage;

    @SerializedName("vote_count")
    private long voteCount;

    public boolean isAdult() {
        return isAdult;
    }

    public void setAdult(boolean adult) {
        isAdult = adult;
    }

    @Nullable
    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(@Nullable String backdropPath) {
        this.backdropPath = backdropPath;
    }

    @Nullable
    public List<Long> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(@Nullable List<Long> genreIds) {
        this.genreIds = genreIds;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Nullable
    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(@Nullable String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    @Nullable
    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(@Nullable String originalTitle) {
        this.originalTitle = originalTitle;
    }

    @Nullable
    public String getOverview() {
        return overview;
    }

    public void setOverview(@Nullable String overview) {
        this.overview = overview;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    @Nullable
    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(@Nullable String posterPath) {
        this.posterPath = posterPath;
    }

    @Nullable
    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(@Nullable String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    public void setTitle(@Nullable String title) {
        this.title = title;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public long getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(long voteCount) {
        this.voteCount = voteCount;
    }
}
