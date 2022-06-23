package com.whatscan.toolkit.forwa.Story;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.Adapter.StoryDownload;
import com.whatscan.toolkit.forwa.R;

import java.io.File;
import java.util.ArrayList;


public class FragmentDownload extends Fragment {
    public ArrayList<File> arrayList;

    public FragmentDownload() {
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_story, container, false);

        RecyclerView rv_status = view.findViewById(R.id.rv_status);
        LottieAnimationView la_empty = view.findViewById(R.id.la_empty);

        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + getString(R.string.app_directory) + "/Images/";
        String path_b = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + getString(R.string.app_directory) + "/Videos/";

        arrayList = new ArrayList<>();
        getListFiles(new File(path));
        getListFiles(new File(path_b));

        if (arrayList.isEmpty()) {
            la_empty.setVisibility(View.VISIBLE);
            rv_status.setVisibility(View.GONE);
        } else {
            rv_status.setVisibility(View.VISIBLE);
            la_empty.setVisibility(View.GONE);
            final StoryDownload adapterStoryVideo = new StoryDownload(arrayList, requireActivity());
            GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
            rv_status.setLayoutManager(manager);
            rv_status.setItemAnimator(new DefaultItemAnimator());
            rv_status.setAdapter(adapterStoryVideo);
            adapterStoryVideo.notifyDataSetChanged();
        }
        return view;
    }

    private void getListFiles(File file) {
        File[] listFiles = file.listFiles();
        if (listFiles != null) {
            for (File file2 : listFiles) {
                if (file2.getName().endsWith(".jpg") || file2.getName().endsWith(".mp4")) {
                    if (!arrayList.contains(file2)) {
                        arrayList.add(file2);
                    }
                }
            }
        }
    }
}