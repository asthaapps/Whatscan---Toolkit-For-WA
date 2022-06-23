package com.whatscan.toolkit.forwa.MsgTools.Caption;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FragmentText extends Fragment {
    public FavAdapter favAdapter;
    public RecyclerView recyclerView;
    public FragmentActivity listener;
    public LottieAnimationView la_empty;
    public ArrayList<String> stringArrayList;

    public static FragmentText newInstance(ArrayList<String> arrayList) {
        FragmentText fragmentText = new FragmentText();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("listText", arrayList);
        fragmentText.setArguments(bundle);
        return fragmentText;
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.listener = (FragmentActivity) context;
        }
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_text, container, false);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.text_fav_recy);
        la_empty = view.findViewById(R.id.la_empty);

        if (getActivity() != null && getArguments() != null) {
            stringArrayList = getArguments().getStringArrayList("listText");

            if (stringArrayList.isEmpty()) {
                la_empty.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                la_empty.setVisibility(View.GONE);
            }
        }

        favAdapter = new FavAdapter(getActivity(), stringArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(favAdapter);
        favAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}