package com.whatscan.toolkit.forwa.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.whatscan.toolkit.forwa.GetSet.StickerArray;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.WSticker.ActivityStickerDetails;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MainStickerSubCategeryAdapter extends RecyclerView.Adapter<MainStickerSubCategeryAdapter.MainSubStickerViewHolder> {

    Context context;
    List<StickerArray> getMainSubSticker;

    public MainStickerSubCategeryAdapter(Context con, List<StickerArray> getSubSticker) {
        this.context = con;
        this.getMainSubSticker = getSubSticker;
    }

    @NotNull
    @Override
    public MainSubStickerViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new MainSubStickerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_main_sub_sticker_cat, parent, false));
    }

    @Override
    public void onBindViewHolder(@NotNull MainSubStickerViewHolder holder, int position) {

        if (Preference.getBooleanTheme(false)) {
            holder.cardOne.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorShape));
            holder.cardTwo.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorShape));
            holder.cat_name.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        } else {
            holder.cardOne.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
            holder.cardTwo.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
            holder.cat_name.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
        }

        holder.cat_name.setText(getMainSubSticker.get(position).getCatageryTitle());

        Glide.with(context).asBitmap().load(Preference.getMain_Url() + Preference.getSticker_path() + getMainSubSticker.get(position).catagery_image).into(holder.cat_image);

        Glide.with(context)
                .load(Preference.getMain_Url() + Preference.getSticker_path() + getMainSubSticker.get(position).getStickers().get(0).sticker_image)
                .placeholder(R.drawable.sticker_error)
                .error(R.drawable.sticker_error)
                .into(holder.img_one);

        Glide.with(context)
                .load(Preference.getMain_Url() + Preference.getSticker_path() + getMainSubSticker.get(position).getStickers().get(1).sticker_image)
                .placeholder(R.drawable.sticker_error)
                .error(R.drawable.sticker_error)
                .into(holder.img_two);

        if (getMainSubSticker.get(position).getStickers().size() > 2) {
            Glide.with(context)
                    .load(Preference.getMain_Url() + Preference.getSticker_path() + getMainSubSticker.get(position).getStickers().get(2).sticker_image)
                    .placeholder(R.drawable.sticker_error)
                    .error(R.drawable.sticker_error)
                    .into(holder.img_three);
        } else {
            holder.img_three.setVisibility(View.INVISIBLE);
        }

        if (getMainSubSticker.get(position).getStickers().size() > 3) {
            Glide.with(context)
                    .load(Preference.getMain_Url() + Preference.getSticker_path() + getMainSubSticker.get(position).getStickers().get(3).sticker_image)
                    .placeholder(R.drawable.sticker_error)
                    .error(R.drawable.sticker_error)
                    .into(holder.img_four);
        } else {
            holder.img_four.setVisibility(View.INVISIBLE);
        }

        if (getMainSubSticker.get(position).getStickers().size() > 4) {
            Glide.with(context)
                    .load(Preference.getMain_Url() + Preference.getSticker_path() + getMainSubSticker.get(position).getStickers().get(4).sticker_image)
                    .placeholder(R.drawable.sticker_error)
                    .error(R.drawable.sticker_error)
                    .into(holder.img_five);
        } else {
            holder.img_five.setVisibility(View.INVISIBLE);
        }

        holder.rl_intent.setOnClickListener(v -> {
            Intent intent = new Intent(context, ActivityStickerDetails.class);
            intent.putExtra("sticker_pack",   getMainSubSticker.get(position));
            context.startActivity(intent, ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle());
        });
    }

    @Override
    public int getItemCount() {
        return getMainSubSticker.size();
    }

    public static class MainSubStickerViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView cat_image;
        RelativeLayout rl_intent;
        TextView cat_name;
        CardView cardOne, cardTwo;
        ImageView img_one, img_two, img_three, img_four, img_five;

        public MainSubStickerViewHolder(View itemView) {
            super(itemView);
            rl_intent = itemView.findViewById(R.id.rl_intent);
            cardOne = itemView.findViewById(R.id.cardOne);
            cardTwo = itemView.findViewById(R.id.cardTwo);
            cat_image = itemView.findViewById(R.id.cat_image);
            cat_name = itemView.findViewById(R.id.cat_name);
            img_one = itemView.findViewById(R.id.img_one);
            img_two = itemView.findViewById(R.id.img_two);
            img_three = itemView.findViewById(R.id.img_three);
            img_four = itemView.findViewById(R.id.img_four);
            img_five = itemView.findViewById(R.id.img_five);
        }
    }
}