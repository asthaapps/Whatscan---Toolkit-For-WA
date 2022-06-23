package com.whatscan.toolkit.forwa.MsgTools.Caption;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.R;

import java.io.File;
import java.util.ArrayList;

public class FragmentPhoto extends Fragment {
    public RecyclerView recyclerView;
    public LottieAnimationView la_empty;
    public int curentsize = 0;

    public FragmentPhoto() {
        // Required empty public constructor
    }

    public static FragmentPhoto newInstance(ArrayList<String> arrayList) {
        FragmentPhoto fragment = new FragmentPhoto();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("data", arrayList);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        recyclerView = view.findViewById(R.id.photo_fav_recy);
        la_empty = view.findViewById(R.id.la_empty);

        new Thread(() -> {
            if (getArguments() != null) {
                ArrayList<String> stringArrayList = getArguments().getStringArrayList("data");
                curentsize = stringArrayList.size();

                if (stringArrayList.isEmpty()) {
                    la_empty.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    la_empty.setVisibility(View.GONE);
                }

                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                recyclerView.setAdapter(new SavedImageAdapter(getActivity(), stringArrayList));
            }
        }).start();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        check();
    }

    private void check() {
        new Thread(() -> {
            File[] listFiles;
            try {
                if (!(getActivity() == null || (listFiles = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES).listFiles((file, str) -> str.endsWith(".jpg"))) == null || curentsize == 0 || listFiles.length <= curentsize)) {
                    final ArrayList<String> arrayList = new ArrayList<>();
                    for (File file : listFiles) {
                        arrayList.add(file.getPath());
                    }
                    curentsize = arrayList.size();
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        public void run() {
                            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                            recyclerView.setAdapter(new SavedImageAdapter(getActivity(), arrayList));
                        }
                    });
                }
            } catch (Exception e) {
                Log.d("downloadedfrag", e.toString());
            }
        }).start();
    }
}