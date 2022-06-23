package com.whatscan.toolkit.forwa.MsgTools.Caption;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.whatscan.toolkit.forwa.Ads.Advertisement;
import com.whatscan.toolkit.forwa.GetSet.CaptionCatagery;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class CaptionStatusAdapter extends RecyclerView.Adapter<CaptionStatusAdapter.CaptionViewholder> {
    public LayoutInflater layoutInflater;
    public Context context;
    public List<CaptionCatagery> getCaptionData;

    public CaptionStatusAdapter(Context con, List<CaptionCatagery> getCaption) {
        this.context = con;
        this.getCaptionData = getCaption;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public CaptionViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.caption_status_item, parent, false);
        return new CaptionViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CaptionViewholder holder, int position) {

        Glide.with(context).asBitmap().load(Preference.getMain_Url() + Preference.getCaption_path() + getCaptionData.get(position).getImage()).placeholder(R.drawable.ic_placeholder_status).into(holder.cat_image);

        holder.itemView.setOnClickListener(v -> {
            Advertisement.getInstance((Activity) context).showFullAds(() -> ItemViewIntent(v, position));
        });
    }

    private void ItemViewIntent(View v, int position) {
        Intent intent = new Intent(context, ActivitySubCaption.class);
        intent.putExtra("image", getCaptionData.get(position).getName());
        intent.putExtra("caption_id", getCaptionData.get(position).getId());
        context.startActivity(intent, ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle());
    }

    @Override
    public int getItemCount() {
        return getCaptionData.size();
    }

    public static class CaptionViewholder extends RecyclerView.ViewHolder {
        public RoundedImageView cat_image;

        public CaptionViewholder(@NonNull View itemView) {
            super(itemView);
            cat_image = itemView.findViewById(R.id.cat_image);
        }
    }
}