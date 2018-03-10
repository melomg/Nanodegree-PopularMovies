package com.projects.melih.popularmovies.ui.base;

import android.arch.lifecycle.Lifecycle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

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
    public void addFragment(@NonNull BaseFragment newFragment) {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            Fragment currentFragment = fragmentManager.findFragmentById(R.id.container);
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }

            String tag = newFragment.getClass().getName();
            if (!fragmentManager.popBackStackImmediate(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE)) {
                transaction.add(R.id.container, newFragment, tag)
                        .show(newFragment)
                        .addToBackStack(tag)
                        .commit();
            }
        }
    }
}
