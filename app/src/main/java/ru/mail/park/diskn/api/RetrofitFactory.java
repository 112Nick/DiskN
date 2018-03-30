package ru.mail.park.diskn.api;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.mail.park.diskn.Constants;

/**
 * Created by nick on 23.03.18.
 */

//Generates the instance of the API interface
public class RetrofitFactory {

    public <T> T create(Class<T> apiClass, String baseUrl) {

        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(createOkHttp())
                .baseUrl(baseUrl)
                .build()
                .create(apiClass);
    }

    private OkHttpClient createOkHttp() {
        return new OkHttpClient.Builder()
                .addInterceptor(chain -> chain.proceed(
                        chain.request().newBuilder()
                                .addHeader("Authorization", Constants.YANDEX_OAUTH_TOKEN)
                                .build()))
                .build();
    }
}