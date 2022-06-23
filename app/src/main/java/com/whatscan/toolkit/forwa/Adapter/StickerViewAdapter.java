package com.whatscan.toolkit.forwa.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.whatscan.toolkit.forwa.GetSet.StickerArrayList;
import com.whatscan.toolkit.forwa.R;
import com.bumptech.glide.Glide;

import java.util.List;


public class StickerViewAdapter extends RecyclerView.Adapter<StickerViewAdapter.StickerPackListItemViewHolder> {
    List<StickerArrayList> stickerPacks;
    String sId;
    Context context;

    public StickerViewAdapter(Context mcontext, List<StickerArrayList> stickerPacks2, String id) {
        this.context = mcontext;
        this.stickerPacks = stickerPacks2;
        this.sId = id;
    }

    @Override
    @NonNull
    public StickerPackListItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new StickerPackListItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sticker_view_list_item, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull StickerPackListItemViewHolder viewHolder, int index) {
        String image = context.getFilesDir() + "/" + "stickers_asset" + "/" + sId + "/" + stickerPacks.get(index).getStickerImage();
        Glide.with(context).load(image).error(R.drawable.sticker_error).placeholder(R.drawable.sticker_error).into(viewHolder.preview);
    }

    @Override
    public int getItemCount() {
        return this.stickerPacks.size();
    }


    public static class StickerPackListItemViewHolder extends RecyclerView.ViewHolder {
        ImageView preview;

        StickerPackListItemViewHolder(View itemView) {
            super(itemView);
            preview = itemView.findViewById(R.id.preview);
        }
    }
}
