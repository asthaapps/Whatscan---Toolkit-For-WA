package com.whatscan.toolkit.forwa.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.Html;
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

import com.whatscan.toolkit.forwa.BulkSender.BulkActivityGroupList;
import com.whatscan.toolkit.forwa.DataBaseHelper.db.GroupModel;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Event;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class GroupSelectionAdapter extends RecyclerView.Adapter<GroupSelectionAdapter.ViewHolderNew> implements FastScrollRecyclerView.SectionedAdapter {
    List<GroupModel> a;
    Activity b;

    public GroupSelectionAdapter(Activity activity, List<GroupModel> groupModels) {
        b = activity;
        a = groupModels;
    }

    private void startGroupDetails(@NonNull ViewHolderNew contactViewHolder, int i) {
        Intent intent = new Intent(contactViewHolder.itemView.getContext(), BulkActivityGroupList.class);
        intent.putExtra(Event.GROUP_ID.name(), a.get(i).getId());
        b.startActivityForResult(intent, 1234);
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
        if (Objects.requireNonNull(a.get(i).getName()).length() <= 0) {
            return "*";
        }
        StringBuilder sb = new StringBuilder();
        String name = a.get(i).getName();
        int i2 = 2;
        if (Objects.requireNonNull(a.get(i).getName()).length() < 2) {
            i2 = 1;
        }
        sb.append(Objects.requireNonNull(name).substring(0, i2));
        sb.append("");
        return sb.toString();
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolderNew holder, int position) {
        if (Preference.getBooleanTheme(false)) {
            holder.nameTextView.setTextColor(ContextCompat.getColor(b, R.color.colorWhite));
        } else {
            holder.nameTextView.setTextColor(ContextCompat.getColor(b, R.color.colorBlack));
        }

        GroupModel groupModel = a.get(position);
        holder.nameTextView.setText(groupModel.getName());
        holder.subtitileTextView.setText(Html.fromHtml("<strong>" + groupModel.getCount() + "</strong>" + " " + b.getString(R.string.select_contact)));
        holder.txtFirstLetter.setText(getSectionText(position));
        holder.select_card_view.setVisibility(View.VISIBLE);
        holder.appCompatCheckBox.setVisibility(View.GONE);
        holder.more_image_view.setVisibility(View.GONE);

        holder.more_image_view.setOnClickListener(view -> GroupSelectionAdapter.this.startGroupDetails(holder, position));
        holder.user_image_card_view.setOnClickListener(view -> GroupSelectionAdapter.this.startGroupDetails(holder, position));
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.putExtra(Event.SELECTED_GROUP.name(), a.get(position));
            b.setResult(-1, intent);
            b.finish();
        });
    }

    @Override
    @NonNull
    public ViewHolderNew onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolderNew(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_contact_selection, viewGroup, false));
    }

    public static class ViewHolderNew extends RecyclerView.ViewHolder {
        public AppCompatCheckBox appCompatCheckBox;
        public ImageView more_image_view, selectImageView;
        public TextView nameTextView, txtFirstLetter, subtitileTextView;
        public FrameLayout select_card_view;
        public CardView user_image_card_view;

        public ViewHolderNew(@NonNull @NotNull View view) {
            super(view);
            txtFirstLetter = view.findViewById(R.id.txtFirstLetter);
            nameTextView = view.findViewById(R.id.name_text_view);
            subtitileTextView = view.findViewById(R.id.subtitletextView);
            appCompatCheckBox = view.findViewById(R.id.checkBox);
            user_image_card_view = view.findViewById(R.id.user_image_card_view);
            more_image_view = view.findViewById(R.id.more_image_view);
            select_card_view = view.findViewById(R.id.select_card_view);
            selectImageView = view.findViewById(R.id.select_image_view);
        }
    }
}