package com.projects.melih.popularmovies.common;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * Created by Melih Gültekin on 17.03.2018
 */

public class IntentUtils {
    private IntentUtils() {

    }

    @SuppressWarnings("WeakerAccess")
    public static boolean isIntentAvailable(@NonNull Context context, @NonNull Intent intent) {
        return (intent.resolveActivity(context.getPackageManager()) != null);
    }

    public static boolean openYoutube(Context context, String key) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
        if (isIntentAvailable(context, appIntent)) {
            context.startActivity(appIntent);
            return true;
        }

        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + key));
        if (isIntentAvailable(context, webIntent)) {
            context.startActivity(webIntent);
            return true;
        }

        return false;
    }
}
