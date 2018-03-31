package com.projects.melih.popularmovies.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ShareCompat;

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

    public static void share(@Nullable Activity activity, @NonNull String shareText, @NonNull String title) {
        if (activity != null) {
            final String mimeType = "text/plain";
            ShareCompat.IntentBuilder
                    .from(activity)
                    .setType(mimeType)
                    .setChooserTitle(title)
                    .setText(shareText)
                    .startChooser();
        }
    }
}