package ru.mail.park.diskn;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mail.park.diskn.api.RetrofitFactory;
import ru.mail.park.diskn.api.YandexApi;
import ru.mail.park.diskn.fragment.FilesFragment;
import ru.mail.park.diskn.model.FilesArr;
import ru.mail.park.diskn.model.ResourceItem;


public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.FileListViewHolder> {
    private final LayoutInflater layoutInflater;
    private final List<ResourceItem> data;
    private final YandexApi yandexApi = new RetrofitFactory().create(YandexApi.class, Constants.YANDEX_BASE_URL);
    //TODO warning
    static Picasso picasso;


    public FileListAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.data = new ArrayList<>();
        picasso = new Picasso.Builder(context)
                .downloader(new OkHttp3Downloader(RetrofitFactory.httpClient))
                .build();

    }

    @NonNull
    @Override
    public FileListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FileListViewHolder(layoutInflater.inflate(R.layout.item, parent, false), this);
    }

    @Override
    public void onBindViewHolder(@NonNull FileListViewHolder holder, int position) {
        holder.bind(data.get(position));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void add(ResourceItem newData) {
        data.add(0, newData);
        notifyItemInserted(0);
    }

    public void remove(ResourceItem r) {
        notifyItemRemoved(data.indexOf(r));
        data.remove(r);
    }


    final static class FileListViewHolder extends RecyclerView.ViewHolder {
        private final TextView fileName;
        private final TextView modified;
        private final ImageView preview;
        private final Context context;
        private final FileListAdapter adapter;
        private final YandexApi yandexApi = new RetrofitFactory().create(YandexApi.class, Constants.YANDEX_BASE_URL);


        FileListViewHolder(View itemView, FileListAdapter parent) {
            super(itemView);
            context = itemView.getContext();
            fileName = itemView.findViewById(R.id.fileName);
            modified = itemView.findViewById(R.id.date);
            preview = itemView.findViewById(R.id.preview);
            adapter = parent;

        }

        void bind(ResourceItem resourceItem) {

            if (resourceItem.isDirectory()) {
                //
                preview.setImageResource(R.drawable.ic_flat_folder);
                fileName.setText(resourceItem.getName());
                modified.setText(resourceItem.getModified());

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MainActivity)context).getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, FilesFragment.newInstance(resourceItem.getPath()))
                                .addToBackStack("history")
                                .commit();
                    }
                });

            } else {
                if (resourceItem.getPreview() != null) {
                    picasso.load(resourceItem.getPreview())
                            .resize(70, 70)
                            .centerCrop()
                            .into(preview);
                } else {
                    preview.setImageResource(R.drawable.ic_file_unknown);
                }

                fileName.setText(resourceItem.getName());
                modified.setText(resourceItem.getModified());

                itemView.setOnClickListener(v -> {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                    //TODO warning
                    View dialogView = LayoutInflater.from(context).inflate(R.layout.item_detailed, null);
                    dialogBuilder.setView(dialogView);
                    AlertDialog dialog = dialogBuilder.create();
                    Button downloadBtn = dialogView.findViewById(R.id.delete);

                    TextView filenameDetailed = dialogView.findViewById(R.id.filename_detailed);
                    filenameDetailed.setText(resourceItem.getName());

                    TextView fileSize = dialogView.findViewById(R.id.size);
                    //TODO warning
                    fileSize.setText(resourceItem.getSize().toString());

                    TextView created = dialogView.findViewById(R.id.created);
                    created.setText(resourceItem.getCreated());

                    ImageView img = dialogView.findViewById(R.id.preview_detailed);

                    downloadBtn.setOnClickListener(v1 ->{

//                        deleteResource(context,resourceItem.getPath());
                        Callback callback = new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
//                                yandexApi.getResources()
                                adapter.remove(resourceItem);
                                dialog.hide();
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                t.printStackTrace();
                            }

                        };

                        yandexApi.deleteResource(resourceItem.getPath()).enqueue(callback);

                    });

                    dialog.show();
                });
            }

        }


    }



}


