package com.whatscan.toolkit.forwa.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Story.ActivityImageView;
import com.whatscan.toolkit.forwa.Story.ActivityVideoView;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

public class StoryDownload extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final ArrayList<File> arrayList;
    private final LayoutInflater layoutInflater;

    public StoryDownload(ArrayList<File> arrayList, Context context) {
        this.context = context;
        this.arrayList = arrayList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view;
        view = layoutInflater.inflate(R.layout.item_story, parent, false);
        return new StoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        final StoryViewHolder storyViewHolder = (StoryViewHolder) holder;

        if (Preference.getBooleanTheme(false)) {
            storyViewHolder.cardMain.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorShape));
        } else {
            storyViewHolder.cardMain.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
        }

        final File file = arrayList.get(position);
        if (arrayList.get(position).getName().contains(".jpg")) {
            storyViewHolder.iv_status.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
        } else {
            Glide.with(context).load("file://" + arrayList.get(position)).into(storyViewHolder.iv_status);
        }

        if (file.getAbsolutePath().endsWith(".mp4")) {
            storyViewHolder.la_play.setVisibility(View.VISIBLE);
        }
        storyViewHolder.tv_download.setVisibility(View.GONE);

        storyViewHolder.iv_status.setOnClickListener(v -> SendIntent(file));
        storyViewHolder.la_play.setOnClickListener(v -> SendIntent(file));
    }

    private void SendIntent(File file) {
        if (file.getAbsolutePath().endsWith(".jpg")) {
            Intent intent = new Intent(context, ActivityImageView.class);
            intent.putExtra("path", file.getPath());
            intent.putExtra("strCheck", "clean");
            context.startActivity(intent);
        } else if (file.getAbsolutePath().endsWith(".mp4")) {
            Intent intent = new Intent(context, ActivityVideoView.class);
            intent.putExtra("path", file.getPath());
            intent.putExtra("strCheck", "clean");
            context.startActivity(intent);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    private static class StoryViewHolder extends RecyclerView.ViewHolder {
        private final ImageView iv_status;
        private final LottieAnimationView la_play;
        private final TextView tv_download;
        private final CardView cardMain;

        public StoryViewHolder(View view) {
            super(view);
            iv_status = view.findViewById(R.id.iv_status);
            la_play = view.findViewById(R.id.la_play);
            tv_download = view.findViewById(R.id.tv_download);
            cardMain = view.findViewById(R.id.cardMain);
        }
    }
}