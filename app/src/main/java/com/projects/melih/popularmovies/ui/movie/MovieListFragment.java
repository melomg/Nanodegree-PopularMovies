package com.projects.melih.popularmovies.ui.movie;

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
import com.projects.melih.popularmovies.model.Movie;
import com.projects.melih.popularmovies.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

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

        List<Movie> movies = new ArrayList<>();

        MovieListAdapter adapter = new MovieListAdapter(context, position -> {
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

        //TODO remove mock data
        Movie movie1 = new Movie();
        movie1.setVoteCount(3507);
        movie1.setId(254128);
        movie1.setVideo(false);
        movie1.setVoteAverage(6);
        movie1.setTitle("San Andreas");
        movie1.setPopularity(257.204285);
        movie1.setPosterPath("/qey0tdcOp9kCDdEZuJ87yE3crSe.jpg");
        movie1.setOriginalLanguage("en");
        movie1.setOriginalTitle("San Andreas");
        movie1.setBackdropPath("/cUfGqafAVQkatQ7N4y08RNV3bgu.jpg");
        movie1.setAdult(false);
        movie1.setOverview("In the aftermath of a massive earthquake in California, a rescue-chopper pilot makes a dangerous journey across the state in order to rescue his estranged daughter.");
        movie1.setReleaseDate("2015-05-27");
        movies.add(movie1);

        Movie movie2 = new Movie();
        movie2.setVoteCount(3510);
        movie2.setId(254128);
        movie2.setVideo(false);
        movie2.setVoteAverage(6);
        movie2.setTitle("San");
        movie2.setPopularity(257.204285);
        movie2.setPosterPath("/qey0tdcOp9kCDdEZuJ87yE3crSe.jpg");
        movie2.setOriginalLanguage("en");
        movie2.setOriginalTitle("San");
        movie2.setBackdropPath("/cUfGqafAVQkatQ7N4y08RNV3bgu.jpg");
        movie2.setAdult(false);
        movie2.setOverview("In the aftermath of a massive earthquake in California, a rescue-chopper pilot makes a dangerous journey across the state in order to rescue his estranged daughter.");
        movie2.setReleaseDate("2017-05-27");
        movies.add(movie2);

        Movie movie3 = new Movie();
        movie3.setVoteCount(3510);
        movie3.setId(254128);
        movie3.setVideo(false);
        movie3.setVoteAverage(6);
        movie3.setTitle("San");
        movie3.setPopularity(257.204285);
        movie3.setPosterPath("/qey0tdcOp9kCDdEZuJ87yE3crSe.jpg");
        movie3.setOriginalLanguage("en");
        movie3.setOriginalTitle("Shining");
        movie3.setBackdropPath("/cUfGqafAVQkatQ7N4y08RNV3bgu.jpg");
        movie3.setAdult(false);
        movie3.setOverview("In the aftermath of a massive earthquake in California, a rescue-chopper pilot makes a dangerous journey across the state in order to rescue his estranged daughter.");
        movie3.setReleaseDate("2017-05-27");
        movies.add(movie3);

        adapter.setMovies(movies);
        adapter.notifyDataSetChanged();

        return binding.getRoot();
    }
}