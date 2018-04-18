package ru.mail.park.diskn.fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ru.mail.park.diskn.R;


public class EmptyFolderDialogFragment extends DialogFragment {

    public static EmptyFolderDialogFragment newInstance() {
        return new EmptyFolderDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.empty_folder_dialog, container, false);
    }

}









