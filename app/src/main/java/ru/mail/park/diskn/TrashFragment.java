package ru.mail.park.diskn;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mail.park.diskn.api.RetrofitFactory;
import ru.mail.park.diskn.api.YandexApi;
import ru.mail.park.diskn.model.FilesArr;

public class TrashFragment extends Fragment {

    private final YandexApi yandexApi = new RetrofitFactory().create(YandexApi.class, Constants.YANDEX_BASE_URL);
    RecyclerView fileListView;

    public static TrashFragment newInstance() {
        TrashFragment trashFragment = new TrashFragment();

        return trashFragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getTrashResources();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.trash_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        fileListView = view.findViewById(R.id.fileList);
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private void getTrashResources() {
        Callback<FilesArr> callback = new Callback<FilesArr>() {

            @Override
            public void onResponse(Call<FilesArr> call, Response<FilesArr> response) {
                fileListView.setLayoutManager(new LinearLayoutManager(getContext()));
                final FileListAdapter adapter = new FileListAdapter(getContext());
                fileListView.setAdapter(adapter);
                fileListView.setHasFixedSize(true);

                if (response.body().getEmbedded().getItems().isEmpty()) {
                    //
                }
                else {
                    for (int i = 0; i < response.body().getEmbedded().getItems().size(); i++) {
                        fileListView.scrollToPosition(0);
                        adapter.add(response.body().getEmbedded().getItems().get(i));
                    }
                }
            }

            @Override
            public void onFailure(Call<FilesArr> call, Throwable t) {
                t.printStackTrace();
            }
        };
        yandexApi.getTrashResources().enqueue(callback);
    }

}




