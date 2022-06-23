package com.whatscan.toolkit.forwa.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.whatscan.toolkit.forwa.GetSet.SearchModel;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchHolder> {
    public Context context;
    public ArrayList<SearchModel> searchModels;
    public SharedPreferences sharedPreferences;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, SearchModel obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public SearchAdapter(Context mContext, ArrayList<SearchModel> strings) {
        this.context = mContext;
        this.searchModels = strings;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(ArrayList<SearchModel> filterdNames) {
        searchModels = filterdNames;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        sharedPreferences = context.getSharedPreferences("MyPreferences", 0);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.apply();
        return new SearchHolder(LayoutInflater.from(context).inflate(R.layout.item_search, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHolder holder, int position) {
        if (Preference.getBooleanTheme(false)) {
            holder.llMain.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_black));
            holder.txtName.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        } else {
            holder.llMain.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_white));
            holder.txtName.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
        }

        holder.txtName.setText(searchModels.get(position).getTxtName());
        Glide.with(context).load(searchModels.get(position).getFabImage()).into(holder.fabImage);

        holder.itemView.setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, searchModels.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchModels.size();
    }

    public static class SearchHolder extends RecyclerView.ViewHolder {
        public ImageView fabImage;
        public TextView txtName;
        public LinearLayout llMain;

        public SearchHolder(@NonNull View itemView) {
            super(itemView);
            fabImage = itemView.findViewById(R.id.fabImage);
            txtName = itemView.findViewById(R.id.txtName);
            llMain = itemView.findViewById(R.id.llMain);
        }
    }
}