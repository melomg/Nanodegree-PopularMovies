package com.projects.melih.popularmovies.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.View;

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

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isNetworkConnected(@NonNull Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (connectivityManager != null) && (connectivityManager.getActiveNetworkInfo() != null);
    }

    public static String getVideoImageYoutubeLink(@Nullable String key) {
        if (key == null) {
            return "";
        }
        return "https://img.youtube.com/vi/" + key.trim() + "/0.jpg";
    }

    public static int dpToPx(@NonNull Context context, int dp) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics()));
    }

    public static void await(@NonNull final View view) {
        view.setEnabled(false);
        view.postDelayed(() -> view.setEnabled(true), view.getContext().getResources().getInteger(android.R.integer.config_mediumAnimTime));
    }
}