package com.whatscan.toolkit.forwa.ContactExport.Helper;

import static com.whatscan.toolkit.forwa.ContactExport.ActivityContactView.getConvertedFiles;
import static com.whatscan.toolkit.forwa.ContactExport.ActivityContactView.saveConvertedFiles;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.RequiresApi;

import com.whatscan.toolkit.forwa.GetSet.ContactModelAuto;
import com.whatscan.toolkit.forwa.R;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

public class JsonUtility {
    public static String FILE_EXTENSION = ".json";

    public static void exportContacts(Context context, List<ContactModelAuto> list, String str, boolean z, HttpServletResponse httpServletResponse) {
        new ExportDataToFile(context, list, str, z, httpServletResponse).execute();
    }

    public static class ExportDataToFile extends AsyncTask<String, Integer, String> {

        List<ContactModelAuto> contacts;
        Context context;
        boolean file_created = false;
        String file_name;
        String file_path;
        boolean fromSvt;
        HttpServletResponse response;

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public ExportDataToFile(Context context2, List<ContactModelAuto> list, String str, boolean z, HttpServletResponse httpServletResponse) {
            this.context = context2;
            this.contacts = list;
            this.file_name = str;
            this.response = httpServletResponse;
            this.fromSvt = z;
            String str2 = Environment.getExternalStorageDirectory().toString() + "/" + context.getString(R.string.app_directory) + File.separator + "WhatsApp Contact Export";
            if (!new File(str2).exists()) {
                new File(str2).mkdirs();
            }
            this.file_path = str2 + "/" + this.file_name + JsonUtility.FILE_EXTENSION;
        }

        public void onPreExecute() {
        }

        public String doInBackground(String... strArr) {
            JSONObject jSONObject = new JSONObject();
            JSONArray jSONArray = new JSONArray();
            int i = 0;
            while (i < this.contacts.size()) {
                try {
                    JSONObject jSONObject2 = new JSONObject();
                    jSONObject2.put("Name", this.contacts.get(i).getName());
                    jSONObject2.put("Number", this.contacts.get(i).getNumber());
                    jSONArray.put(i, jSONObject2);
                    i++;
                } catch (JSONException unused) {
                    this.file_created = false;
                    return null;
                }
            }
            try {
                jSONObject.put("contacts", jSONArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                FileUtils.writeStringToFile(new File(this.file_path), jSONObject.toString(), "utf-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.file_created = true;
            return null;
        }

        public void onPostExecute(String str) {
            super.onPostExecute(str);
            if (this.file_created) {
                Conversion conversion = new Conversion();
                conversion.CONVERTED_FILE_PATH = this.file_path;
                conversion.CONVERTED_FILE_NAME = this.file_name;
                ArrayList<Conversion> convertedFiles = getConvertedFiles(this.context);
                convertedFiles.add(conversion);
                saveConvertedFiles(this.context, convertedFiles);
            }
        }
    }
}
