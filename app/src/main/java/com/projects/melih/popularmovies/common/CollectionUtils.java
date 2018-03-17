package com.projects.melih.popularmovies.common;

import com.projects.melih.popularmovies.model.Video;

import java.util.Collection;
import java.util.List;

/**
 * Created by Melih Gültekin on 10.03.2018
 */

public class CollectionUtils {

    private CollectionUtils() {
        //no-op
    }

    public static boolean isNotEmpty(Collection<?> list) {
        return ((list != null) && !list.isEmpty());
    }

    public static int size(Collection<?> list) {
        return (list == null) ? 0 : list.size();
    }
}
