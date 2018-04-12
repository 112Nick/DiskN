//package ru.mail.park.diskn.fragment;
//
//import android.app.AlertDialog;
//import android.content.Context;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.design.widget.FloatingActionButton;
//import android.support.v4.app.Fragment;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import ru.mail.park.diskn.FileListAdapter;
//import ru.mail.park.diskn.Injector;
//import ru.mail.park.diskn.MainActivity;
//import ru.mail.park.diskn.R;
//import ru.mail.park.diskn.api.YandexApi;
//import ru.mail.park.diskn.model.FilesArr;
//
//public class TrashFragment extends Fragment {
//
//    private final YandexApi yandexApi = Injector.getInstance().yandexApi;
//    private RecyclerView fileListView;
//    private SwipeRefreshLayout swipeRefreshLayout;
//    private FloatingActionButton deleteAll;
//
//
//    public static TrashFragment newInstance() {
//        return new TrashFragment();
//    }
//
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.trash_fragment, container, false);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        fileListView = view.findViewById(R.id.fileList);
//        swipeRefreshLayout = view.findViewById(R.id.swipe);
//        deleteAll = view.findViewById(R.id.fab);
//        getTrashResources();
//        deleteAll.setOnClickListener(v -> {
//            Log.d("CLEAR", "TRASH");
//            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
//            //TODO warning
//            View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.delete_confirm, null);
//            dialogBuilder.setView(dialogView);
//            AlertDialog dialog = dialogBuilder.create();
//            Button yesBtn = dialogView.findViewById(R.id.confirm);
//            Button noBtn = dialogView.findViewById(R.id.no_confirm);
//            dialog.show();
//
//            yesBtn.setOnClickListener(v1 -> {
//                deleteAll();
//                dialog.hide();
//                getTrashResources();
//            });
//            noBtn.setOnClickListener(v12 -> dialog.hide());
//
//        });
//        if (getContext() != null) {
//            ((MainActivity) getContext()).setTitle(R.string.trash_title);
//        }
//        swipeRefreshLayout.setOnRefreshListener(this::getTrashResources);
//    }
//
//    private void deleteAll() {
//
//        Callback<Void> callback = new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                getTrashResources();
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                t.printStackTrace();
//            }
//
//        };
//        yandexApi.deleteAllFromTrash().enqueue(callback);
//    }
//
//    private void getTrashResources() {
//        Callback<FilesArr> callback = new Callback<FilesArr>() {
//
//            @Override
//            public void onResponse(Call<FilesArr> call, Response<FilesArr> response) {
//                fileListView.setLayoutManager(new LinearLayoutManager(getContext()));
//                final FileListAdapter adapter = new FileListAdapter(getContext());
//                fileListView.setAdapter(adapter);
//                fileListView.setHasFixedSize(true);
//
//                FilesArr body = response.body();
//                if (body != null) {
//                    for (int i = body.getEmbedded().getItems().size() - 1; i >= 0; i--) {
//                        fileListView.scrollToPosition(0);
//                        adapter.add(body.getEmbedded().getItems().get(i));
//                    }
//                }
//
//
//                swipeRefreshLayout.setRefreshing(false);
//
////
//
//            }
//
//            @Override
//            public void onFailure(Call<FilesArr> call, Throwable t) {
//                //TODO emptyFragment
//                t.printStackTrace();
//            }
//        };
//        yandexApi.getTrashResources().enqueue(callback);
//    }
//
//}
//
//
//
//
