package com.projects.melih.popularmovies.ui.movie;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.projects.melih.popularmovies.R;
import com.projects.melih.popularmovies.databinding.FragmentMovieListBinding;
import com.projects.melih.popularmovies.ui.SharedViewModel;
import com.projects.melih.popularmovies.ui.base.BaseFragment;

/**
 * Created by Melih Gültekin on 17.02.2018
 */

public class MovieListFragment extends BaseFragment {

    private FragmentMovieListBinding binding;

    public static MovieListFragment newInstance() {
        return new MovieListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_list, container, false);
        final SharedViewModel model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);

        final MovieListAdapter adapter = new MovieListAdapter(context, position -> {
            //TODO open MovieDetailFragment
        });
        final GridLayoutManager layoutManager = new GridLayoutManager(context, 2);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setHasFixedSize(false);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        binding.recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        float listItemWidth = context.getResources().getDimension(R.dimen.list_item_width);
                        int newSpanCount = (int) Math.floor(binding.recyclerView.getMeasuredWidth() / listItemWidth);
                        layoutManager.setSpanCount(newSpanCount);
                    }
                });

        model.getMovies().observe(this, movies -> {
            adapter.setMovies(movies);
            adapter.notifyDataSetChanged();
        });

        return binding.getRoot();
    }
}