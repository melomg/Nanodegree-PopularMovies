package com.projects.melih.popularmovies.ui.reviews;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.projects.melih.popularmovies.common.CollectionUtils;
import com.projects.melih.popularmovies.common.Utils;
import com.projects.melih.popularmovies.model.Review;
import com.projects.melih.popularmovies.network.MovieAPI;
import com.projects.melih.popularmovies.network.MovieService;
import com.projects.melih.popularmovies.network.NetworkState;
import com.projects.melih.popularmovies.network.responses.ResponseReview;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.projects.melih.popularmovies.common.Constants.UNKNOWN_ERROR;

/**
 * Created by Melih Gültekin on 18.03.2018
 */

public class ReviewsViewModel extends AndroidViewModel {
    private static final int FIRST_PAGE = 0;
    private final MovieService movieService;
    private final MutableLiveData<Integer> page;
    private final MutableLiveData<NetworkState> networkState;
    private final MutableLiveData<NetworkState> refreshState;
    private final MutableLiveData<Long> movieId;
    private MutableLiveData<Integer> totalPage;
    private MutableLiveData<List<Review>> list;
    private Call<ResponseReview> call;

    public ReviewsViewModel(@NonNull Application application) {
        super(application);
        page = new MutableLiveData<>();
        page.setValue(FIRST_PAGE);
        networkState = new MutableLiveData<>();
        refreshState = new MutableLiveData<>();
        movieId = new MutableLiveData<>();
        movieService = MovieAPI.getMovieService();
    }

    LiveData<List<Review>> getPagedList() {
        return list;
    }

    LiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    LiveData<NetworkState> getRefreshState() {
        return refreshState;
    }

    synchronized void loadMoreReviews(boolean isFirstPage) {
        if (list == null) {
            list = new MutableLiveData<>();
        }
        if (!Utils.isNetworkConnected(getApplication().getApplicationContext())) {
            list.postValue(list.getValue());
            networkState.postValue(NetworkState.NO_NETWORK);
            refreshState.postValue(NetworkState.LOADED);
        } else {
            if (isFirstPage) {
                list.setValue(new ArrayList<>());
                page.setValue(FIRST_PAGE);
            }
            //noinspection ConstantConditions
            if ((totalPage == null) || (page.getValue() < totalPage.getValue())) {
                final Integer currentPage = page.getValue();
                if (currentPage != null) {
                    page.setValue(currentPage + 1);
                }
                callReviews(page.getValue());
            }
        }
    }

    private void callReviews(final int page) {
        refreshState.postValue(NetworkState.LOADING);
        final Long movieIdValue = movieId.getValue();
        if (movieIdValue != null) {
            call = movieService.getMovieReviews(movieIdValue, page);
            call.enqueue(new Callback<ResponseReview>() {
                @Override
                public void onResponse(@NonNull Call<ResponseReview> call, @NonNull Response<ResponseReview> response) {
                    boolean success = false;
                    if (response.isSuccessful()) {
                        final ResponseReview body = response.body();
                        if (body != null) {
                            final List<Review> reviews = body.getReviews();
                            if (totalPage == null) {
                                totalPage = new MutableLiveData<>();
                            }
                            totalPage.setValue(body.getTotalPages());
                            if (CollectionUtils.isNotEmpty(reviews)) {
                                success = true;
                                List<Review> allReviews = list.getValue();
                                if (allReviews == null) {
                                    allReviews = new ArrayList<>();
                                }
                                allReviews.addAll(reviews);
                                list.postValue(allReviews);
                            }
                        }
                        refreshState.postValue(NetworkState.LOADED);
                        if (!success) {
                            networkState.postValue(NetworkState.error(UNKNOWN_ERROR));
                        }
                    } else {
                        networkState.postValue(NetworkState.error("error code: " + response.code()));
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

    public void setMovieId(long movieId) {
        this.movieId.setValue(movieId);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        call.cancel();
    }
}