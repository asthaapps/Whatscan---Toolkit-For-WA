package com.whatscan.toolkit.forwa.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.whatscan.toolkit.forwa.GetSet.EmoticonCatagery;
import com.whatscan.toolkit.forwa.MsgTools.Emoticon.ActivitySubEmoticon;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Preference;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EmoticonCategoryAdapter extends RecyclerView.Adapter<EmoticonCategoryAdapter.EmoticonViewHolder> {
    public Context context;
    public List<EmoticonCatagery> emoticonCatageries;

    public EmoticonCategoryAdapter(Context con, List<EmoticonCatagery> emoticonCat) {
        this.context = con;
        this.emoticonCatageries = emoticonCat;
    }

    @NotNull
    @Override
    public EmoticonViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new EmoticonViewHolder(LayoutInflater.from(context).inflate(R.layout.item_emoticon_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NotNull EmoticonViewHolder holder, int position) {
        if (Preference.getBooleanTheme(false)){
            holder.rlMain.setBackgroundColor(ContextCompat.getColor(context, R.color.colorShape));
            holder.emoticon_cat.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        }

        holder.emoticon_cat.setText(emoticonCatageries.get(position).getName());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ActivitySubEmoticon.class);
            intent.putExtra("EmoticonId", emoticonCatageries.get(position).getId());
            intent.putExtra("EmoticonName", emoticonCatageries.get(position).getName());
            context.startActivity(intent, ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle());
        });
    }

    @Override
    public int getItemCount() {
        return emoticonCatageries.size();
    }

    public static class EmoticonViewHolder extends RecyclerView.ViewHolder {
        TextView emoticon_cat;
        RelativeLayout rlMain;

        public EmoticonViewHolder(View itemView) {
            super(itemView);
            emoticon_cat = itemView.findViewById(R.id.emoticon_cat);
            rlMain = itemView.findViewById(R.id.rlMain);
        }
    }
}
