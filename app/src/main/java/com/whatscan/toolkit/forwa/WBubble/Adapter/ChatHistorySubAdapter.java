package com.whatscan.toolkit.forwa.WBubble.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.whatscan.toolkit.forwa.DataBaseHelper.ChatHistoryDatabaseHelper;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.WBubble.ChatHistoryPreviewActivity;
import com.whatscan.toolkit.forwa.WBubble.model.ChatHistoryNoResultfound;
import com.whatscan.toolkit.forwa.WBubble.model.ChatHistorySubModel;

import java.util.ArrayList;

public class ChatHistorySubAdapter extends RecyclerView.Adapter<ChatHistorySubAdapter.ProgrammingViewHolder> implements Filterable {
    public static boolean onlongclick = false;
    public static ArrayList<Integer> selectedpositions = new ArrayList<>();
    public String PackageName;
    public Context context;
    public ChatHistoryDatabaseHelper delChatDatabaseHelper;
    public ArrayList<ChatHistorySubModel> delChatModelArrayList;
    public ArrayList<ChatHistorySubModel> delChatModelTemparray;
    public ChatHistoryNoResultfound delChatNoResultfoundl;
    public ItemFilter filter;

    public ChatHistorySubAdapter(Context context2, ArrayList<ChatHistorySubModel> arrayList, String str, ChatHistoryNoResultfound chatHistoryNoResultfound) {
        this.context = context2;
        this.delChatModelArrayList = arrayList;
        this.PackageName = str;
        this.delChatNoResultfoundl = chatHistoryNoResultfound;
        this.delChatDatabaseHelper = new ChatHistoryDatabaseHelper(context2);
        this.delChatModelTemparray = new ArrayList<>();
        this.delChatModelTemparray.addAll(arrayList);
        this.filter = new ItemFilter();
    }

    @Override
    @NonNull
    public ProgrammingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ProgrammingViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull ProgrammingViewHolder programmingViewHolder, int i) {
        if (Preference.getBooleanTheme(false)){
            programmingViewHolder.cv_chat.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorShape));
            programmingViewHolder.txtTitle.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            programmingViewHolder.txtMsg.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            programmingViewHolder.txtTime.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        }

        ChatHistorySubModel chatHistorySubModel = this.delChatModelArrayList.get(i);
        programmingViewHolder.txtTitle.setText(chatHistorySubModel.getName());
        programmingViewHolder.txtMsg.setText(chatHistorySubModel.getTxtmsg());
        if (chatHistorySubModel.getImage() != null) {
            programmingViewHolder.imgIcon.setImageBitmap(chatHistorySubModel.getImage());
        }
        programmingViewHolder.txtTime.setText(chatHistorySubModel.getDtTm());
    }

    @Override
    public int getItemCount() {
        return delChatModelArrayList.size();
    }

    public Filter getFilter() {
        return filter;
    }

    public void deleteChate(String str) {
        for (int i = 0; i < selectedpositions.size(); i++) {
            delChatDatabaseHelper.deleteAllTxt(this.delChatModelArrayList.get(selectedpositions.get(i)).getName(), PackageName);
            delChatModelArrayList.remove(selectedpositions.get(i).intValue());
            notifyItemRemoved(selectedpositions.get(i));
        }
        selectedpositions = new ArrayList<>();
        onlongclick = false;
        notifyDataSetChanged();
        if (this.delChatModelArrayList.size() <= 0) {
            Intent intent = new Intent("Msg");
            intent.putExtra("package", str);
            intent.putExtra("DeleteIntent", true);
            LocalBroadcastManager.getInstance(context.getApplicationContext()).sendBroadcast(intent);
        }
    }

    public void unselectall() {
        selectedpositions = new ArrayList<>();
        onlongclick = false;
        notifyDataSetChanged();
    }

    public class ProgrammingViewHolder extends RecyclerView.ViewHolder {
        CardView cv_chat;
        ImageView imgIcon;
        TextView txtMsg;
        TextView txtTime;
        TextView txtTitle;

        public ProgrammingViewHolder(@NonNull View view) {
            super(view);
            this.cv_chat = view.findViewById(R.id.cv_chat);
            this.imgIcon = view.findViewById(R.id.iv_profile);
            this.txtTitle = view.findViewById(R.id.tv_name);
            this.txtMsg = view.findViewById(R.id.tv_msg);
            this.txtTime = view.findViewById(R.id.tv_date);

            view.setOnClickListener(view1 -> {
                int adapterPosition = ProgrammingViewHolder.this.getAdapterPosition();
                if (!ChatHistorySubAdapter.onlongclick) {
                    String charSequence = ProgrammingViewHolder.this.txtTitle.getText().toString();
                    ProgrammingViewHolder.this.imgIcon.setDrawingCacheEnabled(true);
                    Bitmap drawingCache = ProgrammingViewHolder.this.imgIcon.getDrawingCache();
                    Intent intent = new Intent(context, ChatHistoryPreviewActivity.class);
                    intent.putExtra("PersonName", charSequence);
                    intent.putExtra("PersonDP", drawingCache);
                    intent.putExtra("PackageName", PackageName);
                    context.startActivity(intent, ActivityOptionsCompat.makeScaleUpAnimation(view1, (int) view1.getX(), (int) view1.getY(), view1.getWidth(), view1.getHeight()).toBundle());
                    ((Activity) context).finish();
                    return;
                }
                if (ChatHistorySubAdapter.selectedpositions.contains(adapterPosition)) {
                    ChatHistorySubAdapter.selectedpositions.remove(Integer.valueOf(adapterPosition));
                    if (ChatHistorySubAdapter.selectedpositions.size() <= 0) {
                        boolean unused = ChatHistorySubAdapter.onlongclick = false;
                    }
                } else {
                    ChatHistorySubAdapter.selectedpositions.add(adapterPosition);
                }
                notifyDataSetChanged();
            });
        }
    }

    private class ItemFilter extends Filter {
        private ItemFilter() {
        }

        public FilterResults performFiltering(CharSequence charSequence) {
            String charSequence2 = charSequence.toString();
            FilterResults filterResults = new FilterResults();

            int size = delChatModelTemparray.size();
            ArrayList<ChatHistorySubModel> arrayList3 = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                if (delChatModelTemparray.get(i).getName().toLowerCase().contains(charSequence2.toLowerCase())) {
                    arrayList3.add(delChatModelTemparray.get(i));
                }
            }
            filterResults.values = arrayList3;
            filterResults.count = 0;
            return filterResults;
        }


        public void publishResults(CharSequence charSequence, FilterResults filterResults) {
            delChatModelArrayList = new ArrayList<>();
            delChatModelArrayList = (ArrayList<ChatHistorySubModel>) filterResults.values;
            if (delChatModelArrayList.size() == 0) {
                if (delChatNoResultfoundl != null) {
                    delChatNoResultfoundl.onnoresult(true);
                }
            } else if (delChatNoResultfoundl != null) {
                delChatNoResultfoundl.onnoresult(false);
            }
            notifyDataSetChanged();
        }
    }
}