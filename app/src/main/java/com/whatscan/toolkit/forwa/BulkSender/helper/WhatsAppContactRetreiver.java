package com.whatscan.toolkit.forwa.BulkSender.helper;

import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WhatsAppContactRetreiver {
    static List<ContactModel> a = new ArrayList<>();
    static HashMap<String, Boolean> b = new HashMap<>();

    private static void buildContactListFromCursor(ContentResolver contentResolver, Cursor cursor) {
        Cursor query;
        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            do {
                String string = cursor.getString(cursor.getColumnIndex("contact_id"));
                if (!(string == null || (query = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{"contact_id", "data1", "display_name"}, "contact_id = ?", new String[]{string}, null)) == null)) {
                    try {
                        query.moveToFirst();
                        do {
                            String string2 = query.getString(query.getColumnIndex("contact_id"));
                            String string3 = query.getString(query.getColumnIndex("display_name"));
                            String string4 = query.getString(query.getColumnIndex("data1"));
                            if (!(string4 == null || string4 == null || b.containsKey(string4.replaceAll("\\s+", "")))) {
                                BulkWtBus bulkWtBus = BulkBaseTools.getWtBus();
                                bulkWtBus.send("Saving " + a.size());
                                ContactModel contactModel = new ContactModel();
                                contactModel.setName(string3);
                                contactModel.setPhoneNumber(string4);
                                contactModel.setContactId(string2);
                                b.put(contactModel.getPhoneNumber().replaceAll("\\s+", ""), Boolean.TRUE);
                                a.add(contactModel);
                            }
                        } while (query.moveToNext());
                        query.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
    }

    public static String getContactPhoto(Long l) {
        return Uri.withAppendedPath(ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, l), "photo").getPath();
    }

    public static List<ContactModel> getContacts(Context context) {
        Cursor cursor;
        a.clear();
        b.clear();
        ContentResolver contentResolver = context.getContentResolver();
        ContentProviderClient acquireContentProviderClient = contentResolver.acquireContentProviderClient(ContactsContract.Contacts.CONTENT_URI);
        Cursor cursor2 = null;
        try {
            cursor = acquireContentProviderClient.query(ContactsContract.RawContacts.CONTENT_URI, new String[]{"_id", "contact_id"}, "account_type= ?", new String[]{"com.whatsapp"}, null);
        } catch (RemoteException e) {
            e.printStackTrace();
            cursor = null;
        }
        buildContactListFromCursor(contentResolver, cursor);
        try {
            cursor2 = acquireContentProviderClient.query(ContactsContract.RawContacts.CONTENT_URI, new String[]{"_id", "contact_id"}, "account_type= ?", new String[]{"com.whatsapp.w4b"}, null);
        } catch (RemoteException e2) {
            e2.printStackTrace();
        }
        buildContactListFromCursor(contentResolver, cursor2);
        return a;
    }

    public static List<ContactModel> getMyWhatsappContacts(Context context) {
        return getContacts(context);
    }

    private static List<ContactModel> removeDuplicates(List<ContactModel> list) {
        ArrayList<ContactModel> arrayList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            ContactModel contactModel = list.get(i);
            if (contactModel == null || contactModel.getPhoneNumber() == null || b.containsKey(contactModel.getPhoneNumber().replaceAll("\\s+", ""))) {

            } else {
                b.put(contactModel.getPhoneNumber().replaceAll("\\s+", ""), Boolean.TRUE);
                arrayList.add(contactModel);
            }
        }
        return arrayList;
    }

    public static Bitmap retrieveContactPhoto(Context context, String str) {
        InputStream inputStream;
        if (str != null && str.length() > 0) {
            try {
                inputStream = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(), ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(str).longValue()));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                inputStream = null;
            }
            if (inputStream != null) {
                Bitmap decodeStream = BitmapFactory.decodeStream(inputStream);
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return decodeStream;
            }
        }
        return null;
    }

    public static void unsellectMyWhatsappContacts() {
        for (int i = 0; i < a.size(); i++) {
            a.get(i).setSelected(false);
        }
    }

    public void setMyWhatsappContacts(List<ContactModel> list) {
        a = list;
    }
}
