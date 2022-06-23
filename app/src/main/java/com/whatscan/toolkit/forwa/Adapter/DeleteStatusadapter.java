package com.whatscan.toolkit.forwa.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.whatscan.toolkit.forwa.GetSet.StatusData;
import com.whatscan.toolkit.forwa.GetSet.otherStatusData;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.bumptech.glide.Glide;
import com.devlomi.circularstatusview.CircularStatusView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class DeleteStatusadapter extends RecyclerView.Adapter<DeleteStatusadapter.ViewHolder> {
    Activity activity;
    Context context;
    List<otherStatusData> list;
    SimpleDateFormat simpleDateFormat;

    @SuppressLint("SimpleDateFormat")
    public DeleteStatusadapter(Context context2, List<otherStatusData> list2, Activity activity2) {
        context = context2;
        list = list2;
        activity = activity2;
        simpleDateFormat = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.status_delet_layout, viewGroup, false));
    }

    public void onBindViewHolder(@NotNull ViewHolder viewHolder, final int i) {
        if (i == list.size() - 1) {
            viewHolder.line.setVisibility(View.GONE);
        }
        List<StatusData> dataList = list.get(i).getDataList();
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

        viewHolder.delete_img.setOnClickListener(view -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
            View inflate = LayoutInflater.from(context).inflate(R.layout.delete_dialog, null);
            bottomSheetDialog.setContentView(inflate);

            RelativeLayout rl_dialog = inflate.findViewById(R.id.rl_dialog);
            TextView tv_dialog_title = inflate.findViewById(R.id.tv_dialog_title);
            TextView tv_dialog_tip = inflate.findViewById(R.id.tv_dialog_tip);
            AppCompatButton bt_yes = inflate.findViewById(R.id.bt_yes);
            AppCompatButton bt_no = inflate.findViewById(R.id.bt_no);

            if (Preference.getBooleanTheme(false)){
                rl_dialog.setBackgroundColor(ContextCompat.getColor(context, R.color.darkBlack));
                tv_dialog_title.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                tv_dialog_tip.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            }

            tv_dialog_title.setText(context.getString(R.string.my_status));
            tv_dialog_tip.setText(context.getString(R.string.delete_chat));

            bt_no.setOnClickListener(v1 -> bottomSheetDialog.dismiss());

            bt_yes.setOnClickListener(v1 -> {
                try {
                    list.remove(i);
                    SharedPreferences.Editor edit = context.getSharedPreferences("data", 0).edit();
                    edit.putString("other", new Gson().toJson(list));
                    edit.apply();
                    Toast.makeText(context, "Delete success", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                    if (list.size() == 0) {
                        activity.finish();
                    }
                } catch (ArrayIndexOutOfBoundsException unused) {
                    Toast.makeText(context, "Delete Faild!!!", Toast.LENGTH_SHORT).show();
                }
                bottomSheetDialog.dismiss();
            });

            bottomSheetDialog.show();
        });
        for (int i2 = 0; i2 < dataList.size(); i2++) {
            StatusData statusData = dataList.get(i2);
            if (statusData.getViewedcompleted() != 0) {
                viewHolder.circularStatusView.setPortionColorForIndex(i2, context.getResources().getColor(R.color.white));
            }
        }
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
        public ImageView delete_img;
        public LinearLayout line;
        public CircleImageView profile_image;
        public TextView tvName;
        public TextView tvTime;

        public ViewHolder(View view) {
            super(view);
            delete_img = view.findViewById(R.id.delete_img);
            profile_image = view.findViewById(R.id.profile_image);
            circularStatusView = view.findViewById(R.id.circular_status_view);
            tvName = view.findViewById(R.id.tvName);
            tvTime = view.findViewById(R.id.tvTime);
            line = view.findViewById(R.id.line);
        }
    }
}
