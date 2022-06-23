package com.whatscan.toolkit.forwa.Adapter;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.whatscan.toolkit.forwa.GetSet.GetSetMagic;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.Util.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MagicTextAdapter extends RecyclerView.Adapter<MagicTextAdapter.MyViewHolder> {
    List<GetSetMagic> magicList;
    Context context;

    public MagicTextAdapter(Context context, List<GetSetMagic> listdata) {
        this.context = context;
        this.magicList = listdata;
    }

    @NotNull
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_emoticon_sub_list, parent, false));
    }

    @SuppressLint({"LongLogTag"})
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if (Preference.getBooleanTheme(false)) {
            holder.rlBackground.setBackgroundColor(ContextCompat.getColor(context, R.color.colorShape));
            holder.emoticon_subcat.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        }

        holder.emoticon_subcat.setText(magicList.get(position).getName());
        holder.emoticon_share.setOnClickListener(v -> {
            String FACE = magicList.get(position).getName();
            Intent whatsappIntent = new Intent("android.intent.action.SEND");
            whatsappIntent.setType("text/plain");
            whatsappIntent.setPackage("com.whatsapp");
            whatsappIntent.putExtra("android.intent.extra.TEXT", FACE);
            try {
                context.startActivity(whatsappIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
            }
        });

        holder.emoticon_share_all.setOnClickListener(v -> {
            String FACE = magicList.get(position).getName();
            Intent whatsappIntent = new Intent("android.intent.action.SEND");
            whatsappIntent.setType("text/plain");
            whatsappIntent.putExtra("android.intent.extra.TEXT", FACE);
            try {
                context.startActivity(whatsappIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, "Some problems", Toast.LENGTH_SHORT).show();
            }
        });

        holder.emoticon_copy.setOnClickListener(v -> {
            String FACE = magicList.get(position).getName();
            Utils.setClipboard(context, FACE);
            Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show();
        });
    }

    public int getItemCount() {
        return (magicList == null) ? 0 : magicList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView emoticon_subcat;
        RelativeLayout rlBackground;
        ImageView emoticon_share, emoticon_share_all, emoticon_copy;

        MyViewHolder(View itemView) {
            super(itemView);
            emoticon_subcat = itemView.findViewById(R.id.emoticon_subcat);
            emoticon_share = itemView.findViewById(R.id.emoticon_share);
            emoticon_share_all = itemView.findViewById(R.id.emoticon_share_all);
            emoticon_copy = itemView.findViewById(R.id.emoticon_copy);
            rlBackground = itemView.findViewById(R.id.rlBackground);
        }
    }
}
