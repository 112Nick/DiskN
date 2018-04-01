package ru.mail.park.diskn;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mail.park.diskn.api.RetrofitFactory;
import ru.mail.park.diskn.api.YandexApi;
import ru.mail.park.diskn.model.Disk;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggleButton;
    private Context context;
    private final YandexApi yandexApi = new RetrofitFactory().create(YandexApi.class, Constants.YANDEX_BASE_URL);


    // TODO REQUEST_LOGIN_SDK = 0
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawerLayout);
        toggleButton = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        context = this;
        getDiskInfo();

        NavigationView navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, FilesFragment.newInstance())
                .commit();
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
                View navHeader = ((NavigationView) findViewById(R.id.nav_view)).getHeaderView(0);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.files: {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, FilesFragment.newInstance())
                        .commit();
                drawerLayout.closeDrawers();
                break;
            }
            case R.id.trash: {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, TrashFragment.newInstance())
                        .commit();
                drawerLayout.closeDrawers();
                break;
            }

        }
        return true;
    }
}

