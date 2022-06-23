package com.whatscan.toolkit.forwa.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.Ads.Advertisement;
import com.whatscan.toolkit.forwa.GetSet.StickerCatagery;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.WSticker.ActivityStickerSubCategory;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MainStickerCategeryAdapter extends RecyclerView.Adapter<MainStickerCategeryAdapter.MainStickerViewHolder> {

    Context context;
    List<StickerCatagery> getMainSticker;

    public MainStickerCategeryAdapter(Context con, List<StickerCatagery> getMainStickercat) {
        this.context = con;
        this.getMainSticker = getMainStickercat;
    }

    @NonNull
    @NotNull
    @Override
    public MainStickerViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new MainStickerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_main_sticker_cat, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MainStickerViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if (Preference.getBooleanTheme(false)) {
            holder.rlSticker.setBackgroundColor(ContextCompat.getColor(context, R.color.colorShape));
            holder.txt_cat_name.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        } else {
            holder.rlSticker.setBackgroundColor(Color.parseColor("#97F6F6F6"));
            holder.txt_cat_name.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
        }

        holder.txt_cat_name.setText(getMainSticker.get(position).getCatageryName());

        holder.lottie_cat.setAnimationFromUrl(Preference.getSticker_url() + getMainSticker.get(position).getCatageryJson());


        holder.itemView.setOnClickListener(v -> {
            Advertisement.getInstance((Activity) context).showFullAds(() -> {
                Intent intent = new Intent(context, ActivityStickerSubCategory.class);
                intent.putExtra("Cat_Name", getMainSticker.get(position).getCatageryName());
                intent.putExtra("Sticker_Id", getMainSticker.get(position).getId());
                context.startActivity(intent, ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle());
            });
        });
    }

    @Override
    public int getItemCount() {
        return getMainSticker.size();
    }

    public static class MainStickerViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout rlSticker;
        public TextView txt_cat_name;
        public LottieAnimationView lottie_cat;

        public MainStickerViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            rlSticker = itemView.findViewById(R.id.rlSticker);
            txt_cat_name = itemView.findViewById(R.id.txt_cat_name);
            lottie_cat = itemView.findViewById(R.id.lottie_cat);
        }
    }
}
