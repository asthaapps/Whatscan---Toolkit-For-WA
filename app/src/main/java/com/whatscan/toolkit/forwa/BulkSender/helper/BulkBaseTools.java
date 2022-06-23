package com.whatscan.toolkit.forwa.BulkSender.helper;

import android.content.Context;

public class BulkBaseTools {
    private static final boolean isAutoForwardingEnabled = false;
    private static BulkBaseTools bulkBaseTools = null;
    private static Context mContext = null;
    private static BulkWtBus bulkWtBus;

    private BulkBaseTools() {
    }

    public static Context getContext() {
        return mContext;
    }

    public static BulkBaseTools getInstance() {
        if (bulkBaseTools == null) {
            bulkBaseTools = new BulkBaseTools();
        }
        return bulkBaseTools;
    }

    public static BulkWtBus getWtBus() {
        return bulkWtBus;
    }

    public static boolean isAutoForwardingEnabled() {
        return isAutoForwardingEnabled;
    }

    public void init(Context context) {
        mContext = context;
        bulkWtBus = new BulkWtBus();
    }
}