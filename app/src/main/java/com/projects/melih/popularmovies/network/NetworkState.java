package com.projects.melih.popularmovies.network;

import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.projects.melih.popularmovies.network.State.*;

/**
 * Created by Melih Gültekin on 24.02.2018
 */

public class NetworkState {
    public static final NetworkState LOADED = new NetworkState(State.SUCCESS);
    public static final NetworkState LOADING = new NetworkState(State.RUNNING);
    public static final NetworkState NO_NETWORK = new NetworkState(State.NO_NETWORK);

    @State
    private final int state;
    private final String errorMessage;

    public NetworkState(@State int state) {
        this(state, null);
    }

    public NetworkState(@State int state, @Nullable String errorMessage) {
        this.state = state;
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public static NetworkState error(@Nullable String errorMessage) {
        return new NetworkState(State.FAILED, errorMessage);
    }
}

@Retention(RetentionPolicy.SOURCE)
@IntDef(value = {
        SUCCESS,
        RUNNING,
        FAILED,
        NO_NETWORK
})
@interface State {
    int SUCCESS = 0;
    int RUNNING = SUCCESS + 1;
    int FAILED = RUNNING + 1;
    int NO_NETWORK = FAILED + 1;
}