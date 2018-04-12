package ru.mail.park.diskn;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mail.park.diskn.api.YandexApi;
import ru.mail.park.diskn.fragment.AccountFragment;
import ru.mail.park.diskn.fragment.FilesFragment;
import ru.mail.park.diskn.model.Disk;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private final YandexApi yandexApi =Injector.getInstance().yandexApi;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggleButton;

    // TODO REQUEST_LOGIN_SDK = 0
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawerLayout);
        toggleButton = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        getDiskInfo();

        NavigationView navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, FilesFragment.newInstance("/", "files"))
                .addToBackStack(null)
                .commit();

        drawerLayout.addDrawerListener(toggleButton);

        toggleButton.syncState();

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

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
                Disk body = response.body();
                Log.d("MyTag", String.valueOf(body));
                View navHeader = ((NavigationView) findViewById(R.id.nav_view)).getHeaderView(0);
                TextView displayName = navHeader.findViewById(R.id.display_name);
//                TextView userLogin = navHeader.findViewById(R.id.login);
                if (body != null) {
                    displayName.append(body.getUser().getDisplayName());
                }
            }

            @Override
            public void onFailure(Call<Disk> call, Throwable t) {
                //TODO
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
                        .replace(R.id.container, FilesFragment.newInstance("/", "files"))
                        .addToBackStack(null)
                        .commit();

                drawerLayout.closeDrawers();
                break;
            }
            case R.id.trash: {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, FilesFragment.newInstance("/", "trash"))
                        .addToBackStack(null)
                        .commit();
                drawerLayout.closeDrawers();
                break;
            }
            case R.id.account: {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, AccountFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                drawerLayout.closeDrawers();
                break;
            }


        }
        return true;
    }
}

