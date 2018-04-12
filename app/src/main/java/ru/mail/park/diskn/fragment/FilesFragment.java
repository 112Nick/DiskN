package ru.mail.park.diskn.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mail.park.diskn.FileListAdapter;
import ru.mail.park.diskn.Injector;
import ru.mail.park.diskn.MainActivity;
import ru.mail.park.diskn.R;
import ru.mail.park.diskn.api.YandexApi;
import ru.mail.park.diskn.model.FilesArr;

public class FilesFragment extends Fragment {
    private static final String PATH_EXTRA = "PATH_EXTRA";
    private static final String TYPE = "TYPE";
    private static final String TRASH = "trash";
    private final YandexApi yandexApi = Injector.getInstance().yandexApi;
    private RecyclerView fileListView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String path;
    private String type;

    public static FilesFragment newInstance(String path, String type) {
        FilesFragment firstFragment = new FilesFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PATH_EXTRA, path);
        bundle.putString(TYPE, type);
        firstFragment.setArguments(bundle);
        return firstFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            path = arguments.getString(PATH_EXTRA);
            type = arguments.getString(TYPE);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle arguments = getArguments();
        if (arguments != null) {
            path = arguments.getString(PATH_EXTRA);
            type = arguments.getString(TYPE);

        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (type.equals(TRASH)) {
            return inflater.inflate(R.layout.trash_fragment, container, false);
        } else {
            return inflater.inflate(R.layout.files_fragment, container, false);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        fileListView = view.findViewById(R.id.fileList);
        swipeRefreshLayout = view.findViewById(R.id.swipe);
        if (getContext() != null) {
            if(type.equals(TRASH)) {
                getTrashResources();
                FloatingActionButton deleteAll = view.findViewById(R.id.fab);
                getTrashResources();
                deleteAll.setOnClickListener(v -> {
                    Log.d("CLEAR", "TRASH");
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                    //TODO warning
                    View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.delete_confirm, null);
                    dialogBuilder.setView(dialogView);
                    AlertDialog dialog = dialogBuilder.create();
                    Button yesBtn = dialogView.findViewById(R.id.confirm);
                    Button noBtn = dialogView.findViewById(R.id.no_confirm);
                    dialog.show();

                    yesBtn.setOnClickListener(v1 -> {
                        deleteAll();
                        dialog.hide();
                    });
                    noBtn.setOnClickListener(v12 -> dialog.hide());

                });
                if (getContext() != null) {
                    ((MainActivity) getContext()).setTitle(R.string.trash_title);
                }
                ((MainActivity) getContext()).setTitle(R.string.trash_title);
                swipeRefreshLayout.setOnRefreshListener(this::getTrashResources);

            } else {

                getResourcesList();
                if (getContext() != null) {
                    ((MainActivity) getContext()).setTitle(R.string.files_title);
                }
                swipeRefreshLayout.setOnRefreshListener(this::getResourcesList);

            }
        }


    }

    private void deleteAll() {

        Callback<Void> callback = new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                getTrashResources();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }

        };
        yandexApi.deleteAllFromTrash().enqueue(callback);
    }

    private void getTrashResources() {
        Callback<FilesArr> callback = new Callback<FilesArr>() {

            @Override
            public void onResponse(Call<FilesArr> call, Response<FilesArr> response) {
                fileListView.setLayoutManager(new LinearLayoutManager(getContext()));
                final FileListAdapter adapter = new FileListAdapter(getContext(), type);
                fileListView.setAdapter(adapter);
                fileListView.setHasFixedSize(true);

                FilesArr body = response.body();
                if (body != null) {
//                    if (body.getEmbedded().getItems().isEmpty()) {
//                        ((MainActivity)getContext()).getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.container, EmptyFragment.newInstance())
//                                .commit();
//                    }
                    for (int i = body.getEmbedded().getItems().size() - 1; i >= 0; i--) {
                        fileListView.scrollToPosition(0);
                        adapter.add(body.getEmbedded().getItems().get(i));
                    }
                }


                swipeRefreshLayout.setRefreshing(false);

//

            }

            @Override
            public void onFailure(Call<FilesArr> call, Throwable t) {
                t.printStackTrace();
            }
        };
        yandexApi.getTrashResources(path).enqueue(callback);
    }


    private void getResourcesList() {
        Callback<FilesArr> callback = new Callback<FilesArr>() {

            @Override
            public void onResponse(Call<FilesArr> call, Response<FilesArr> response) {
                fileListView.setLayoutManager(new LinearLayoutManager(getContext()));
                final FileListAdapter adapter = new FileListAdapter(getContext(), type);
                fileListView.setAdapter(adapter);
                fileListView.setHasFixedSize(true);

                FilesArr body = response.body();
                if (body != null) {
                    for (int i = body.getEmbedded().getItems().size() - 1; i >= 0; i--) {
                        fileListView.scrollToPosition(0);
                        adapter.add(body.getEmbedded().getItems().get(i));
//                        Log.d("PATH", body.getEmbedded().getItems().get(i).getFolder());
                    }
                }

                swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<FilesArr> call, Throwable t) {
                t.printStackTrace();
            }

        };

        yandexApi.getResources(path).enqueue(callback);
    }

}




