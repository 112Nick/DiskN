package ru.mail.park.diskn.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mail.park.diskn.FileListAdapter;
import ru.mail.park.diskn.Injector;
import ru.mail.park.diskn.MainActivity;
import ru.mail.park.diskn.R;
import ru.mail.park.diskn.api.YandexApi;
import ru.mail.park.diskn.model.FilesArr;
import ru.mail.park.diskn.model.ResourceItem;

public class FilesFragment extends Fragment implements ItemDialogFragment.Delegate {
    private static final String PATH_EXTRA = "PATH_EXTRA";
    private static final String TYPE = "TYPE";
    private static final String TRASH = "trash";
    private static final String TYPE_FILES = "files";

    private final YandexApi yandexApi = Injector.getInstance().yandexApi;
    private RecyclerView fileListView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar pBar;
    private String path;
    private String type;

    private FileListAdapter adapter;

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
        adapter = new FileListAdapter(getContext(), this::onResourceItemClick);


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
        pBar = view.findViewById(R.id.progressBar);
        pBar.setVisibility(View.GONE);


        if (getContext() != null) {
            if (type.equals(TRASH)) {
                getTrashResources();
                pBar.setVisibility(View.VISIBLE);
                FloatingActionButton deleteAll = view.findViewById(R.id.fab);
                deleteAll.setOnClickListener(v -> {
                    if (getContext() != null) {
                        FragmentManager manager = ((MainActivity) getContext()).getSupportFragmentManager();
                        DeleteConfirmDialogFragment deleteConfirmDialogFragment = DeleteConfirmDialogFragment.newInstance();
                        deleteConfirmDialogFragment.show(manager, "confirm");
                    }

                });
                if (getContext() != null) {
                    ((MainActivity) getContext()).setTitle(R.string.trash_title);
                }
                ((MainActivity) getContext()).setTitle(R.string.trash_title);
                swipeRefreshLayout.setOnRefreshListener(this::getTrashResources);

            } else {

                getResourcesList();
                pBar.setVisibility(View.VISIBLE);
                if (getContext() != null) {
                    ((MainActivity) getContext()).setTitle(R.string.files_title);
                }
                swipeRefreshLayout.setOnRefreshListener(this::getResourcesList);

            }
        }


    }


    private void getTrashResources() {
        Callback<FilesArr> callback = new Callback<FilesArr>() {

            @Override
            public void onResponse(Call<FilesArr> call, Response<FilesArr> response) {
                fileListView.setLayoutManager(new LinearLayoutManager(getContext()));
                fileListView.setAdapter(adapter);
                fileListView.setHasFixedSize(true);
                pBar.setVisibility(View.GONE);

                FilesArr body = response.body();
                if (body != null) {
                    if (body.getEmbedded().getItems().isEmpty()) {
                        if (getContext() != null) {
                            android.support.v4.app.FragmentManager manager = ((MainActivity) getContext()).getSupportFragmentManager();
                            EmptyFolderDialogFragment emptyFolderDialogFragment = EmptyFolderDialogFragment.newInstance();
                            emptyFolderDialogFragment.show(manager, "empty");
                        }
                    }
                    adapter.removeAll();
                    for (int i = body.getEmbedded().getItems().size() - 1; i >= 0; i--) {
                        fileListView.scrollToPosition(0);
                        adapter.add(body.getEmbedded().getItems().get(i));
                    }
                }
                swipeRefreshLayout.setRefreshing(false);
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
                fileListView.setAdapter(adapter);
                fileListView.setHasFixedSize(true);
                pBar.setVisibility(View.GONE);
                FilesArr body = response.body();
                if (body != null) {
                    if (body.getEmbedded().getItems().isEmpty()) {
                        if (getContext() != null) {
                            android.support.v4.app.FragmentManager manager = ((MainActivity) getContext()).getSupportFragmentManager();
                            EmptyFolderDialogFragment emptyFolderDialogFragment = EmptyFolderDialogFragment.newInstance();
                            emptyFolderDialogFragment.show(manager, "empty");
                        }

                    }
                    adapter.removeAll();
                    for (int i = body.getEmbedded().getItems().size() - 1; i >= 0; i--) {
                        fileListView.scrollToPosition(0);
                        adapter.add(body.getEmbedded().getItems().get(i));
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

    private void onResourceItemClick(ResourceItem resourceItem) {
        if (resourceItem.isDirectory()) {
            if (type.equals(TYPE_FILES)) {
                requireFragmentManager().beginTransaction()
                        .replace(R.id.container, FilesFragment.newInstance(resourceItem.getPath(), type))
                        .addToBackStack(null)
                        .commit();
            }
        } else {
            if (type.equals(TYPE_FILES)) {
                FragmentManager manager = getChildFragmentManager();
                ItemDialogFragment itemDialogFragment = ItemDialogFragment.newInstance(resourceItem, false);
                itemDialogFragment.show(manager, "files");


            } else {
                FragmentManager manager = getChildFragmentManager();
                ItemDialogFragment itemDialogFragment = ItemDialogFragment.newInstance(resourceItem, true);
                itemDialogFragment.show(manager, "trash");

            }
        }
    }

    @Override
    public void onDelete(ResourceItem resourceItem) {
        adapter.remove(resourceItem);
    }

    @Override
    public void onRestore(ResourceItem resourceItem) {
        adapter.remove(resourceItem);
    }

    @Override
    public void onMakeCopy(ResourceItem resourceItem) {
        adapter.addToPosition(resourceItem);
    }
}




