package org.androidbh.podcast;

import android.app.Application;
import android.support.v4.util.Pair;

import com.facebook.stetho.Stetho;

import org.androidbh.podcast.api.RestAdpter;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;

/**
 * Created by felipearimateia on 20/03/17.
 */

public class PodcastApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }

        initRetrofit();
    }


    private void initRetrofit() {

        RestAdpter.Builder builder = new RestAdpter.Builder();
        builder.setDebug(BuildConfig.DEBUG)
                .setHostname(BuildConfig.HOSTNAME)
                .setProtocol("https")
                .setInterceptors(Arrays.asList(queryInterceptor()))
                .build();
    }

    private Interceptor queryInterceptor() {

        return chain -> {

            Request request = chain.request();
            HttpUrl url = request.url().newBuilder()
                    .addQueryParameter("client_key", BuildConfig.CLIENT_KEY)
                    .build();
            request = request.newBuilder().url(url).build();

            return chain.proceed(request);
        };
    }
}
