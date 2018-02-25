package com.projects.melih.popularmovies.common;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.projects.melih.popularmovies.BuildConfig;

/**
 * Created by Melih Gültekin on 20.02.2018
 */

public class Utils {
    private Utils() {
        // no-op
    }

    @NonNull
    public static String getImagePathWithPoster(@Nullable String path) {
        if (path == null) {
            return "";
        }
        return BuildConfig.API_BASE_IMAGE_URL + "w185/" + path;
    }
}
