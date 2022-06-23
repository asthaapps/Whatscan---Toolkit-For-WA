package com.whatscan.toolkit.forwa.Adapter;

import static com.whatscan.toolkit.forwa.WChatLocker.FragmentChatLock.ITEMS;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.whatscan.toolkit.forwa.DataBaseHelper.DatabaseHandler;
import com.whatscan.toolkit.forwa.GetSet.ChatLockModel;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.WChatLocker.FragmentChatLock;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ChatLockAdapter extends RecyclerView.Adapter<ChatLockAdapter.ViewHolder> {
    public final FragmentChatLock.OnListFragmentInteractionListener mListener;
    public final List<ChatLockModel> mValues;
    public List<Integer> selectedIds = new ArrayList<>();
    Context context;
    DatabaseHandler databaseHandler;

    public ChatLockAdapter(List<ChatLockModel> list, FragmentChatLock.OnListFragmentInteractionListener onListFragmentInteractionListener) {
        this.mValues = list;
        this.mListener = onListFragmentInteractionListener;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        databaseHandler = new DatabaseHandler(viewGroup.getContext());
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_lock, viewGroup, false));
    }

    public void setChats(List<Integer> list) {
        selectedIds = list;
        notifyDataSetChanged();
    }

    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        if (Preference.getBooleanTheme(false)) {
            viewHolder.cardMain.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorShape));
            viewHolder.textView_name.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        }

        boolean z = true;
        viewHolder.mItem = mValues.get(i);
        viewHolder.apphabet_text.setText(mValues.get(i).getUsername().substring(0, 1));

        if (mValues.get(i).getUsername() != null) {
            viewHolder.textView_name.setText(mValues.get(i).getUsername());
        }

        if (mValues.get(i).getIsLock() > 0) {
            z = false;
        }

        viewHolder.lock.setChecked(z);
        viewHolder.lock.setTag(i);

        viewHolder.lock.setOnCheckedChangeListener((compoundButton, z1) -> {
            ChatLockModel homeModel = new ChatLockModel();
            homeModel.setId(mValues.get(i).getId());
            if (z1) {
                Toast.makeText(context, "Chat UnLocked", Toast.LENGTH_SHORT).show();
                homeModel.setIsLock(0);
            } else {
                Toast.makeText(context, "Chat Locked", Toast.LENGTH_SHORT).show();
                homeModel.setIsLock(1);
            }

            homeModel.setUsername(mValues.get(i).getUsername());
            homeModel.setIsToCheckLock(mValues.get(i).getIsToCheckLock());
            databaseHandler.updateChatLock(homeModel);
        });

        viewHolder.mView.setOnClickListener(view -> {
            if (mListener != null) {
                mListener.onListFragmentInteraction(viewHolder.mItem);
            }
        });

        viewHolder.ic_delete.setOnClickListener(v -> {
            ChatLockModel item = getItem(i);
            if (item != null) {
                if (selectedIds.contains(item.getId())) {
                    selectedIds.remove(Integer.valueOf(item.getId()));
                } else {
                    selectedIds.add(item.getId());
                }
                setChats(selectedIds);
            }

            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
            View inflate = LayoutInflater.from(context).inflate(R.layout.delete_dialog, null);
            bottomSheetDialog.setContentView(inflate);

            RelativeLayout rl_dialog = inflate.findViewById(R.id.rl_dialog);
            TextView tv_dialog_title = inflate.findViewById(R.id.tv_dialog_title);
            TextView tv_dialog_tip = inflate.findViewById(R.id.tv_dialog_tip);
            AppCompatButton bt_yes = inflate.findViewById(R.id.bt_yes);
            AppCompatButton bt_no = inflate.findViewById(R.id.bt_no);

            if (Preference.getBooleanTheme(false)) {
                rl_dialog.setBackgroundColor(ContextCompat.getColor(context, R.color.darkBlack));
                tv_dialog_title.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                tv_dialog_tip.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            }

            tv_dialog_title.setText(context.getString(R.string.chat_locker));
            tv_dialog_tip.setText(context.getString(R.string.delete_chat));

            bt_no.setOnClickListener(v1 -> bottomSheetDialog.dismiss());

            bt_yes.setOnClickListener(v1 -> {
                for (ChatLockModel chatLockModel : databaseHandler.getAllChatLock()) {
                    if (selectedIds.contains(chatLockModel.getId())) {
                        databaseHandler.deleteChatInfo(chatLockModel);
                    }
                }
                ITEMS.clear();
                ITEMS.addAll(databaseHandler.getAllChatLock());
                notifyDataSetChanged();
                bottomSheetDialog.dismiss();
            });

            bottomSheetDialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public ChatLockModel getItem(int i) {
        return mValues.get(i);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView apphabet_text;
        public final CheckBox lock;
        public final View mView;
        public final TextView textView_name;
        public final CardView card_view;
        public final ImageView ic_delete;
        public final CardView cardMain;
        public ChatLockModel mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            cardMain = view.findViewById(R.id.cardMain);
            apphabet_text = view.findViewById(R.id.apphabet_text);
            textView_name = view.findViewById(R.id.textView_name);
            card_view = view.findViewById(R.id.card_view);
            ic_delete = view.findViewById(R.id.ic_delete);
            lock = view.findViewById(R.id.lock);
        }

        @NotNull
        @Override
        public String toString() {
            return super.toString() + " '" + textView_name.getText() + "'";
        }
    }
}