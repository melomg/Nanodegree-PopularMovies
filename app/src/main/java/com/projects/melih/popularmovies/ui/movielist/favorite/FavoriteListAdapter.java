package com.projects.melih.popularmovies.ui.movielist.favorite;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.projects.melih.popularmovies.R;
import com.projects.melih.popularmovies.model.Movie;
import com.projects.melih.popularmovies.repository.local.FavoritesContract;
import com.projects.melih.popularmovies.ui.movielist.MovieListener;
import com.projects.melih.popularmovies.ui.movielist.MovieViewHolder;

/**
 * Created by Melih Gültekin on 29.03.2018
 */

class FavoriteListAdapter extends RecyclerView.Adapter<MovieViewHolder> {
    private final Context context;
    private final MovieListener movieItemListener;
    private Cursor cursor;

    FavoriteListAdapter(@NonNull Context context, @NonNull MovieListener movieItemListener) {
        this.context = context;
        this.movieItemListener = movieItemListener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new MovieViewHolder(inflater.inflate(R.layout.item_movie_list, parent, false), context, true, movieItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        if (cursor.moveToPosition(position)) {
            Movie movie = getMovieFromCursor(cursor);
            holder.bindTo(movie);
        }
    }

    @Override
    public int getItemCount() {
        return (null == cursor) ? 0 : cursor.getCount();
    }

    void swapCursor(@NonNull Cursor newCursor) {
        cursor = newCursor;
        notifyDataSetChanged();
    }

    @NonNull
    private Movie getMovieFromCursor(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(FavoritesContract.FavoriteEntry.COLUMN_MOVIE_ID);
        int titleIndex = cursor.getColumnIndex(FavoritesContract.FavoriteEntry.COLUMN_MOVIE_TITLE);
        int synopsisIndex = cursor.getColumnIndex(FavoritesContract.FavoriteEntry.COLUMN_MOVIE_SYNOPSIS);
        int releaseDateIndex = cursor.getColumnIndex(FavoritesContract.FavoriteEntry.COLUMN_MOVIE_RELEASE_DATE);
        int averageRateIndex = cursor.getColumnIndex(FavoritesContract.FavoriteEntry.COLUMN_MOVIE_AVERAGE_RATE);
        int posterPathIndex = cursor.getColumnIndex(FavoritesContract.FavoriteEntry.COLUMN_MOVIE_POSTER_PATH);
        int backdropPathIndex = cursor.getColumnIndex(FavoritesContract.FavoriteEntry.COLUMN_MOVIE_BACKDROP_PATH);
        Movie movie = new Movie();
        movie.setId(cursor.getLong(idIndex));
        movie.setTitle(cursor.getString(titleIndex));
        movie.setOverview(cursor.getString(synopsisIndex));
        movie.setReleaseDate(cursor.getString(releaseDateIndex));
        movie.setVoteAverage(cursor.getDouble(averageRateIndex));
        movie.setPosterPath(cursor.getString(posterPathIndex));
        movie.setBackdropPath(cursor.getString(backdropPathIndex));
        return movie;
    }
}
