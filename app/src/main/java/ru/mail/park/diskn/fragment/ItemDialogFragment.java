package ru.mail.park.diskn.fragment;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mail.park.diskn.Injector;
import ru.mail.park.diskn.R;
import ru.mail.park.diskn.api.YandexApi;
import ru.mail.park.diskn.model.ResourceItem;

public class ItemDialogFragment extends DialogFragment {

    private static final String ITEM = "ITEM";
    private static final String IS_TRASH = "IS_TRASH";

    @SuppressWarnings("FieldCanBeLocal")
    private TextView filenameDetailed;
    @SuppressWarnings("FieldCanBeLocal")
    private TextView mediaType;
    @SuppressWarnings("FieldCanBeLocal")
    private TextView fileSize;
    @SuppressWarnings("FieldCanBeLocal")
    private TextView created;
    @SuppressWarnings("FieldCanBeLocal")
    private TextView modified;
    private Button downloadBtn;
    private Button deleteBtn;
    private Button copyBtn;
    private Button restoreBtn;
    @SuppressWarnings("FieldCanBeLocal")
    private ImageView previewDetailed;

    private ResourceItem resourceItem;
    private Boolean isTrash;
    private final YandexApi yandexApi = Injector.getInstance().yandexApi;

    private Delegate delegate;

    public static ItemDialogFragment newInstance(ResourceItem resourceItem, boolean isTrash) {
        ItemDialogFragment itemDialogFragment = new ItemDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ITEM, resourceItem);
        bundle.putBoolean(IS_TRASH, isTrash);
        itemDialogFragment.setArguments(bundle);
        return itemDialogFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            resourceItem = arguments.getParcelable(ITEM);
            isTrash = arguments.getBoolean(IS_TRASH);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof Delegate) {
            delegate = (Delegate) parentFragment;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (isTrash) {
            return inflater.inflate(R.layout.item_detailed_trash, container, false);

        }
        return inflater.inflate(R.layout.item_detailed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (isTrash) {
            restoreBtn = view.findViewById(R.id.restore);
        } else {
            deleteBtn = view.findViewById(R.id.delete);
            copyBtn = view.findViewById(R.id.copy);
            downloadBtn = view.findViewById(R.id.download);

        }

        previewDetailed = view.findViewById(R.id.preview_detailed);
        filenameDetailed = view.findViewById(R.id.filename_detailed);
        mediaType = view.findViewById(R.id.media_type);
        fileSize = view.findViewById(R.id.size);
        created = view.findViewById(R.id.created);
        modified = view.findViewById(R.id.modified);

        if (resourceItem.getPreview() != null) {
            Injector.getInstance().picasso.load(resourceItem.getPreview())
                    .resize(130, 130)
                    .centerCrop()
                    .into(previewDetailed);
        } else {
            previewDetailed.setImageResource(R.drawable.ic_file_unknown);
        }

        mediaType.append(resourceItem.getMedia_type());
        filenameDetailed.setText(resourceItem.getName());
        fileSize.append(resourceItem.getSize());
        created.append(resourceItem.getCreated());
        modified.append(resourceItem.getModified());

        if (isTrash) {
            restoreBtn.setOnClickListener(v12 -> {
                this.dismiss();
                Callback<Void> callback = new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        delegate.onRestore(resourceItem);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        t.printStackTrace();
                    }

                };
                Log.d("PATH", resourceItem.getPath());
                yandexApi.restoreResource(resourceItem.getPath()).enqueue(callback);

            });
        } else {
            downloadBtn.setOnClickListener(v12 -> {
                this.dismiss();
                if (getContext() != null) {
                    DownloadManager downloadManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri uri = Uri.parse(resourceItem.getFileURL());
                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    if (downloadManager != null) {
                        downloadManager.enqueue(request);
                    }
                }
            });


            deleteBtn.setOnClickListener(v1 -> {
                this.dismiss();
                Callback<Void> callback = new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        yandexApi.getResources(resourceItem.getPath());
                        delegate.onDelete(resourceItem);

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        t.printStackTrace();
                    }

                };

                yandexApi.deleteResource(resourceItem.getPath()).enqueue(callback);

            });

            copyBtn.setOnClickListener(v1 -> {
                this.dismiss();
                Callback<Void> callback = new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        delegate.onMakeCopy(resourceItem);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        t.printStackTrace();
                    }

                };
                yandexApi.copyResource(resourceItem.getPath(), resourceItem.getFolder() + "Copy_" + resourceItem.getName()).enqueue(callback);

            });
        }


    }

    public interface Delegate {
        void onDelete(ResourceItem resourceItem);
        void onRestore(ResourceItem resourceItem);
        void onMakeCopy(ResourceItem resourceItem);
    }
}

