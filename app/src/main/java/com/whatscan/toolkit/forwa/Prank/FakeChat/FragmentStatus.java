package com.whatscan.toolkit.forwa.Prank.FakeChat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.whatscan.toolkit.forwa.Adapter.OtherStatusadapter;
import com.whatscan.toolkit.forwa.GetSet.StatusData;
import com.whatscan.toolkit.forwa.GetSet.otherStatusData;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.bumptech.glide.Glide;
import com.devlomi.circularstatusview.CircularStatusView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentStatus extends Fragment {
    public List<otherStatusData> totalotherlist = new ArrayList<>();
    public List<StatusData> totallist = new ArrayList<>();
    public RelativeLayout chat_row_container;
    public CircularStatusView circularStatusView;
    public FloatingActionButton mainFab;
    public CircleImageView profile_image;
    public RecyclerView rvohterStatus;
    public ImageView cusmenu;
    public SimpleDateFormat simpleDateFormat;
    public LinearLayout status_lay;
    public TextView tvName, tvTime;
    public View view;

    public FragmentStatus() {
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        view = layoutInflater.inflate(R.layout.fragment_status, viewGroup, false);

        chat_row_container = view.findViewById(R.id.chat_row_container);
        status_lay = view.findViewById(R.id.status_lay);
        profile_image = view.findViewById(R.id.profile_image);
        mainFab = view.findViewById(R.id.mainFab);
        circularStatusView = view.findViewById(R.id.circular_status_view);
        tvName = view.findViewById(R.id.tvName);
        tvTime = view.findViewById(R.id.tvTime);
        rvohterStatus = view.findViewById(R.id.rvohterStatus);
        cusmenu = view.findViewById(R.id.cusmenu);

        cusmenu.setVisibility(View.VISIBLE);
        rvohterStatus.setLayoutManager(new LinearLayoutManager(getContext()));
        simpleDateFormat = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");

        updateMystatus();
        updateRecent();

        view.findViewById(R.id.miniFab).setOnClickListener(view -> {
            if (getOtherData().size() > 0) {
                startActivity(new Intent(getActivity(), DeleteStoryActivity.class));
                return;
            }
            Toast.makeText(getContext(), "please add status...", Toast.LENGTH_SHORT).show();
        });

        cusmenu.setOnClickListener(view -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
            View inflate = LayoutInflater.from(requireContext()).inflate(R.layout.delete_dialog, null);
            bottomSheetDialog.setContentView(inflate);

            RelativeLayout rl_dialog = inflate.findViewById(R.id.rl_dialog);
            TextView tv_dialog_title = inflate.findViewById(R.id.tv_dialog_title);
            TextView tv_dialog_tip = inflate.findViewById(R.id.tv_dialog_tip);
            AppCompatButton bt_yes = inflate.findViewById(R.id.bt_yes);
            AppCompatButton bt_no = inflate.findViewById(R.id.bt_no);

            if (Preference.getBooleanTheme(false)) {
                rl_dialog.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.darkBlack));
                tv_dialog_title.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite));
                tv_dialog_tip.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite));
            }

            tv_dialog_title.setText(getString(R.string.my_status));
            tv_dialog_tip.setText(getString(R.string.delete_chat));

            bt_no.setOnClickListener(v1 -> bottomSheetDialog.dismiss());

            bt_yes.setOnClickListener(v1 -> {
                try {
                    SharedPreferences.Editor edit = requireContext().getSharedPreferences("data", 0).edit();
                    new Gson();
                    edit.putString("mystatus", "");
                    edit.apply();
                    Toast.makeText(getContext(), "success", Toast.LENGTH_SHORT).show();
                    updateMystatus();
                } catch (ArrayIndexOutOfBoundsException ignored) {
                }
                bottomSheetDialog.dismiss();
            });

            bottomSheetDialog.show();
        });

        status_lay.setOnClickListener(view -> startActivity(new Intent(getActivity(), StoriesProgressActivity.class).putExtra("type", "mystatus")));

        chat_row_container.setOnClickListener(view -> startActivity(new Intent(getActivity(), AddStatusActivity.class)));

        mainFab.setOnClickListener(view -> startActivity(new Intent(getActivity(), AddStatusActivity.class)));

        return view;
    }

    public List<StatusData> getdata() {
        totallist = new Gson().fromJson(requireContext().getSharedPreferences("data", 0).getString("mystatus", ""), new TypeToken<List<StatusData>>() {
        }.getType());
        if (totallist == null || totallist.size() <= 0) {
            return new ArrayList<>();
        }
        return totallist;
    }

    public List<otherStatusData> getOtherData() {
        totalotherlist = new Gson().fromJson(requireContext().getSharedPreferences("data", 0).getString("other", ""), new TypeToken<List<otherStatusData>>() {
        }.getType());
        if (totalotherlist == null || totalotherlist.size() <= 0) {
            totalotherlist = new ArrayList<>();
            return new ArrayList<>();
        }
        return totalotherlist;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (status_lay != null) {
            updateMystatus();
        }
        updateRecent();
    }

    public void updateMystatus() {
        if (getdata().size() > 0) {
            status_lay.setVisibility(View.VISIBLE);
            circularStatusView.setPortionsCount(totallist.size());
            Glide.with(this).load(totallist.get(0).getImageurl()).into(profile_image);
            chat_row_container.setVisibility(View.GONE);
            tvName.setText(getString(R.string.my_status));

            String str = totallist.get(totallist.size() - 1).getTimeanddate().split(" ")[0];
            try {
                String[] split = totallist.get(totallist.size() - 1).getTimeanddate().split(" ");
                SimpleDateFormat simpleDateFormat2 = simpleDateFormat;
                Date parse = simpleDateFormat2.parse(split[1] + " " + split[0] + ":00");
                String sb = split[0] + "";
                printDifference(parse, sb);
            } catch (ParseException e) {
                e.printStackTrace();
                TextView textView = tvTime;
                textView.setText(Html.fromHtml("Today, " + str));
            }

            for (int i = 0; i < totallist.size(); i++) {
                if (totallist.get(i).getViewedcompleted() != 0) {
                    circularStatusView.setPortionColorForIndex(i, requireContext().getResources().getColor(R.color.colorWhite));
                }
            }
            return;
        }
        status_lay.setVisibility(View.GONE);
        chat_row_container.setVisibility(View.VISIBLE);
    }

    public void updateRecent() {
        if (getOtherData().size() > 0) {
            rvohterStatus.setAdapter(new OtherStatusadapter(getContext(), totalotherlist));
            view.findViewById(R.id.txt_recent).setVisibility(View.VISIBLE);
            return;
        }
        rvohterStatus.setAdapter(new OtherStatusadapter(getContext(), new ArrayList<>()));
        view.findViewById(R.id.txt_recent).setVisibility(View.GONE);
    }

    @SuppressLint("SetTextI18n")
    public void printDifference(Date date, String str) {
        Date date2;
        try {
            date2 = simpleDateFormat.parse(new SimpleDateFormat("dd/M/yyyy HH:mm:ss", Locale.getDefault()).format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
            TextView textView = tvTime;
            textView.setText("Today, " + str);
            date2 = null;
        }
        long time = date2.getTime() - date.getTime();
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
                tvTime.setText("yesterday, " + str);
                return;
            }
            tvTime.setText(j + " day ago");
        } else if (j3 > 0) {
            if (j3 > 6) {
                tvTime.setText("Today, " + str);
                return;
            }
            tvTime.setText(j3 + " hours ago");
        } else if (j5 > 0) {
            if (j5 > 59) {
                tvTime.setText("1 hours ago");
                return;
            }
            tvTime.setText(j5 + " minutes ago");
        } else if (j6 > 0 && j6 < 59) {
            tvTime.setText("Just now");
        }
    }
}