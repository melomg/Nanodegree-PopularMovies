package com.projects.melih.popularmovies.common;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * Created by Melih Gültekin on 17.03.2018
 */

public class IntentUtils {
    public static final String YOUTUBE_WATCH_LINK = "http://www.youtube.com/watch?v=";

    private IntentUtils() {
        //no-op
    }

    @SuppressWarnings("WeakerAccess")
    public static boolean isIntentAvailable(@NonNull Context context, @NonNull Intent intent) {
        return (intent.resolveActivity(context.getPackageManager()) != null);
    }

    public static boolean openYoutube(@NonNull Context context, String key) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
        if (isIntentAvailable(context, appIntent)) {
            context.startActivity(appIntent);
            return true;
        }

        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_WATCH_LINK + key));
        if (isIntentAvailable(context, webIntent)) {
            context.startActivity(webIntent);
            return true;
        }

        return false;
    }

    public static void share(@NonNull Context context, @NonNull String shareText, @NonNull String title) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        context.startActivity(Intent.createChooser(shareIntent, title));
    }
}