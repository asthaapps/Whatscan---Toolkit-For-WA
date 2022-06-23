package com.whatscan.toolkit.forwa.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.whatscan.toolkit.forwa.BulkSender.helper.ContactModel;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.Util.Utils;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import kotlin.TypeCastException;

public class ContactSelectionAdapter extends RecyclerView.Adapter<ContactSelectionAdapter.ContactViewHolder> implements FastScrollRecyclerView.SectionedAdapter {
    public final String TAG = "ContactSelectionAdapter";
    public ArrayList<ContactModel> contactModelList = new ArrayList<>();
    public ArrayList<ContactModel> contactModelListCopy = new ArrayList<>();
    public OnContactSelection onContactSelection;
    public int selectedCount;
    public SparseArray<ContactModel> selectedItems = new SparseArray<>();
    public SparseBooleanArray selectedItemsPosition = new SparseBooleanArray();
    public Context context;

    public ContactSelectionAdapter(Context context) {
        this.context = context;
    }

    private CharSequence getSectionText(int i) {
        ContactModel contactModel = contactModelList.get(i);
        String name = contactModel.getName();
        int i2 = 1;
        if (name.length() > 0) {
            String name2 = contactModel.getName();
            if (contactModel.getName().length() >= 2) {
                i2 = 2;
            }
            if (name2 != null) {
                return name2.substring(0, i2);
            }
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String phoneNumber = contactModel.getPhoneNumber();
        if (!(phoneNumber.length() > 0)) {
            return "#";
        }
        String phoneNumber2 = contactModel.getPhoneNumber();
        if (contactModel.getPhoneNumber().length() >= 2) {
            i2 = 2;
        }
        if (phoneNumber2 != null) {
            return phoneNumber2.substring(0, i2);
        }
        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
    }

    public final void notifySelection() {
        if (onContactSelection != null) {
            onContactSelection.onContactSelection(selectedCount);
        }
    }

    private void selectContactItem(int i, ContactModel contactModel) {
        selectedItems.put(contactModel.getKey(), contactModel);
        contactModel.setSelected(true);
        selectedItemsPosition.put(i, true);
    }

    public final void toggleSelection(int i, ContactModel contactModel) {
        if (selectedItems.get(contactModel.getKey(), null) != null) {
            unselectContactItem(i, contactModel);
        } else {
            selectContactItem(i, contactModel);
        }
        notifyItemChanged(i);
    }

    private void unselectContactItem(int i, ContactModel contactModel) {
        selectedItems.delete(contactModel.getKey());
        contactModel.setSelected(false);
        selectedItemsPosition.delete(i);
    }

    @NonNull
    @NotNull
    @Override
    public String getSectionName(int position) {
        return getSectionText(position).toString();
    }

    @Override
    public int getItemCount() {
        return contactModelList.size();
    }

    public final List<ContactModel> getContactModelList() {
        return contactModelList;
    }

    public final void setContactModelList(@NotNull ArrayList<ContactModel> arrayList) {
        contactModelList = arrayList;
        contactModelListCopy.clear();
        contactModelListCopy.addAll(arrayList);
        notifyDataSetChanged();
    }

    public final OnContactSelection getOnContactSelection() {
        return onContactSelection;
    }

    public final void setOnContactSelection(@Nullable OnContactSelection onContactSelection2) {
        onContactSelection = onContactSelection2;
    }

    public final int getSelectedCount() {
        return selectedCount;
    }

    public final void setSelectedCount(int i) {
        selectedCount = i;
    }

    public final List<ContactModel> getSelectedList() {
        ArrayList<ContactModel> arrayList = new ArrayList<>();
        int size = contactModelListCopy.size();
        for (int i = 0; i < size; i++) {
            ContactModel contactModel = contactModelListCopy.get(i);
            if (contactModel.isSelected()) {
                arrayList.add(contactModelListCopy.get(i));
            }
        }
        return arrayList;
    }

    public final ArrayList<Integer> selectedContactsIds() {
        ArrayList<Integer> arrayList = new ArrayList<>();
        int size = contactModelListCopy.size();
        for (int i = 0; i < size; i++) {
            ContactModel contactModel = contactModelListCopy.get(i);
            if (contactModel.isSelected()) {
                ContactModel contactModel2 = contactModelListCopy.get(i);
                arrayList.add(contactModel2.getId());
                StringBuilder sb = new StringBuilder();
                sb.append("selectedContactsIds: ");
                ContactModel contactModel3 = contactModelListCopy.get(i);
                sb.append(contactModel3.getId());
                Log.d(TAG, sb.toString());
            }
        }
        return arrayList;
    }

    public final void setectAll() {
        int size = contactModelList.size();
        for (int i = 0; i < size; i++) {
            ContactModel contactModel = contactModelList.get(i);
            contactModel.setSelected(true);
            ContactModel contactModel2 = contactModelList.get(i);
            selectContactItem(i, contactModel2);
        }
        selectedCount = contactModelList.size();
        notifySelection();
        notifyDataSetChanged();
    }

    public final void showAll() {
        contactModelList.clear();
        contactModelList.addAll(contactModelListCopy);
        notifyDataSetChanged();
    }

    public final void showImportedContacts(boolean z) {
        contactModelList.clear();
        if (z) {
            int size = contactModelListCopy.size();
            for (int i = 0; i < size; i++) {
                ContactModel contactModel = contactModelListCopy.get(i);
                if (contactModel.getContactId() == null) {
                    contactModelList.add(contactModelListCopy.get(i));
                }
            }
        } else {
            int size2 = contactModelListCopy.size();
            for (int i2 = 0; i2 < size2; i2++) {
                ContactModel contactModel2 = contactModelListCopy.get(i2);
                if (contactModel2.getContactId() != null) {
                    ContactModel contactModel3 = contactModelListCopy.get(i2);
                    String contactId = contactModel3.getContactId();
                    if (contactId.length() > 0) {
                        contactModelList.add(contactModelListCopy.get(i2));
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    public final void unSetectAll() {
        selectedItemsPosition = new SparseBooleanArray();
        selectedItems = new SparseArray<>();
        int size = contactModelList.size();
        for (int i = 0; i < size; i++) {
            ContactModel contactModel = contactModelList.get(i);
            contactModel.setSelected(false);
        }
        selectedCount = 0;
        notifySelection();
        notifyDataSetChanged();
    }

    @SuppressLint("WrongConstant")
    public void onBindViewHolder(@NotNull ContactViewHolder contactViewHolder, int i) {
        if (Preference.getBooleanTheme(false)) {
            contactViewHolder.nameTextView.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        }else {
            contactViewHolder.nameTextView.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
        }

        ContactModel contactModel2 = contactModelList.get(i);
        contactModel2.setKey(i);
        if (contactModel2.getName() != null) {
            String name = contactModel2.getName();
            if (name.length() > 0) {
                contactViewHolder.nameTextView.setText(contactModel2.getName());
                contactViewHolder.nameTextView.setVisibility(0);
                contactViewHolder.subtitileTextView.setVisibility(0);
                contactViewHolder.subtitileTextView.setText(contactModel2.getPhoneNumber());
                contactViewHolder.txtFirstLetter.setText(getSectionText(i));

                contactViewHolder.user_image_card_view.setOnClickListener(v -> {
                    View view2 = contactViewHolder.itemView;
                    Utils.openWhatsAppConversation(view2.getContext(), contactModel2.getPhoneNumber(), "Hi");
                });

                contactViewHolder.appCompatCheckBox.setOnCheckedChangeListener(null);
                contactViewHolder.setContactModel(contactModel2);
                contactViewHolder.appCompatCheckBox.setChecked(contactModel2.isSelected());

                contactViewHolder.appCompatCheckBox.setOnCheckedChangeListener((buttonView, z) -> {
                    toggleSelection(i, contactModel2);
                    if (z) {
                        setSelectedCount(getSelectedCount() + 1);
                    } else {
                        setSelectedCount(getSelectedCount() - 1);
                    }
                    notifySelection();
                });

                contactViewHolder.itemView.setOnClickListener(v -> contactViewHolder.appCompatCheckBox.performClick());
            }
        }
    }

    @Override
    @NotNull
    public ContactViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_contact_selection, viewGroup, false);
        return new ContactViewHolder(inflate);
    }

    public void filterList(ArrayList<ContactModel> filterllist) {
        contactModelList = filterllist;
        notifyDataSetChanged();
    }

    public interface OnContactSelection {
        void onContactSelection(int i);
    }

    public static final class ContactViewHolder extends RecyclerView.ViewHolder {
        public AppCompatCheckBox appCompatCheckBox;
        public ContactModel contactModel;
        public ImageView more_image_view;
        public TextView nameTextView;
        public ImageView selectImageView;
        public FrameLayout select_card_view;
        public TextView subtitileTextView, txtFirstLetter;
        public CardView user_image_card_view;

        public ContactViewHolder(@NotNull View view) {
            super(view);
            nameTextView = view.findViewById(R.id.name_text_view);
            subtitileTextView = view.findViewById(R.id.subtitletextView);
            txtFirstLetter = view.findViewById(R.id.txtFirstLetter);
            appCompatCheckBox = view.findViewById(R.id.checkBox);
            user_image_card_view = view.findViewById(R.id.user_image_card_view);
            more_image_view = view.findViewById(R.id.more_image_view);
            select_card_view = view.findViewById(R.id.select_card_view);
            selectImageView = view.findViewById(R.id.select_image_view);
        }

        public final ContactModel getContactModel() {
            return contactModel;
        }

        public final void setContactModel(@Nullable ContactModel contactModel2) {
            contactModel = contactModel2;
        }
    }
}