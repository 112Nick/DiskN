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
import ru.mail.park.diskn.FileListAdapter;
import ru.mail.park.diskn.Injector;
import ru.mail.park.diskn.R;
import ru.mail.park.diskn.api.YandexApi;
import ru.mail.park.diskn.model.FilesArr;

public class TrashFragment extends Fragment {

    private final YandexApi yandexApi = Injector.getInstance().yandexApi;
    private RecyclerView fileListView;
    private SwipeRefreshLayout swipeRefreshLayout;


    public static TrashFragment newInstance() {
        return new TrashFragment();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.trash_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        fileListView = view.findViewById(R.id.fileList);
        swipeRefreshLayout = view.findViewById(R.id.swipe);
        getTrashResources();
        swipeRefreshLayout.setOnRefreshListener(this::getTrashResources);
    }


    private void getTrashResources() {
        Callback<FilesArr> callback = new Callback<FilesArr>() {

            @Override
            public void onResponse(Call<FilesArr> call, Response<FilesArr> response) {
                fileListView.setLayoutManager(new LinearLayoutManager(getContext()));
                final FileListAdapter adapter = new FileListAdapter(getContext());
                fileListView.setAdapter(adapter);
                fileListView.setHasFixedSize(true);

                FilesArr body = response.body();
                if (body != null) {
                    for (int i = 0; i < body.getEmbedded().getItems().size(); i++) {
                        fileListView.scrollToPosition(0);
                        adapter.add(body.getEmbedded().getItems().get(i));
                    }
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
                //TODO emptyFragment
                t.printStackTrace();
            }
        };
        yandexApi.getTrashResources().enqueue(callback);
    }

}




