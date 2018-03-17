package com.projects.melih.popularmovies.ui.moviedetail;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.projects.melih.popularmovies.common.CollectionUtils;
import com.projects.melih.popularmovies.common.Utils;
import com.projects.melih.popularmovies.model.Video;
import com.projects.melih.popularmovies.network.MovieAPI;
import com.projects.melih.popularmovies.network.MovieService;
import com.projects.melih.popularmovies.network.NetworkState;
import com.projects.melih.popularmovies.network.responses.ResponseVideo;

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
    private final MutableLiveData<NetworkState> networkState;
    private MediatorLiveData<List<Video>> list;
    private MutableLiveData<Long> movieId;
    private Call<ResponseVideo> call;

    public MovieDetailViewModel(@NonNull Application application) {
        super(application);
        movieService = MovieAPI.getMovieService();
        networkState = new MutableLiveData<>();
        movieId = new MutableLiveData<>();
    }

    LiveData<NetworkState> getNetworkStateLiveData() {
        return networkState;
    }

    LiveData<List<Video>> getVideosLiveData() {
        if (list == null) {
            list = new MediatorLiveData<>();
            list.addSource(movieId, id -> {
                if (id != null) {
                    callVideos(id);
                }
            });
        }
        return list;
    }

    private void callVideos(long id) {
        if (!Utils.isNetworkConnected(getApplication().getApplicationContext())) {
            networkState.postValue(NetworkState.NO_NETWORK);
        } else {
            call = movieService.getMovieVideos(id);
            call.enqueue(new Callback<ResponseVideo>() {
                @Override
                public void onResponse(@NonNull Call<ResponseVideo> call, @NonNull Response<ResponseVideo> response) {
                    boolean success = false;
                    if (response.isSuccessful()) {
                        final ResponseVideo body = response.body();
                        if (body != null) {
                            final List<Video> videos = body.getVideos();
                            if (CollectionUtils.isNotEmpty(videos)) {
                                success = true;
                                list.postValue(videos);
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

    void setMovieId(long movieId) {
        this.movieId.setValue(movieId);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (call != null) {
            call.cancel();
        }
    }
}