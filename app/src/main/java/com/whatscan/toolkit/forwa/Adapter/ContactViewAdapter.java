package com.whatscan.toolkit.forwa.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.whatscan.toolkit.forwa.BulkSender.helper.ContactModel;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.Util.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class ContactViewAdapter extends RecyclerView.Adapter<ContactViewAdapter.ContactSentViewHolder> {
    public int positionTillContactsSend = 0;
    ArrayList<ContactModel> contactModelList;
    Context context;

    public ContactViewAdapter(Context con, ArrayList<ContactModel> contactList) {
        this.context = con;
        this.contactModelList = contactList;
    }

    @NonNull
    @NotNull
    @Override
    public ContactSentViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact_sent, parent, false);
        return new ContactSentViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ContactSentViewHolder holder, int position) {
        if (Preference.getBooleanTheme(false)) {
            holder.nameTextView.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        }

        ContactModel contactModel2 = contactModelList.get(position);
        contactModel2.setKey(position);
        if (contactModel2.getName() != null) {
            String name = contactModel2.getName();
            if (name.length() > 0) {
                holder.nameTextView.setText(contactModel2.getName());
                holder.subtitileTextView.setText(contactModel2.getPhoneNumber());
            }

            holder.user_image_card_view.setOnClickListener(v -> Utils.openWhatsAppConversation(holder.itemView.getContext(), contactModel2.getPhoneNumber(), "Hi"));

            holder.txtFirstLetter.setText(getSectionText(position));
        }
    }

    @Override
    public int getItemCount() {
        return contactModelList.size();
    }

    public void setPositionTillContactsSend(int i) {
        positionTillContactsSend = i;
    }

    private CharSequence getSectionText(int i) {
        ContactModel contactModel = contactModelList.get(i);
        String name = contactModel.getName();
        int i2 = 1;
        if (name.length() > 0) {
            if (name.length() >= 2) {
                i2 = 2;
            }
            return name.substring(0, i2);
        }

        String phoneNumber = contactModel.getPhoneNumber();
        if (!(phoneNumber.length() > 0)) {
            return "#";
        }

        if (phoneNumber.length() >= 2) {
            i2 = 2;
        }
        return phoneNumber.substring(0, i2);
    }

    public static class ContactSentViewHolder extends RecyclerView.ViewHolder {
        public TextView txtFirstLetter, nameTextView, subtitileTextView;
        public CardView user_image_card_view;

        public ContactSentViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            txtFirstLetter = itemView.findViewById(R.id.txtFirstLetter);
            nameTextView = itemView.findViewById(R.id.name_text_view);
            subtitileTextView = itemView.findViewById(R.id.subtitletextView);
            user_image_card_view = itemView.findViewById(R.id.user_image_card_view);

        }
    }
}