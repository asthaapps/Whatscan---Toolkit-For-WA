package com.whatscan.toolkit.forwa.MsgTools.Caption;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class FavData {
    public void savedata(String str, Context context) {
        try {
            SQLiteDatabase writableDatabase = new Data(context).getWritableDatabase();
            if (!exists(writableDatabase, str)) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("fav", str);
                writableDatabase.insert("data", null, contentValues);
            }
            writableDatabase.close();
        } catch (Exception e) {
            Log.d("favdatalogs", e.toString());
        }
    }

    private boolean exists(SQLiteDatabase sQLiteDatabase, String str) {
        Cursor query = sQLiteDatabase.query("data", new String[]{"fav"}, "fav=?", new String[]{str}, null, null, null);
        if (query == null || query.getCount() <= 0) {
            return false;
        }
        query.close();
        return true;
    }

    public ArrayList<String> getdata(Context context) {
        ArrayList<String> arrayList = new ArrayList<>();
        Cursor query = new Data(context).getReadableDatabase().query("data", null, null, null, null, null, null);
        if (query != null) {
            while (query.moveToNext()) {
                arrayList.add(query.getString(1));
            }
            query.close();
        }
        return arrayList;
    }

    public boolean remove(String str, Context context) {
        if (new Data(context).getWritableDatabase().delete("data", "fav=?", new String[]{str}) > 0) {
            return true;
        }
        return false;
    }


    public static class Data extends SQLiteOpenHelper {
        private static final String create_table = "CREATE TABLE data (id INTEGER PRIMARY KEY AUTOINCREMENT ,fav TEXT unique);";

        public Data(Context context) {
            super(context, "data.db", null, 1);
        }

        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        }

        public void onCreate(SQLiteDatabase sQLiteDatabase) {
            sQLiteDatabase.execSQL(create_table);
        }
    }
}
