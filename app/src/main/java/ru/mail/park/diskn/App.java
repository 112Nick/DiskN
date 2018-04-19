package ru.mail.park.diskn;

import android.app.Application;

/**
 * Created by nick on 15.03.18.
 */

public class App extends Application {
//    private static App app;

//    public static App getInstance() {
//        return app;
//    }

    @Override
    public void onCreate() {
        super.onCreate();
        Injector.init(this);

//        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//                .detectAll()
//                .penaltyLog()
//                .build());

//        app = this;
    }
}
