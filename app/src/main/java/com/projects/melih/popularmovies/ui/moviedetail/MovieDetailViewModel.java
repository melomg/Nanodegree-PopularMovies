package com.projects.melih.popularmovies.ui.moviedetail;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.projects.melih.popularmovies.common.CollectionUtils;
import com.projects.melih.popularmovies.common.SingleLiveEvent;
import com.projects.melih.popularmovies.common.Utils;
import com.projects.melih.popularmovies.model.Movie;
import com.projects.melih.popularmovies.model.Review;
import com.projects.melih.popularmovies.model.Video;
import com.projects.melih.popularmovies.network.MovieAPI;
import com.projects.melih.popularmovies.network.MovieService;
import com.projects.melih.popularmovies.network.NetworkState;
import com.projects.melih.popularmovies.network.responses.ResponseReview;
import com.projects.melih.popularmovies.network.responses.ResponseVideo;
import com.projects.melih.popularmovies.repository.local.FavoritesContract;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.projects.melih.popularmovies.common.Constants.UNKNOWN_ERROR;

/**
 * Created by Melih Gültekin on 13.03.2018
 */

public class MovieDetailViewModel extends AndroidViewModel {

    private final MovieService movieService;
    private final SingleLiveEvent<NetworkState> networkState;
    private final MutableLiveData<Long> movieId;
    private final MutableLiveData<Boolean> shouldFetchLocalLiveData;
    private MediatorLiveData<List<Video>> videoList;
    private MediatorLiveData<ArrayList<Review>> reviewList;
    private MediatorLiveData<Movie> localMovie;
    private Call<ResponseVideo> callVideos;
    private Call<ResponseReview> callReviews;
    private AsyncTask<Void, Integer, Movie> localMovieTask;

    public MovieDetailViewModel(@NonNull Application application) {
        super(application);
        movieService = MovieAPI.getMovieService();
        networkState = new SingleLiveEvent<>();
        movieId = new MutableLiveData<>();
        shouldFetchLocalLiveData = new MutableLiveData<>();
    }

    LiveData<NetworkState> getNetworkStateLiveData() {
        return networkState;
    }

    LiveData<List<Video>> getVideosLiveData() {
        if (videoList == null) {
            videoList = new MediatorLiveData<>();
            videoList.addSource(movieId, id -> {
                if (id != null) {
                    callVideos(id);
                }
            });
        }
        return videoList;
    }

    LiveData<ArrayList<Review>> getReviewsLiveData() {
        if (reviewList == null) {
            reviewList = new MediatorLiveData<>();
            reviewList.addSource(movieId, id -> {
                if (id != null) {
                    callReviews(id);
                }
            });
        }
        return reviewList;
    }

    void setMovieId(long movieId) {
        this.movieId.setValue(movieId);
    }

    long getMovieId() {
        final Long value = movieId.getValue();
        return (value == null) ? 0 : value;
    }

    void setShouldFetchLocalMovie(boolean shouldFetchLocal) {
        if (shouldFetchLocal) {
            //noinspection ConstantConditions
            shouldFetchLocalLiveData.setValue(shouldFetchLocal);
        }
    }

    LiveData<Movie> getLocalMovie() {
        if (localMovie == null) {
            localMovie = new MediatorLiveData<>();
            localMovie.addSource(shouldFetchLocalLiveData, aBoolean -> {
                cancelTaskIfNotNull();
                localMovieTask = new GetLocalMovieTask(getApplication().getApplicationContext(), getMovieId(), movie -> localMovie.setValue(movie)).execute();
            });
        }
        return localMovie;
    }

    private void callVideos(long id) {
        if (!Utils.isNetworkConnected(getApplication().getApplicationContext())) {
            networkState.postValue(NetworkState.NO_NETWORK);
        } else {
            callVideos = movieService.getMovieVideos(id);
            callVideos.enqueue(new Callback<ResponseVideo>() {
                @Override
                public void onResponse(@NonNull Call<ResponseVideo> call, @NonNull Response<ResponseVideo> response) {
                    boolean success = false;
                    if (response.isSuccessful()) {
                        final ResponseVideo body = response.body();
                        if (body != null) {
                            success = true;
                            final List<Video> videos = body.getVideos();
                            if (CollectionUtils.isNotEmpty(videos)) {
                                videoList.postValue(videos);
                            }
                        }
                    }
                    if (!success) {
                        networkState.postValue(NetworkState.error(UNKNOWN_ERROR));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseVideo> call, @NonNull Throwable t) {
                    final String message = t.getMessage();
                    networkState.postValue(NetworkState.error((message == null) ? UNKNOWN_ERROR : message));
                }
            });
        }
    }

    private void callReviews(long id) {
        if (!Utils.isNetworkConnected(getApplication().getApplicationContext())) {
            networkState.postValue(NetworkState.NO_NETWORK);
        } else {
            callReviews = movieService.getMovieReviews(id, 1);
            callReviews.enqueue(new Callback<ResponseReview>() {
                @Override
                public void onResponse(@NonNull Call<ResponseReview> call, @NonNull Response<ResponseReview> response) {
                    boolean success = false;
                    if (response.isSuccessful()) {
                        final ResponseReview body = response.body();
                        if (body != null) {
                            success = true;
                            final ArrayList<Review> reviews = body.getReviews();
                            if (CollectionUtils.isNotEmpty(reviews)) {
                                reviewList.postValue(reviews);
                            }
                        }
                    }
                    if (!success) {
                        networkState.postValue(NetworkState.error(UNKNOWN_ERROR));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseReview> call, @NonNull Throwable t) {
                    final String message = t.getMessage();
                    networkState.postValue(NetworkState.error((message == null) ? UNKNOWN_ERROR : message));
                }
            });
        }
    }

    private void cancelTaskIfNotNull() {
        if (localMovieTask != null) {
            localMovieTask.cancel(true);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (callVideos != null) {
            callVideos.cancel();
        }
        if (callReviews != null) {
            callReviews.cancel();
        }
        cancelTaskIfNotNull();
    }

    private static class GetLocalMovieTask extends AsyncTask<Void, Integer, Movie> {
        @SuppressLint("StaticFieldLeak")
        private final Context context;
        private final GetLocalMovieTaskListener listener;
        private final long movieId;

        GetLocalMovieTask(@NonNull Context context, long movieId, @NonNull GetLocalMovieTaskListener listener) {
            this.context = context;
            this.movieId = movieId;
            this.listener = listener;
        }

        protected Movie doInBackground(Void... params) {
            if (isCancelled()) {
                return null;
            }
            Uri uri = FavoritesContract.FavoriteEntry.CONTENT_URI.buildUpon().appendPath(Long.toString(movieId)).build();
            Cursor cursor = context.getContentResolver().query(uri,
                    null,
                    null,
                    null,
                    null);

            Movie movie = null;
            if ((cursor != null) && (cursor.getCount() != 0)) {
                cursor.moveToFirst();
                movie = getMovieFromCursor(cursor);
            }
            return movie;
        }

        protected void onPostExecute(Movie result) {
            listener.onResult(result);
        }

        @NonNull
        private Movie getMovieFromCursor(@NonNull Cursor cursor) {
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

    interface GetLocalMovieTaskListener {
        void onResult(@Nullable Movie movie);
    }
}