package com.projects.melih.popularmovies.network;

import android.support.annotation.NonNull;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.projects.melih.popularmovies.BuildConfig;
import com.projects.melih.popularmovies.common.Constants;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Melih Gültekin on 18.02.2018
 */

public class MovieAPI {
    private static final int TIMEOUT_SECOND = 60;
    private static final Object lock = new Object();
    private static MovieService movieService;

    private MovieAPI() {
        //no-op
    }

    private static OkHttpClient.Builder getOkkHttpClient(Interceptor... interceptors) {
        final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(TIMEOUT_SECOND, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_SECOND, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_SECOND, TimeUnit.SECONDS);

        if (interceptors != null) {
            for (Interceptor interceptor : interceptors) {
                httpClient.addInterceptor(interceptor);
            }
        }

        final HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            httpClient.addNetworkInterceptor(new StethoInterceptor());
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(httpLoggingInterceptor);
        } else {
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
            httpClient.addInterceptor(httpLoggingInterceptor);
        }
        return httpClient;
    }

    public static MovieService getMovieService() {
        synchronized (lock) {
            if (movieService == null) {
                final OkHttpClient.Builder httpClient = getOkkHttpClient(new ApiKeyInterceptor());
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BuildConfig.API_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(httpClient.build())
                        .build();
                movieService = retrofit.create(MovieService.class);
            }
            return movieService;
        }
    }

    private static class ApiKeyInterceptor implements Interceptor {
        @Override
        public Response intercept(@NonNull Interceptor.Chain chain) throws IOException {
            Request original = chain.request();
            HttpUrl originalHttpUrl = original.url();
            HttpUrl url = originalHttpUrl.newBuilder()
                    .addQueryParameter("api_key", Constants.MOVIE_DB_API_KEY)
                    .build();

            Request.Builder requestBuilder = original.newBuilder()
                    .url(url);

            return chain.proceed(requestBuilder.build());
        }
    }
}
