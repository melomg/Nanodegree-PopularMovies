package com.projects.melih.popularmovies.ui.movie;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.projects.melih.popularmovies.R;
import com.projects.melih.popularmovies.model.Movie;
import com.projects.melih.popularmovies.ui.base.ItemClickListener;

import java.util.List;

/**
 * Created by Melih Gültekin on 18.02.2018
 */

class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> {
    private final Context context;
    private final ItemClickListener itemClickListener;
    private List<Movie> movies;

    MovieListAdapter(@NonNull Context context, @NonNull ItemClickListener itemClickListener) {
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MovieViewHolder(context, LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movie, parent, false), itemClickListener);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bindTo(movies.get(position));
    }

    @Override
    public int getItemCount() {
        return (movies == null) ? 0 : movies.size();
    }

    void setMovies(@Nullable List<Movie> movies) {
        this.movies = movies;
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final Context context;
        private final ItemClickListener itemClickListener;
        private final ImageView ivMovie;
        private final TextView tvTitle;

        MovieViewHolder(Context context, View itemView, ItemClickListener itemClickListener) {
            super(itemView);
            this.context = context;
            this.itemClickListener = itemClickListener;
            ivMovie = itemView.findViewById(R.id.image);
            tvTitle = itemView.findViewById(R.id.title);
        }

        void bindTo(@NonNull Movie movie) {
            tvTitle.setText(movie.getTitle());
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .dontAnimate()
                    .placeholder(R.drawable.ic_movie_placeholder)
                    .error(R.drawable.ic_movie_placeholder);
            Glide.with(context)
                    .asBitmap()
                    .apply(options)
                    .load("http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg")
                    .thumbnail(0.1f)
                    .into(ivMovie);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                itemClickListener.onItemClick(position);
            }
        }
    }
}