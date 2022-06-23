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

import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Preference;

import org.jetbrains.annotations.NotNull;

public class TextTools extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private final String[] tools_name_text;
    private ItemClickListenerText mClickListener;

    public TextTools(Context con, String[] tools_name_text) {
        this.context = con;
        this.tools_name_text = tools_name_text;

    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text_cell, parent, false);
        return new ToolsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        ToolsViewHolder toolsViewHolder = (ToolsViewHolder) holder;

        if (Preference.getBooleanTheme(false)) {
            toolsViewHolder.txt_cat_name.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        } else {
            toolsViewHolder.txt_cat_name.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
        }
        if (position == 0) {
            toolsViewHolder.lottie_cat.setImageResource(R.drawable.w_magic);
            toolsViewHolder.rlMain.setBackground(ContextCompat.getDrawable(context, R.drawable.text_magic));
        } else if (position == 1) {
            toolsViewHolder.lottie_cat.setImageResource(R.drawable.w_text_to_emoji);
            toolsViewHolder.rlMain.setBackground(ContextCompat.getDrawable(context, R.drawable.text_to_emoji));
        } else if (position == 2) {
            toolsViewHolder.lottie_cat.setImageResource(R.drawable.w_repeater);
            toolsViewHolder.rlMain.setBackground(ContextCompat.getDrawable(context, R.drawable.text_repeater));
        } else if (position == 3) {
            toolsViewHolder.lottie_cat.setImageResource(R.drawable.w_emoticon);
            toolsViewHolder.rlMain.setBackground(ContextCompat.getDrawable(context, R.drawable.text_emoticon));
        }
        toolsViewHolder.txt_cat_name.setText(tools_name_text[position]);

    }

    @Override
    public int getItemCount() {
        return tools_name_text.length;
    }

    public void setClickListener(ItemClickListenerText itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListenerText {
        void onItemClickText(View view, int position);
    }

    private class ToolsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView lottie_cat;
        private final TextView txt_cat_name;
        private final RelativeLayout rlMain;

        public ToolsViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            lottie_cat = view.findViewById(R.id.lottie_cat);
            txt_cat_name = view.findViewById(R.id.txt_cat_name);
            rlMain = view.findViewById(R.id.rlMain);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null)
                mClickListener.onItemClickText(v, getAbsoluteAdapterPosition());
        }
    }
}