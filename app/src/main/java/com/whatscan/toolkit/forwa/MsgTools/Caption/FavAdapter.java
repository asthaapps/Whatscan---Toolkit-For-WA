package com.whatscan.toolkit.forwa.MsgTools.Caption;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.Util.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.MyViewHolder> {
    private final Context context;
    private final ArrayList<String> list;

    public FavAdapter(Context context2, ArrayList<String> list2) {
        this.context = context2;
        this.list = list2;
    }

    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.caption_sub_status_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (Preference.getBooleanTheme(false)) {
            holder.cardOne.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorShape));
            holder.maintext.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        }

        holder.fav_button.setVisibility(View.GONE);
        holder.maintext.setText(list.get(position));

        holder.maintext.setOnClickListener(view -> {
            Intent intent = new Intent(context, CaptionEditActivity.class);
            intent.putExtra("data", list.get(position));
            FavAdapter.this.context.startActivity(intent);
        });

        holder.copy_button.setOnClickListener(view -> Utils.setClipboard(context, list.get(position)));

        holder.share_button.setOnClickListener(view -> {
            try {
                Intent intent2 = new Intent("android.intent.action.SEND");
                intent2.setType("text/plain");
                intent2.putExtra("android.intent.extra.TEXT", list.get(position));
                context.startActivity(intent2);
            } catch (Exception e2) {
                Log.d("favadapterlogs", e2.toString());
            }
        });

        holder.edit_button.setOnClickListener(view -> context.startActivity(new Intent(context, CaptionEditActivity.class).putExtra("data", list.get(position)), ActivityOptionsCompat.makeScaleUpAnimation(view, (int) view.getX(), (int) view.getY(), view.getWidth(), view.getHeight()).toBundle()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardOne;
        TextView maintext;
        LottieAnimationView lottie_copy;
        LottieAnimationView lottie_fav;
        LottieAnimationView lottie_share;
        LottieAnimationView lottie_edit;
        LinearLayout copy_button, fav_button, share_button, edit_button;

        public MyViewHolder(View view) {
            super(view);
            cardOne = view.findViewById(R.id.cardOne);
            maintext = view.findViewById(R.id.textView);
            copy_button = view.findViewById(R.id.copy_button);
            fav_button = view.findViewById(R.id.fav_button);
            share_button = view.findViewById(R.id.share_button);
            edit_button = view.findViewById(R.id.edit_button);
            lottie_copy = view.findViewById(R.id.lottie_copy);
            lottie_fav = view.findViewById(R.id.lottie_fav);
            lottie_share = view.findViewById(R.id.lottie_share);
            lottie_edit = view.findViewById(R.id.lottie_edit);

        }
    }
}