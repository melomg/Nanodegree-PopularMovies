package com.projects.melih.popularmovies.common;

import android.text.TextUtils;

/**
 * Created by Melih Gültekin on 17.03.2018
 */

public class StringUtils {
    private StringUtils() {
        //no-op
    }

    public static String getAppendedText(String... strings) {
        if (strings != null) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String string : strings) {
                if (!TextUtils.isEmpty(string)) {
                    if (stringBuilder.length() > 0) {
                        stringBuilder.append(" ");
                    }
                    stringBuilder.append(string);
                }
            }
            return stringBuilder.toString();
        } else {
            return "";
        }
    }
}
