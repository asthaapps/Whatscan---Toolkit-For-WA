package com.whatscan.toolkit.forwa.Adapter;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.whatscan.toolkit.forwa.GetSet.GetSetContact;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Preference;

import java.util.ArrayList;

public class ExportContactAdapter extends RecyclerView.Adapter<ExportContactAdapter.ExportViewHolder> {
    ArrayList<GetSetContact> contactValue;
    Context context;
    LayoutInflater inflater;

    @SuppressLint("WrongConstant")
    public ExportContactAdapter(ArrayList<GetSetContact> arrayList, Context context2) {
        contactValue = arrayList;
        context = context2;
        inflater = (LayoutInflater) context2.getSystemService("layout_inflater");
    }

    @NonNull
    @Override
    public ExportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ExportViewHolder(inflater.inflate(R.layout.item_contact_sent, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ExportViewHolder holder, int position) {
        if (Preference.getBooleanTheme(false)){
            holder.tvName.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        }

        holder.tvName.setText(contactValue.get(position).getName());
        holder.tvNumber.setText(contactValue.get(position).getNumber());
        holder.txtFirstLetter.setText(getSectionText(position));

        holder.tvNumber.setEnabled(false);
        holder.tvNumber.setClickable(false);
        holder.tvName.setEnabled(false);
        holder.tvName.setClickable(false);

        holder.rlIntent.setOnClickListener(view1 -> {
            if (appInstalledOrNot("com.whatsapp") && appInstalledOrNot("com.whatsapp.w4b")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Oh! Looks like you are using WhatsApp Business along with WhatsApp Messenger. Please choose either of them to go the contacts.");
                builder.setPositiveButton("Go to Whatsapp", (dialogInterface, i1) -> {
                    try {
                        Intent intent = new Intent("android.intent.action.MAIN");
                        intent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                        intent.setAction("android.intent.action.SEND");
                        intent.setType("text/plain");
                        intent.putExtra("android.intent.extra.TEXT", "");
                        intent.putExtra("jid", holder.tvNumber.getText() + "@s.whatsapp.net");
                        intent.setPackage("com.whatsapp");
                        context.startActivity(intent);
                    } catch (Exception unused) {
                        Toast.makeText(context, "No contact found", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Go to Whatsapp Business", (dialogInterface, i1) -> {
                    try {
                        Intent intent = new Intent("android.intent.action.MAIN");
                        intent.setComponent(new ComponentName("com.whatsapp.w4b", "com.whatsapp.Conversation"));
                        intent.setAction("android.intent.action.SEND");
                        intent.setType("text/plain");
                        intent.putExtra("android.intent.extra.TEXT", "");
                        intent.putExtra("jid", holder.tvNumber.getText() + "@s.whatsapp.net");
                        intent.setPackage("com.whatsapp.w4b");
                        context.startActivity(intent);
                    } catch (Exception unused) {
                        Toast.makeText(context, "No contact found", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNeutralButton("Cancel", null);
                builder.create().show();
            } else if (contactValue.get(position).getAcType().equals("com.whatsapp")) {
                try {
                    Intent intent = new Intent("android.intent.action.MAIN");
                    intent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                    intent.setAction("android.intent.action.SEND");
                    intent.setType("text/plain");
                    intent.putExtra("android.intent.extra.TEXT", "");
                    intent.putExtra("jid", holder.tvNumber.getText() + "@s.whatsapp.net");
                    intent.setPackage("com.whatsapp");
                    context.startActivity(intent);
                } catch (Exception unused) {
                    Toast.makeText(context, "No contact found", Toast.LENGTH_SHORT).show();
                }
            } else if (contactValue.get(position).getAcType().equals("com.whatsapp.w4b")) {
                try {
                    Intent intent2 = new Intent("android.intent.action.MAIN");
                    intent2.setComponent(new ComponentName("com.whatsapp.w4b", "com.whatsapp.Conversation"));
                    intent2.setAction("android.intent.action.SEND");
                    intent2.setType("text/plain");
                    intent2.putExtra("android.intent.extra.TEXT", "");
                    intent2.putExtra("jid", holder.tvNumber.getText() + "@s.whatsapp.net");
                    intent2.setPackage("com.whatsapp.w4b");
                    context.startActivity(intent2);
                } catch (Exception unused2) {
                    Toast.makeText(context, "No contact found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private CharSequence getSectionText(int i) {
        GetSetContact contactModel = contactValue.get(i);
        String name = contactModel.getName();
        int i2 = 1;
        if (name.length() > 0) {
            if (name.length() >= 2) {
                i2 = 2;
            }
            return name.substring(0, i2);
        }

        String phoneNumber = contactModel.getNumber();
        if (!(phoneNumber.length() > 0)) {
            return "#";
        }

        if (phoneNumber.length() >= 2) {
            i2 = 2;
        }
        return phoneNumber.substring(0, i2);
    }

    @Override
    public int getItemCount() {
        return contactValue.size();
    }

    public boolean appInstalledOrNot(String str) {
        try {
            context.getPackageManager().getPackageInfo(str, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    public static class ExportViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rlIntent;
        TextView tvName, tvNumber, txtFirstLetter;

        public ExportViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.name_text_view);
            rlIntent = itemView.findViewById(R.id.rlIntent);
            tvNumber = itemView.findViewById(R.id.subtitletextView);
            txtFirstLetter = itemView.findViewById(R.id.txtFirstLetter);
        }
    }
}