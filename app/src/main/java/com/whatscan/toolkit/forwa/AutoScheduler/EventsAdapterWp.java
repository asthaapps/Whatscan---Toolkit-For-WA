package com.whatscan.toolkit.forwa.AutoScheduler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.whatscan.toolkit.forwa.DataBaseHelper.DBHelperScheduler;
import com.whatscan.toolkit.forwa.GetSet.EventModel;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Preference;

import java.util.ArrayList;

public class EventsAdapterWp extends RecyclerView.Adapter<EventsAdapterWp.MyViewHolder> {
    public Context context;
    public DBHelperScheduler dbManager;
    public ArrayList<EventModel> eventlist;

    public EventsAdapterWp(Context context2, ArrayList<EventModel> arrayList) {
        this.context = context2;
        this.eventlist = arrayList;
        this.dbManager = new DBHelperScheduler(context2);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_event_whatsapp, viewGroup, false));
    }

    @SuppressLint({"RestrictedApi", "SetTextI18n"})
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        if (Preference.getBooleanTheme(false)) {
            myViewHolder.rowFG.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorShape));
            myViewHolder.name.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            myViewHolder.msg.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            myViewHolder.date.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            myViewHolder.time.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        }

        EventModel event_model = eventlist.get(i);
        String name = event_model.getName();
        String date = event_model.getDate();
        String day = event_model.getDay();
        String time = event_model.getTime();
        String message = event_model.getMessage();
        String status = event_model.getStatus();

        myViewHolder.name.setText(name);
        myViewHolder.date.setText(day + ", " + date);
        myViewHolder.time.setText(time);
        myViewHolder.msg.setText(message);

        if (status.equals("pending")) {
            myViewHolder.status.setTextColor(context.getResources().getColor(R.color.colorBlack));
            myViewHolder.status.setText("Pending");
        }

        if (status.equals("success")) {
            myViewHolder.status.setTextColor(context.getResources().getColor(R.color.colorAccent));
            myViewHolder.status.setText("Sent");
        }

        if (status.equals("failed")) {
            myViewHolder.status.setTextColor(context.getResources().getColor(R.color.fingerprint_error));
            myViewHolder.status.setText("Failed");
        }
    }

    @Override
    public int getItemCount() {
        return eventlist.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView rowFG;
        TextView date;
        TextView msg;
        TextView name;
        TextView status;
        TextView time;

        public MyViewHolder(View view) {
            super(view);
            rowFG = view.findViewById(R.id.rowFG);
            name = view.findViewById(R.id.tv_contact_name);
            date = view.findViewById(R.id.tv_date);
            time = view.findViewById(R.id.tv_time);
            msg = view.findViewById(R.id.tv_msg);
            status = view.findViewById(R.id.tv_status);
        }
    }
}