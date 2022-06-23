package com.whatscan.toolkit.forwa.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Pager extends FragmentStatePagerAdapter {
    private final List<Fragment> nFrgLst = new ArrayList<>();
    private final List<String> nFrgTLst = new ArrayList<>();

    public Pager(@NonNull @NotNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {
        return nFrgLst.get(position);
    }

    @Override
    public int getCount() {
        return nFrgLst.size();
    }

    public void addFragment(Fragment fragment, String title) {
        this.nFrgLst.add(fragment);
        this.nFrgTLst.add(title);
    }

    public CharSequence getPageTitle(int position) {
        return nFrgTLst.get(position);
    }
}