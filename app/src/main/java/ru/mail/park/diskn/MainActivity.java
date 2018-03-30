package ru.mail.park.diskn;

import android.content.Context;
import android.os.Bundle;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.yandex.authsdk.YandexAuthSdk;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mail.park.diskn.api.RetrofitFactory;
import ru.mail.park.diskn.api.YandexApi;
import ru.mail.park.diskn.model.Disk;
import ru.mail.park.diskn.model.Test;
import ru.mail.park.diskn.model.TrashFragment;


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
        getDiskInfo();

        Menu navMenu = ((NavigationView)findViewById(R.id.nav_view)).getMenu();
        MenuItem files = navMenu.findItem(R.id.files);
        MenuItem trash = navMenu.findItem(R.id.trash);

        MenuItem.OnMenuItemClickListener listener = new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.d("FILES", "QASERFTYHBHJBYFRDSESEDFGVHJJNMK");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, FilesFragment.newInstance())
                        .commit();
                return false;
            }
        };

        MenuItem.OnMenuItemClickListener listener2 = new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.d("TRASH", "QASERFTYHBHJBYFRDSESEDFGVHJJNMK");

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, TrashFragment.newInstance())
                        .commit();
                return false;
            }
        };

        files.setOnMenuItemClickListener(listener);
        trash.setOnMenuItemClickListener(listener2);

//        getResourcesList();
//        getTrashResources();
//        getSupportFragmentManager().beginTransaction()
//                .add(R.id.drawerLayout, FilesFragment.newInstance())
//                .commit();
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

//    private void getResourcesList() {
//        Callback<Test> callback = new Callback<Test>() {
//
//            @Override
//            public void onResponse(Call<Test> call, Response<Test> response) {
////                Log.d("MyTag", String.valueOf(response.body()));
//                final RecyclerView fileListView = findViewById(R.id.fileList);
//                fileListView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//                final FileListAdapter adapter = new FileListAdapter(getApplicationContext());
//                fileListView.setAdapter(adapter);
//                fileListView.setHasFixedSize(true);
//
//                for (int i = 0; i < response.body().getEmbedded().getItems().size(); i++) {
//                    fileListView.scrollToPosition(0);
//                    Log.d("MyFILENAME", response.body().getEmbedded().getItems().get(i).getName());
//                    adapter.add(response.body().getEmbedded().getItems().get(i));
//                }
//                /////////////////
//
//
//            }
//
//            @Override
//            public void onFailure(Call<Test> call, Throwable t) {
//                t.printStackTrace();
//            }
//        };
//        yandexApi.getResources().enqueue(callback);
//    }

//    private void getTrashResources() {
//        Callback<Test> callback = new Callback<Test>() {
//
//            @Override
//            public void onResponse(Call<Test> call, Response<Test> response) {
////                Log.d("MyTag", String.valueOf(response.body()));
//                final RecyclerView fileListView = findViewById(R.id.fileList);
//                fileListView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//                final FileListAdapter adapter = new FileListAdapter(getApplicationContext());
//                fileListView.setAdapter(adapter);
//                fileListView.setHasFixedSize(true);
//
//                if (response.body().getEmbedded().getItems().isEmpty()) {
//                    //
//                }
//                else {
//                    for (int i = 0; i < response.body().getEmbedded().getItems().size(); i++) {
//                        fileListView.scrollToPosition(0);
//                        Log.d("MyFILENAME", response.body().getEmbedded().getItems().get(i).getName());
//                        adapter.add(response.body().getEmbedded().getItems().get(i));
//                    }
//                }
//
//                /////////////////
//
//
//            }
//
//            @Override
//            public void onFailure(Call<Test> call, Throwable t) {
//                t.printStackTrace();
//            }
//        };
//        yandexApi.getTrashResources().enqueue(callback);
//    }

}

