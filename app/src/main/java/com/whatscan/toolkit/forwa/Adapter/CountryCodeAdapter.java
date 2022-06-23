package com.whatscan.toolkit.forwa.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.whatscan.toolkit.forwa.GetSet.Country;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Preference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CountryCodeAdapter extends RecyclerView.Adapter<CountryCodeAdapter.CountryCodeViewHolder> {
    public Activity activity;
    public List<Country> countryList = new ArrayList<>();
    public List<Country> countryModelList = new ArrayList<>();

    public CountryCodeAdapter(Activity activity) {
        this.activity = activity;
    }

    @NotNull
    @Override
    public CountryCodeViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int i) {
        return new CountryCodeViewHolder(LayoutInflater.from(viewGroup.getContext()), viewGroup);
    }

    @SuppressLint("SetTextI18n")
    public void onBindViewHolder(@NotNull CountryCodeViewHolder countryCOdeViewHolder, final int i) {
        if (countryList != null && i < countryList.size()) {

            if (Preference.getBooleanTheme(false)) {
                countryCOdeViewHolder.titleTextView.setTextColor(ContextCompat.getColor(activity, R.color.colorWhite));
            } else {
                countryCOdeViewHolder.titleTextView.setTextColor(ContextCompat.getColor(activity, R.color.colorBlack));
            }

            countryCOdeViewHolder.titleTextView.setText(countryList.get(i).getCountryName());
            countryCOdeViewHolder.subtitleTextView.setText("(" + countryList.get(i).getCountryCode() + ")");

            countryCOdeViewHolder.view.setOnClickListener(view -> {
                try {
                    Intent intent = new Intent();
                    intent.putExtra("code", countryList.get(i).getCountryCode());
                    activity.setResult(100, intent);
                    activity.finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void filter(String str) {
        String lowerCase = str.toLowerCase(Locale.getDefault());
        countryList.clear();
        if (lowerCase.length() == 0) {
            countryList.addAll(countryModelList);
        } else {
            for (int i = 0; i < countryModelList.size(); i++) {
                if (countryModelList.get(i).getCountryName().toLowerCase(Locale.getDefault()).contains(lowerCase)) {
                    countryList.add(countryModelList.get(i));
                }
            }
        }
        notifyDataSetChanged();
    }

    public void setCountryList(List<Country> list) {
        countryList.addAll(list);
        countryModelList.addAll(list);
    }

    @Override
    public int getItemCount() {
        if (countryList != null) {
            return countryList.size();
        }
        return 0;
    }

    public static class CountryCodeViewHolder extends RecyclerView.ViewHolder {
        public TextView subtitleTextView = itemView.findViewById(R.id.subtitletextView);
        public TextView titleTextView = itemView.findViewById(R.id.titletextView);
        public View view;

        public CountryCodeViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
            super(layoutInflater.inflate(R.layout.item_country_selection, viewGroup, false));
            view = itemView;
        }
    }
}