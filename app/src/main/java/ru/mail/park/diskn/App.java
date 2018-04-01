package ru.mail.park.diskn;

import android.app.Activity;
import android.app.Application;

import com.yandex.authsdk.YandexAuthOptions;
import com.yandex.authsdk.YandexAuthSdk;

import java.util.HashSet;
import java.util.Set;

import static android.support.v4.app.ActivityCompat.startActivityForResult;
import static java.security.AccessController.getContext;

/**
 * Created by nick on 15.03.18.
 */

public class App extends Application {
    private static App app ;
    public static App getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        //final Set<String> scopes = new HashSet<>();
        super.onCreate();
        //YandexAuthSdk sdk = new YandexAuthSdk(new YandexAuthOptions(this, true));
        //startActivityForResult(sdk.createLoginIntent(this, scopes), 0);
        app = this;
    }
}
