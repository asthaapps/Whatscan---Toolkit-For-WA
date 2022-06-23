package com.whatscan.toolkit.forwa.MsgTools.Caption;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.whatscan.toolkit.forwa.R;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

public class SingleImageFragment extends Fragment {
    public static SingleImageFragment newinstance(String str) {
        SingleImageFragment SingleImageFragment = new SingleImageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("path", str);
        SingleImageFragment.setArguments(bundle);
        return SingleImageFragment;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_single, viewGroup, false);
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (getActivity() != null && getArguments() != null) {
            Glide.with(getActivity()).load(getArguments().getString("path")).into((ImageView) view);
        }
    }
}
