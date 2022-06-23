package com.whatscan.toolkit.forwa.DeletedMedia;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.whatscan.toolkit.forwa.GetSet.ChatModel;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Preference;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdapterChatList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final List<ChatModel> chatModels;
    private final LayoutInflater layoutInflater;

    public AdapterChatList(Context context, List<ChatModel> chatModels) {
        this.context = context;
        this.chatModels = chatModels;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_chat_list, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        ChatViewHolder chatViewHolder = (ChatViewHolder) holder;

        if (Preference.getBooleanTheme(false)) {
            chatViewHolder.tv_time.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        }

        chatViewHolder.tv_chat.setText(chatModels.get(position).getMessage());
        chatViewHolder.tv_time.setText(chatModels.get(position).getMtime());
    }

    @Override
    public int getItemCount() {
        return chatModels.size();
    }

    private static class ChatViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_chat, tv_time;

        public ChatViewHolder(View view) {
            super(view);
            tv_chat = view.findViewById(R.id.tv_chat);
            tv_time = view.findViewById(R.id.tv_time);
        }
    }
}