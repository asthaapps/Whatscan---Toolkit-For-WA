package com.whatscan.toolkit.forwa.MsgTools.Caption;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Time;
import java.util.Date;

public class SaveDialog {
    private final Bitmap bitmap;
    private final CaptionEditActivity single_page;
    Context context;

    public SaveDialog(Context context2, Bitmap bitmap2, CaptionEditActivity imageEditor) {
        this.context = context2;
        this.bitmap = bitmap2;
        this.single_page = imageEditor;
    }

    public void dilog() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View inflate = LayoutInflater.from(context).inflate(R.layout.saving_dilog, null);
        bottomSheetDialog.setContentView(inflate);
        bottomSheetDialog.setTitle(context.getString(R.string.cp_save));

        RelativeLayout rlSave = inflate.findViewById(R.id.rlSave);
        TextView heading = inflate.findViewById(R.id.heading);
        TextView detail = inflate.findViewById(R.id.detail);

        if (Preference.getBooleanTheme(false)) {
            rlSave.setBackgroundColor(ContextCompat.getColor(context, R.color.darkBlack));
            heading.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            detail.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        }

        inflate.findViewById(R.id.medium).setOnClickListener(view -> {
            new Saving_image(SaveDialog.this.context).save(Bitmap.createScaledBitmap(SaveDialog.this.bitmap, 800, 650, false));
            SaveDialog.this.single_page.alreadySaved = true;
            bottomSheetDialog.cancel();
        });

        inflate.findViewById(R.id.hd).setOnClickListener(view -> {
            new Saving_image(SaveDialog.this.context).save(SaveDialog.this.bitmap);
            SaveDialog.this.single_page.alreadySaved = true;
            bottomSheetDialog.cancel();
        });

        bottomSheetDialog.show();
    }

    private class Saving_image {

        public Saving_image(Context con) {
            context = con;
        }

        @SuppressLint("StaticFieldLeak")
        public void save(final Bitmap bitmap) {
            new AsyncTask<Void, Void, String>() {
                public String doInBackground(Void... voidArr) {
                    try {
                        Time time = new Time(new Date().getTime());
                        FileOutputStream fileOutputStream = new FileOutputStream(new File(new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath()).getAbsolutePath() + "/" + (context.getString(R.string.app_name) + time.toString() + ".jpg")));
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                        fileOutputStream.flush();
                        fileOutputStream.close();
                        return "Image saved";
                    } catch (IOException e) {
                        e.printStackTrace();
                        return "Error while saving";
                    }
                }


                @SuppressLint("WrongConstant")
                public void onPostExecute(String str) {
                    super.onPostExecute(str);
                    Toast.makeText(context, str, 0).show();
                }
            }.execute();
        }
    }
}