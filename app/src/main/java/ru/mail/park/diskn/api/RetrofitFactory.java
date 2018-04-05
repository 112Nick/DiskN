package ru.mail.park.diskn.api;

import android.content.SharedPreferences;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.mail.park.diskn.App;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by nick on 23.03.18.
 */

//Generates the instance of the API interface
public class RetrofitFactory {

    private final static String KEY_OAUTH = "oauth";
    private final static String STORAGE_NAME = "storage";

    public static OkHttpClient httpClient;

    private final SharedPreferences prefs  = App.getInstance().getSharedPreferences(STORAGE_NAME, MODE_PRIVATE);

    public <T> T create(Class<T> apiClass, String baseUrl) {
        httpClient = createOkHttp();
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .baseUrl(baseUrl)
                .build()
                .create(apiClass);
    }



    private OkHttpClient createOkHttp() {
        return new OkHttpClient.Builder()
                .addInterceptor(chain -> chain.proceed(
                        chain.request().newBuilder()
                                .addHeader("Authorization", "OAuth " + prefs.getString(KEY_OAUTH , "Can't read"))
                                .build()))
                .build();
    }
}