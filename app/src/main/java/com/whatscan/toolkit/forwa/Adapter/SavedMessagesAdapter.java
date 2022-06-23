package com.whatscan.toolkit.forwa.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.whatscan.toolkit.forwa.BulkSender.helper.BottomSheetQuickReply;
import com.whatscan.toolkit.forwa.DataBaseHelper.db.DatabaseHandler;
import com.whatscan.toolkit.forwa.GetSet.QuickReply;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Event;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.Util.Utils;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class SavedMessagesAdapter extends RecyclerView.Adapter<SavedMessagesAdapter.ItemViewHolder> {
    public Activity activity;
    public String strSearch = "";
    List<QuickReply> quickReplayList = new ArrayList<>();
    List<QuickReply> quickReplayModelList = new ArrayList<>();

    public SavedMessagesAdapter(Activity activity) {
        this.activity = activity;
    }

    @NotNull
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_quick_reply, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ItemViewHolder itemViewHolder, final int i) {
        if (Preference.getBooleanTheme(false)) {
            itemViewHolder.cardMain.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.colorShape));
            itemViewHolder.textView.setTextColor(ContextCompat.getColor(activity, R.color.colorWhite));
            itemViewHolder.imgMore.setImageResource(R.drawable.ic_more_white);
        }

        final QuickReply quickReply = quickReplayList.get(i);
        itemViewHolder.textView.setText(quickReply.getMessage());

        itemViewHolder.imgMore.setOnClickListener(view -> showMoreQuickReplyAction(view, activity, quickReply, i));

        itemViewHolder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.putExtra(Event.MESSAGE.name(), quickReply.getMessage());
            intent.putExtra(Event.PHONE_NUMBER.name(), quickReply.getPhoneNumber());
            activity.setResult(-1, intent);
            activity.finish();
        });
    }

    @Override
    public int getItemCount() {
        return quickReplayList.size();
    }

    public void setQuickReplyList(List<QuickReply> list) {
        quickReplayList = list;
        quickReplayModelList.addAll(list);
        notifyDataSetChanged();
    }

    public void showMoreQuickReplyAction(View view, final Activity activity, final QuickReply quickReply, final int i) {
        PopupMenu popupMenu = new PopupMenu(activity, view);
        try {
            Field[] declaredFields = popupMenu.getClass().getDeclaredFields();
            int length = declaredFields.length;
            int i2 = 0;
            while (true) {
                if (i2 >= length) {
                    break;
                }
                Field field = declaredFields[i2];
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object obj = field.get(popupMenu);
                    Class.forName(obj.getClass().getName()).getMethod("setForceShowIcon", Boolean.TYPE).invoke(obj, Boolean.TRUE);
                    break;
                }
                i2++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        popupMenu.getMenuInflater().inflate(R.menu.menu_saved_messages, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            int itemId = menuItem.getItemId();
            if (itemId == R.id.action_edit) {
                BottomSheetQuickReply.newInstance(quickReply1 -> {
                    quickReplayList.set(i, quickReply1);
                    notifyDataSetChanged();
                }, quickReply).show(((AppCompatActivity) activity).getSupportFragmentManager(), "Dialog Fragment");
                return false;
            } else if (itemId == R.id.action_delete) {
                new AlertDialog.Builder(activity).setMessage(activity.getString(R.string.delete_chat)).setPositiveButton(activity.getString(R.string.delete), (dialogInterface, i0) -> {
                    new DatabaseHandler(activity).deletehashtag(quickReply);
                    quickReplayList.remove(i);
                    notifyDataSetChanged();
                }).setNegativeButton("Cancel", (dialogInterface, i0) -> {
                }).show();
                return false;
            } else if (itemId != R.id.action_copy) {
                return false;
            } else {
                Utils.setClipboard(activity, quickReply.getMessage());
                return false;
            }
        });
        popupMenu.show();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageView imgMore;
        public CardView cardMain;

        public ItemViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.textView);
            imgMore = view.findViewById(R.id.imgMore);
            cardMain = view.findViewById(R.id.cardMain);
        }
    }
}