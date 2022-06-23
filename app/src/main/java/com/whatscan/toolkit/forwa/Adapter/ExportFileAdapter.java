package com.whatscan.toolkit.forwa.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Preference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ExportFileAdapter extends RecyclerView.Adapter<ExportFileAdapter.fileViewHolder> {
    public Context context;
    public List<File> files;
    public LottieAnimationView no_file;

    public ExportFileAdapter(Context context2, List<File> list, LottieAnimationView textView) {
        context = context2;
        files = list;
        no_file = textView;
    }

    @NonNull
    public fileViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new fileViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_export_file, viewGroup, false), i);
    }

    public void onBindViewHolder(@NonNull final fileViewHolder fileviewholder, @SuppressLint("RecyclerView") final int position) {
        if (Preference.getBooleanTheme(false)) {
            fileviewholder.rlMain.setBackgroundColor(ContextCompat.getColor(context, R.color.colorShape));
            fileviewholder.tvName.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            fileviewholder.view1.setVisibility(View.GONE);
        }

        fileviewholder.tvName.setText(files.get(position).getName());
        fileviewholder.relativeLayout.setOnClickListener(view -> openFile(position));
        fileviewholder.imgShare.setOnClickListener(v -> share(position));

        fileviewholder.imgDelete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(true);
            builder.setMessage(context.getResources().getString(R.string.delete_chat));
            builder.setPositiveButton(context.getString(R.string.yes), (dialogInterface, i) -> delete(position));
            builder.setNegativeButton(context.getString(R.string.no), (dialogInterface, i) -> {
            });
            final AlertDialog create = builder.create();
            create.setOnShowListener(dialogInterface -> {
                create.getButton(-1).setTextColor(context.getResources().getColor(R.color.colorTools));
                create.getButton(-2).setTextColor(context.getResources().getColor(R.color.colorBlack));
            });
            create.show();
        });
    }

    public void delete(int i) {
        File file = new File(Environment.getExternalStorageDirectory().toString() + "/" + context.getString(R.string.app_directory) + "/WhatsApp Contact Export/" + files.get(i).getName());
        Context context2 = context;
        context.getContentResolver().delete(FileProvider.getUriForFile(context2, context.getApplicationContext().getPackageName() + ".provider", file), null, null);
        files.remove(i);
        notifyItemRemoved(i);
        notifyItemRangeChanged(i, files.size());
    }

    public void share(int i) {
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build());
        Intent intent = new Intent("android.intent.action.SEND");
        File file = new File(Environment.getExternalStorageDirectory().toString() + "/" + context.getString(R.string.app_directory) + "/WhatsApp Contact Export/" + files.get(i).getName());
        if (file.exists()) {
            intent.setType("text/*");
            intent.putExtra("android.intent.extra.STREAM", Uri.parse("file://" + Environment.getExternalStorageDirectory().toString() + "/" + context.getString(R.string.app_directory) + "/WhatsApp Contact Export/" + files.get(i).getName()));
            intent.putExtra("android.intent.extra.SUBJECT", "Share File...");
            context.startActivity(Intent.createChooser(intent, "Share File"));
        }
    }

    @SuppressLint("WrongConstant")
    private void openFile(int i) {
        try {
            File file = new File(Environment.getExternalStorageDirectory().toString() + "/" + context.getString(R.string.app_directory) + "/WhatsApp Contact Export/" + files.get(i).getName());
            Intent intent = new Intent("android.intent.action.VIEW");
            Context context2 = context;
            intent.setDataAndType(FileProvider.getUriForFile(context2, context.getPackageName() + ".provider", file), "*/*");
            intent.setFlags(1);
            intent.addFlags(268435456);
            context.startActivity(intent);
        } catch (Exception unused) {
            Context context3 = context;
            Toast.makeText(context3, "Couldn\\'t find apps to open this file.", Toast.LENGTH_SHORT).show();
        }
    }

    public int getItemCount() {
        return files.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateItems(ArrayList<File> newList) {
        files.clear();
        files.addAll(newList);
        notifyDataSetChanged();
    }

    public static class fileViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgShare, imgDelete;
        public RelativeLayout rlMain, relativeLayout;
        public TextView tvName;
        public View view1;

        public fileViewHolder(View view, int i) {
            super(view);
            tvName = view.findViewById(R.id.filename);
            rlMain = view.findViewById(R.id.rlMain);
            relativeLayout = view.findViewById(R.id.lay_file);
            imgShare = view.findViewById(R.id.imgShare);
            imgDelete = view.findViewById(R.id.imgDelete);
            view1 = view.findViewById(R.id.view1);
        }
    }
}