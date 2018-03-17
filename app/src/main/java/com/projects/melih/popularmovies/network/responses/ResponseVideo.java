package com.projects.melih.popularmovies.network.responses;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.projects.melih.popularmovies.model.Video;

import java.util.List;

/**
 * Created by Melih Gültekin on 13.03.2018
 */

@SuppressWarnings("unused")
public class ResponseVideo {
    @SerializedName("id")
    private long id;

    @Nullable
    @SerializedName("results")
    private List<Video> videos;

    public long getId() {
        return id;
    }

    @Nullable
    public List<Video> getVideos() {
        return videos;
    }
}