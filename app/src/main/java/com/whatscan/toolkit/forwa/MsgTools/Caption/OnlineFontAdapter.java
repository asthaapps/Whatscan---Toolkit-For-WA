package com.whatscan.toolkit.forwa.MsgTools.Caption;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.whatscan.toolkit.forwa.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class OnlineFontAdapter extends RecyclerView.Adapter<OnlineFontAdapter.MyViewHolder> {
    public ArrayList<String> font_style = new ArrayList<>();
    public Context context;
    CaptionEditActivity captionEditActivity;


    public OnlineFontAdapter(String[] font_style, Context context, CaptionEditActivity editActivity) {
        super();
        for (String file : font_style) {
            add(file);
        }
        this.context = context;
        this.captionEditActivity = editActivity;
    }

    void add(String path) {
        font_style.add(path);
    }

    public Object getItem(int position) {
        return this.font_style.get(position);
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_font_prev, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        AssetManager assets = this.context.getAssets();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("fonts/");
        stringBuilder.append(font_style.get(position));

        Typeface tf_regular1;
        tf_regular1 = Typeface.createFromAsset(assets, stringBuilder.toString());
        holder.font_style.setTypeface(tf_regular1);

        holder.font_style.setOnClickListener(v -> captionEditActivity.addtypeFace(tf_regular1));
    }

    @Override
    public int getItemCount() {
        return font_style.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView font_style;

        public MyViewHolder(View v) {
            super(v);
            font_style = v.findViewById(R.id.font_prev);
        }
    }
}
