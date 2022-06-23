package com.whatscan.toolkit.forwa.Adapter;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.whatscan.toolkit.forwa.BulkSender.helper.ContactModel;
import com.whatscan.toolkit.forwa.R;

public class ContactForGroupSelectionAdapter extends ContactSelectionAdapter {
    OnGroupContactSelection a;

    public ContactForGroupSelectionAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemCount() {
        return contactModelList.size();
    }

    public OnGroupContactSelection getOnGroupContactSelection() {
        return a;
    }

    public void setOnGroupContactSelection(OnGroupContactSelection onGroupContactSelection) {
        a = onGroupContactSelection;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactSelectionAdapter.ContactViewHolder contactViewHolder, final int i) {
        super.onBindViewHolder(contactViewHolder, i);
        contactViewHolder.more_image_view.setVisibility(View.VISIBLE);
        contactViewHolder.more_image_view.setImageResource(R.drawable.ic_w_delete);
        contactViewHolder.itemView.setOnClickListener(null);
        contactViewHolder.appCompatCheckBox.setVisibility(View.GONE);

        contactViewHolder.more_image_view.setOnClickListener(view -> {
            contactModelList.remove(i);
            notifyItemRemoved(i);
            notifyItemRangeChanged(i, contactModelList.size());
            if (a != null && contactModelList.size() > 0) {
                a.onContactRemove(contactModelList.get(i));
            }
        });
    }

    public interface OnGroupContactSelection {
        void onContactRemove(ContactModel contactModel);
    }
}