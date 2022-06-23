package com.whatscan.toolkit.forwa.AutoResponse;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.whatscan.toolkit.forwa.DataBaseHelper.CustomReplyDatabaseHelper;
import com.whatscan.toolkit.forwa.GetSet.ReplyModel;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Service.AllNotificationService;
import com.whatscan.toolkit.forwa.Service.MainService;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class FragmentHome extends Fragment {
    public static boolean isSpinnerInitial = true;
    public static ToggleButton service_on_off;
    public Boolean notificationcheck = false;
    public LinearLayout addcustommsgreply, llOne, llTwo, llThree;
    public TextView autoreplytext, txtOne, txtTwo, txtThree, txtFour;
    public Spinner customreply_list;
    public ImageView edit_reply_text, imgReply;
    public Spinner msgs_list;
    public ReplyModel replyModel;
    public CustomReplyDatabaseHelper customReplyDatabaseHelper;
    public ArrayList<String> msgslist;
    public ArrayList<ReplyModel> replyModels;
    public Context context;

    public FragmentHome() {
    }

    @SuppressLint({"ResourceType"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_home, container, false);

        context = getContext();
        autoreplytext = inflate.findViewById(R.id.autoreplytext);
        txtOne = inflate.findViewById(R.id.txtOne);
        txtTwo = inflate.findViewById(R.id.txtTwo);
        txtThree = inflate.findViewById(R.id.txtThree);
        txtFour = inflate.findViewById(R.id.txtFour);
        addcustommsgreply = inflate.findViewById(R.id.addcustommsgreply);
        edit_reply_text = inflate.findViewById(R.id.edit_reply_text);
        imgReply = inflate.findViewById(R.id.imgReply);
        msgs_list = inflate.findViewById(R.id.msgs_list);
        customreply_list = inflate.findViewById(R.id.customreply_list);
        service_on_off = inflate.findViewById(R.id.service_on_off);
        llOne = inflate.findViewById(R.id.llOne);
        llTwo = inflate.findViewById(R.id.llTwo);
        llThree = inflate.findViewById(R.id.llThree);

        if (Preference.getBooleanTheme(false)) {
            llOne.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_w));
            llTwo.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_w));
            llThree.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_w));
            txtOne.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtTwo.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtThree.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtFour.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            autoreplytext.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            imgReply.setImageResource(R.drawable.ic_back_white);
        }

        addcustommsgreply.setOnClickListener(v -> startActivity(new Intent(context, ActivityCustomReply.class), ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle()));

        edit_reply_text.setOnClickListener(view -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
            View inflate1 = LayoutInflater.from(FragmentHome.this.getActivity()).inflate(R.layout.text_update_dialog, null);
            bottomSheetDialog.setContentView(inflate1);

            RelativeLayout rlAutoReplay = inflate1.findViewById(R.id.rlAutoReplay);
            TextView txt_header = inflate1.findViewById(R.id.txt_header);
            TextInputLayout text_input1 = inflate1.findViewById(R.id.text_input1);
            EditText editText = inflate1.findViewById(R.id.editreply_text);

            if (Preference.getBooleanTheme(false)) {
                rlAutoReplay.setBackgroundColor(ContextCompat.getColor(context, R.color.darkBlack));
                txt_header.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                text_input1.setDefaultHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorWhite)));
                editText.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            }

            (inflate1.findViewById(R.id.close_dialog)).setOnClickListener(view1 -> bottomSheetDialog.dismiss());

            editText.setText(FragmentHome.this.autoreplytext.getText().toString());
            editText.setSelection(editText.getText().length());
            (inflate1.findViewById(R.id.updatebtn)).setOnClickListener(view12 -> {
                Toast.makeText(context, "Text Updated", Toast.LENGTH_SHORT).show();
                Preference.setstring(context, "servicecheck", "listmsgs", editText.getText().toString());
                autoreplytext.setText(editText.getText().toString());
                bottomSheetDialog.dismiss();
            });

            bottomSheetDialog.show();
        });

        msgslist = new ArrayList<>(Arrays.asList("Can't talk now , text you later", "I am sleeping", "I am driving", "I am in meeting", "Talk to you later", "I am busy", "I am sleeping, text you later", "At work, text you later"));
        ArrayAdapter arrayAdapter = new ArrayAdapter(context, R.layout.spinnertext, msgslist);
        arrayAdapter.setDropDownViewResource(17367049);
        msgs_list.setAdapter(arrayAdapter);

        msgs_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                if (Preference.getBooleanTheme(false)) {
                    ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                }

                if (isSpinnerInitial) {
                    isSpinnerInitial = false;
                    return;
                }
                String obj = adapterView.getItemAtPosition(i).toString();
                Preference.setstring(context, "servicecheck", "listmsgs", obj);
                autoreplytext.setText(obj);
            }
        });

        customReplyDatabaseHelper = new CustomReplyDatabaseHelper(getActivity());

        replyModels = new ArrayList<>();
        replyModels = viewAll();
        if (replyModels.size() > 0) {
            Collections.reverse(replyModels);
            customreply_list.setVisibility(View.VISIBLE);
            customreply_list.setAdapter(new CustomAdapter(getActivity(), replyModels));
        } else {
            customreply_list.setVisibility(View.GONE);
        }

        customreply_list.setOnItemSelectedListener(null);
        autoreplytext.setText(Preference.getstring(context, "servicecheck", "listmsgs", "Can't talk now , text you later"));

        if (Preference.getBoolean(getActivity(), "servicecheck", "onoff", false)) {
            service_on_off.setChecked(true);
        } else if (Preference.getPermission_notification().equals("true")) {
            context.startService(new Intent(context, MainService.class));
            Preference.setBoolean(context, "servicecheck", "onoff", true);
            Preference.setBoolean(context, "servicecheck", "reply_once_radio", false);
        }

        service_on_off.setOnCheckedChangeListener((compoundButton, z) -> {
            if (!compoundButton.isPressed()) {
                return;
            }
            if (NotificationManagerCompat.getEnabledListenerPackages(context).contains(context.getPackageName())) {
                notificationcheck = false;
                if (z) {
                    context.startService(new Intent(context, MainService.class));
                    Preference.setBoolean(context, "servicecheck", "onoff", true);
                    Preference.setBoolean(context, "servicecheck", "reply_once_radio", false);
                    return;
                }
                if (AllNotificationService.mainService != null) {
                    AllNotificationService.mainService.stopForeground(true);
                }
                Preference.setBoolean(context, "servicecheck", "onoff", false);
                return;
            }
            service_on_off.setChecked(false);
            notificationcheck = true;
            startActivityForResult(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"), 100);
        });
        return inflate;
    }

    public ArrayList<ReplyModel> viewAll() {
        Cursor allData = customReplyDatabaseHelper.getAllData();
        replyModels = new ArrayList<>();
        while (allData.moveToNext()) {
            replyModel = new ReplyModel();
            replyModel.setId(allData.getString(0));
            replyModel.setIncomingmsg(allData.getString(1));
            replyModel.setReplymsg(allData.getString(2));
            replyModel.setSelectedoption(allData.getString(3));
            replyModels.add(replyModel);
        }
        return replyModels;
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 100) {
            new Handler().postDelayed(() -> {
                if (notificationcheck && !NotificationManagerCompat.getEnabledListenerPackages(context).contains(context.getPackageName())) {
                    service_on_off.setChecked(false);
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Please apply notification access permission or you can't use this app");
                    builder.setCancelable(true);
                    builder.setPositiveButton(getString(R.string.txt_ok), (dialogInterface, i1) -> {
                        dialogInterface.dismiss();
                        notificationcheck = true;
                        startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                    });
                    builder.setNegativeButton("Later", (dialogInterface, i1) -> {
                        dialogInterface.cancel();
                        requireActivity().finish();
                    });
                    builder.create().show();
                } else if (notificationcheck) {
                    service_on_off.setChecked(true);
                    Preference.setPermission_notification("true");
                    Preference.setBoolean(context, "servicecheck", "onoff", true);
                    Preference.setBoolean(context, "servicecheck", "reply_once_radio", false);
                }
            }, 500);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Preference.getPermission_notification().equals("true") && Preference.getBoolean(context, "servicecheck", "onoff", false)) {
            service_on_off.setChecked(true);
            Preference.setBoolean(getActivity(), "servicecheck", "reply_once_radio", false);
        }
        autoreplytext.setText(Preference.getstring(getActivity(), "servicecheck", "listmsgs", "Can't talk now , text you later"));
        replyModels = new ArrayList<>();
        replyModels = viewAll();
        if (replyModels.size() > 0) {
            Collections.reverse(replyModels);
            customreply_list.setVisibility(View.VISIBLE);
            customreply_list.setAdapter(new CustomAdapter(getActivity(), replyModels));
            return;
        }
        customreply_list.setVisibility(View.GONE);
    }

    public static class CustomAdapter extends BaseAdapter {
        Context context;
        LayoutInflater inflter;
        ArrayList<ReplyModel> listofmsgs;

        public CustomAdapter(Context context2, ArrayList<ReplyModel> arrayList) {
            this.context = context2;
            this.listofmsgs = arrayList;
            this.inflter = LayoutInflater.from(context2);
        }

        public Object getItem(int i) {
            return null;
        }

        public long getItemId(int i) {
            return 0;
        }

        public int getCount() {
            return listofmsgs.size();
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            @SuppressLint("ViewHolder") View inflate = inflter.inflate(R.layout.reply_item_layout, null);
            if (Preference.getBooleanTheme(false)) {
                ((TextView) inflate.findViewById(R.id.incominmsg)).setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                ((TextView) inflate.findViewById(R.id.replymsg)).setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            }
            ((TextView) inflate.findViewById(R.id.incominmsg)).setText(listofmsgs.get(i).getIncomingmsg());
            ((TextView) inflate.findViewById(R.id.replymsg)).setText(listofmsgs.get(i).getReplymsg());
            return inflate;
        }
    }
}