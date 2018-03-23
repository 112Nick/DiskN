package ru.mail.park.diskn;

import android.content.Context;
import android.content.Intent;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
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

import static android.support.v4.app.ActivityCompat.startActivityForResult;


public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggleButton;
    private MenuItem logIn;
    private Context context;
    private YandexAuthSdk sdk;
    private final YandexApi yandexApi = new RetrofitFactory().create(YandexApi.class, Constants.YANDEX_BASE_URL);


    // TODO REQUEST_LOGIN_SDK = 0
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawerLayout);
        toggleButton = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        logIn = ((NavigationView)findViewById(R.id.nav_view)).getMenu().findItem(R.id.login);
//
        context = this;

        MenuItem.OnMenuItemClickListener listener = new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                final Set<String> scopes = new HashSet<>();
                sdk = new YandexAuthSdk(context, new YandexAuthOptions(context, true));
                startActivityForResult(sdk.createLoginIntent(context, scopes), 0);
                return false;
            }
        };
        /////////
        drawerLayout.addDrawerListener(toggleButton);
        toggleButton.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        logIn.setOnMenuItemClickListener(listener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggleButton.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 0) {
            TextView txt = findViewById(R.id.txt);
            try {
                final YandexAuthToken yandexAuthToken = sdk.extractToken(resultCode, data);
                if (yandexAuthToken != null) {
                    // Success auth
//                    sdk.extractToken(resultCode, data);
                    Constants.YANDEX_OAUTH_TOKEN = yandexAuthToken.getValue();
                    txt.append("Token success: " + yandexAuthToken.getValue());
                    makeRequest();

                }
            } catch (YandexAuthException e) {
                // Process error
//                e.printStackTrace();
//                e.toString()
                txt.append("Error");
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void makeRequest() {
        Callback<Disk> callback = new Callback<Disk>() {

            @Override
            public void onResponse(Call<Disk> call, Response<Disk> response) {
                Log.d("MyTag", String.valueOf(response.body()));
                TextView txt = findViewById(R.id.txt);
                txt.append(String.valueOf(response.body()));

            }

            @Override
            public void onFailure(Call<Disk> call, Throwable t) {
                t.printStackTrace();
            }
        };
        yandexApi.getDiskInfo().enqueue(callback);

    }

}

