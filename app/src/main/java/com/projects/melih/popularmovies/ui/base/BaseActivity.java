package com.projects.melih.popularmovies.ui.base;

import android.arch.lifecycle.Lifecycle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.projects.melih.popularmovies.R;

/**
 * Created by Melih Gültekin on 21.02.2018
 */

public abstract class BaseActivity extends AppCompatActivity implements NavigationListener {

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 1) {
            fragmentManager.popBackStackImmediate();
        } else {
            super.finish();
        }
    }

    @Override
    public void addFragment(@NonNull BaseFragment newFragment, @Nullable View sharedView) {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment currentFragment = fragmentManager.findFragmentById(R.id.container);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.hide(currentFragment);

            if (sharedView != null) {
                transaction.addSharedElement(sharedView, ViewCompat.getTransitionName(sharedView));
            }

            String tag = newFragment.getClass().getName();
            if (!fragmentManager.popBackStackImmediate(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE)) {
                transaction.add(R.id.container, newFragment, tag)
                        .show(newFragment)
                        .addToBackStack(tag)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        }
    }
}
