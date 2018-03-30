package ru.mail.park.diskn.model;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mail.park.diskn.Constants;
import ru.mail.park.diskn.FileListAdapter;
import ru.mail.park.diskn.FilesFragment;
import ru.mail.park.diskn.R;
import ru.mail.park.diskn.api.RetrofitFactory;
import ru.mail.park.diskn.api.YandexApi;

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

        FragmentActivity activity = getActivity();
//        if (activity instanceof Smth) {
//            smth = (Smth) activity;
//        } else {
//            throw new IllegalArgumentException("");
//        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.trash_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        fileListView = view.findViewById(R.id.fileList);
//        Button button = view.findViewById(R.id.button);
//        button.setOnClickListener(v -> {
//            smth.goNext();
//            requireFragmentManager().beginTransaction()
//                    .add(R.id.container, new SecondFragment())
//                    .addToBackStack(null)
//                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                    .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_in_right)
//                    .commit();
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getResourcesList();

//        String name = getArguments().getString(NAME_EXTRA);
    }


    private void getTrashResources() {
        Callback<Test> callback = new Callback<Test>() {

            @Override
            public void onResponse(Call<Test> call, Response<Test> response) {
//                Log.d("MyTag", String.valueOf(response.body()));
//                final RecyclerView fileListView = findViewById(R.id.fileList);
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
                        Log.d("MyFILENAME", response.body().getEmbedded().getItems().get(i).getName());
                        adapter.add(response.body().getEmbedded().getItems().get(i));
                    }
                }

                /////////////////


            }

            @Override
            public void onFailure(Call<Test> call, Throwable t) {
                t.printStackTrace();
            }
        };
        yandexApi.getTrashResources().enqueue(callback);
    }

}




