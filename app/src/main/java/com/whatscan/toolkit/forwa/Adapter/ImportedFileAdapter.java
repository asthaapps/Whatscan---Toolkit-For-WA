package com.whatscan.toolkit.forwa.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.whatscan.toolkit.forwa.BulkSender.BulkActivityImportedContact;
import com.whatscan.toolkit.forwa.DataBaseHelper.db.ImportedFile;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Event;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ImportedFileAdapter extends RecyclerView.Adapter<ContactSelectionAdapter.ContactViewHolder> implements FastScrollRecyclerView.SectionedAdapter {
    public List<ImportedFile> a = new ArrayList<>();
    public Activity b;

    public ImportedFileAdapter(Activity activity) {
        b = activity;
    }

    private void startGroupDetails(@NonNull ContactSelectionAdapter.ContactViewHolder contactViewHolder, int i) {
        Intent intent = new Intent(contactViewHolder.itemView.getContext(), BulkActivityImportedContact.class);
        intent.putExtra(Event.IMPORTED_FILE_ID.name(), a.get(i).getImportedFileId());
        b.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return a.size();
    }

    @NotNull
    @Override
    public String getSectionName(int i) {
        return getSectionText(i).toString();
    }

    public CharSequence getSectionText(int i) {
        if (a.get(i).getName().length() <= 0) {
            return "*";
        }
        StringBuilder sb = new StringBuilder();
        String name = a.get(i).getName();
        int i2 = 2;
        if (a.get(i).getName().length() < 2) {
            i2 = 1;
        }
        sb.append(name.substring(0, i2));
        sb.append("");
        return sb.toString();
    }

    public void onBindViewHolder(@NonNull final ContactSelectionAdapter.ContactViewHolder contactViewHolder, final int i) {
        if (Preference.getBooleanTheme(false)) {
            contactViewHolder.nameTextView.setTextColor(ContextCompat.getColor(b, R.color.colorWhite));
        } else {
            contactViewHolder.nameTextView.setTextColor(ContextCompat.getColor(b, R.color.colorBlack));
        }

        ImportedFile importedFile = a.get(i);
        contactViewHolder.nameTextView.setText(importedFile.getName());
        TextView textView = contactViewHolder.subtitileTextView;
        textView.setText(Html.fromHtml(importedFile.getCount() + " members"));
        contactViewHolder.select_card_view.setVisibility(View.GONE);
        contactViewHolder.appCompatCheckBox.setVisibility(View.GONE);
        contactViewHolder.more_image_view.setVisibility(View.GONE);
        contactViewHolder.txtFirstLetter.setText(getSectionText(i));

        contactViewHolder.more_image_view.setOnClickListener(view -> startGroupDetails(contactViewHolder, i));

        contactViewHolder.user_image_card_view.setOnClickListener(view -> startGroupDetails(contactViewHolder, i));

        contactViewHolder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra(Event.SELECTED_IMPORT.name(), a.get(i));
            b.setResult(-1, intent);
            b.finish();
        });
    }

    @Override
    @NonNull
    public ContactSelectionAdapter.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ContactSelectionAdapter.ContactViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_contact_selection, viewGroup, false));
    }
}
