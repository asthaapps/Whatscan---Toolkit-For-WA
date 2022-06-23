package com.whatscan.toolkit.forwa.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.whatscan.toolkit.forwa.GetSet.StatusData;
import com.whatscan.toolkit.forwa.GetSet.otherStatusData;
import com.whatscan.toolkit.forwa.Prank.FakeChat.StoriesProgressActivity;
import com.whatscan.toolkit.forwa.R;
import com.bumptech.glide.Glide;
import com.devlomi.circularstatusview.CircularStatusView;

import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class OtherStatusadapter extends RecyclerView.Adapter<OtherStatusadapter.ViewHolder> {
    Context context;
    List<otherStatusData> list;
    SimpleDateFormat simpleDateFormat;

    @SuppressLint("SimpleDateFormat")
    public OtherStatusadapter(Context context2, List<otherStatusData> list2) {
        context = context2;
        list = list2;
        simpleDateFormat = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.status_layout, viewGroup, false));
    }

    public void onBindViewHolder(@NotNull ViewHolder viewHolder, int i) {
        final List<StatusData> dataList = list.get(i).getDataList();
        viewHolder.circularStatusView.setPortionsCount(dataList.size());
        Glide.with(context).load(dataList.get(0).getImageurl()).into(viewHolder.profile_image);
        viewHolder.tvName.setText(dataList.get(0).getName());
        if (dataList.size() > 0 && dataList.get(dataList.size() - 1).getTimeanddate().length() > 0) {
            String str = dataList.get(dataList.size() - 1).getTimeanddate().split(" ")[0];
            try {
                String[] split = dataList.get(dataList.size() - 1).getTimeanddate().split(" ");
                SimpleDateFormat simpleDateFormat2 = simpleDateFormat;
                Date parse = simpleDateFormat2.parse(split[1] + " " + split[0] + ":00");
                String sb = split[0] + "";
                printDifference(parse, sb, viewHolder.tvTime);
            } catch (ParseException e) {
                e.printStackTrace();
                TextView textView = viewHolder.tvTime;
                textView.setText(Html.fromHtml(context.getString(R.string.today) + ", " + str));
            }
        }
        for (int i2 = 0; i2 < dataList.size(); i2++) {
            StatusData statusData = dataList.get(i2);
            if (statusData.getViewedcompleted() != 0) {
                Log.e("TAGd", "onBindViewHolder: " + statusData.getViewedcompleted());
                viewHolder.circularStatusView.setPortionColorForIndex(i2, context.getResources().getColor(R.color.white));
            }
        }

        viewHolder.itemView.setOnClickListener(view -> context.startActivity(new Intent(context, StoriesProgressActivity.class).putExtra("type", dataList.get(0).getName())));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @SuppressLint("SetTextI18n")
    public void printDifference(Date date, String str, TextView textView) {
        Date date2;
        try {
            date2 = simpleDateFormat.parse(new SimpleDateFormat("dd/M/yyyy HH:mm:ss", Locale.getDefault()).format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
            textView.setText("Today, " + str);
            date2 = null;
        }
        long time = Objects.requireNonNull(date2).getTime() - date.getTime();
        PrintStream printStream = System.out;
        printStream.println("startDate : " + date2);
        PrintStream printStream2 = System.out;
        printStream2.println("endDate : " + date);
        PrintStream printStream3 = System.out;
        printStream3.println("different : " + time);
        long j = time / 86400000;
        long j2 = time % 86400000;
        long j3 = j2 / 3600000;
        long j4 = j2 % 3600000;
        long j5 = j4 / 60000;
        long j6 = (j4 % 60000) / 1000;
        System.out.printf("%d days, %d hours, %d minutes, %d seconds%n", j, j3, j5, j6);
        if (j > 0) {
            if (j == 1) {
                textView.setText("yesterday, " + str);
                return;
            }
            textView.setText(j + " day ago");
        } else if (j3 > 0) {
            if (j3 > 6) {
                textView.setText("Today, " + str);
                return;
            }
            textView.setText(j3 + " hours ago");
        } else if (j5 > 0) {
            if (j5 > 59) {
                textView.setText("1 hours ago");
                return;
            }
            textView.setText(j5 + " minutes ago");
        } else if (j6 > 0 && j6 < 59) {
            textView.setText("Just now");
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CircularStatusView circularStatusView;
        public CircleImageView profile_image;
        public TextView tvName;
        public TextView tvTime;

        public ViewHolder(View view) {
            super(view);
            profile_image = view.findViewById(R.id.profile_image);
            circularStatusView = view.findViewById(R.id.circular_status_view);
            tvName = view.findViewById(R.id.tvName);
            tvTime = view.findViewById(R.id.tvTime);
        }
    }
}