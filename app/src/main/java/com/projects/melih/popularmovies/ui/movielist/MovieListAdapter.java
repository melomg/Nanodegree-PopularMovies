package com.projects.melih.popularmovies.ui.movielist;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.projects.melih.popularmovies.R;
import com.projects.melih.popularmovies.common.Utils;
import com.projects.melih.popularmovies.model.Movie;
import com.projects.melih.popularmovies.repository.NetworkState;
import com.projects.melih.popularmovies.ui.base.ItemClickListener;

/**
 * Created by Melih Gültekin on 18.02.2018
 */

class MovieListAdapter extends PagedListAdapter<Movie, RecyclerView.ViewHolder> {
    static final int VIEW_TYPE_ITEM = 1;
    @SuppressWarnings("WeakerAccess")
    static final int VIEW_TYPE_FOOTER = 2;
    private static final int FOOTER_ITEM_COUNT = 1;

    private final Context context;
    private final ItemClickListener<Movie> itemClickListener;
    private NetworkState networkState;

    MovieListAdapter(@NonNull Context context, @NonNull ItemClickListener<Movie> itemClickListener) {
        super(Movie.DIFF_CALLBACK);
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_ITEM) {
            return new MovieViewHolder(inflater.inflate(R.layout.item_movie_list, parent, false), context, itemClickListener);
        } else {
            return new NetworkStateViewHolder(inflater.inflate(R.layout.item_network_state, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_ITEM) {
            ((MovieViewHolder) holder).bindTo(getItem(position));
        } else {
            ((NetworkStateViewHolder) holder).bindTo(networkState);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (hasExtraRow() && (position == (getItemCount() - FOOTER_ITEM_COUNT))) {
            return VIEW_TYPE_FOOTER;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }

    void setNetworkState(@Nullable NetworkState newNetworkState) {
        NetworkState previousState = this.networkState;
        boolean previousExtraRow = hasExtraRow();
        this.networkState = newNetworkState;
        boolean newExtraRow = hasExtraRow();
        final int itemCount = getItemCount();
        if (previousExtraRow != newExtraRow) {
            if (previousExtraRow) {
                notifyItemRemoved(itemCount);
            } else {
                notifyItemInserted(itemCount);
            }
        } else if (newExtraRow && (previousState != newNetworkState)) {
            notifyItemChanged(itemCount - FOOTER_ITEM_COUNT);
        }
    }

    private boolean hasExtraRow() {
        return (networkState != null) && (networkState != NetworkState.LOADED);
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
                        .placeholder(R.drawable.ic_movie_placeholder)
                        .error(R.drawable.ic_movie_placeholder);
                Glide.with(context)
                        .asBitmap()
                        .apply(options)
                        .load(Utils.getImagePathWithPoster(movie.getPosterPath()))
                        .thumbnail(0.1f)
                        .into(ivMovie);

                ViewCompat.setTransitionName(ivMovie, movie.getPosterPath());

                itemView.setOnClickListener(v -> itemClickListener.onItemClick(movie));
            }
        }
    }

    static class NetworkStateViewHolder extends RecyclerView.ViewHolder {
        private final ProgressBar progressBar;
        private final TextView tvErrorMessage;

        NetworkStateViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progress_bar);
            tvErrorMessage = itemView.findViewById(R.id.error_message);
        }

        void bindTo(@Nullable NetworkState networkState) {
            if (networkState != null) {
                progressBar.setVisibility((networkState == NetworkState.LOADING) ? View.VISIBLE : View.GONE);

                final String errorMessage = networkState.getErrorMessage();
                tvErrorMessage.setVisibility((errorMessage != null) ? View.VISIBLE : View.GONE);
                tvErrorMessage.setText(errorMessage);
            }
        }
    }
}