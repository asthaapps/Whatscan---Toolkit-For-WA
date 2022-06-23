package com.whatscan.toolkit.forwa.DeletedMedia;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.BuildConfig;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Story.ActivityImageView;
import com.whatscan.toolkit.forwa.Story.ActivityVideoView;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

public class AdapterMedia extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final ArrayList<File> fileArrayList;
    private final LayoutInflater layoutInflater;

    public AdapterMedia(Context context, ArrayList<File> fileArrayList) {
        this.context = context;
        this.fileArrayList = fileArrayList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_media, parent, false);
        return new MediaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        final MediaViewHolder mediaViewHolder = (MediaViewHolder) holder;
        File file = fileArrayList.get(mediaViewHolder.getAdapterPosition());
        Glide.with(context).load(file.getAbsolutePath()).into(mediaViewHolder.iv_media);
        mediaViewHolder.cv_media.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickItemMedia(file, mediaViewHolder);
            }
        });

    }

    private void ClickItemMedia(File file, MediaViewHolder mediaViewHolder) {
        if (file.getAbsolutePath().endsWith(".jpg") || file.getAbsolutePath().endsWith(".jpeg") || file.getAbsolutePath().endsWith(".webp")) {
            Intent intent = new Intent(context, ActivityImageView.class);
            intent.putExtra("path", file.getPath());
            intent.putExtra("strCheck", "clean");
            context.startActivity(intent);
        } else if (file.getAbsolutePath().endsWith(".mp4")) {
            mediaViewHolder.la_play.setAnimation(R.raw.play);
            mediaViewHolder.la_play.setVisibility(View.VISIBLE);
            Intent intent = new Intent(context, ActivityVideoView.class);
            intent.putExtra("path", file.getPath());
            intent.putExtra("strCheck", "clean");
            context.startActivity(intent);
        } else if (file.getAbsolutePath().endsWith(".mp3") || file.getAbsolutePath().endsWith(".m4a") || file.getAbsolutePath().endsWith(".opus") || file.getAbsolutePath().endsWith(".aac") || file.getAbsolutePath().endsWith(".amr")) {
            mediaViewHolder.la_play.setAnimation(R.raw.icon_player);
            mediaViewHolder.la_play.setVisibility(View.VISIBLE);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file), "audio/*");
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        } else if (file.getAbsolutePath().endsWith(".pdf") || file.getAbsolutePath().endsWith(".docx")) {
            mediaViewHolder.la_play.setAnimation(R.raw.icon_doc);
            mediaViewHolder.la_play.setVisibility(View.VISIBLE);
            Uri path = Uri.fromFile(file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            if (path.toString().contains(".doc") || path.toString().contains(".docx")) {
                intent.setDataAndType(FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file), "application/msword");
            } else if (path.toString().contains(".pdf")) {
                intent.setDataAndType(FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file), "application/pdf");
            } else {
                intent.setDataAndType(path, "*/*");
            }
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return fileArrayList.size();
    }

    private static class MediaViewHolder extends RecyclerView.ViewHolder {

        private final ImageView iv_media;
        private final LottieAnimationView la_play;
        private final CardView cv_media;

        public MediaViewHolder(View view) {
            super(view);
            iv_media = view.findViewById(R.id.iv_media);
            la_play = view.findViewById(R.id.la_play);
            cv_media = view.findViewById(R.id.cv_media);
        }
    }
}