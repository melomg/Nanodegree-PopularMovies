package com.projects.melih.popularmovies.ui.movielist.favorite;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.projects.melih.popularmovies.model.Movie;
import com.projects.melih.popularmovies.network.NetworkState;
import com.projects.melih.popularmovies.repository.local.FavoritesContract;

/**
 * Created by Melih Gültekin on 25.03.2018
 */

public class FavoriteMoviesViewModel extends AndroidViewModel {
    private final MutableLiveData<NetworkState> networkState;
    private final MutableLiveData<NetworkState> refreshState;
    private final MutableLiveData<String> triggerListData;
    private final MediatorLiveData<Cursor> listLiveData;
    private AsyncTask<Void, Integer, Cursor> favoritesTask;

    public FavoriteMoviesViewModel(@NonNull Application application) {
        super(application);
        networkState = new MutableLiveData<>();
        refreshState = new MutableLiveData<>();
        triggerListData = new MutableLiveData<>();
        listLiveData = new MediatorLiveData<>();
        listLiveData.addSource(triggerListData, state -> getFavoriteMovies());
    }

    private synchronized void getFavoriteMovies() {
        cancelTaskIfNotNull();
        refreshState.setValue(NetworkState.LOADING);
        favoritesTask = new GetFavoritesTask(getApplication().getApplicationContext(), cursor -> {
            refreshState.setValue(NetworkState.LOADED);
            if (cursor == null) {
                networkState.setValue(NetworkState.FAILED);
            } else if (cursor.getCount() == 0) {
                networkState.setValue(NetworkState.EMPTY);
            } else {
                listLiveData.setValue(cursor);
            }
        }).execute();
    }

    LiveData<Cursor> getFavoriteList() {
        return listLiveData;
    }

    LiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    LiveData<NetworkState> getRefreshState() {
        return refreshState;
    }

    public void fetchData() {
        this.triggerListData.setValue(null);
    }

    @SuppressWarnings("UnusedReturnValue")
    public synchronized Uri addFavorite(@NonNull Movie movie) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FavoritesContract.FavoriteEntry.COLUMN_MOVIE_ID, movie.getId());
        contentValues.put(FavoritesContract.FavoriteEntry.COLUMN_MOVIE_TITLE, movie.getTitle());
        contentValues.put(FavoritesContract.FavoriteEntry.COLUMN_MOVIE_SYNOPSIS, movie.getOverview());
        contentValues.put(FavoritesContract.FavoriteEntry.COLUMN_MOVIE_RELEASE_DATE, movie.getReleaseDate());
        contentValues.put(FavoritesContract.FavoriteEntry.COLUMN_MOVIE_AVERAGE_RATE, movie.getVoteAverage());
        contentValues.put(FavoritesContract.FavoriteEntry.COLUMN_MOVIE_POSTER_PATH, movie.getPosterPath());
        contentValues.put(FavoritesContract.FavoriteEntry.COLUMN_MOVIE_BACKDROP_PATH, movie.getBackdropPath());
        Uri uri = getApplication().getApplicationContext().getContentResolver().insert(FavoritesContract.FavoriteEntry.CONTENT_URI, contentValues);
        if (uri != null) {
            fetchData();
        }
        return uri;
    }

    @SuppressWarnings("UnusedReturnValue")
    public synchronized int deleteFavorite(@NonNull Movie movie) {
        String id = Long.toString(movie.getId());
        Uri uri = FavoritesContract.FavoriteEntry.CONTENT_URI.buildUpon().appendPath(id).build();
        int numberOfDeletedRows = getApplication().getApplicationContext().getContentResolver().delete(uri, null, null);
        if (numberOfDeletedRows > 0) {
            fetchData();
        }
        return numberOfDeletedRows;
    }

    private void cancelTaskIfNotNull() {
        if (favoritesTask != null) {
            favoritesTask.cancel(true);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        cancelTaskIfNotNull();
    }

    private static class GetFavoritesTask extends AsyncTask<Void, Integer, Cursor> {
        @SuppressLint("StaticFieldLeak")
        private final Context context;
        private final CursorListener listener;

        GetFavoritesTask(@NonNull Context context, @NonNull CursorListener listener) {
            this.listener = listener;
            this.context = context;
        }

        protected Cursor doInBackground(Void... params) {
            if (isCancelled()) {
                return null;
            }
            return context.getContentResolver().query(FavoritesContract.FavoriteEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null);
        }

        protected void onPostExecute(Cursor result) {
            listener.onResult(result);
        }
    }

    interface CursorListener {
        void onResult(@Nullable Cursor cursor);
    }
}