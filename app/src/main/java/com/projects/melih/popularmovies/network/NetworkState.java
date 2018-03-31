package com.projects.melih.popularmovies.network;

import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Melih Gültekin on 24.02.2018
 */

public class NetworkState {
    private static final int STATE_SUCCESS = 0;
    private static final int STATE_RUNNING = STATE_SUCCESS + 1;
    private static final int STATE_FAILED = STATE_RUNNING + 1;
    private static final int STATE_NO_NETWORK = STATE_FAILED + 1;
    private static final int STATE_EMPTY = STATE_NO_NETWORK + 1;

    public static final NetworkState LOADED = new NetworkState(STATE_SUCCESS);
    public static final NetworkState LOADING = new NetworkState(STATE_RUNNING);
    public static final NetworkState FAILED = new NetworkState(STATE_FAILED);
    public static final NetworkState NO_NETWORK = new NetworkState(STATE_NO_NETWORK);
    public static final NetworkState EMPTY = new NetworkState(STATE_EMPTY);

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    @State
    private final int state;
    private final String errorMessage;

    public NetworkState(@State int state) {
        this(state, null);
    }

    @SuppressWarnings("WeakerAccess")
    public NetworkState(@State int state, @Nullable String errorMessage) {
        this.state = state;
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public static NetworkState error(@Nullable String errorMessage) {
        return new NetworkState(STATE_FAILED, errorMessage);
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef(value = {
            STATE_SUCCESS,
            STATE_RUNNING,
            STATE_FAILED,
            STATE_NO_NETWORK,
            STATE_EMPTY
    })
    public @interface State {
    }
}