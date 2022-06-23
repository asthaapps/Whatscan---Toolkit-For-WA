package com.whatscan.toolkit.forwa.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MainStickerSubListCategeryAdapter extends RecyclerView.Adapter<MainStickerSubListCategeryAdapter.MainSubStickerViewHolder> {

    Context context;
    List<String> stickerArrayLists;

    public MainStickerSubListCategeryAdapter(Context con, List<String> stickerLists) {
        this.context = con;
        this.stickerArrayLists = stickerLists;
    }

    @NotNull
    @Override
    public MainSubStickerViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new MainSubStickerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_main_sub_list_sticker_cat, parent, false));
    }

    @Override
    public void onBindViewHolder(@NotNull MainSubStickerViewHolder holder, int position) {
        Glide.with(context).load(Preference.getMain_Url() + Preference.getSticker_path() + stickerArrayLists.get(position))
                .placeholder(ContextCompat.getDrawable(context, R.drawable.sticker_error)).into(holder.img_list);
    }

    @Override
    public int getItemCount() {
        return stickerArrayLists.size();
    }

    public static class MainSubStickerViewHolder extends RecyclerView.ViewHolder {
        ImageView img_list;

        public MainSubStickerViewHolder(View itemView) {
            super(itemView);
            img_list = itemView.findViewById(R.id.img_list);
        }
    }
}