package com.whatscan.toolkit.forwa.MsgTools.Caption;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.whatscan.toolkit.forwa.R;

import org.jetbrains.annotations.NotNull;

public class TextSizeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public Context context;
    public CaptionEditActivity single_page;
    public String[] txtsize;

    public TextSizeAdapter(Context context2, String[] strArr, CaptionEditActivity imageEditor) {
        this.context = context2;
        this.txtsize = strArr;
        this.single_page = imageEditor;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int i) {
        return new SizeHolder(LayoutInflater.from(this.context).inflate(R.layout.item_text_size, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NotNull final RecyclerView.ViewHolder viewHolder, int i) {
        SizeHolder sizeHolder = (SizeHolder) viewHolder;
        sizeHolder.textView.setText(this.txtsize[i]);

        sizeHolder.textView.setOnClickListener(view -> single_page.add_textSize(Float.parseFloat(txtsize[viewHolder.getAdapterPosition()])));
    }

    @Override
    public int getItemCount() {
        return this.txtsize.length;
    }

    public static class SizeHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public SizeHolder(View view) {
            super(view);
            this.textView = view.findViewById(R.id.font_prev);
        }
    }
}
