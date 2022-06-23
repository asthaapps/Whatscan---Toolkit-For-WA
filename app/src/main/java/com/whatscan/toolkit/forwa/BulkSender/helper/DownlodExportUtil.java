package com.whatscan.toolkit.forwa.BulkSender.helper;

import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DownlodExportUtil {

    public File ExportCSVFile(ArrayList<ContactModel> arrayList) {
        boolean z;
        File file = new File(Environment.getExternalStorageDirectory() + "/" + "Whatscan Toolkit for WhatsApp");
        if (!file.exists()) {
            z = file.mkdir();
        } else {
            z = false;
        }
        if (file.exists() || z) {
            new SimpleDateFormat("ddMMyyyy_HHmmss").format(Calendar.getInstance().getTime());
            String str = file + "/" + "SampleContacts.csv";
            try {
                FileWriter fileWriter = new FileWriter(str);
                for (int i = 0; i < arrayList.size(); i++) {
                    fileWriter.append((CharSequence) ("" + arrayList.get(i).getPhoneNumber()));
                    fileWriter.append((CharSequence) " , ");
                    fileWriter.append((CharSequence) ("" + arrayList.get(i).getName()));
                    fileWriter.append((CharSequence) " , ");
                    fileWriter.append((CharSequence) ("" + arrayList.get(i).getCountryCode()));
                    fileWriter.append((CharSequence) "\n");
                }
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public File downloadSampleCsv() {
        ArrayList<ContactModel> arrayList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ContactModel contactModel = new ContactModel();
            contactModel.setPhoneNumber("912345678" + i);
            contactModel.setName("Sample " + i);
            contactModel.setCountryCode("91");
            arrayList.add(contactModel);
        }
        return ExportCSVFile(arrayList);
    }
}
