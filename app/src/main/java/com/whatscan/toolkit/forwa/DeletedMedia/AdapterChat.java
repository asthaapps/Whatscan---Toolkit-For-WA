package com.whatscan.toolkit.forwa.DeletedMedia;

import android.content.Context;
import android.content.Intent;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.whatscan.toolkit.forwa.GetSet.NameModel;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Preference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AdapterChat extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final LayoutInflater layoutInflater;
    private final Context context;
    SparseBooleanArray selectedItems = new SparseBooleanArray();
    private List<NameModel> nameModelList;

    public AdapterChat(Context context, List<NameModel> nameModelList) {
        this.context = context;
        this.nameModelList = nameModelList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_chat, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        final ChatViewHolder chatViewHolder = (ChatViewHolder) holder;

        if (Preference.getBooleanTheme(false)) {
            chatViewHolder.cv_chat.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorShape));
            chatViewHolder.tv_name.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            chatViewHolder.tv_msg.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            chatViewHolder.tv_date.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        } else {
            chatViewHolder.cv_chat.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
            chatViewHolder.tv_name.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            chatViewHolder.tv_msg.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            chatViewHolder.tv_date.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
        }

        chatViewHolder.tv_name.setText(nameModelList.get(position).getName().split("-")[0].trim());
        chatViewHolder.tv_msg.setText(nameModelList.get(position).getLastmsg());
        chatViewHolder.tv_date.setText(nameModelList.get(position).getDate());
        chatViewHolder.cv_chat.setOnClickListener(v -> {
            Intent intent = new Intent(context, ActivityChat.class);
            intent.putExtra("strName", nameModelList.get(position).getName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return nameModelList.size();
    }

    public void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public void filterList(ArrayList<NameModel> filterllist) {
        nameModelList = filterllist;
        notifyDataSetChanged();
    }

    private static class ChatViewHolder extends RecyclerView.ViewHolder {

        private final CardView cv_chat;
        private final TextView tv_name, tv_msg, tv_date;

        public ChatViewHolder(View view) {
            super(view);
            tv_name = view.findViewById(R.id.tv_name);
            tv_msg = view.findViewById(R.id.tv_msg);
            tv_date = view.findViewById(R.id.tv_date);
            cv_chat = view.findViewById(R.id.cv_chat);
        }
    }
}