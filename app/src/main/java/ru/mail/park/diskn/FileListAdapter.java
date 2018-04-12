package ru.mail.park.diskn;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mail.park.diskn.api.YandexApi;
import ru.mail.park.diskn.fragment.FilesFragment;
import ru.mail.park.diskn.model.ResourceItem;


public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.FileListViewHolder> {
    //TODO warning
    private final LayoutInflater layoutInflater;
    private final List<ResourceItem> data;
    private final String type;


    public FileListAdapter(Context context, String type) {
        layoutInflater = LayoutInflater.from(context);
        this.data = new ArrayList<>();
        this.type = type;

    }

    @NonNull
    @Override
    public FileListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FileListViewHolder(layoutInflater.inflate(R.layout.item, parent, false), this);
    }

    @Override
    public void onBindViewHolder(@NonNull FileListViewHolder holder, int position) {
        holder.bind(data.get(position), type);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void add(ResourceItem newData) {
        data.add(0, newData);
        notifyItemInserted(0);
    }
    private void addToPosition(ResourceItem newData) {
        data.add(data.indexOf(newData), newData);
        notifyItemInserted(data.indexOf(newData));
    }

    private void remove(ResourceItem r) {
        notifyItemRemoved(data.indexOf(r));
        data.remove(r);
    }


    final static class FileListViewHolder extends RecyclerView.ViewHolder {
        private final TextView fileName;
        private final TextView modified;
        //        private final TextView mediaType;
        private final ImageView preview;
        private final Context context;
        private final FileListAdapter adapter;
        private final YandexApi yandexApi = Injector.getInstance().yandexApi;
        private final Picasso picasso = Injector.getInstance().picasso;


        FileListViewHolder(View itemView, FileListAdapter parent) {
            super(itemView);
            context = itemView.getContext();
            fileName = itemView.findViewById(R.id.fileName);
            modified = itemView.findViewById(R.id.date);
            preview = itemView.findViewById(R.id.preview);

            adapter = parent;

        }

        void bind(ResourceItem resourceItem, String type) {

            if (resourceItem.isDirectory()) {
                //
                preview.setImageResource(R.drawable.ic_flat_folder);
                fileName.setText(resourceItem.getName());
                modified.setText(resourceItem.getModified());
                if (type.equals("files")) {
                    itemView.setOnClickListener(v -> (
                            (MainActivity) context).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, FilesFragment.newInstance(resourceItem.getPath(), type))
                            .addToBackStack(null)
                            .commit());
                }


            } else {
                if (resourceItem.getPreview() != null) {
                    picasso.load(resourceItem.getPreview())
                            .resize(90, 90)
                            .centerCrop()
                            .into(preview);
                } else {
                    preview.setImageResource(R.drawable.ic_file_unknown);
                }

                fileName.setText(resourceItem.getName());
                modified.setText(resourceItem.getModified());

                if (type.equals("files")) {
                    itemView.setOnClickListener(v -> {
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                        //TODO warning
                        View dialogView = LayoutInflater.from(context).inflate(R.layout.item_detailed, null);
                        dialogBuilder.setView(dialogView);
                        AlertDialog dialog = dialogBuilder.create();
                        Button downloadBtn = dialogView.findViewById(R.id.download);
                        Button deleteBtn = dialogView.findViewById(R.id.delete);
                        Button copyBtn = dialogView.findViewById(R.id.copy);
                        ImageView previewDetailed = dialogView.findViewById(R.id.preview_detailed);


                        if (resourceItem.getPreview() != null) {
                            picasso.load(resourceItem.getPreview())
                                    .resize(130,130)
                                    .centerCrop()
                                    .into(previewDetailed);
                            Log.d("FileURL", resourceItem.getFileURL());
                        } else {
                            previewDetailed.setImageResource(R.drawable.ic_file_unknown);
                        }

                        TextView filenameDetailed = dialogView.findViewById(R.id.filename_detailed);

                        TextView mediaType = dialogView.findViewById(R.id.media_type);
                        mediaType.append(resourceItem.getMedia_type());


                        filenameDetailed.setText(resourceItem.getName());

                        TextView fileSize = dialogView.findViewById(R.id.size);
                        //TODO warning
                        fileSize.append(resourceItem.getSize());

                        TextView created = dialogView.findViewById(R.id.created);
                        created.append(resourceItem.getCreated());

                        TextView modified = dialogView.findViewById(R.id.modified);
                        modified.append(resourceItem.getModified());

                        downloadBtn.setOnClickListener(v12 -> {
                            dialog.hide();
                            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                            Uri uri = Uri.parse(resourceItem.getFileURL());
                            DownloadManager.Request request = new DownloadManager.Request(uri);
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            if (downloadManager != null) {
                                downloadManager.enqueue(request);
                            }
                        });


                        deleteBtn.setOnClickListener(v1 -> {

                            Callback<Void> callback = new Callback<Void>() {
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

                        copyBtn.setOnClickListener(v1 -> {

//                        deleteResource(context,resourceItem.getPath());
                            Callback<Void> callback = new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    adapter.addToPosition(resourceItem);
                                    dialog.hide();
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    t.printStackTrace();
                                }

                            };
                            yandexApi.copyResource(resourceItem.getPath(), resourceItem.getFolder()+"Copy_"+resourceItem.getName()).enqueue(callback);

                        });

                        dialog.show();
                    });
                } else {
                    itemView.setOnClickListener(v -> {
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                        //TODO warning
                        View dialogView = LayoutInflater.from(context).inflate(R.layout.item_detailed_trash, null);
                        dialogBuilder.setView(dialogView);
                        AlertDialog dialog = dialogBuilder.create();
                        Button restoreBtn = dialogView.findViewById(R.id.restore);

                        ImageView previewDetailed = dialogView.findViewById(R.id.preview_detailed);


                        if (resourceItem.getPreview() != null) {
                            picasso.load(resourceItem.getPreview())
                                    .resize(130,130)
                                    .centerCrop()
                                    .into(previewDetailed);
                            Log.d("FileURL", resourceItem.getFileURL());
                        } else {
                            previewDetailed.setImageResource(R.drawable.ic_file_unknown);
                        }

                        TextView filenameDetailed = dialogView.findViewById(R.id.filename_detailed);

                        TextView mediaType = dialogView.findViewById(R.id.media_type);
                        mediaType.append(resourceItem.getMedia_type());


                        filenameDetailed.setText(resourceItem.getName());

                        TextView fileSize = dialogView.findViewById(R.id.size);
                        //TODO warning
                        fileSize.append(resourceItem.getSize());

                        TextView created = dialogView.findViewById(R.id.created);
                        created.append(resourceItem.getCreated());

                        TextView modified = dialogView.findViewById(R.id.modified);
                        modified.append(resourceItem.getModified());



                        restoreBtn.setOnClickListener(v12 -> {
                            Callback<Void> callback = new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
//                                yandexApi.getResources()
                                    Log.d("PATH", String.valueOf(response.code()));

                                    adapter.remove(resourceItem);
                                    dialog.hide();
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    t.printStackTrace();
                                }

                            };
                            Log.d("PATH", resourceItem.getPath());
                            yandexApi.restoreResource(resourceItem.getPath()).enqueue(callback);

                        });

                        dialog.show();
                    });
                }

            }

        }


    }


}


