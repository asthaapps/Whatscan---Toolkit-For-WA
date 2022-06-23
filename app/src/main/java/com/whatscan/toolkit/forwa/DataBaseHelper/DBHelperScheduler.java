package com.whatscan.toolkit.forwa.DataBaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.whatscan.toolkit.forwa.GetSet.EventModel;

import java.util.ArrayList;

public class DBHelperScheduler extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String EVENTS_COLUMN_DATE = "date";
    public static final String EVENTS_COLUMN_DAY = "day";
    public static final String EVENTS_COLUMN_ID = "id";
    public static final String EVENTS_COLUMN_MESSAGE = "message";
    public static final String EVENTS_COLUMN_NAME = "name";
    public static final String EVENTS_COLUMN_PACKAGE = "packagename";
    public static final String EVENTS_COLUMN_REQUESTCODE = "requestcode";
    public static final String EVENTS_COLUMN_STATUS = "status";
    public static final String EVENTS_COLUMN_TIME = "time";
    public static final String EVENTS_TABLE_NAME = "events";

    public DBHelperScheduler(Context context) {
        super(context, DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 1);
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("create table events (id integer primary key, name text,day text,date text, time text,message text,requestcode int,packagename text,status text)");
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS events");
        onCreate(sQLiteDatabase);
    }

    public boolean insertContact(String str, String str2, String str3, String str4, String str5, int i, String str6, String str7) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", str);
        contentValues.put(EVENTS_COLUMN_DAY, str2);
        contentValues.put(EVENTS_COLUMN_DATE, str3);
        contentValues.put(EVENTS_COLUMN_TIME, str4);
        contentValues.put(EVENTS_COLUMN_MESSAGE, str5);
        contentValues.put(EVENTS_COLUMN_REQUESTCODE, Integer.valueOf(i));
        contentValues.put(EVENTS_COLUMN_PACKAGE, str6);
        contentValues.put("status", str7);
        writableDatabase.insert(EVENTS_TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getData(int i) {
        Cursor rawQuery = getReadableDatabase().rawQuery("SELECT * FROM events WHERE requestcode='" + i, null);
        if (rawQuery != null) {
            rawQuery.moveToFirst();
        }
        return rawQuery;
    }

    public int numberOfRows() {
        return (int) DatabaseUtils.queryNumEntries(getReadableDatabase(), EVENTS_TABLE_NAME);
    }

    public boolean updateContact(Integer num, String str, String str2, String str3, String str4, String str5, int i, String str6, String str7) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", str);
        contentValues.put(EVENTS_COLUMN_DAY, str2);
        contentValues.put(EVENTS_COLUMN_DATE, str3);
        contentValues.put(EVENTS_COLUMN_TIME, str4);
        contentValues.put(EVENTS_COLUMN_MESSAGE, str5);
        contentValues.put(EVENTS_COLUMN_REQUESTCODE, Integer.valueOf(i));
        contentValues.put(EVENTS_COLUMN_PACKAGE, str6);
        contentValues.put("status", str7);
        writableDatabase.update(EVENTS_TABLE_NAME, contentValues, "id = ? ", new String[]{Integer.toString(num.intValue())});
        return true;
    }

    public boolean updateStatus(Integer num, String str) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", str);
        writableDatabase.update(EVENTS_TABLE_NAME, contentValues, "requestcode = ? ", new String[]{Integer.toString(num.intValue())});
        return true;
    }

    public Integer deleteContact(Integer num) {
        return Integer.valueOf(getWritableDatabase().delete(EVENTS_TABLE_NAME, "id = ? ", new String[]{Integer.toString(num.intValue())}));
    }

    public ArrayList<EventModel> getAllCotacts(String str) {
        Cursor cursor;
        ArrayList<EventModel> arrayList = new ArrayList<>();
        try {
            cursor = getReadableDatabase().query(EVENTS_TABLE_NAME, null, "packagename=?", new String[]{str}, null, null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            cursor = null;
        }
        if (cursor != null) {
            cursor.moveToFirst();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                EventModel event_model = new EventModel();
                String string = cursor.getString(cursor.getColumnIndex(EVENTS_COLUMN_ID));
                String string2 = cursor.getString(cursor.getColumnIndex("name"));
                String string3 = cursor.getString(cursor.getColumnIndex(EVENTS_COLUMN_DAY));
                String string4 = cursor.getString(cursor.getColumnIndex(EVENTS_COLUMN_DATE));
                String string5 = cursor.getString(cursor.getColumnIndex(EVENTS_COLUMN_TIME));
                String string6 = cursor.getString(cursor.getColumnIndex(EVENTS_COLUMN_MESSAGE));
                int i = cursor.getInt(cursor.getColumnIndex(EVENTS_COLUMN_REQUESTCODE));
                String string7 = cursor.getString(cursor.getColumnIndex(EVENTS_COLUMN_PACKAGE));
                String string8 = cursor.getString(cursor.getColumnIndex("status"));
                event_model.setName(string2);
                event_model.setDay(string3);
                event_model.setDate(string4);
                event_model.setTime(string5);
                event_model.setMessage(string6);
                event_model.setId(string);
                event_model.setRequestcode(i);
                event_model.setPackagename(string7);
                event_model.setStatus(string8);
                arrayList.add(event_model);
                cursor.moveToNext();
            }
        }
        return arrayList;
    }
}