package ru.mail.park.diskn;

import android.app.Application;

/**
 * Created by nick on 15.03.18.
 */

public class App extends Application {
    private static App app;

    public static App getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        //final Set<String> scopes = new HashSet<>();
        super.onCreate();
        //YandexAuthSdk sdk = new YandexAuthSdk(new YandexAuthOptions(this, true));
        //startActivityForResult(sdk.createLoginIntent(this, scopes), 0);
        Injector.init(this);
        app = this;
    }
}
