package com.projects.melih.popularmovies.common;

import android.content.Context;
import android.net.ConnectivityManager;
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

    @NonNull
    public static String getImagePathWithBackdrop(@Nullable String path) {
        if (path == null) {
            return "";
        }
        return BuildConfig.API_BASE_IMAGE_URL + "w780/" + path;
    }

    public static boolean isNetworkConnected(@NonNull Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (connectivityManager != null) && (connectivityManager.getActiveNetworkInfo() != null);
    }
}
