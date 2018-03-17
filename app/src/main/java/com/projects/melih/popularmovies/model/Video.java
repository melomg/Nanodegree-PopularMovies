package com.projects.melih.popularmovies.model;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Melih Gültekin on 13.03.2018
 */

@SuppressWarnings("unused")
public class Video {
    @SerializedName("id")
    @Nullable
    private String id;

    @SerializedName("iso_639_1")
    @Nullable
    private String iso639;

    @SerializedName("iso_3166_1")
    @Nullable
    private String iso3166;

    @SerializedName("key")
    @Nullable
    private String key;

    @SerializedName("name")
    @Nullable
    private String name;

    @SerializedName("site")
    @Nullable
    private String site;

    @SerializedName("size")
    private int size;

    @SerializedName("type")
    @Nullable
    private String type;

    @Nullable
    public String getId() {
        return id;
    }

    public void setId(@Nullable String id) {
        this.id = id;
    }

    @Nullable
    public String getIso639() {
        return iso639;
    }

    public void setIso639(@Nullable String iso639) {
        this.iso639 = iso639;
    }

    @Nullable
    public String getIso3166() {
        return iso3166;
    }

    public void setIso3166(@Nullable String iso3166) {
        this.iso3166 = iso3166;
    }

    @Nullable
    public String getKey() {
        return key;
    }

    public void setKey(@Nullable String key) {
        this.key = key;
    }

    @Nullable
    public String getName() {
        return name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    @Nullable
    public String getSite() {
        return site;
    }

    public void setSite(@Nullable String site) {
        this.site = site;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Nullable
    public String getType() {
        return type;
    }

    public void setType(@Nullable String type) {
        this.type = type;
    }
}