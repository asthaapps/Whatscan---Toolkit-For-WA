package com.whatscan.toolkit.forwa.DeletedMedia;

import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class FragmentMedia extends Fragment {

    static ActionMode actionMode;
    RecyclerView rv_media;
    ActionModeCallback actionModeCallback;
    ArrayList<File> fileArrayList;

    public FragmentMedia() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_media, container, false);

        rv_media = view.findViewById(R.id.rv_media);
        LottieAnimationView la_empty = view.findViewById(R.id.la_empty);
        TextView tv_empty = view.findViewById(R.id.tv_empty);

        actionModeCallback = new ActionModeCallback();
        fileArrayList = new ArrayList<>();

        File file = new File(Environment.getExternalStorageDirectory().toString() + "/" + getString(R.string.app_directory) + "/Deleted Media/");
        if (!file.exists()){
            file.mkdirs();
        }
        for (int i = 0; i < getListFiles(file).size(); i++) {
            fileArrayList.add(getListFiles(file).get(i));
            if (i == 1) {
                fileArrayList.add(new File(""));
            }
        }

        if (fileArrayList.isEmpty()) {
            la_empty.setVisibility(View.VISIBLE);
            tv_empty.setVisibility(View.VISIBLE);
            rv_media.setVisibility(View.GONE);
        } else {
            rv_media.setVisibility(View.VISIBLE);
            la_empty.setVisibility(View.GONE);
            tv_empty.setVisibility(View.GONE);
            AdapterMedia adapterMedia = new AdapterMedia(requireActivity(), fileArrayList);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
            rv_media.setLayoutManager(gridLayoutManager);
            rv_media.setAdapter(adapterMedia);
            adapterMedia.notifyDataSetChanged();
        }

        return view;
    }

    private ArrayList<File> getListFiles(File file) {
        ArrayList<File> arrayList = new ArrayList<>();
        File[] listFiles = file.listFiles((file12, str) -> !str.endsWith(".cached Files"));
        if (listFiles != null) {
            arrayList.addAll(Arrays.asList(listFiles));
            Collections.sort(arrayList, (file1, file2) -> {
                long lastModified = file2.lastModified() - file1.lastModified();
                if (lastModified > 0) {
                    return 1;
                }
                return lastModified < 1 ? -1 : 0;
            });
        }
        return arrayList;
    }

    private static class ActionModeCallback implements ActionMode.Callback {
        private ActionModeCallback() {
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            actionMode.finish();
            return true;
        }

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            FragmentMedia.actionMode = null;
        }
    }
}