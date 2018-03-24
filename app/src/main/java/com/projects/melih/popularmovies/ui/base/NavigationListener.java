package com.projects.melih.popularmovies.ui.base;

import android.support.annotation.NonNull;

/**
 * Created by Melih Gültekin on 21.02.2018
 */

public interface NavigationListener {
    void onBackPressed();

    void addFragment(@NonNull BaseFragment newFragment);

    void addFragment(@NonNull BaseFragment newFragment, @BaseActivity.SlideAnimType int animType);
}
