package com.whatscan.toolkit.forwa.WSticker.editimage.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.WSticker.editimage.fragment.StickerFragment;

import org.jetbrains.annotations.NotNull;

public class StickerTypeAdapter extends RecyclerView.Adapter<ViewHolder> {
    public static final String[] stickerPath = {"stickers/type1", "stickers/type2", "stickers/type3", "stickers/type4", "stickers/type5", "stickers/type6"};
    public static final String[] stickerPathName = {"Pack 1", "Pack 2", "Pack 3", "Pack 4", "Pack 5", "Pack 6"};
    public StickerFragment mStickerFragment;
    public ImageClick mImageClick = new ImageClick();

    public StickerTypeAdapter(StickerFragment fragment) {
        super();
        this.mStickerFragment = fragment;
    }

    @Override
    public int getItemCount() {
        return stickerPathName.length;
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
                R.layout.view_sticker_type_item, parent, false);
        ImageHolder holer = new ImageHolder(v);
        return holer;
    }

    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {
        ImageHolder imageHoler = (ImageHolder) holder;
        String name = stickerPathName[position];
        imageHoler.text.setText(name);
        imageHoler.text.setTag(stickerPath[position]);
        imageHoler.text.setOnClickListener(mImageClick);
    }

    public static class ImageHolder extends ViewHolder {
        public ImageView icon;
        public TextView text;

        public ImageHolder(View itemView) {
            super(itemView);
            this.icon = itemView.findViewById(R.id.icon);
            this.text = itemView.findViewById(R.id.text);
        }
    }

    private final class ImageClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            String data = (String) v.getTag();
            mStickerFragment.swipToStickerDetails(data);
        }
    }
}