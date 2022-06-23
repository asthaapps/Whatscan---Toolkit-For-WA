package com.whatscan.toolkit.forwa.Story;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.Adapter.StoryImage;
import com.whatscan.toolkit.forwa.GetSet.StatusModel;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;

import java.util.ArrayList;

public class FragmentImage extends Fragment {

    private final ArrayList<StatusModel> statusModels;

    public FragmentImage(ArrayList<StatusModel> statusModels) {
        this.statusModels = statusModels;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_story, container, false);

        RecyclerView rv_status = view.findViewById(R.id.rv_status);
        LottieAnimationView la_empty = view.findViewById(R.id.la_empty);

        if (statusModels.isEmpty()) {
            la_empty.setVisibility(View.VISIBLE);
            rv_status.setVisibility(View.GONE);
        } else {
            rv_status.setVisibility(View.VISIBLE);
            la_empty.setVisibility(View.GONE);
            final StoryImage adapterStoryImage = new StoryImage(statusModels, requireActivity());
            GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
            rv_status.setLayoutManager(manager);
            rv_status.setItemAnimator(new DefaultItemAnimator());
            rv_status.setAdapter(adapterStoryImage);
            adapterStoryImage.notifyDataSetChanged();
        }
        return view;
    }
}