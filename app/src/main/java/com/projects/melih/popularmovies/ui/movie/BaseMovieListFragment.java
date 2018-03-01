package com.projects.melih.popularmovies.ui.movie;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.projects.melih.popularmovies.R;
import com.projects.melih.popularmovies.databinding.FragmentMovieListBinding;
import com.projects.melih.popularmovies.ui.base.BaseFragment;

/**
 * Created by Melih Gültekin on 1.03.2018
 */

abstract class BaseMovieListFragment extends BaseFragment {
    protected static final int DEFAULT_SPAN_COUNT = 2;

    protected FragmentMovieListBinding binding;
    protected MovieListAdapter adapter;

    @CallSuper
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_list, container, false);

        binding.swipeRefresh.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);

        adapter = new MovieListAdapter(context, (movie, imageView) -> navigationListener.addFragment(MovieDetailFragment.newInstance(movie), imageView));
        final GridLayoutManager layoutManager = new GridLayoutManager(context, DEFAULT_SPAN_COUNT);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setHasFixedSize(false);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        binding.recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        layoutManager.setSpanCount(getNewSpanCount());
                    }
                });

        ((GridLayoutManager) binding.recyclerView.getLayoutManager()).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adapter.getItemViewType(position) != MovieListAdapter.VIEW_TYPE_ITEM) {
                    return getNewSpanCount();
                }
                return 1;
            }
        });
        return binding.getRoot();
    }

    private int getNewSpanCount() {
        float listItemWidth = context.getResources().getDimension(R.dimen.list_item_width);
        return Math.max(DEFAULT_SPAN_COUNT, (int) Math.floor(binding.recyclerView.getMeasuredWidth() / listItemWidth));
    }
}