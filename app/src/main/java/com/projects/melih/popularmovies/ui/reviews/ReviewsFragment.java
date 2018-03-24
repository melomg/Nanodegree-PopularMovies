package com.projects.melih.popularmovies.ui.reviews;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projects.melih.popularmovies.R;
import com.projects.melih.popularmovies.databinding.FragmentReviewsBinding;
import com.projects.melih.popularmovies.network.NetworkState;
import com.projects.melih.popularmovies.ui.base.BaseFragment;
import com.projects.melih.popularmovies.ui.base.EndlessRecyclerViewScrollListener;

/**
 * Created by Melih Gültekin on 18.03.2018
 */

public class ReviewsFragment extends BaseFragment implements View.OnClickListener {

    private static final String ARGUMENT_MOVIE_ID = "ARGUMENT_MOVIE_ID";
    private FragmentReviewsBinding binding;
    private ReviewsViewModel model;
    private ReviewsAdapter reviewsAdapter;
    private EndlessRecyclerViewScrollListener scrollListener;

    public static ReviewsFragment newInstance(long movieId) {
        Bundle arguments = new Bundle();
        arguments.putLong(ARGUMENT_MOVIE_ID, movieId);
        ReviewsFragment fragment = new ReviewsFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reviews, container, false);
        model = ViewModelProviders.of(this).get(ReviewsViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.swipeRefresh.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);

        final Bundle arguments = getArguments();
        if (arguments != null) {
            long movieId = arguments.getLong(ARGUMENT_MOVIE_ID);
            model.setMovieId(movieId);
            model.loadMoreReviews(false);

            model.getPagedList().observe(this, allReviews -> {
                if (allReviews != null) {
                    reviewsAdapter.submitReviews(allReviews);
                }
            });
            model.getNetworkState().observe(this, networkState -> {
                if (networkState == NetworkState.NO_NETWORK) {
                    showToast(R.string.network_error);
                } else if (networkState != null) {
                    showToast(networkState.getErrorMessage());
                }
            });
            model.getRefreshState().observe(this, networkState -> binding.swipeRefresh.setRefreshing(networkState == NetworkState.LOADING));
        }

        reviewsAdapter = new ReviewsAdapter(context, review -> {
            //TODO open review.getUrl() link
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setHasFixedSize(false);
        binding.recyclerView.setAdapter(reviewsAdapter);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView recyclerView) {
                model.loadMoreReviews(false);
            }
        };

        binding.swipeRefresh.setOnRefreshListener(() -> {
            reviewsAdapter.submitReviews(null);
            scrollListener.resetState();
            model.loadMoreReviews(true);
        });

        binding.recyclerView.addOnScrollListener(scrollListener);
        binding.toolbarMenu.setOnClickListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        binding.recyclerView.removeOnScrollListener(scrollListener);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbarMenu:
                navigationListener.onBackPressed();
                break;
        }
    }
}