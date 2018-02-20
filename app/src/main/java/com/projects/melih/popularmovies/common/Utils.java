package com.projects.melih.popularmovies.common;

import com.projects.melih.popularmovies.BuildConfig;

/**
 * Created by Melih Gültekin on 20.02.2018
 */

public class Utils {
    private Utils() {
        // no-op
    }

    public static String getImagePathWithPoster(String path) {
        StringBuilder builder = new StringBuilder();
        builder.append(BuildConfig.API_BASE_IMAGE_URL)
                .append("w185/")
                .append(path);
        return builder.toString();
    }
}
