package com.projects.melih.popularmovies.ui.base;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by Melih Gültekin on 21.02.2018
 */

public interface NavigationListener {
    void onBackPressed();

    void addFragment(@NonNull BaseFragment newFragment, @Nullable View sharedView);
}
