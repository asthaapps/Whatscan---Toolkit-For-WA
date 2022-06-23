package com.whatscan.toolkit.forwa.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Preference;

public class ReviewSliderAdapter extends SliderViewAdapter<ReviewSliderAdapter.SliderAdapterVH> {
    private final Context context;
    private final String[] nameArray;
    private final String[] reviewArray;
    private final Integer[] imageArray;


    public ReviewSliderAdapter(Context context, Integer[] imageArray, String[] nameArray, String[] reviewArray) {
        this.context = context;
        this.imageArray = imageArray;
        this.nameArray = nameArray;
        this.reviewArray = reviewArray;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_slider_setting, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {
        Glide.with(viewHolder.itemView)
                .load(imageArray[position])
                .into(viewHolder.review_icon);
        viewHolder.tv_name.setText(nameArray[position]);
        viewHolder.tv_review.setText(reviewArray[position]);

        if (Preference.getBooleanTheme(false)) {
            viewHolder.tv_name.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            viewHolder.tv_review.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        } else {
            viewHolder.tv_name.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            viewHolder.tv_review.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
        }

    }

    @Override
    public int getCount() {
        return nameArray.length;
    }

    class SliderAdapterVH extends ViewHolder {

        private final ImageView review_icon;
        private final TextView tv_review, tv_name;
        View itemView;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            this.itemView = itemView;
            review_icon = itemView.findViewById(R.id.review_icon);
            tv_review = itemView.findViewById(R.id.tv_review);
            tv_name = itemView.findViewById(R.id.tv_name);
        }
    }
}