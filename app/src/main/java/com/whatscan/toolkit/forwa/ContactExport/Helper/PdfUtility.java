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
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

public class PdfUtility {
    public static String FILE_EXTENSION = ".pdf";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void exportContacts(Context context, List<ContactModelAuto> list, String str, boolean z, HttpServletResponse httpServletResponse) {
        new ExportDataToFile(context, list, str, z, httpServletResponse).execute();
    }

    public static String writeContact(Context context, ContactModelAuto contactGS) {
        String str;
        String str2;
        StringBuilder sb = new StringBuilder();
        String name = contactGS.getName() == null ? "" : contactGS.getName();
        if (contactGS.getNumber() == null) {
            str = "";
        } else {
            str = contactGS.getNumber();
        }
        if (name.isEmpty()) {
            str2 = "";
        } else {
            str2 = "<b>" + name + "</b>";
        }
        if (str2.endsWith("<br/><br/>")) {
            str2 = str2.substring(0, str2.lastIndexOf("<br/><br/>"));
        }
        if (!str2.isEmpty()) {
            sb.append("<br/><font size='5'><b>" + "Name" + "</b></font><br/><font size='6'>").append(str2).append("</font><br/>");
        }
        String str3 = "" + "<b>" + str + "</b><br/>";
        if (str3.endsWith("<br/><br/>")) {
            str3 = str3.substring(0, str3.lastIndexOf("<br/><br/>"));
        }
        if (!str3.isEmpty()) {
            String str4 = "<font size='5'><b>" + "Number" + "</b></font><br/><font size='6'>" + str3 + "</font>";
            if (!sb.toString().isEmpty()) {
                str4 = "<br/>" + str4;
            }
            sb.append(str4);
        }
        return sb.toString();
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
            this.file_name = str;
            this.contacts = list;
            this.response = httpServletResponse;
            this.fromSvt = z;
            String str2 = Environment.getExternalStorageDirectory().toString() + "/" + context.getString(R.string.app_directory) + File.separator + "WhatsApp Contact Export";
            if (!new File(str2).exists()) {
                new File(str2).mkdirs();
            }
            this.file_path = str2 + "/" + this.file_name + PdfUtility.FILE_EXTENSION;
        }

        public void onPreExecute() {
        }

        public String doInBackground(String... strArr) {
            try {
                if (this.contacts.size() <= 0) {
                    return null;
                }
                Document document = new Document(PageSize.A4);
                PdfWriter instance = PdfWriter.getInstance(document, new FileOutputStream(this.file_path));
                document.open();
                int size = this.contacts.size();
                StringBuilder sb = new StringBuilder();
                sb.append("<html><body>");
                for (int i = 0; i < size; i++) {
                    if (this.contacts.get(i) != null) {
                        sb.append("<font color='#FF0000' size='7'><b>" + (i + 1) + "</b></font><br/><br/>" + PdfUtility.writeContact(this.context, this.contacts.get(i)) + "<br/><br/><br/><br/>");
                    }
                }
                sb.append("</body></html>");
                XMLWorkerHelper.getInstance().parseXHtml(instance, document, new ByteArrayInputStream(sb.toString().getBytes()), StandardCharsets.UTF_8);
                document.close();
                this.file_created = true;
                return null;
            } catch (Exception unused) {
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
