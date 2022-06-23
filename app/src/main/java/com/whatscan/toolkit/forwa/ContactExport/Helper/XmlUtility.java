package com.whatscan.toolkit.forwa.ContactExport.Helper;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.RequiresApi;

import com.whatscan.toolkit.forwa.GetSet.ContactModelAuto;
import com.whatscan.toolkit.forwa.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class XmlUtility {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void exportContacts(Context context, List<ContactModelAuto> list, String str, boolean z, HttpServletResponse httpServletResponse) {
        new ExportDataToFile(context, list, str, z, httpServletResponse).execute();
    }

    public static class ExportDataToFile extends AsyncTask<String, Integer, String> {
        List<ContactModelAuto> contacts;
        Context context;
        Document doc;
        boolean file_created = false;
        String file_name;
        String file_path;

        public void onPreExecute() {
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public ExportDataToFile(Context context2, List<ContactModelAuto> list, String str, boolean z, HttpServletResponse httpServletResponse) {
            this.context = context2;
            this.file_name = str;
            this.contacts = list;
            String str2 = Environment.getExternalStorageDirectory().toString() + "/" + context.getString(R.string.app_directory) + File.separator + "WhatsApp Contact Export";
            if (!new File(str2).exists()) {
                new File(str2).mkdirs();
            }
            this.file_path = str2 + "/" + str;
        }

        public String doInBackground(String... strArr) {
            try {
                Document newDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
                this.doc = newDocument;
                Element createElement = newDocument.createElement("List");
                this.doc.appendChild(createElement);
                for (int i = 0; i < this.contacts.size(); i++) {
                    Element createElement2 = this.doc.createElement("Contacts");
                    createElement.appendChild(createElement2);
                    Element createElement3 = this.doc.createElement("Name");
                    createElement3.appendChild(this.doc.createTextNode(String.valueOf(this.contacts.get(i).getName())));
                    createElement2.appendChild(createElement3);
                    Element createElement4 = this.doc.createElement("Number");
                    createElement4.appendChild(this.doc.createTextNode(String.valueOf(this.contacts.get(i).getNumber())));
                    createElement2.appendChild(createElement4);
                }
                this.file_created = true;
                return null;
            } catch (Exception unused) {
                this.file_created = false;
                return null;
            }
        }

        public void onPostExecute(String str) {
            super.onPostExecute(str);
            try {
                Transformer newTransformer = TransformerFactory.newInstance().newTransformer();
                newTransformer.setOutputProperty("encoding", "ISO-8859-1");
                newTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
                newTransformer.setOutputProperty("indent", "yes");
                DOMSource dOMSource = new DOMSource(this.doc);
                newTransformer.transform(dOMSource, new StreamResult(new FileWriter(this.file_path + ".xml")));
            } catch (IOException | TransformerException ignored) {
            }
        }
    }
}
