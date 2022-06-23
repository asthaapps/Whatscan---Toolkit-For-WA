package com.whatscan.toolkit.forwa.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.whatscan.toolkit.forwa.GetSet.StickerArray;
import com.whatscan.toolkit.forwa.GetSet.StickerArrayList;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.AppController;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.bumptech.glide.Glide;

import java.util.List;

public class StickerPackListAdapter extends RecyclerView.Adapter<StickerPackListAdapter.StickerPackListItemViewHolder> {
    List<StickerArray> stickerPacks;
    Context context;

    public StickerPackListAdapter(Context mcontext, List<StickerArray> stickerPacks2) {
        this.context = mcontext;
        this.stickerPacks = stickerPacks2;
    }

    @Override
    @NonNull
    public StickerPackListItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new StickerPackListItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sticker_packs_list_item, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull StickerPackListItemViewHolder viewHolder, int index) {
        if (Preference.getBooleanTheme(false)){
            viewHolder.cardOne.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorShape));
            viewHolder.titleView.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            viewHolder.publisherView.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        }

        viewHolder.publisherView.setText(stickerPacks.get(index).publisher_name);
        viewHolder.titleView.setText(stickerPacks.get(index).catagery_title);

        String demo = context.getFilesDir() + "/" + "stickers_asset" + "/" + stickerPacks.get(index).id + "/try" + "/" + stickerPacks.get(index).catagery_image;
        Glide.with(context).load(demo).error(R.drawable.sticker_error).into(viewHolder.bigcateicon);

        List<StickerArrayList> stickerArrayLists = stickerPacks.get(index).getStickers();
        if (AppController.tinyDB != null) {
            stickerArrayLists = AppController.tinyDB.getListSticker("arraySticker", StickerArrayList.class);
            StickerViewAdapter stickerViewAdapter = new StickerViewAdapter(context, stickerArrayLists, stickerPacks.get(index).id);
            viewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
            viewHolder.recyclerView.setAdapter(stickerViewAdapter);
        }
    }

    public void setStickerPackList(List<StickerArray> stickerPackList) {
        this.stickerPacks = stickerPackList;
    }

    @Override
    public int getItemCount() {
        return this.stickerPacks.size();
    }


    public static class StickerPackListItemViewHolder extends RecyclerView.ViewHolder {
        CardView cardOne;
        ImageView bigcateicon;
        View container;
        TextView publisherView;
        TextView titleView;
        RecyclerView recyclerView;

        StickerPackListItemViewHolder(View itemView) {
            super(itemView);
            container = itemView;
            cardOne = itemView.findViewById(R.id.cardOne);
            titleView = itemView.findViewById(R.id.sticker_pack_title);
            publisherView = itemView.findViewById(R.id.sticker_pack_publisher);
            bigcateicon = itemView.findViewById(R.id.bigcateicon);
            recyclerView = itemView.findViewById(R.id.sticker_view);
        }
    }
}
