package com.whatscan.toolkit.forwa.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.whatscan.toolkit.forwa.BuildConfig;
import com.whatscan.toolkit.forwa.BulkSender.helper.FileExtension;
import com.whatscan.toolkit.forwa.BulkSender.helper.FileSharingUtils;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Event;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;

import kotlin.io.FilesKt;

public final class FileSelectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public final Activity activity;
    public final ArrayList<File> fileArrayList;
    public ImageView imgVideoView, imgPlayView, imgMediaView;

    public FileSelectionAdapter(@NotNull Activity activity2, @NotNull ArrayList<File> arrayList) {
        activity = activity2;
        fileArrayList = arrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int i) {
        if (i == Event.VIDEO.ordinal() || i == Event.IMAGE.ordinal()) {
            View inflate = LayoutInflater.from(activity).inflate(R.layout.item_image_video, viewGroup, false);
            return new ImageVideoHolder(inflate);
        } else if (i == Event.PDF.ordinal()) {
            View inflate2 = LayoutInflater.from(this.activity).inflate(R.layout.item_pdf, viewGroup, false);
            return new PDFHolder(inflate2);
        } else if (i == Event.AUDIO.ordinal()) {
            View inflate3 = LayoutInflater.from(this.activity).inflate(R.layout.item_audio, viewGroup, false);
            return new AudioHolder(inflate3);
        } else {
            View inflate4 = LayoutInflater.from(this.activity).inflate(R.layout.item_image_video, viewGroup, false);
            return new ImageVideoHolder(inflate4);
        }
    }

    @Override
    public void onBindViewHolder(@NotNull RecyclerView.ViewHolder viewHolder, int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("getItemViewType  ");
        File file = this.fileArrayList.get(i);
        sb.append(file.getAbsolutePath());
        if (getItemViewType(i) == Event.IMAGE.ordinal()) {
            try {
                imgVideoView = viewHolder.itemView.findViewById(R.id.imgVideoView);
                imgPlayView = viewHolder.itemView.findViewById(R.id.imgPlayView);
                imgMediaView = viewHolder.itemView.findViewById(R.id.imgMediaView);
                imgVideoView.setVisibility(View.GONE);
                imgPlayView.setVisibility(View.GONE);

                Glide.with(activity).asBitmap().load(file.getAbsolutePath()).thumbnail(0.1f).into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull @NotNull Bitmap resource, @androidx.annotation.Nullable @Nullable Transition<? super Bitmap> transition) {
                        imgMediaView.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadCleared(@androidx.annotation.Nullable @Nullable Drawable placeholder) {

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (getItemViewType(i) == Event.VIDEO.ordinal()) {
            imgVideoView = viewHolder.itemView.findViewById(R.id.imgVideoView);
            imgPlayView = viewHolder.itemView.findViewById(R.id.imgPlayView);
            imgMediaView = viewHolder.itemView.findViewById(R.id.imgMediaView);
            imgVideoView.setVisibility(View.VISIBLE);
            imgPlayView.setVisibility(View.VISIBLE);

            Glide.with(activity).asBitmap().load(fileArrayList.get(i).getAbsolutePath()).thumbnail(0.1f).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull @NotNull Bitmap resource, @androidx.annotation.Nullable @Nullable Transition<? super Bitmap> transition) {
                    imgMediaView.setImageBitmap(resource);
                }

                @Override
                public void onLoadCleared(@androidx.annotation.Nullable @Nullable Drawable placeholder) {

                }
            });
        } else if (getItemViewType(i) == Event.PDF.ordinal()) {
            TextView txtPdfName = viewHolder.itemView.findViewById(R.id.txtPdfName);
            TextView txtPdfInfo = viewHolder.itemView.findViewById(R.id.txtPdfInfo);
            CardView cardView = viewHolder.itemView.findViewById(R.id.cardView);

            if (Preference.getBooleanTheme(false)) {
                cardView.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.colorShape));
                txtPdfName.setTextColor(ContextCompat.getColor(activity, R.color.colorWhite));
                txtPdfInfo.setTextColor(ContextCompat.getColor(activity, R.color.colorWhite));
            }

            txtPdfName.setText(fileArrayList.get(i).getName());
            txtPdfInfo.setText(FileSharingUtils.INSTANCE.getFileSizeInString(fileArrayList.get(i).length()));
        } else if (getItemViewType(i) == Event.AUDIO.ordinal()) {
            TextView txtAudioName = viewHolder.itemView.findViewById(R.id.txtAudioName);
            TextView txtAudioSize = viewHolder.itemView.findViewById(R.id.txtAudioSize);
            CardView cardView = viewHolder.itemView.findViewById(R.id.cardView);

            if (Preference.getBooleanTheme(false)) {
                cardView.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.colorShape));
                txtAudioName.setTextColor(ContextCompat.getColor(activity, R.color.colorWhite));
            }

            txtAudioName.setText(fileArrayList.get(i).getName());
            txtAudioSize.setText(FileSharingUtils.INSTANCE.getFileSizeInString(fileArrayList.get(i).length()));
        }
    }

    @Override
    public int getItemViewType(int i) {
        File file = fileArrayList.get(i);
        String extension = FilesKt.getExtension(file);
        switch (extension) {
            case FileExtension.pdf:
                return Event.PDF.ordinal();
            case FileExtension.Audio.mp3:
            case FileExtension.Audio.flac:
            case FileExtension.Audio.wav:
            case FileExtension.Audio.ogg:
            case FileExtension.Audio.m4a:
            case FileExtension.Audio.aac:
            case FileExtension.Audio.amr:
                return Event.AUDIO.ordinal();
            case "png":
            case "jpg":
            case FileExtension.Image.jpeg:
            case "webp":
                return Event.IMAGE.ordinal();
            default:
                if (extension.equals(FileExtension.Video.mp4) || extension.equals(FileExtension.Video.mkv) || extension.equals(FileExtension.Video.threegp) || extension.equals(FileExtension.Video.webm)) {
                    return Event.VIDEO.ordinal();
                }
                return Event.IMAGE.ordinal();
        }
    }

    @Override
    public int getItemCount() {
        return fileArrayList.size();
    }

    public final class AudioHolder extends RecyclerView.ViewHolder {
        @SuppressLint("WrongConstant")
        public AudioHolder(@NotNull View view) {
            super(view);
            view.setOnClickListener(view1 -> {
                Intent intent;
                if (Build.VERSION.SDK_INT >= 24) {
                    intent = new Intent("android.intent.action.VIEW", FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".provider", fileArrayList.get(getAbsoluteAdapterPosition())));
                } else {
                    intent = new Intent("android.intent.action.VIEW", Uri.fromFile(fileArrayList.get(getAbsoluteAdapterPosition())));
                }
                intent.setFlags(268435457);
                intent.setType("audio/mp3");
                try {
                    activity.startActivity(Intent.createChooser(intent, "Open Audio"));
                } catch (ActivityNotFoundException unused) {
                    unused.printStackTrace();
                }
            });

            view.findViewById(R.id.imgAudioRemove).setOnClickListener(view12 -> {
                fileArrayList.remove(getAbsoluteAdapterPosition());
                notifyItemRemoved(getAbsoluteAdapterPosition());
            });
        }
    }

    public final class ImageVideoHolder extends RecyclerView.ViewHolder {
        public ImageVideoHolder(@NotNull View view) {
            super(view);
            view.findViewById(R.id.imgVideoRemove).setOnClickListener(view12 -> {
                fileArrayList.remove(getAbsoluteAdapterPosition());
                notifyItemRemoved(getAbsoluteAdapterPosition());
            });
        }
    }

    public final class PDFHolder extends RecyclerView.ViewHolder {
        @SuppressLint("WrongConstant")
        public PDFHolder(@NotNull View view) {
            super(view);
            view.setOnClickListener(view1 -> {
                Intent intent;
                if (Build.VERSION.SDK_INT >= 24) {
                    intent = new Intent("android.intent.action.VIEW", FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".provider", fileArrayList.get(getAbsoluteAdapterPosition())));
                } else {
                    intent = new Intent("android.intent.action.VIEW", Uri.fromFile(fileArrayList.get(getAbsoluteAdapterPosition())));
                }
                intent.setFlags(268435457);
                intent.setType("application/pdf");
                try {
                    activity.startActivity(Intent.createChooser(intent, "Open PDF"));
                } catch (ActivityNotFoundException unused) {
                    unused.printStackTrace();
                }
            });

            view.findViewById(R.id.optionImageView).setOnClickListener(view12 -> {
                fileArrayList.remove(getAbsoluteAdapterPosition());
                notifyItemRemoved(getAbsoluteAdapterPosition());
            });
        }
    }
}