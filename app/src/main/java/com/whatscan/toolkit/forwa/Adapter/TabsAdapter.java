package com.whatscan.toolkit.forwa.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.whatscan.toolkit.forwa.Cleaner.FilesFragment;
import com.whatscan.toolkit.forwa.Util.Constant;

public class TabsAdapter extends FragmentStatePagerAdapter {
    public String receivedPath;
    public String sentPath;
    public String category;

    public TabsAdapter(FragmentManager fm, String category, String receivedPath, String sentPath) {
        super(fm);
        this.category = category;
        this.receivedPath = receivedPath;
        this.sentPath = sentPath;
    }

    @NonNull
    @Override
    public Fragment getItem(int i) {
        switch (category) {
            default:
            case Constant.IMAGE:
                switch (i) {
                    default:
                    case 0:
                        return FilesFragment.newInstance(Constant.IMAGE, receivedPath);
                    case 1:
                        return FilesFragment.newInstance(Constant.IMAGE, sentPath);
                }
            case Constant.DOCUMENT:
                switch (i) {
                    default:
                    case 0:
                        return FilesFragment.newInstance(Constant.DOCUMENT, receivedPath);
                    case 1:
                        return FilesFragment.newInstance(Constant.DOCUMENT, sentPath);
                }
            case Constant.VIDEO:
                switch (i) {
                    default:
                    case 0:
                        return FilesFragment.newInstance(Constant.VIDEO, receivedPath);
                    case 1:
                        return FilesFragment.newInstance(Constant.VIDEO, sentPath);
                }
            case Constant.AUDIO:
                switch (i) {
                    default:
                    case 0:
                        return FilesFragment.newInstance(Constant.AUDIO, receivedPath);
                    case 1:
                        return FilesFragment.newInstance(Constant.AUDIO, sentPath);
                }
            case Constant.GIF:
                switch (i) {
                    default:
                    case 0:
                        return FilesFragment.newInstance(Constant.GIF, receivedPath);
                    case 1:
                        return FilesFragment.newInstance(Constant.GIF, sentPath);
                }
            case Constant.WALLPAPER:
                switch (i) {
                    default:
                    case 0:
                        return FilesFragment.newInstance(Constant.WALLPAPER, receivedPath);
                    case 1:
                        return FilesFragment.newInstance(Constant.WALLPAPER, sentPath);
                }
            case Constant.VOICE:
                switch (i) {
                    default:
                    case 0:
                        return FilesFragment.newInstance(Constant.VOICE, receivedPath);
                    case 1:
                        return FilesFragment.newInstance(Constant.VOICE, sentPath);
                }
            case Constant.NONDEFAULT:
                switch (i) {
                    default:
                    case 0:
                        return FilesFragment.newInstance(Constant.NONDEFAULT, receivedPath);
                    case 1:
                        return FilesFragment.newInstance(Constant.NONDEFAULT, sentPath);
                }
        }

    }

    @Override
    public int getCount() {
        if (!category.equals(Constant.NONDEFAULT))
            return 2;
        else
            return 1;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (!category.equals(Constant.NONDEFAULT)) {
            switch (position) {
                case 0:
                    return "Recieved Files";
                case 1:
                    return "Sent Files";
                default:
                    return "";
            }
        } else {
            String folderName = receivedPath.substring(receivedPath.indexOf("a/") + 2);
            if (folderName.startsWith("WhatsApp ")) {
                folderName = folderName.substring(9);
            }
            return folderName;
        }
    }
}