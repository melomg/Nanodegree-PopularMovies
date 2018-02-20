package com.projects.melih.popularmovies.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.projects.melih.popularmovies.R;
import com.projects.melih.popularmovies.ui.movie.MovieListFragment;

public class MainActivity extends AppCompatActivity {
    private SharedViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        model = ViewModelProviders.of(this).get(SharedViewModel.class);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MovieListFragment.newInstance())
                    .commit();

            model.sortByPopular();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_by_popular:
                model.sortByPopular();
                return true;
            case R.id.action_sort_by_top_rated:
                model.sortByTopRated();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
