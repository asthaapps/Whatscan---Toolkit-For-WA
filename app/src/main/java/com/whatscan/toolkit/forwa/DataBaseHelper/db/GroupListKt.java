package com.whatscan.toolkit.forwa.DataBaseHelper.db;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import kotlin.collections.CollectionsKt;

public final class GroupListKt {
    @NotNull
    private static final ArrayList<String> groupList = CollectionsKt.arrayListOf("Family", "Friends", "Business Promotion", "New Product Announcement");

    @NotNull
    public static final ArrayList<String> getGroupList() {
        return groupList;
    }
}
