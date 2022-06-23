package com.whatscan.toolkit.forwa.MsgTools.Caption;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.whatscan.toolkit.forwa.Adapter.Pager;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

public class FavouriteFragment extends Fragment {
    public TabLayout tabLayout;
    public ViewPager viewPager;
    public ArrayList<String> arrayList;
    public ArrayList<String> arrayList2;
    public FragmentActivity listener;

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.listener = (FragmentActivity) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arrayList2 = new ArrayList<>();
        arrayList = new FavData().getdata(listener);

        File[] listFiles = listener.getExternalFilesDir(Environment.DIRECTORY_PICTURES).listFiles((file, str) -> str.endsWith(".jpg"));

        if (listFiles != null) {
            for (File file : listFiles) {
                arrayList2.add(file.getPath());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favourite, parent, false);
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabLayout = view.findViewById(R.id.tab_story);
        viewPager = view.findViewById(R.id.viewPager);

        if (Preference.getBooleanTheme(false)){
            tabLayout.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.darkBlack));
            tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(requireContext(), R.color.colorWhite));
            tabLayout.setTabTextColors(ContextCompat.getColor(requireContext(), R.color.colorAccent), ContextCompat.getColor(requireContext(), R.color.colorWhite));
        }

        Pager pager = new Pager(getChildFragmentManager());
        pager.addFragment(FragmentText.newInstance(arrayList), "Text Favourites");
        pager.addFragment(FragmentPhoto.newInstance(arrayList2), "Photos Favourites");
        viewPager.setAdapter(pager);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.listener = null;
    }
}