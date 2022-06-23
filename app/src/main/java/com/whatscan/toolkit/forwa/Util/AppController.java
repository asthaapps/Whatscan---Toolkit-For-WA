package com.whatscan.toolkit.forwa.Util;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDex;

import com.facebook.FacebookSdk;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;
import com.orhanobut.hawk.Hawk;
import com.whatscan.toolkit.forwa.Ads.AppOpenManager;
import com.whatscan.toolkit.forwa.BulkSender.helper.BulkBaseTools;
import com.whatscan.toolkit.forwa.BulkSender.helper.BulkWtBus;
import com.whatscan.toolkit.forwa.DataBaseHelper.TinyDB;
import com.yariksoffice.lingver.Lingver;
import com.yariksoffice.lingver.store.PreferenceLocaleStore;

import java.util.Locale;

public class AppController extends Application {

    public static AppController application;
    public static TinyDB tinyDB;
    public static BulkWtBus bulkWtBus;
    public AppOpenManager appOpenManager;

    public AppController() {
        application = this;
    }

    public static synchronized AppController getInstance() {
        return application;
    }

    public static AppController getApp() {
        if (application == null) {
            application = new AppController();
        }
        return application;
    }

    public static BulkWtBus getWtBus() {
        return bulkWtBus;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        Lingver.init(this, new PreferenceLocaleStore(this, new Locale(Preference.getCheckActiveLanguage())));
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        AudienceNetworkAds.initialize(this);
        MobileAds.initialize(this, initializationStatus -> {
        });
        FacebookSdk.setAutoInitEnabled(true);
        FacebookSdk.fullyInitialize();
        FacebookSdk.sdkInitialize(getApplicationContext());

        Hawk.init(this).build();

        appOpenManager = new AppOpenManager(this);
        tinyDB = new TinyDB(this);

        bulkWtBus = new BulkWtBus();
        BulkBaseTools.getInstance().init(application);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

    public void adjustFontScale(Configuration configuration) {
        configuration.fontScale = (float) 1.0;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale * metrics.density;
        getBaseContext().getResources().updateConfiguration(configuration, metrics);
    }
}