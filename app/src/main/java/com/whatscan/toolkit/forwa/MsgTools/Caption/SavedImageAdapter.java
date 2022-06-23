package com.whatscan.toolkit.forwa.MsgTools.Caption;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.BuildConfig;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.Util.Utils;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

public class SavedImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final ArrayList<String> list;

    public SavedImageAdapter(Context context2, ArrayList<String> arrayList) {
        context = context2;
        list = arrayList;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_saved_imgae, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NotNull RecyclerView.ViewHolder viewHolder, final int i) {
        ViewHolder viewHolder2 = (ViewHolder) viewHolder;

        if (Preference.getBooleanTheme(false)) {
            viewHolder2.cardOne.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorShape));
        }

        Glide.with(context).load(list.get(i)).into(viewHolder2.mainImage);

        viewHolder2.copy_button.setOnClickListener(v -> Utils.setClipboard(context, list.get(i)));

        viewHolder2.share_button.setOnClickListener(v -> {
            try {
                Intent intent2 = new Intent("android.intent.action.SEND");
                intent2.setType("image/jpg");
                intent2.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", new File(list.get(i))));
                context.startActivity(intent2);
            } catch (ActivityNotFoundException unused2) {
                Context context3 = context;
                Toast.makeText(context3, context3.getString(R.string.fake_chat), Toast.LENGTH_SHORT).show();
            }
        });

        viewHolder2.mainImage.setOnClickListener(view -> context.startActivity(new Intent(context, ActivityPhotoSlide.class).putStringArrayListExtra("paths", list).putExtra("position", i), ActivityOptionsCompat.makeScaleUpAnimation(view, (int) view.getX(), (int) view.getY(), view.getWidth(), view.getHeight()).toBundle()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardOne;
        ImageView mainImage;
        LinearLayout copy_button;
        LinearLayout share_button;
        LottieAnimationView lottie_copy;
        LottieAnimationView lottie_share;

        public ViewHolder(View view) {
            super(view);
            cardOne = view.findViewById(R.id.cardOne);
            mainImage = view.findViewById(R.id.mainimage);
            copy_button = view.findViewById(R.id.copy_button);
            share_button = view.findViewById(R.id.share_button);
            lottie_copy = view.findViewById(R.id.lottie_copy);
            lottie_share = view.findViewById(R.id.lottie_share);
        }
    }
}