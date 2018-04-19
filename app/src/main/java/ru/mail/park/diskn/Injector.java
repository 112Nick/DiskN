package ru.mail.park.diskn;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.mail.park.diskn.api.YandexApi;

import static android.content.Context.MODE_PRIVATE;


public class Injector {

    private final static String KEY_OAUTH = "oauth";
    private final static String STORAGE_NAME = "storage";

    @SuppressLint("StaticFieldLeak") // application context - singleton
    private static Injector instance = null;
    public final Picasso picasso;
    public final YandexApi yandexApi;

    private final SharedPreferences prefs;


    private Injector(Context context) {
        Context contextApp = context.getApplicationContext();

        this.prefs = context.getSharedPreferences(STORAGE_NAME, MODE_PRIVATE);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(chain ->
                        chain.proceed(
                        chain.request().newBuilder()
                                .addHeader("Authorization", "OAuth " + prefs.getString(KEY_OAUTH, "Can't read"))
                                .build()))
                .build();
        this.picasso = new Picasso.Builder(contextApp)
                .downloader(new OkHttp3Downloader(httpClient))
                .build();

        this.yandexApi = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .baseUrl(YandexApi.YANDEX_BASE_URL)
                .build()
                .create(YandexApi.class);
    }

    public static Injector getInstance() {
        return instance;
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new Injector(context);
        }
    }
}
