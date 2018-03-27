package ru.mail.park.diskn;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nick on 25.03.18.
 */

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.FileListViewHolder> {
    private final LayoutInflater layoutInflater;
    private final List<String> data;
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
        holder.textView.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void add(String newData) {
        data.add(0, newData);
        notifyItemInserted(0);
    }


    final static class FileListViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        public FileListViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.fileName);
        }
    }
}


