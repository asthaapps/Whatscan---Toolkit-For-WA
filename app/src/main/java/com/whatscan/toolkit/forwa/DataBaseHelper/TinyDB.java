package com.whatscan.toolkit.forwa.DataBaseHelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.whatscan.toolkit.forwa.GetSet.StatusData;
import com.whatscan.toolkit.forwa.GetSet.StickerArray;
import com.whatscan.toolkit.forwa.GetSet.StickerArrayList;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TinyDB {

    private SharedPreferences preferences;

    public TinyDB(Context appContext) {
        preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
    }

    public void putListAdVideo(String key, List<StickerArray> objArray) {
        checkForNullKey(key);
        Gson gson = new Gson();
        ArrayList<String> objStrings = new ArrayList<String>();
        for (Object obj : objArray) {
            objStrings.add(gson.toJson(obj));
        }
        putListString(key, objStrings);
    }

    public void putListSticker(String key, List<StickerArrayList> objArray) {
        checkForNullKey(key);
        Gson gson = new Gson();
        ArrayList<String> objStrings = new ArrayList<String>();
        for (Object obj : objArray) {
            objStrings.add(gson.toJson(obj));
        }
        putListString(key, objStrings);
    }

    public void putListCall(String key, List<StatusData> objArray) {
        checkForNullKey(key);
        Gson gson = new Gson();
        ArrayList<String> objStrings = new ArrayList<String>();
        for (Object obj : objArray) {
            objStrings.add(gson.toJson(obj));
        }
        putListString(key, objStrings);
    }

    public ArrayList<StickerArray> getListAdVideo(String key, Class<StickerArray> mClass) {
        Gson gson = new Gson();

        ArrayList<String> objStrings = getListString(key);
        ArrayList<StickerArray> objects = new ArrayList<StickerArray>();

        for (String jObjString : objStrings) {
            StickerArray value = gson.fromJson(jObjString, mClass);
            objects.add(value);

        }
        return objects;
    }

    public ArrayList<StatusData> getListCall(String key, Class<StatusData> mClass) {
        Gson gson = new Gson();

        ArrayList<String> objStrings = getListString(key);
        ArrayList<StatusData> objects = new ArrayList<StatusData>();

        for (String jObjString : objStrings) {
            StatusData value = gson.fromJson(jObjString, mClass);
            objects.add(value);

        }
        return objects;
    }

    public ArrayList<StickerArrayList> getListSticker(String key, Class<StickerArrayList> mClass) {
        Gson gson = new Gson();

        ArrayList<String> objStrings = getListString(key);
        ArrayList<StickerArrayList> objects = new ArrayList<StickerArrayList>();

        for (String jObjString : objStrings) {
            StickerArrayList value = gson.fromJson(jObjString, mClass);
            objects.add(value);

        }
        return objects;
    }

    public ArrayList<String> getListString(String key) {
        return new ArrayList<String>(Arrays.asList(TextUtils.split(preferences.getString(key, ""), "‚‗‚")));
    }

    public void putListString(String key, ArrayList<String> stringList) {
        checkForNullKey(key);
        String[] myStringList = stringList.toArray(new String[stringList.size()]);
        preferences.edit().putString(key, TextUtils.join("‚‗‚", myStringList)).apply();
    }

    public void checkForNullKey(String key) {
        if (key == null) {
            throw new NullPointerException();
        }
    }

}