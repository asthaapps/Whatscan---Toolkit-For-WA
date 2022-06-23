package com.whatscan.toolkit.forwa.WSticker.editimage.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.WSticker.editimage.fragment.StickerFragment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StickerAdapter extends RecyclerView.Adapter<ViewHolder> {
    public DisplayImageOptions imageOption = new DisplayImageOptions.Builder().cacheInMemory(true).showImageOnLoading(R.drawable.yd_image_tx).build();
    public StickerFragment mStickerFragment;
    public ImageClick mImageClick = new ImageClick();
    public List<String> pathList = new ArrayList<String>();

    public StickerAdapter(StickerFragment fragment) {
        super();
        this.mStickerFragment = fragment;
    }

    @Override
    public int getItemCount() {
        return pathList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewtype) {
        View v = null;
        v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.view_sticker_item, parent, false);
        ImageHolder holer = new ImageHolder(v);
        return holer;
    }

    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {
        ImageHolder imageHoler = (ImageHolder) holder;
        String path = pathList.get(position);
        ImageLoader.getInstance().displayImage("assets://" + path,
                imageHoler.image, imageOption);
        imageHoler.image.setTag(path);
        imageHoler.image.setOnClickListener(mImageClick);
    }

    public void addStickerImages(String folderPath) {
        pathList.clear();
        try {
            String[] files = mStickerFragment.getActivity().getAssets()
                    .list(folderPath);
            for (String name : files) {
                pathList.add(folderPath + File.separator + name);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.notifyDataSetChanged();
    }

    public static class ImageHolder extends ViewHolder {
        public ImageView image;

        public ImageHolder(View itemView) {
            super(itemView);
            this.image = itemView.findViewById(R.id.img);
        }
    }

    private final class ImageClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            String data = (String) v.getTag();
            mStickerFragment.selectedStickerItem(data);
        }
    }
}