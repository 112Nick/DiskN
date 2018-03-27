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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yandex.authsdk.YandexAuthException;
import com.yandex.authsdk.YandexAuthOptions;
import com.yandex.authsdk.YandexAuthSdk;
import com.yandex.authsdk.YandexAuthToken;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mail.park.diskn.APIs.RetrofitFactory;
import ru.mail.park.diskn.APIs.YandexApi;
import ru.mail.park.diskn.Models.Disk;
import ru.mail.park.diskn.Models.Embedded;
import ru.mail.park.diskn.Models.ResourceItem;
import ru.mail.park.diskn.Models.Test;

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
        context = this;

        final RecyclerView fileListView = findViewById(R.id.fileList);
        fileListView.setLayoutManager(new LinearLayoutManager(this));
        final FileListAdapter adapter = new FileListAdapter(this);
        fileListView.setAdapter(adapter);
        fileListView.setHasFixedSize(true);
        getDiskInfo();
        getResourcesList();


        drawerLayout.addDrawerListener(toggleButton);
        toggleButton.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggleButton.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getDiskInfo() {
        Callback<Disk> callback = new Callback<Disk>() {

            @Override
            public void onResponse(Call<Disk> call, Response<Disk> response) {
                Log.d("MyTag", String.valueOf(response.body()));
                View navHeader = ((NavigationView)findViewById(R.id.nav_view)).getHeaderView(0);
                TextView displayName = navHeader.findViewById(R.id.display_name);
//                TextView userLogin = navHeader.findViewById(R.id.login);
                displayName.append(response.body().getUser().getDisplayName());
                //userLogin.append(response.body().getUser().getLogin());


            }

            @Override
            public void onFailure(Call<Disk> call, Throwable t) {
                t.printStackTrace();
            }
        };
        yandexApi.getDiskInfo().enqueue(callback);
    }

    private void getResourcesList() {
        Callback<Test> callback = new Callback<Test>() {

            @Override
            public void onResponse(Call<Test> call, Response<Test> response) {
//                Log.d("MyTag", String.valueOf(response.body()));
                final RecyclerView fileListView = findViewById(R.id.fileList);
                fileListView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                final FileListAdapter adapter = new FileListAdapter(getApplicationContext());
                fileListView.setAdapter(adapter);
                fileListView.setHasFixedSize(true);

                for (int i = 0; i < response.body().getEmbedded().getItems().size(); i++) {
                    fileListView.scrollToPosition(0);
                    Log.d("MyFILENAME", response.body().getEmbedded().getItems().get(i).getName());
                    adapter.add(response.body().getEmbedded().getItems().get(i).getName());
                }
                /////////////////


            }

            @Override
            public void onFailure(Call<Test> call, Throwable t) {
                t.printStackTrace();
            }
        };
        yandexApi.getResources().enqueue(callback);
    }

}

