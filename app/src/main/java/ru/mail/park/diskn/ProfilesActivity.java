package ru.mail.park.diskn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.yandex.authsdk.YandexAuthException;
import com.yandex.authsdk.YandexAuthOptions;
import com.yandex.authsdk.YandexAuthSdk;
import com.yandex.authsdk.YandexAuthToken;

import java.util.HashSet;
import java.util.Set;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;


public class ProfilesActivity extends AppCompatActivity {
    private final static String KEY_IS_FIRST = "is_first";
    private final static String KEY_OAUTH = "oauth";
    private final static String STORAGE_NAME = "storage";
    private YandexAuthSdk sdk;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles);
        SharedPreferences prefs = getSharedPreferences(STORAGE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        //TODO check if logged in
        if (!prefs.getBoolean(KEY_IS_FIRST, true)) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);//TODO fix
            startActivity(intent);
        }

        Button YandexLoginBtn = findViewById(R.id.YandexLoginBtn);
        YandexLoginBtn.setOnClickListener(v -> {
            final Set<String> scopes = new HashSet<>();
            sdk = new YandexAuthSdk(context, new YandexAuthOptions(context, true));
            startActivityForResult(sdk.createLoginIntent(context, scopes), 0);
            editor.putBoolean(KEY_IS_FIRST, false);
            editor.apply();
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        SharedPreferences.Editor editor = getSharedPreferences(STORAGE_NAME, MODE_PRIVATE).edit();
        if (requestCode == 0) {
            try {
                final YandexAuthToken yandexAuthToken = sdk.extractToken(resultCode, data);
                if (yandexAuthToken != null) {
                    // Success auth
                    editor.putString(KEY_OAUTH, yandexAuthToken.getValue());
                    editor.apply();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }
            } catch (YandexAuthException e) {
                // Process error
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
