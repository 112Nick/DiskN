package ru.mail.park.diskn.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mail.park.diskn.Constants;
import ru.mail.park.diskn.FileListAdapter;
import ru.mail.park.diskn.R;
import ru.mail.park.diskn.api.RetrofitFactory;
import ru.mail.park.diskn.api.YandexApi;
import ru.mail.park.diskn.model.FilesArr;
import ru.mail.park.diskn.model.ResourceItem;

public class FilesFragment extends Fragment {

    private final YandexApi yandexApi = new RetrofitFactory().create(YandexApi.class, Constants.YANDEX_BASE_URL);
    RecyclerView fileListView;
    SwipeRefreshLayout swipeRefreshLayout;
    private String path;

    public static FilesFragment newInstance(String path) {
        FilesFragment firstFragment = new FilesFragment();
        firstFragment.path = path;
        return firstFragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getResourcesList(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.files_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        fileListView = view.findViewById(R.id.fileList);
        swipeRefreshLayout = view.findViewById(R.id.swipe);

        swipeRefreshLayout.setOnRefreshListener(() -> getResourcesList(getContext()));

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    private void getResourcesList(Context context) {
        Callback<FilesArr> callback = new Callback<FilesArr>() {

            @Override
            public void onResponse(Call<FilesArr> call, Response<FilesArr> response) {
                fileListView.setLayoutManager(new LinearLayoutManager(context));
                final FileListAdapter adapter = new FileListAdapter(context);
                fileListView.setAdapter(adapter);
                fileListView.setHasFixedSize(true);

                try {
                    for (int i = 0; i < response.body().getEmbedded().getItems().size(); i++) {
                        fileListView.scrollToPosition(0);
                        adapter.add(response.body().getEmbedded().getItems().get(i));
                    }
                } catch (NullPointerException e) {
                    //TODO emptyFragment

                }
                swipeRefreshLayout.setRefreshing(false);

//                if (response.body().getEmbedded().getItems().isEmpty()) {
//                    // emptyFragment
//                }
//                else {
//                    for (int i = 0; i < response.body().getEmbedded().getItems().size(); i++) {
//                        fileListView.scrollToPosition(0);
//                        adapter.add(response.body().getEmbedded().getItems().get(i));
//                    }
//                }

            }

            @Override
            public void onFailure(Call<FilesArr> call, Throwable t) {
                //TODO empty fragment
                t.printStackTrace();
            }

        };

        yandexApi.getResources(path).enqueue(callback);
    }

}




