package com.whatscan.toolkit.forwa.MsgTools.Caption;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.GetSet.CaptionSubCategory;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.Util.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CaptionSubStatusAdapter extends RecyclerView.Adapter<CaptionSubStatusAdapter.CaptionSubViewHolder> {
    public FavData favdata = new FavData();
    public List<CaptionSubCategory> getCaptionsubData;
    public Context context;
    public LottieAnimationView la_up;

    CaptionSubStatusAdapter(Context applicationContext, List<CaptionSubCategory> getCaptionsub, LottieAnimationView la_up) {
        this.context = applicationContext;
        this.getCaptionsubData = getCaptionsub;
        this.la_up = la_up;
    }

    @NonNull
    @Override
    public CaptionSubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CaptionSubViewHolder(LayoutInflater.from(context).inflate(R.layout.caption_sub_status_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CaptionSubViewHolder holder, int position) {
        if (Preference.getBooleanTheme(false)){
            holder.cardOne.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorShape));
            holder.maintext.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        }

        holder.maintext.setText(getCaptionsubData.get(holder.getAbsoluteAdapterPosition()).getCaption());

        if (holder.getAbsoluteAdapterPosition() < 15) {
            la_up.setVisibility(View.GONE);
        } else {
            la_up.setVisibility(View.VISIBLE);
        }

        holder.copy_button.setOnClickListener(view -> {
            String str = getCaptionsubData.get(holder.getAbsoluteAdapterPosition()).getCaption();
            Utils.setClipboard(context, str);
        });

        holder.share_button.setOnClickListener(view -> {
            String str = getCaptionsubData.get(holder.getAbsoluteAdapterPosition()).getCaption();
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("text/plain");
            intent.setPackage("com.whatsapp");
            intent.putExtra("android.intent.extra.TEXT", str);
            if (intent.resolveActivity(CaptionSubStatusAdapter.this.context.getPackageManager()) != null) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                CaptionSubStatusAdapter.this.context.startActivity(intent);
            }
        });

        holder.fav_button.setOnClickListener(view -> new Thread(() -> {
            favdata.savedata(getCaptionsubData.get(holder.getAbsoluteAdapterPosition()).getCaption(), context);
            new Handler(Looper.getMainLooper()).post(() -> {
                Toast.makeText(context, "Added to favourites", Toast.LENGTH_SHORT).show();
                holder.lottie_fav.setAnimation(R.raw.fav_red);
            });
        }).start());

        holder.edit_button.setOnClickListener(view -> {
            Intent intent = new Intent(context, CaptionEditActivity.class);
            intent.putExtra("data", getCaptionsubData.get(holder.getAbsoluteAdapterPosition()).getCaption());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });

        holder.rl_view.setOnClickListener(v -> {
            Intent intent = new Intent(context, CaptionEditActivity.class);
            intent.putExtra("data", getCaptionsubData.get(holder.getAbsoluteAdapterPosition()).getCaption());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return getCaptionsubData.size();
    }


    public static class CaptionSubViewHolder extends RecyclerView.ViewHolder {
        TextView maintext;
        CardView cardOne;
        LottieAnimationView lottie_copy;
        LottieAnimationView lottie_fav;
        LottieAnimationView lottie_share;
        LottieAnimationView lottie_edit;
        RelativeLayout rl_view;
        LinearLayout copy_button, fav_button, share_button, edit_button;

        public CaptionSubViewHolder(@NonNull @NotNull View view) {
            super(view);
            cardOne = view.findViewById(R.id.cardOne);
            maintext = view.findViewById(R.id.textView);
            rl_view = view.findViewById(R.id.rl_view);
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
