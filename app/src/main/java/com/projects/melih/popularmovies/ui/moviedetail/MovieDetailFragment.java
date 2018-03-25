package com.projects.melih.popularmovies.ui.moviedetail;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.projects.melih.popularmovies.R;
import com.projects.melih.popularmovies.common.CollectionUtils;
import com.projects.melih.popularmovies.common.IntentUtils;
import com.projects.melih.popularmovies.common.StringUtils;
import com.projects.melih.popularmovies.common.Utils;
import com.projects.melih.popularmovies.databinding.FragmentMovieDetailBinding;
import com.projects.melih.popularmovies.model.Movie;
import com.projects.melih.popularmovies.model.Review;
import com.projects.melih.popularmovies.model.Video;
import com.projects.melih.popularmovies.network.NetworkState;
import com.projects.melih.popularmovies.ui.base.BaseActivity;
import com.projects.melih.popularmovies.ui.base.BaseFragment;
import com.projects.melih.popularmovies.ui.reviews.ReviewsFragment;

import java.util.List;

import static com.projects.melih.popularmovies.common.IntentUtils.YOUTUBE_WATCH_LINK;

/**
 * Created by Melih Gültekin on 21.02.2018
 */

public class MovieDetailFragment extends BaseFragment implements View.OnClickListener {
    private static final String ARGUMENT_MOVIE = "ARGUMENT_MOVIE";
    private static final int TRIMMED_REVIEWS_COUNT = 3;
    private FragmentMovieDetailBinding binding;
    private MovieDetailViewModel model;
    private Movie movie;
    @Nullable
    private Video firstVideo;
    private VideosAdapter videosAdapter;
    private ShortReviewsAdapter reviewsAdapter;

    public static MovieDetailFragment newInstance(@NonNull Movie movie) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(ARGUMENT_MOVIE, movie);
        MovieDetailFragment fragment = new MovieDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_detail, container, false);
        model = ViewModelProviders.of(this).get(MovieDetailViewModel.class);

        model.getVideosLiveData().observe(this, videos -> {
            if (CollectionUtils.isNotEmpty(videos)) {
                if (firstVideo == null) {
                    firstVideo = videos.get(0);
                }
                binding.videos.setVisibility(View.VISIBLE);
            } else {
                binding.videos.setVisibility(View.GONE);
            }
            videosAdapter.setVideos(videos);
        });
        model.getReviewsLiveData().observe(this, reviews -> {
            if (CollectionUtils.isNotEmpty(reviews)) {
                binding.reviews.setVisibility(View.VISIBLE);
                if (reviews.size() > TRIMMED_REVIEWS_COUNT) {
                    List<Review> trimmedReviews = reviews.subList(0, 3);
                    reviewsAdapter.setReviews(trimmedReviews);
                } else {
                    reviewsAdapter.setReviews(reviews);
                }
            } else {
                binding.reviews.setVisibility(View.GONE);
            }
        });
        model.getNetworkStateLiveData().observe(this, networkState -> {
            if (networkState == NetworkState.NO_NETWORK) {
                showToast(R.string.network_error);
            } else if (networkState != null) {
                showToast(networkState.getErrorMessage());
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Bundle arguments = getArguments();
        if (arguments != null) {
            movie = arguments.getParcelable(ARGUMENT_MOVIE);
            if (movie != null) {
                model.setMovieId(movie.getId());
                binding.collapsingToolbar.setTitle(movie.getTitle());
                binding.releaseDate.setText(movie.getReleaseDate());
                binding.voteAverage.setText(String.valueOf(movie.getVoteAverage()));
                binding.overview.setText(movie.getOverview());

                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .dontAnimate()
                        .placeholder(R.drawable.ic_movie_placeholder_4_3)
                        .error(R.drawable.ic_movie_placeholder_4_3);
                Glide.with(context)
                        .asBitmap()
                        .apply(options)
                        .load(Utils.getImagePathWithBackdrop(movie.getBackdropPath()))
                        .thumbnail(0.1f)
                        .into(binding.backdrop.image);

                model.setMovieId(movie.getId());
            }
        }

        videosAdapter = new VideosAdapter(context, video -> {
            if (!IntentUtils.openYoutube(context, video.getKey())) {
                showToast(R.string.unknown_error);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerViewVideos.setLayoutManager(layoutManager);
        binding.recyclerViewVideos.addItemDecoration(new GridSpacingItemDecoration(Utils.dpToPx(context, 6)));
        binding.recyclerViewVideos.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewVideos.setAdapter(videosAdapter);

        reviewsAdapter = new ShortReviewsAdapter(context);
        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.recyclerViewReviews.setLayoutManager(layoutManager);
        binding.recyclerViewReviews.setAdapter(reviewsAdapter);

        binding.toolbarMenu.setOnClickListener(this);
        binding.reviewsSeeMore.setOnClickListener(this);
        binding.share.setOnClickListener(this);
        binding.favoriteArea.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //TODO
        switch (v.getId()) {
            case R.id.toolbarMenu:
                navigationListener.onBackPressed();
                break;
            case R.id.reviews_see_more:
                navigationListener.addFragment(ReviewsFragment.newInstance(model.getMovieId()), BaseActivity.BOTTOM_TO_TOP);
                break;
            case R.id.share:
                String videoUrl = (firstVideo == null) ? "" : YOUTUBE_WATCH_LINK + firstVideo.getKey();
                IntentUtils.share(getActivity(), StringUtils.getAppendedText(movie.getTitle(), movie.getReleaseDate(), videoUrl), context.getString(R.string.share));
                break;
            case R.id.favorite_area:
                binding.favoriteCheck.setChecked(!binding.favoriteCheck.isChecked());
                break;
        }
    }

    private static class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private final int space;

        GridSpacingItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            outRect.left = (position == 0) ? space : 0;
            outRect.right = space;
            outRect.top = space;
            outRect.bottom = space;
        }
    }
}