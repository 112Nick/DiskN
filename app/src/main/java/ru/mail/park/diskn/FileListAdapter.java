package ru.mail.park.diskn;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ru.mail.park.diskn.model.ResourceItem;


public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.FileListViewHolder> {
    private final LayoutInflater layoutInflater;
    private final List<ResourceItem> data;
    private final OnItemClickListener<ResourceItem> onItemClickListener;

    public FileListAdapter(Context context, OnItemClickListener<ResourceItem> onItemClickListener) {
        layoutInflater = LayoutInflater.from(context);
        this.data = new ArrayList<>();
        this.onItemClickListener = onItemClickListener;

    }

    @NonNull
    @Override
    public FileListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FileListViewHolder(layoutInflater.inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FileListViewHolder holder, int position) {
        holder.bind(data.get(position), onItemClickListener);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void add(ResourceItem newData) {
        data.add(0, newData);
        notifyItemInserted(0);
    }

    public void addToPosition(ResourceItem newData) {
        data.add(data.indexOf(newData), newData);
        notifyItemInserted(data.indexOf(newData));
    }

    public void remove(ResourceItem r) {
        notifyItemRemoved(data.indexOf(r));
        data.remove(r);
    }

    public void removeAll() {
        data.clear();
    }


    final static class FileListViewHolder extends RecyclerView.ViewHolder {
        private final TextView fileName;
        private final TextView modified;
        private final ImageView preview;
        private final Picasso picasso = Injector.getInstance().picasso;


        FileListViewHolder(View itemView) {
            super(itemView);
            fileName = itemView.findViewById(R.id.fileName);
            modified = itemView.findViewById(R.id.date);
            preview = itemView.findViewById(R.id.preview);
        }

        void bind(ResourceItem resourceItem, OnItemClickListener<ResourceItem> onItemClickListener) {

            if (resourceItem.isDirectory()) {
                preview.setImageResource(R.drawable.ic_flat_folder);
                fileName.setText(resourceItem.getName());
                modified.setText(resourceItem.getModified());

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

            }
            itemView.setOnClickListener(v -> onItemClickListener.onItemClick(resourceItem));
        }
    }
}


