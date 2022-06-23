package com.whatscan.toolkit.forwa.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.GetSet.StatusModel;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Story.ActivityImageView;
import com.whatscan.toolkit.forwa.Story.ActivityVideoView;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

public class StoryImageHome extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final ArrayList<StatusModel> arrayList;
    private final LayoutInflater layoutInflater;

    public StoryImageHome(ArrayList<StatusModel> arrayList, Context context) {
        this.context = context;
        this.arrayList = arrayList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view;
        view = layoutInflater.inflate(R.layout.item_story_home, parent, false);
        return new StoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        final StoryViewHolder storyViewHolder = (StoryViewHolder) holder;

        final StatusModel file = arrayList.get(position);
        if (arrayList.get(position).getFilePath().endsWith(".mp4")) {
            storyViewHolder.la_play.setVisibility(View.VISIBLE);
            Glide.with(context).load(file.getFilePath()).into(storyViewHolder.iv_status);
        } else {
            Glide.with(context).load(arrayList.get(position).getFilePath()).into(((StoryViewHolder) holder).iv_status);
        }

        storyViewHolder.iv_status.setOnClickListener(v -> {
            if (arrayList.get(position).getFilePath().endsWith(".mp4")) {
                Intent intent = new Intent(context, ActivityVideoView.class);
                intent.putExtra("path", file.getFilePath());
                intent.putExtra("strCheck", "story");
                context.startActivity(intent);
            } else {
                Intent intent = new Intent(context, ActivityImageView.class);
                intent.putExtra("path", file.getFilePath());
                intent.putExtra("strCheck", "story");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    private static class StoryViewHolder extends RecyclerView.ViewHolder {

        private final ImageView iv_status;
        private final LottieAnimationView la_play;

        public StoryViewHolder(View view) {
            super(view);
            iv_status = view.findViewById(R.id.iv_status);
            la_play = view.findViewById(R.id.la_play);
        }
    }
}