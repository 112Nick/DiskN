package ru.mail.park.diskn;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ru.mail.park.diskn.model.ResourceItem;

/**
 * Created by nick on 25.03.18.
 */

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.FileListViewHolder> {
    private final LayoutInflater layoutInflater;
    private final List<ResourceItem> data;


    public FileListAdapter (Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.data = new ArrayList<>();
    }

    @Override
    public FileListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FileListViewHolder(layoutInflater.inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(FileListViewHolder holder, int position) {
        Picasso.get()
                .load(data.get(position).getPreview())
                .resize(70, 70)
                .centerCrop()
                .into(holder.preview);
//        Log.d("MyPreview", data.get(position).getPreview());
        holder.fileName.setText(data.get(position).getName());
        holder.modified.setText(data.get(position).getModified());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void add(ResourceItem newData) {
        data.add(0, newData);
        notifyItemInserted(0);
    }


    final static class FileListViewHolder extends RecyclerView.ViewHolder {
        private final TextView fileName;
        private final TextView modified;
        private final ImageView preview;
        public FileListViewHolder(View itemView) {
            super(itemView);
            fileName = itemView.findViewById(R.id.fileName);
            modified = itemView.findViewById(R.id.date);
            preview = itemView.findViewById(R.id.preview);
        }
    }
}


