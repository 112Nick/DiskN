package ru.mail.park.diskn.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mail.park.diskn.Injector;
import ru.mail.park.diskn.R;

public class DeleteConfirmDialogFragment extends DialogFragment {


    public static DeleteConfirmDialogFragment newInstance() {
        return new DeleteConfirmDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.delete_confirm, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button yes = view.findViewById(R.id.confirm);
        Button no = view.findViewById(R.id.no_confirm);

        yes.setOnClickListener(v -> deleteAll());

        no.setOnClickListener(v -> this.dismiss());
    }

    private void deleteAll() {
        this.dismiss();
        Callback<Void> callback = new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                //TODO
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }

        };
        Injector.getInstance().yandexApi.deleteAllFromTrash().enqueue(callback);
    }
}
