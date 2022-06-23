package com.whatscan.toolkit.forwa.WBubble.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.WBubble.model.ChatHistoryModel;

import java.util.ArrayList;

public class ChatHistoryAdapter extends RecyclerView.Adapter<ChatHistoryAdapter.ChatViewHolder> {
    public ArrayList<ChatHistoryModel> arrayList;
    public Context context;

    public ChatHistoryAdapter(Context context2, ArrayList<ChatHistoryModel> arrayList2) {
        this.context = context2;
        this.arrayList = arrayList2;
    }

    @Override
    @NonNull
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ChatViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_list, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull ChatViewHolder chatViewHolder, int i) {
        if (Preference.getBooleanTheme(false)) {
            chatViewHolder.chat_time.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        }
        ChatHistoryModel chatHistoryModel = this.arrayList.get(i);
        chatViewHolder.txtMsgInChat.setText(chatHistoryModel.getTxtmsg());
        chatViewHolder.chat_time.setText(chatHistoryModel.getDtTm());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView chat_time;
        TextView txtMsgInChat;

        public ChatViewHolder(View view) {
            super(view);
            txtMsgInChat = view.findViewById(R.id.tv_chat);
            chat_time = view.findViewById(R.id.tv_time);
        }
    }
}
