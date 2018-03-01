package com.projects.melih.popularmovies.ui;

import android.os.Bundle;

import com.projects.melih.popularmovies.R;
import com.projects.melih.popularmovies.ui.base.BaseActivity;
import com.projects.melih.popularmovies.ui.home.HomeFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, HomeFragment.newInstance())
                    .addToBackStack("")
                    .commit();
        }
    }
}
