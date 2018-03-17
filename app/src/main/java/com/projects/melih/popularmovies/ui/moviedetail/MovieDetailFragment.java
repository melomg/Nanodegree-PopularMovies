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
import com.projects.melih.popularmovies.common.Utils;
import com.projects.melih.popularmovies.databinding.FragmentMovieDetailBinding;
import com.projects.melih.popularmovies.model.Movie;
import com.projects.melih.popularmovies.network.NetworkState;
import com.projects.melih.popularmovies.ui.base.BaseFragment;

/**
 * Created by Melih Gültekin on 21.02.2018
 */

public class MovieDetailFragment extends BaseFragment implements View.OnClickListener {
    private static final String ARGUMENT_MOVIE = "ARGUMENT_MOVIE";
    private FragmentMovieDetailBinding binding;
    private MovieDetailViewModel model;
    private VideosAdapter adapter;

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
            binding.videos.setVisibility((CollectionUtils.isNotEmpty(videos)) ? View.VISIBLE : View.GONE);
            adapter.setVideos(videos);
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
            Movie movie = arguments.getParcelable(ARGUMENT_MOVIE);
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

        adapter = new VideosAdapter(context, video -> {
            if (!IntentUtils.openYoutube(context, video.getKey())) {
                showToast(R.string.unknown_error);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerViewVideos.setLayoutManager(layoutManager);
        binding.recyclerViewVideos.addItemDecoration(new GridSpacingItemDecoration(Utils.dpToPx(context, 6)));
        binding.recyclerViewVideos.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewVideos.setAdapter(adapter);

        binding.toolbarMenu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        navigationListener.onBackPressed();
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