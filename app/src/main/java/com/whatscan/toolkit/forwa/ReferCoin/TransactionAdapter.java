package com.whatscan.toolkit.forwa.ReferCoin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.whatscan.toolkit.forwa.GetSet.TransactionSubData;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Preference;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {
    public Context context;
    public List<TransactionSubData> transactionSubList;

    public TransactionAdapter(Context context, List<TransactionSubData> transactionSubList) {
        this.context = context;
        this.transactionSubList = transactionSubList;
    }

    @NonNull
    @NotNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new TransactionViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coin_history, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull TransactionViewHolder holder, int position) {
        if (Preference.getBooleanTheme(false)) {
            holder.tvMonth.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            holder.tvTransactionTitle.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        }

        holder.tvMonth.setText(Html.fromHtml(transactionSubList.get(position).getCurrent_month() + ", " + Calendar.getInstance().get(Calendar.YEAR)));
        holder.tvTransactionTitle.setText(transactionSubList.get(position).getName());
        if (transactionSubList.get(position).getT_name().equals("Earn")) {
            holder.tvTransactionCoin.setTextColor(ContextCompat.getColor(context, R.color.colorChatLight));
            holder.tvTransactionCoin.setText("+" + transactionSubList.get(position).getCoin());
        } else {
            holder.tvTransactionCoin.setTextColor(ContextCompat.getColor(context, R.color.materialcolorpicker__red));
            holder.tvTransactionCoin.setText("-" + transactionSubList.get(position).getCoin());
        }

        holder.tvDate.setText(transactionSubList.get(position).getCreated_at());
    }

    @Override
    public int getItemCount() {
        return transactionSubList.size();
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {
        public TextView tvMonth, tvTransactionTitle, tvTransactionCoin, tvDate;

        public TransactionViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvMonth = itemView.findViewById(R.id.tvMonth);
            tvTransactionCoin = itemView.findViewById(R.id.tv_transaction_coin);
            tvTransactionTitle = itemView.findViewById(R.id.tv_transaction_title);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }
}