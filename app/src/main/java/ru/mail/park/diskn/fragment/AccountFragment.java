package ru.mail.park.diskn.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mail.park.diskn.Injector;
import ru.mail.park.diskn.MainActivity;
import ru.mail.park.diskn.ProfilesActivity;
import ru.mail.park.diskn.R;
import ru.mail.park.diskn.api.YandexApi;
import ru.mail.park.diskn.model.Disk;

import static android.content.Context.MODE_PRIVATE;

public class AccountFragment extends Fragment {
    private final static String KEY_OAUTH = "oauth";
    private final static String STORAGE_NAME = "storage";
    private final static String KEY_IS_FIRST = "is_first";

    private final YandexApi yandexApi = Injector.getInstance().yandexApi;
    private TextView displayname;
    private TextView county;
    private TextView totalSpace;
    private TextView usedSpace;
    private TextView trashSize;

    public static AccountFragment newInstance() {
        return new AccountFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.account_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        displayname = view.findViewById(R.id.account_name);
        county = view.findViewById(R.id.country);
        totalSpace = view.findViewById(R.id.total_space);
        usedSpace = view.findViewById(R.id.used_space);
        trashSize = view.findViewById(R.id.trash_size);
        Button logOutBtn = view.findViewById(R.id.log_out);

        logOutBtn.setOnClickListener(v -> {
            if (getContext() != null) {
                SharedPreferences prefs = getContext().getSharedPreferences(STORAGE_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(KEY_OAUTH, "");
                editor.putBoolean(KEY_IS_FIRST, true);
                editor.apply();
                Intent intent = new Intent(getContext(), ProfilesActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        getDiskInfo();
        if (getContext() != null) {
            ((MainActivity) getContext()).setTitle(R.string.account_title);
        }

    }


    private void getDiskInfo() {
        Callback<Disk> callback = new Callback<Disk>() {

            @Override
            public void onResponse(Call<Disk> call, Response<Disk> response) {
                Disk body = response.body();
                if (body != null) {
                    displayname.append(body.getUser().getDisplayName());
                    county.append(body.getUser().getCountry());
                    totalSpace.append(body.getTotalSpace());
                    usedSpace.append(body.getUsedSpace());
                    trashSize.append(body.getTrashSize());
                }
            }

            @Override
            public void onFailure(Call<Disk> call, Throwable t) {

                t.printStackTrace();
            }
        };
        yandexApi.getDiskInfo().enqueue(callback);
    }
}
