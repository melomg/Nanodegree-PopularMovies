package com.projects.melih.popularmovies.ui.movielist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.projects.melih.popularmovies.R;
import com.projects.melih.popularmovies.common.Utils;
import com.projects.melih.popularmovies.model.Movie;

public class MovieViewHolder extends RecyclerView.ViewHolder {
    private final Context context;
    private final boolean isFromFavorites;
    private final MovieListener movieItemListener;
    private final ImageView ivMovie;
    private final View favoriteArea;
    private final CheckBox checkFavoriteArea;

    public MovieViewHolder(@NonNull View itemView, @NonNull Context context, boolean isFromFavorites, @NonNull MovieListener movieItemListener) {
        super(itemView);
        this.context = context;
        this.isFromFavorites = isFromFavorites;
        this.movieItemListener = movieItemListener;
        ivMovie = itemView.findViewById(R.id.image);
        favoriteArea = itemView.findViewById(R.id.favorite_area);
        checkFavoriteArea = itemView.findViewById(R.id.favorite_check);
    }

    public void bindTo(@Nullable final Movie movie) {
        if (movie != null) {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .placeholder(R.drawable.ic_movie_placeholder_3_4)
                    .error(R.drawable.ic_movie_placeholder_3_4);
            Glide.with(context)
                    .asBitmap()
                    .apply(options)
                    .load(Utils.getImagePathWithPoster(movie.getPosterPath()))
                    .thumbnail(0.1f)
                    .into(ivMovie);

            if (isFromFavorites) {
                checkFavoriteArea.setChecked(true);
                favoriteArea.setVisibility(View.VISIBLE);
                favoriteArea.setOnClickListener(v -> movieItemListener.onFavoriteDelete(movie));
            }
            itemView.setOnClickListener(v -> movieItemListener.onItemClick(movie));
        }
    }
}