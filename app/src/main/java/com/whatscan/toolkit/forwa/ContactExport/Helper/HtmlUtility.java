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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

public class HtmlUtility {
    public static String FILE_EXTENSION = ".html";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void exportContacts(Context context, List<ContactModelAuto> list, String str, boolean z, HttpServletResponse httpServletResponse) {
        new ExportDataToFile(context, list, str, z, httpServletResponse).execute();
    }

    public static class ExportDataToFile extends AsyncTask<String, Integer, String> {
        public String filename;
        List<ContactModelAuto> contacts;
        Context context;
        boolean file_created = false;
        String file_name;
        String file_path;

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public ExportDataToFile(Context context2, List<ContactModelAuto> list, String str, boolean z, HttpServletResponse httpServletResponse) {
            this.context = context2;
            this.contacts = list;
            this.file_name = str;
            String str2 = Environment.getExternalStorageDirectory().toString() + "/" + context.getString(R.string.app_directory) + File.separator + "WhatsApp Contact Export";
            if (!new File(str2).exists()) {
                new File(str2).mkdirs();
            }
            this.file_path = str2 + "/" + this.file_name + HtmlUtility.FILE_EXTENSION;
        }

        public void onPreExecute() {
        }

        public String doInBackground(String... strArr) {
            try {
                StringBuilder str = new StringBuilder("<table border=1 width='100%25' style=border-spacing:0px;>");
                for (int i = 0; i < this.contacts.size(); i++) {
                    str = new StringBuilder((str + "<tr>") + ("" + "<td style='font-size:14; padding: 15px; text-align: center;'>" + this.contacts.get(i).getName() + " </td><td style='font-size:14; padding: 15px; text-align: center;'>" + this.contacts.get(i).getNumber() + " </td>") + "</tr>");
                }
                FileUtils.writeStringToFile(new File(this.file_path), (str + "</table>"), "utf-8");
                this.file_created = true;
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                this.file_created = false;
                return null;
            }
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