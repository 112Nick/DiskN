package ru.mail.park.diskn.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class FilesFragment extends Fragment {
    private static final String PATH_EXTRA = "PATH_EXTRA";
    private final YandexApi yandexApi = Injector.getInstance().yandexApi;
    private RecyclerView fileListView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String path;

    public static FilesFragment newInstance(String path) {
        FilesFragment firstFragment = new FilesFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PATH_EXTRA, path);
        firstFragment.setArguments(bundle);
        return firstFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            path = arguments.getString(PATH_EXTRA);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle arguments = getArguments();
        if (arguments != null) {
            path = arguments.getString(PATH_EXTRA);
        }

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
        getResourcesList();
        swipeRefreshLayout.setOnRefreshListener(this::getResourcesList);

    }


    private void getResourcesList() {
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




