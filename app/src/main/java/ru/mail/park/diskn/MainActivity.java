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

import static android.support.v4.app.ActivityCompat.startActivityForResult;


public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggleButton;
    private MenuItem logIn;
    private Context context;
    YandexAuthSdk sdk;

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
                    txt.append("Token success: " + yandexAuthToken.getValue());

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

}
