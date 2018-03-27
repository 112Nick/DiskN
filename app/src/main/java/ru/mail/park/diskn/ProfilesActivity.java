package ru.mail.park.diskn;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yandex.authsdk.YandexAuthException;
import com.yandex.authsdk.YandexAuthOptions;
import com.yandex.authsdk.YandexAuthSdk;
import com.yandex.authsdk.YandexAuthToken;

import java.util.HashSet;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mail.park.diskn.APIs.RetrofitFactory;
import ru.mail.park.diskn.APIs.YandexApi;
import ru.mail.park.diskn.Models.Disk;

public class ProfilesActivity extends AppCompatActivity {
    private YandexAuthSdk sdk;
    private Context context;
    private final YandexApi yandexApi = new RetrofitFactory().create(YandexApi.class, Constants.YANDEX_BASE_URL);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles);
        Button YandexLoginBtn = findViewById(R.id.YandexLoginBtn);
        YandexLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Set<String> scopes = new HashSet<>();
                sdk = new YandexAuthSdk(context, new YandexAuthOptions(context, true));
                startActivityForResult(sdk.createLoginIntent(context, scopes), 0);
            }
        });

    }
    //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 0) {
            try {
                final YandexAuthToken yandexAuthToken = sdk.extractToken(resultCode, data);
                if (yandexAuthToken != null) {
                    // Success auth
                    Constants.YANDEX_OAUTH_TOKEN = yandexAuthToken.getValue();

                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
//                   getResourcesList();
                    //getDiskInfo();

                }
            } catch (YandexAuthException e) {
                // Process error
//                e.printStackTrace();
//                e.toString()
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



}
