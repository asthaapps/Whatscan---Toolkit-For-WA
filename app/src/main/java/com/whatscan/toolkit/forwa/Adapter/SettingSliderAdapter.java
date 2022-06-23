package com.whatscan.toolkit.forwa.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.whatscan.toolkit.forwa.R;
import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;

public class SettingSliderAdapter extends SliderViewAdapter<SettingSliderAdapter.SliderAdapterVH> {
    private final Context context;
    private final Integer[] slideArray;

    public SettingSliderAdapter(Context context, Integer[] slideArray) {
        this.context = context;
        this.slideArray = slideArray;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item_setting, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {
        Glide.with(viewHolder.itemView)
                .load(slideArray[position])
                .into(viewHolder.iv_auto_image_slider);

    }

    @Override
    public int getCount() {
        return slideArray.length;
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView iv_auto_image_slider;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            iv_auto_image_slider = itemView.findViewById(R.id.iv_auto_image_slider);
            this.itemView = itemView;
        }
    }

}
