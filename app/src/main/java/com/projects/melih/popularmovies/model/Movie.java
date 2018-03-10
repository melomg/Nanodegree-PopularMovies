package com.projects.melih.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.recyclerview.extensions.DiffCallback;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Melih Gültekin on 17.02.2018
 */

@SuppressWarnings("unused")
public class Movie implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isAdult ? (byte) 1 : (byte) 0);
        dest.writeString(this.backdropPath);
        dest.writeList(this.genreIds);
        dest.writeLong(this.id);
        dest.writeString(this.originalLanguage);
        dest.writeString(this.originalTitle);
        dest.writeString(this.overview);
        dest.writeDouble(this.popularity);
        dest.writeString(this.posterPath);
        dest.writeString(this.releaseDate);
        dest.writeString(this.title);
        dest.writeByte(this.isVideo ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.voteAverage);
        dest.writeLong(this.voteCount);
    }

    public Movie() {
        //no-op
    }

    private Movie(Parcel in) {
        this.isAdult = in.readByte() != 0;
        this.backdropPath = in.readString();
        this.genreIds = new ArrayList<>();
        in.readList(this.genreIds, Long.class.getClassLoader());
        this.id = in.readLong();
        this.originalLanguage = in.readString();
        this.originalTitle = in.readString();
        this.overview = in.readString();
        this.popularity = in.readDouble();
        this.posterPath = in.readString();
        this.releaseDate = in.readString();
        this.title = in.readString();
        this.isVideo = in.readByte() != 0;
        this.voteAverage = in.readDouble();
        this.voteCount = in.readLong();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public static DiffCallback<Movie> DIFF_CALLBACK = new DiffCallback<Movie>() {
        @Override
        public boolean areItemsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
            return oldItem.equals(newItem);
        }
    };
}
