package com.whatscan.toolkit.forwa.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.whatscan.toolkit.forwa.Ads.ActivityPremium;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Preference;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReviewPlanAdapter extends RecyclerView.Adapter<ReviewPlanAdapter.ReviewPlanHolder> {


    private final Context context;
    private final Integer[] imageArray;
    private final String[] nameArray, reviewArray;

    public ReviewPlanAdapter(Context context, Integer[] imageArray, String[] nameArray, String[] reviewArray) {
        this.context = context;
        this.imageArray = imageArray;
        this.nameArray = nameArray;
        this.reviewArray = reviewArray;
    }


    @NonNull
    @Override
    public ReviewPlanHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_review_plan, parent, false);
        return new ReviewPlanHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewPlanHolder viewHolder, int position) {
        Glide.with(viewHolder.itemView)
                .load(imageArray[position]).placeholder(R.drawable.ic_profile)
                .into(viewHolder.imgProfile);
        viewHolder.tv_name.setText(nameArray[position]);
        viewHolder.tv_review.setText(reviewArray[position]);

        if (Preference.getBooleanTheme(false)) {
            viewHolder.tv_name.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            viewHolder.tv_review.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            viewHolder.rl_adp_review.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_black));
        } else {
            viewHolder.tv_name.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            viewHolder.tv_review.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            viewHolder.rl_adp_review.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_white));

        }
        if (position == 0) {
            viewHolder.rl_icon.setBackground(ContextCompat.getDrawable(context, R.drawable.round_shape1));
            viewHolder.imgGallery.setBackground(ContextCompat.getDrawable(context, R.drawable.round_menu1));
            viewHolder.imgGallery.setImageResource(R.drawable.d_class);
        } else if (position == 1) {
            viewHolder.rl_icon.setBackground(ContextCompat.getDrawable(context, R.drawable.round_shape));
            viewHolder.imgGallery.setBackground(ContextCompat.getDrawable(context, R.drawable.round_menu3));
            viewHolder.imgGallery.setImageResource(R.drawable.d_pre);
        } else if (position == 2) {
            viewHolder.rl_icon.setBackground(ContextCompat.getDrawable(context, R.drawable.round_shape2));
            viewHolder.imgGallery.setBackground(ContextCompat.getDrawable(context, R.drawable.round_menu2));
            viewHolder.imgGallery.setImageResource(R.drawable.d_master);
        } else if (position == 3) {
            viewHolder.rl_icon.setBackground(ContextCompat.getDrawable(context, R.drawable.round_shape1));
            viewHolder.imgGallery.setBackground(ContextCompat.getDrawable(context, R.drawable.round_menu1));
            viewHolder.imgGallery.setImageResource(R.drawable.d_class);
        } else if (position == 4) {
            viewHolder.rl_icon.setBackground(ContextCompat.getDrawable(context, R.drawable.round_shape2));
            viewHolder.imgGallery.setBackground(ContextCompat.getDrawable(context, R.drawable.round_menu2));
            viewHolder.imgGallery.setImageResource(R.drawable.d_master);
        } else if (position == 5) {
            viewHolder.rl_icon.setBackground(ContextCompat.getDrawable(context, R.drawable.round_shape));
            viewHolder.imgGallery.setBackground(ContextCompat.getDrawable(context, R.drawable.round_menu3));
            viewHolder.imgGallery.setImageResource(R.drawable.d_pre);
        }
    }


    @Override
    public int getItemCount() {
        return nameArray.length;
    }

    public static class ReviewPlanHolder extends RecyclerView.ViewHolder {

        private final CircleImageView imgProfile;
        private final ImageView imgGallery;
        private final TextView tv_review, tv_name;
        private final RelativeLayout rl_icon;
        RelativeLayout rl_adp_review;

        public ReviewPlanHolder(View itemView) {
            super(itemView);
            rl_icon = itemView.findViewById(R.id.rl_icon);
            imgProfile = itemView.findViewById(R.id.imgProfile);
            imgGallery = itemView.findViewById(R.id.imgGallery);
            tv_review = itemView.findViewById(R.id.tv_review);
            tv_name = itemView.findViewById(R.id.tv_name);
            rl_adp_review = itemView.findViewById(R.id.rl_adp_review);
        }
    }

}
