package com.projects.melih.popularmovies.ui.movielist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.recyclerview.extensions.AsyncListDiffer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.projects.melih.popularmovies.R;
import com.projects.melih.popularmovies.common.Utils;
import com.projects.melih.popularmovies.model.Movie;
import com.projects.melih.popularmovies.ui.base.ItemClickListener;

import java.util.List;

/**
 * Created by Melih Gültekin on 18.02.2018
 */

class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> {

    private final AsyncListDiffer<Movie> differ = new AsyncListDiffer<>(this, Movie.DIFF_CALLBACK);
    private final Context context;
    private final ItemClickListener<Movie> itemClickListener;

    MovieListAdapter(@NonNull Context context, @NonNull ItemClickListener<Movie> itemClickListener) {
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new MovieViewHolder(inflater.inflate(R.layout.item_movie_list, parent, false), context, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.bindTo(differ.getCurrentList().get(position));
    }

    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }

    void submitMovies(@Nullable List<Movie> newMovies) {
        differ.submitList(newMovies);
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        private final Context context;
        private final ItemClickListener<Movie> itemClickListener;
        private final ImageView ivMovie;

        MovieViewHolder(View itemView, Context context, ItemClickListener<Movie> itemClickListener) {
            super(itemView);
            this.context = context;
            this.itemClickListener = itemClickListener;
            ivMovie = itemView.findViewById(R.id.image);
        }

        void bindTo(@Nullable final Movie movie) {
            if (movie != null) {
                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .dontAnimate()
                        .placeholder(R.drawable.ic_movie_placeholder_3_4)
                        .error(R.drawable.ic_movie_placeholder_3_4);
                Glide.with(context)
                        .asBitmap()
                        .apply(options)
                        .load(Utils.getImagePathWithPoster(movie.getPosterPath()))
                        .thumbnail(0.1f)
                        .into(ivMovie);

                itemView.setOnClickListener(v -> itemClickListener.onItemClick(movie));
            }
        }
    }
}