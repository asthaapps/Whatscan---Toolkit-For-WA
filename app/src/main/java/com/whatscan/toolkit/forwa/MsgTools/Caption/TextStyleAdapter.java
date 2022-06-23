package com.whatscan.toolkit.forwa.MsgTools.Caption;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.whatscan.toolkit.forwa.R;

import org.jetbrains.annotations.NotNull;

public class TextStyleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public Context context;
    public int[] images;
    public CaptionEditActivity single_page;

    public TextStyleAdapter(Context context2, int[] iArr, CaptionEditActivity imageEditor) {
        this.context = context2;
        this.images = iArr;
        this.single_page = imageEditor;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int i) {
        return new StyleHolder(LayoutInflater.from(this.context).inflate(R.layout.item_text_style, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NotNull RecyclerView.ViewHolder viewHolder, final int i) {
        StyleHolder styleHolder = (StyleHolder) viewHolder;
        styleHolder.imageView.setImageResource(this.images[i]);
        styleHolder.imageView.setOnClickListener(view -> single_page.addStyle(i + 1));
    }

    @Override
    public int getItemCount() {
        return this.images.length;
    }

    public static class StyleHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public StyleHolder(View view) {
            super(view);
            this.imageView = view.findViewById(R.id.image);
        }
    }
}