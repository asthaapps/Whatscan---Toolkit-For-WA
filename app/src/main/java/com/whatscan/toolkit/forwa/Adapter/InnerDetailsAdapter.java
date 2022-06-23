package com.whatscan.toolkit.forwa.Adapter;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.whatscan.toolkit.forwa.BuildConfig;
import com.whatscan.toolkit.forwa.GetSet.FileDetails;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class InnerDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public final int IMAGES = 1;
    public final int VIDEOS = 2;
    public final int AUDIOS = 3;
    public final int FILE = 4;
    public final int VOICE = 6;
    public Context ctx;
    public ArrayList<FileDetails> innerDataList;
    public OnCheckboxListener onCheckboxListener;
    public int type;

    public InnerDetailsAdapter(int type, Context ctx, ArrayList<FileDetails> innerDataList, OnCheckboxListener onCheckboxListener) {
        this.type = type;
        this.ctx = ctx;
        this.innerDataList = innerDataList;
        this.onCheckboxListener = onCheckboxListener;
    }

    @Override
    public int getItemViewType(int position) {
        switch (type) {
            default:
            case IMAGES:
                return IMAGES;
            case VIDEOS:
                return VIDEOS;
            case AUDIOS:
                return AUDIOS;
            case FILE:
                return FILE;
            case VOICE:
                return VOICE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == IMAGES) {
            View view = LayoutInflater.from(ctx).inflate(R.layout.item_image_clean, parent, false);
            return new InnerDataViewHolderMultimedia(view);
        } else if (viewType == VIDEOS) {
            View view = LayoutInflater.from(ctx).inflate(R.layout.item_videos_clean, parent, false);
            return new InnerDataViewHolderVideos(view);
        } else if (viewType == VOICE) {
            View view = LayoutInflater.from(ctx).inflate(R.layout.item_doc_clean, parent, false);
            return new InnerDataViewHolderDoc(view);
        } else {
            View view = LayoutInflater.from(ctx).inflate(R.layout.item_doc_clean, parent, false);
            return new InnerDataViewHolderDoc(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int positions) {
        if (getItemViewType(positions) == IMAGES) {
            final InnerDataViewHolderMultimedia innerDataViewHolderMultimedia = (InnerDataViewHolderMultimedia) viewHolder;
            final FileDetails details = innerDataList.get(positions);

            Glide.with(ctx)
                    .load(details.getPath())
                    .transition(withCrossFade())
                    .placeholder(R.drawable.ic_image)
                    .into(innerDataViewHolderMultimedia.imageView);

            innerDataViewHolderMultimedia.imageView.setOnLongClickListener(v -> {
                File a = new File(String.valueOf(Uri.parse(details.getPath())));
                Uri uri = FileProvider.getUriForFile(ctx, BuildConfig.APPLICATION_ID + ".provider", a);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String mime = "*/*";
                MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                if (mimeTypeMap.hasExtension(MimeTypeMap.getFileExtensionFromUrl(uri.toString()))) {
                    mime = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(uri.toString()));
                }
                try {
                    Log.e("Mime", mime);
                    intent.setDataAndType(uri, mime);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    ctx.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(ctx, "Couldn't find app that open this file ", Toast.LENGTH_SHORT).show();
                }
                return true;
            });

            innerDataViewHolderMultimedia.imageView.setOnClickListener(v -> {
                if (innerDataViewHolderMultimedia.checkBox.isChecked()) {
                    innerDataViewHolderMultimedia.checkBox.setChecked(false);
                    innerDataList.get(innerDataViewHolderMultimedia.getAbsoluteAdapterPosition()).setSelected(false);
                } else {
                    innerDataViewHolderMultimedia.checkBox.setChecked(true);
                    innerDataList.get(innerDataViewHolderMultimedia.getAbsoluteAdapterPosition()).setSelected(true);
                }

                if (onCheckboxListener != null) {
                    onCheckboxListener.oncheckboxlistener(v, innerDataList);
                }
            });

            innerDataViewHolderMultimedia.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                innerDataList.get(innerDataViewHolderMultimedia.getAbsoluteAdapterPosition()).setSelected(isChecked);
                if (onCheckboxListener != null) {
                    onCheckboxListener.oncheckboxlistener(buttonView, innerDataList);
                }
            });

            if (details.isSelected()) {
                innerDataViewHolderMultimedia.checkBox.setChecked(true);
            } else {
                innerDataViewHolderMultimedia.checkBox.setChecked(false);
            }
        } else if (getItemViewType(positions) == VIDEOS) {
            final InnerDataViewHolderVideos innerDataViewHolderVideos = (InnerDataViewHolderVideos) viewHolder;
            final FileDetails details = innerDataList.get(positions);

            innerDataViewHolderVideos.circleImageView.setImageResource(details.getImage());

            Glide.with(ctx)
                    .load(details.getPath())
                    .transition(withCrossFade())
                    .thumbnail(Glide.with(ctx).load(R.drawable.ic_video))
                    .into(innerDataViewHolderVideos.imageView);

            innerDataViewHolderVideos.imageView.setOnLongClickListener(v -> {
                File a = new File(String.valueOf(Uri.parse(details.getPath())));

                Uri uri = FileProvider.getUriForFile(ctx, BuildConfig.APPLICATION_ID + ".provider", a);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String mime = "*/*";
                MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                if (mimeTypeMap.hasExtension(MimeTypeMap.getFileExtensionFromUrl(uri.toString()))) {
                    mime = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(uri.toString()));
                }
                try {
                    Log.e("Mime", mime);
                    intent.setDataAndType(uri, mime);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    ctx.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(ctx, "Couldn't find app that open this file ", Toast.LENGTH_SHORT).show();
                }
                return true;
            });

            innerDataViewHolderVideos.imageView.setOnClickListener(v -> {
                if (innerDataViewHolderVideos.checkBox.isChecked()) {
                    innerDataViewHolderVideos.checkBox.setChecked(false);
                    innerDataList.get(innerDataViewHolderVideos.getAbsoluteAdapterPosition()).setSelected(false);
                } else {
                    innerDataViewHolderVideos.checkBox.setChecked(true);
                    innerDataList.get(innerDataViewHolderVideos.getAbsoluteAdapterPosition()).setSelected(true);
                }
                if (onCheckboxListener != null) {
                    onCheckboxListener.oncheckboxlistener(v, innerDataList);
                }
            });

            innerDataViewHolderVideos.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                innerDataList.get(innerDataViewHolderVideos.getAbsoluteAdapterPosition()).setSelected(isChecked);
                if (onCheckboxListener != null) {
                    onCheckboxListener.oncheckboxlistener(buttonView, innerDataList);
                }
            });

            if (details.isSelected()) {
                innerDataViewHolderVideos.checkBox.setChecked(true);
            } else {
                innerDataViewHolderVideos.checkBox.setChecked(false);
            }
        } else if (getItemViewType(positions) == AUDIOS) {
            final InnerDataViewHolderDoc innerDataViewHolder = (InnerDataViewHolderDoc) viewHolder;
            final FileDetails details = innerDataList.get(positions);
            innerDataViewHolder.tittle_name.setText(details.getName());
            innerDataViewHolder.data.setText(String.valueOf(details.getSize()));
            innerDataViewHolder.ext.setText(details.getExt());
            innerDataViewHolder.imageView.setImageResource(details.getImage());

            innerDataViewHolder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                innerDataList.get(innerDataViewHolder.getAbsoluteAdapterPosition()).setSelected(isChecked);
                if (onCheckboxListener != null) {
                    onCheckboxListener.oncheckboxlistener(buttonView, innerDataList);
                }
            });

            if (details.isSelected()) {
                innerDataViewHolder.checkBox.setChecked(true);
            } else {
                innerDataViewHolder.checkBox.setChecked(false);
            }

            innerDataViewHolder.cardView.setOnClickListener(v -> {
                File a = new File(String.valueOf(Uri.parse(details.getPath())));
                Uri uri = FileProvider.getUriForFile(ctx, BuildConfig.APPLICATION_ID + ".provider", a);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String mime = "*/*";
                MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                if (mimeTypeMap.hasExtension(MimeTypeMap.getFileExtensionFromUrl(uri.toString()))) {
                    mime = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(uri.toString()));
                }
                try {
                    Log.e("Mime", mime);
                    intent.setDataAndType(uri, mime);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    ctx.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(ctx, "Couldn't find app that open this file ", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (getItemViewType(positions) == VOICE) {
            final InnerDataViewHolderDoc innerDataViewHolder = (InnerDataViewHolderDoc) viewHolder;
            final FileDetails details = innerDataList.get(positions);
            innerDataViewHolder.tittle_name.setText(details.getName());
            innerDataViewHolder.data.setText(String.valueOf(details.getSize()));
            innerDataViewHolder.ext.setText(details.getExt());
            innerDataViewHolder.imageView.setImageResource(details.getImage());

            innerDataViewHolder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                innerDataList.get(innerDataViewHolder.getAbsoluteAdapterPosition()).setSelected(isChecked);
                if (onCheckboxListener != null) {
                    onCheckboxListener.oncheckboxlistener(buttonView, innerDataList);
                }
            });

            if (details.isSelected()) {
                innerDataViewHolder.checkBox.setChecked(true);
            } else {
                innerDataViewHolder.checkBox.setChecked(false);
            }

            innerDataViewHolder.cardView.setOnClickListener(v -> {
                File a = new File(String.valueOf(Uri.parse(details.getPath())));
                Uri uri = FileProvider.getUriForFile(ctx, BuildConfig.APPLICATION_ID + ".provider", a);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String mime = "*/*";
                MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                if (mimeTypeMap.hasExtension(MimeTypeMap.getFileExtensionFromUrl(uri.toString()))) {
                    mime = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(uri.toString()));
                }
                try {
                    Log.e("Mime", mime);
                    intent.setDataAndType(uri, mime);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    ctx.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(ctx, "Couldn't find app that open this file ", Toast.LENGTH_SHORT).show();
                }
            });

        } else if (getItemViewType(positions) == FILE) {
            final InnerDataViewHolderDoc innerDataViewHolder = (InnerDataViewHolderDoc) viewHolder;

            if (Preference.getBooleanTheme(false)) {
                innerDataViewHolder.cardView.setCardBackgroundColor(ContextCompat.getColor(ctx, R.color.colorShape));
                innerDataViewHolder.tittle_name.setTextColor(ContextCompat.getColor(ctx, R.color.colorWhite));
                innerDataViewHolder.data.setTextColor(ContextCompat.getColor(ctx, R.color.colorWhite));
            } else {
                innerDataViewHolder.cardView.setCardBackgroundColor(ContextCompat.getColor(ctx, R.color.colorWhite));
                innerDataViewHolder.tittle_name.setTextColor(ContextCompat.getColor(ctx, R.color.colorBlack));
            }
            final FileDetails details = innerDataList.get(positions);
            innerDataViewHolder.tittle_name.setText(details.getName());
            innerDataViewHolder.data.setText(String.valueOf(details.getSize()));
            innerDataViewHolder.ext.setText(details.getExt());
            innerDataViewHolder.imageView.setImageResource(details.getImage());

            innerDataViewHolder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                innerDataList.get(innerDataViewHolder.getAbsoluteAdapterPosition()).setSelected(isChecked);
                if (onCheckboxListener != null) {
                    onCheckboxListener.oncheckboxlistener(buttonView, innerDataList);
                }
            });

            if (details.isSelected()) {
                innerDataViewHolder.checkBox.setChecked(true);
            } else {
                innerDataViewHolder.checkBox.setChecked(false);
            }

            innerDataViewHolder.cardView.setOnClickListener(v -> {
                File a = new File(String.valueOf(Uri.parse(details.getPath())));
                Uri uri = FileProvider.getUriForFile(ctx, BuildConfig.APPLICATION_ID + ".provider", a);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String mime = "*/*";
                MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                if (mimeTypeMap.hasExtension(MimeTypeMap.getFileExtensionFromUrl(uri.toString()))) {
                    mime = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(uri.toString()));
                }
                try {
                    Log.e("Mime", mime);
                    intent.setDataAndType(uri, mime);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    ctx.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(ctx, "Couldn't find app that open this file ", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return innerDataList.size();
    }

    public interface OnCheckboxListener {
        void oncheckboxlistener(View view, List<FileDetails> updatedFiles);
    }

    public static class InnerDataViewHolderMultimedia extends RecyclerView.ViewHolder {
        ImageView imageView;
        CardView cardView;
        CheckBox checkBox;

        public InnerDataViewHolderMultimedia(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            cardView = itemView.findViewById(R.id.recycler_view);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }

    public static class InnerDataViewHolderVideos extends RecyclerView.ViewHolder {
        ImageView imageView, circleImageView;
        CardView cardView;
        CheckBox checkBox;

        public InnerDataViewHolderVideos(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            cardView = itemView.findViewById(R.id.recycler_view);
            checkBox = itemView.findViewById(R.id.checkbox);
            circleImageView = itemView.findViewById(R.id.play);
        }
    }

    public static class InnerDataViewHolderDoc extends RecyclerView.ViewHolder {
        TextView tittle_name, data, ext;
        CardView cardView;
        CheckBox checkBox;
        ImageView imageView;

        public InnerDataViewHolderDoc(View itemView) {
            super(itemView);
            tittle_name = itemView.findViewById(R.id.title);
            data = itemView.findViewById(R.id.data);
            cardView = itemView.findViewById(R.id.recycler_view);
            checkBox = itemView.findViewById(R.id.checkbox);
            imageView = itemView.findViewById(R.id.image);
            ext = itemView.findViewById(R.id.extension);
        }
    }
}