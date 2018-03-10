package com.projects.melih.popularmovies.ui.moviedetail;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.projects.melih.popularmovies.R;
import com.projects.melih.popularmovies.common.Utils;
import com.projects.melih.popularmovies.databinding.FragmentMovieDetailBinding;
import com.projects.melih.popularmovies.model.Movie;
import com.projects.melih.popularmovies.ui.base.BaseFragment;

/**
 * Created by Melih Gültekin on 21.02.2018
 */

public class MovieDetailFragment extends BaseFragment implements View.OnClickListener {
    private static final String ARGUMENT_MOVIE = "ARGUMENT_MOVIE";
    private FragmentMovieDetailBinding binding;

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
        final Bundle arguments = getArguments();
        if (arguments != null) {
            Movie movie = arguments.getParcelable(ARGUMENT_MOVIE);
            binding.collapsingToolbar.setTitle(movie.getTitle());
            binding.releaseDate.setText(movie.getReleaseDate());
            binding.voteAverage.setText(String.valueOf(movie.getVoteAverage()));
            binding.overview.setText(movie.getOverview());

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .dontAnimate()
                    .error(R.drawable.ic_movie_placeholder);
            Glide.with(context)
                    .asBitmap()
                    .apply(options)
                    .load(Utils.getImagePathWithBackdrop(movie.getBackdropPath()))
                    .thumbnail(0.1f)
                    .into(binding.backdrop.image);
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.toolbarMenu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        navigationListener.onBackPressed();
    }
}