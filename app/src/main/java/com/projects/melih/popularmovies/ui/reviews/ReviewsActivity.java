package com.projects.melih.popularmovies.ui.reviews;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.projects.melih.popularmovies.R;
import com.projects.melih.popularmovies.common.Utils;
import com.projects.melih.popularmovies.databinding.ActivityReviewsBinding;
import com.projects.melih.popularmovies.network.NetworkState;
import com.projects.melih.popularmovies.ui.base.BaseActivity;
import com.projects.melih.popularmovies.ui.base.EndlessRecyclerViewScrollListener;

public class ReviewsActivity extends BaseActivity implements View.OnClickListener {
    private static final String KEY_MOVIE_ID = "KEY_MOVIE_ID";
    private ActivityReviewsBinding binding;
    private ReviewsViewModel model;
    private ReviewsAdapter reviewsAdapter;
    private EndlessRecyclerViewScrollListener scrollListener;

    public static Intent newIntent(@NonNull Context context, long movieId) {
        Intent intent = new Intent(context, ReviewsActivity.class);
        intent.putExtra(ReviewsActivity.KEY_MOVIE_ID, movieId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reviews);
        model = ViewModelProviders.of(this).get(ReviewsViewModel.class);

        binding.swipeRefresh.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);

        final Intent intent = getIntent();
        long movieId = intent.getLongExtra(KEY_MOVIE_ID, 0);

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

        reviewsAdapter = new ReviewsAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
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
        Utils.await(v);
        switch (v.getId()) {
            case R.id.toolbarMenu:
                finish();
                break;
        }
    }
}