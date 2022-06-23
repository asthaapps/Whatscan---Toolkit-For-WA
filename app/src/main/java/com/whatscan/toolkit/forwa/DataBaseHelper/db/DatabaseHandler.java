package com.whatscan.toolkit.forwa.DataBaseHelper.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.whatscan.toolkit.forwa.BulkSender.helper.ContactModel;
import com.whatscan.toolkit.forwa.GetSet.QuickReply;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "QuickReplyDB";
    private static final String KEY_PHONE_NUMBER = "phone_number";
    private static final String TABLE_CONTACTS = "hashtagSaved";

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE hashtagSaved(id INTEGER PRIMARY KEY,message TEXT,phone_number TEXT,name TEXT)");
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS hashtagSaved");
        onCreate(sQLiteDatabase);
        if (i2 > i) {
            sQLiteDatabase.execSQL("ALTER TABLE hashtagSaved ADD COLUMN KEY_NAME TEXT DEFAULT ''");
        }
    }

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 3);
    }

    public void addQuickReply(QuickReply quickReply) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("message", quickReply.getMessage());
        contentValues.put(KEY_PHONE_NUMBER, quickReply.getPhoneNumber());
        contentValues.put("name", quickReply.getContactName());
        writableDatabase.insert(TABLE_CONTACTS, null, contentValues);
        writableDatabase.close();
    }

    public void deletehashtag(QuickReply quickReply) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.delete(TABLE_CONTACTS, "id = ?", new String[]{String.valueOf(quickReply.getId())});
        writableDatabase.close();
    }

    public ArrayList<QuickReply> getAllQuickReplies() {
        ArrayList<QuickReply> arrayList = new ArrayList<>();
        Cursor rawQuery = getWritableDatabase().rawQuery("SELECT  * FROM hashtagSaved ORDER BY id DESC", null);
        if (rawQuery.moveToFirst()) {
            do {
                QuickReply quickReply = new QuickReply();
                quickReply.setId(Integer.parseInt(rawQuery.getString(0)));
                quickReply.setMessage(rawQuery.getString(1));
                quickReply.setPhoneNumber(rawQuery.getString(2));
                quickReply.setContactName(rawQuery.getString(3));
                arrayList.add(quickReply);
            } while (rawQuery.moveToNext());
        }
        return arrayList;
    }

    public List<String> getAllQuickReplyMessage() {
        ArrayList arrayList = new ArrayList();
        Cursor rawQuery = getWritableDatabase().rawQuery("SELECT  * FROM hashtagSaved", null);
        if (rawQuery.moveToFirst()) {
            do {
                arrayList.add(rawQuery.getString(1));
            } while (rawQuery.moveToNext());
        }
        return arrayList;
    }

    public QuickReply getContact(int i) {
        Cursor query = getReadableDatabase().query(TABLE_CONTACTS, new String[]{"id", "message", KEY_PHONE_NUMBER}, "id=?", new String[]{String.valueOf(i)}, null, null, null, null);
        if (query != null) {
            query.moveToFirst();
        }
        return new QuickReply(Integer.parseInt(query.getString(0)), query.getString(1), query.getString(2));
    }

    public int getCount() {
        Cursor rawQuery = getReadableDatabase().rawQuery("SELECT  * FROM hashtagSaved", null);
        rawQuery.close();
        return rawQuery.getCount();
    }



    public int updateHashtag(QuickReply quickReply) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("message", quickReply.getMessage());
        contentValues.put(KEY_PHONE_NUMBER, quickReply.getPhoneNumber());
        contentValues.put("id", Integer.valueOf(quickReply.getId()));
        contentValues.put("name", quickReply.getContactName());
        return writableDatabase.update(TABLE_CONTACTS, contentValues, "id = ?", new String[]{String.valueOf(quickReply.getId())});
    }
}