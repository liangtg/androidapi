package github.liangtg.androidapi.session;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import github.liangtg.androidapi.data.ClientUser;
import github.liangtg.androidapi.data.DataService;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by liangtg on 17-7-4.
 */

public class AppSession {
    private static AppSession instance;
    private DataService dataService;

    public AppSession() {
        OkHttpClient client = new OkHttpClient.Builder().retryOnConnectionFailure(false).addNetworkInterceptor(new LoggingInterceptor()).build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://example.com:8000/").addConverterFactory(GsonConverterFactory.create()).client(
                client).build();
        dataService = retrofit.create(DataService.class);
        ClientUser.init();
    }

    public static AppSession current() {
        if (null == instance) instance = new AppSession();
        return instance;
    }

    DataService dataService() {
        return dataService;
    }

    public void clearCache() {
    }

    private static class LoggingInterceptor implements Interceptor {
        private static volatile AtomicInteger REQUEST = new AtomicInteger();

        @Override
        public Response intercept(Chain chain) throws IOException {
            int id = REQUEST.incrementAndGet();
            Request request = chain.request();
            long t1 = System.nanoTime();
            Log.d("net", String.format("request %d %s", id, request.url()));
            if (null != request.body() && request.body().contentLength() > 0) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                BufferedSink buffer = Okio.buffer(Okio.sink(byteArrayOutputStream));
                request.body().writeTo(buffer);
                buffer.flush();
                Log.d("net.param", String.format("%d param: %s", id, byteArrayOutputStream.toString()));
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
            Response response = chain.proceed(request);
            long t2 = System.nanoTime();
            Log.d("net", String.format("%d %d %s in %.1fms", id, response.code(), response.request().url(), (t2 - t1) / 1e6d));
            if (response.body().contentLength() > 0) {
                Log.d("net.response", response.peekBody(response.body().contentLength()).string());
            }
            return response;
        }
    }

}
