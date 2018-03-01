package com.projects.melih.popularmovies.ui.base;

import android.view.View;

/**
 * Created by Melih Gültekin on 18.02.2018
 */

public interface ItemClickListener<T> {
    void onItemClick(T object, View view);
}