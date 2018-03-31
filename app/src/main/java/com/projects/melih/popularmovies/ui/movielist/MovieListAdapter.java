package com.projects.melih.popularmovies.ui.movielist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.recyclerview.extensions.AsyncListDiffer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.projects.melih.popularmovies.R;
import com.projects.melih.popularmovies.model.Movie;

import java.util.List;

/**
 * Created by Melih Gültekin on 18.02.2018
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieViewHolder> {

    private final AsyncListDiffer<Movie> differ = new AsyncListDiffer<>(this, Movie.DIFF_CALLBACK);
    private final Context context;
    private final MovieListener movieItemListener;

    MovieListAdapter(@NonNull Context context, @NonNull MovieListener movieItemListener) {
        this.context = context;
        this.movieItemListener = movieItemListener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new MovieViewHolder(inflater.inflate(R.layout.item_movie_list, parent, false), context, false, movieItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.bindTo(differ.getCurrentList().get(position));
    }

    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }

    public void submitMovies(@Nullable List<Movie> newMovies) {
        differ.submitList(newMovies);
    }
}