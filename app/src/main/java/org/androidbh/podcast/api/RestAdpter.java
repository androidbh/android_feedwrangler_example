package org.androidbh.podcast.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.CertificatePinner;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestAdpter {

    private static Retrofit retrofit;

    private RestAdpter() {
    }

    private static void init(Builder builder) {
        OkHttpClient okHttpClient = createClient(builder);
        retrofit = createRetrofit(okHttpClient, builder);
    }

    public static synchronized <T>  T getApi(@NonNull Class<T> service) {
        return retrofit.create(service);
    }

    public static Retrofit retrofit() {
        return retrofit;
    }

    public static class Builder {

        private String protocol;
        private String hostname;
        private File cacheDir;
        private SSLContext sslContext;
        private X509TrustManager trustManager;
        private boolean debug;
        private String[] pins;
        private int cacheSize;
        private int timeOut;
        private List<Interceptor> interceptors;

        public Builder setProtocol(@NonNull  String protocol) {
            this.protocol = protocol;
            return this;
        }

        public Builder setHostname(@NonNull String hostname) {
            this.hostname = hostname;
            return this;
        }

        public Builder setCacheDir(@Nullable File cacheDir) {
            this.cacheDir = cacheDir;
            return this;
        }

        public Builder setSSlContext(@Nullable SSLContext sslContext) {
            this.sslContext = sslContext;
            return this;
        }

        public Builder setTrustManager(@Nullable X509TrustManager trustManager) {
            this.trustManager = trustManager;
            return this;
        }

        public Builder setDebug(boolean debug) {
            this.debug = debug;
            return this;
        }

        public Builder setPins(@Nullable String[] pins) {
            this.pins = pins;
            return this;
        }

        public Builder setCacheSize(int cacheSize) {
            this.cacheSize = cacheSize;
            return this;
        }

        public Builder setTimeOut(int timeOut) {
            this.timeOut = timeOut;
            return this;
        }

        public Builder setInterceptors(List<Interceptor> interceptors) {
            this.interceptors = interceptors;
            return this;
        }

        public void build() {
            RestAdpter.init(this);
        }
    }

    private static Retrofit createRetrofit(OkHttpClient okHttpClient, Builder builder) {
        return  new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(builder.protocol +"://" + builder.hostname)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private static OkHttpClient createClient(Builder builder) {

        OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();

        if (builder.debug) {
            okBuilder.addInterceptor(new StethoInterceptor());
        }

        if (builder.pins != null && builder.pins.length > 0) {
            CertificatePinner certificatePinner = new CertificatePinner.Builder()
                    .add(builder.hostname, builder.pins)
                    .build();
            okBuilder.certificatePinner(certificatePinner);
        }

        //Sample: https://github.com/square/okhttp/blob/master/samples/guide/src/main/java/okhttp3/recipes/CustomTrust.java
        if (builder.sslContext != null && builder.trustManager != null) {
            okBuilder.sslSocketFactory(builder.sslContext.getSocketFactory(), builder.trustManager);
        }

        if (builder.cacheDir != null && builder.cacheSize > 0) {
            Cache cache = new Cache(builder.cacheDir, builder.cacheSize);
            okBuilder.cache(cache);
        }

        if (builder.timeOut > 0 ) {
            okBuilder.connectTimeout(builder.timeOut, TimeUnit.SECONDS);
        }

        if (builder.interceptors != null && builder.interceptors.size() > 0) {
            for (Interceptor interceptor : builder.interceptors) {
                okBuilder.addInterceptor(interceptor);
            }
        }

        return okBuilder.build();
    }
}
