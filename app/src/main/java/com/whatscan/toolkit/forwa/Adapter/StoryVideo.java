package com.whatscan.toolkit.forwa.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.GetSet.StatusModel;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Story.ActivityVideoView;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.Util.Utils;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Objects;

public class StoryVideo extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final ArrayList<StatusModel> arrayList;
    private final LayoutInflater layoutInflater;

    public StoryVideo(ArrayList<StatusModel> arrayList, Context context) {
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

        if (!Utils.getBack(arrayList.get(position).getFilePath(), "((\\.mp4|\\.webm|\\.ogg|\\.mpK|\\.avi|\\.mkv|\\.flv|\\.mpg|\\.wmv|\\.vob|\\.ogv|\\.mov|\\.qt|\\.rm|\\.rmvb\\.|\\.asf|\\.m4p|\\.m4v|\\.mp2|\\.mpeg|\\.mpe|\\.mpv|\\.m2v|\\.3gp|\\.f4p|\\.f4a|\\.f4b|\\.f4v)$)").isEmpty()) {
            storyViewHolder.la_play.setVisibility(View.VISIBLE);
        } else {
            storyViewHolder.la_play.setVisibility(View.GONE);
        }

        Glide.with(context).load(arrayList.get(position).getFilePath()).into(storyViewHolder.iv_status);

        storyViewHolder.tv_download.setOnClickListener(v -> Utils.download(context, arrayList.get(position).getFilePath()));

        storyViewHolder.iv_status.setOnClickListener(v -> SendIntent(arrayList.get(position).getFilePath()));
        storyViewHolder.la_play.setOnClickListener(v -> SendIntent(arrayList.get(position).getFilePath()));
    }

    private void SendIntent(String file) {
        Intent intent = new Intent(context, ActivityVideoView.class);
        intent.putExtra("path", file);
        intent.putExtra("strCheck", "story");
        context.startActivity(intent);
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