package com.projects.melih.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Melih Gültekin on 13.03.2018
 */

@SuppressWarnings("unused")
public class Review implements Parcelable {
    @SerializedName("author")
    @Nullable
    private String author;

    @SerializedName("content")
    @Nullable
    private String content;

    @SerializedName("id")
    @Nullable
    private String id;

    @SerializedName("url")
    @Nullable
    private String url;

    @Nullable
    public String getAuthor() {
        return author;
    }

    @Nullable
    public String getContent() {
        return content;
    }

    @Nullable
    public String getId() {
        return id;
    }

    @Nullable
    public String getUrl() {
        return url;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.author);
        dest.writeString(this.content);
        dest.writeString(this.id);
        dest.writeString(this.url);
    }

    private Review() {
    }

    private Review(Parcel in) {
        this.author = in.readString();
        this.content = in.readString();
        this.id = in.readString();
        this.url = in.readString();
    }

    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel source) {
            return new Review(source);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public static final DiffUtil.ItemCallback<Review> DIFF_CALLBACK = new DiffUtil.ItemCallback<Review>() {
        @Override
        public boolean areItemsTheSame(@NonNull Review oldReview, @NonNull Review newReview) {
            return TextUtils.equals(oldReview.getId(), newReview.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Review oldReview, @NonNull Review newReview) {
            return oldReview.equals(newReview);
        }
    };
}