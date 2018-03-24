package com.projects.melih.popularmovies.ui.moviedetail;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.projects.melih.popularmovies.common.CollectionUtils;
import com.projects.melih.popularmovies.common.SingleLiveEvent;
import com.projects.melih.popularmovies.common.Utils;
import com.projects.melih.popularmovies.model.Review;
import com.projects.melih.popularmovies.model.Video;
import com.projects.melih.popularmovies.network.MovieAPI;
import com.projects.melih.popularmovies.network.MovieService;
import com.projects.melih.popularmovies.network.NetworkState;
import com.projects.melih.popularmovies.network.responses.ResponseReview;
import com.projects.melih.popularmovies.network.responses.ResponseVideo;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.projects.melih.popularmovies.common.Constants.UNKNOWN_ERROR;

/**
 * Created by Melih Gültekin on 13.03.2018
 */

class MovieDetailViewModel extends AndroidViewModel {

    private final MovieService movieService;
    private final SingleLiveEvent<NetworkState> networkState;
    private final MutableLiveData<Long> movieId;
    private MediatorLiveData<List<Video>> videoList;
    private MediatorLiveData<ArrayList<Review>> reviewList;
    private Call<ResponseVideo> callVideos;
    private Call<ResponseReview> callReviews;

    MovieDetailViewModel(@NonNull Application application) {
        super(application);
        movieService = MovieAPI.getMovieService();
        networkState = new SingleLiveEvent<>();
        movieId = new MutableLiveData<>();
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
                            final List<Video> videos = body.getVideos();
                            if (CollectionUtils.isNotEmpty(videos)) {
                                success = true;
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
                            final ArrayList<Review> reviews = body.getReviews();
                            if (CollectionUtils.isNotEmpty(reviews)) {
                                success = true;
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

    void setMovieId(long movieId) {
        this.movieId.setValue(movieId);
    }

    long getMovieId() {
        final Long value = movieId.getValue();
        return (value == null) ? 0 : value;
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
    }
}