package com.whatscan.toolkit.forwa.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.whatscan.toolkit.forwa.GetSet.EmoticonSubCatagery;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.Util.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EmoticonSubCategoryAdapter extends RecyclerView.Adapter<EmoticonSubCategoryAdapter.EmoticonViewHolder> {
    Context context;
    List<EmoticonSubCatagery> emoticonSubCatageries;

    public EmoticonSubCategoryAdapter(Context con, List<EmoticonSubCatagery> emoticonSubCat) {
        this.context = con;
        this.emoticonSubCatageries = emoticonSubCat;
    }

    @NotNull
    @Override
    public EmoticonViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new EmoticonViewHolder(LayoutInflater.from(context).inflate(R.layout.item_emoticon_sub_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NotNull EmoticonViewHolder holder, int position) {
        if (Preference.getBooleanTheme(false)){
            holder.rlBackground.setBackgroundColor(ContextCompat.getColor(context, R.color.colorShape));
            holder.emoticon_subcat.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        }

        holder.emoticon_subcat.setText(emoticonSubCatageries.get(position).getEmoji());

        holder.emoticon_share.setOnClickListener(v -> {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.setAction("android.intent.action.VIEW");
            intent.setPackage("com.whatsapp");
            intent.setData(Uri.parse("https://api.whatsapp.com/send?phone=&text=" + emoticonSubCatageries.get(position).getEmoji()));
            context.startActivity(intent);
        });

        holder.emoticon_copy.setOnClickListener(v -> {
            Utils.setClipboard(context, emoticonSubCatageries.get(position).getEmoji());
        });

        holder.emoticon_share_all.setOnClickListener(v -> {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("text/plain");
            intent.putExtra("android.intent.extra.TEXT", emoticonSubCatageries.get(position).getEmoji());
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return emoticonSubCatageries.size();
    }

    public static class EmoticonViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rlBackground;
        TextView emoticon_subcat;
        ImageView emoticon_share, emoticon_share_all, emoticon_copy;

        public EmoticonViewHolder(View itemView) {
            super(itemView);
            rlBackground = itemView.findViewById(R.id.rlBackground);
            emoticon_subcat = itemView.findViewById(R.id.emoticon_subcat);
            emoticon_share = itemView.findViewById(R.id.emoticon_share);
            emoticon_share_all = itemView.findViewById(R.id.emoticon_share_all);
            emoticon_copy = itemView.findViewById(R.id.emoticon_copy);
        }
    }
}
