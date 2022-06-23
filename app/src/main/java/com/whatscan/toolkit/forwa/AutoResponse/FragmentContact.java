package com.whatscan.toolkit.forwa.AutoResponse;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.whatscan.toolkit.forwa.DataBaseHelper.ContactDatabaseHelper;
import com.whatscan.toolkit.forwa.DataBaseHelper.GroupDatabaseHelper;
import com.whatscan.toolkit.forwa.GetSet.ContactModelAuto;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FragmentContact extends Fragment {
    public int CONTACT_PICK_REQUEST = 1000;
    public ImageView addcontact_btn;
    public ContactDatabaseHelper contactDatabaseHelper;
    public ContactModelAuto contactModel;
    public ArrayList<ContactModelAuto> contactModels = new ArrayList<>();
    public Button contact_group;
    public RadioGroup contacts_radio_group;
    public GroupDatabaseHelper groupDatabaseHelper;
    public com.whatscan.toolkit.forwa.GetSet.GroupModel groupModel;
    public ArrayList<com.whatscan.toolkit.forwa.GetSet.GroupModel> groupModelModels;
    public Button groups_group;
    public RadioGroup groups_radio_group;
    public TextView info_txt;
    public TextView list_info_text;
    public AlertDialog permission_alert;
    public RecyclerView selected_contact_list;
    public Context context;

    public FragmentContact() {
        // Required empty public constructor
    }

    @SuppressLint({"WrongConstant", "NonConstantResourceId"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        context = getContext();
        contactDatabaseHelper = new ContactDatabaseHelper(context);
        groupDatabaseHelper = new GroupDatabaseHelper(context);
        contacts_radio_group = view.findViewById(R.id.contacts_radio_group);
        groups_radio_group = view.findViewById(R.id.groups_radio_group);
        list_info_text = view.findViewById(R.id.list_info_text);
        selected_contact_list = view.findViewById(R.id.selected_contact_list);
        addcontact_btn = view.findViewById(R.id.addcontact_btn);
        info_txt = view.findViewById(R.id.info_txt);
        contact_group = view.findViewById(R.id.contact_group);
        groups_group = view.findViewById(R.id.groups_group);
        LinearLayout llOne = view.findViewById(R.id.llOne);
        RadioButton everyone_radio = view.findViewById(R.id.everyone_radio);
        RadioButton mycontact_radio = view.findViewById(R.id.mycontact_radio);
        RadioButton exceptcontact_radio = view.findViewById(R.id.exceptcontact_radio);
        RadioButton nonecontact_radio = view.findViewById(R.id.nonecontact_radio);
        RadioButton everyone_group = view.findViewById(R.id.everyone_group);
        RadioButton mygroup_radio = view.findViewById(R.id.mygroup_radio);
        RadioButton exceptgroup_radio = view.findViewById(R.id.exceptgroup_radio);
        RadioButton nonegroup_radio = view.findViewById(R.id.nonegroup_radio);

        if (Preference.getBooleanTheme(false)) {
            groups_group.setBackgroundColor(getResources().getColor(R.color.colorShape));
            contact_group.setBackgroundColor(getResources().getColor(R.color.colorTools));
            groups_group.setTextColor(getResources().getColor(R.color.colorWhite));
            contact_group.setTextColor(getResources().getColor(R.color.colorWhite));
            list_info_text.setTextColor(getResources().getColor(R.color.colorWhite));
            info_txt.setTextColor(getResources().getColor(R.color.colorWhite));
            llOne.setBackgroundColor(getResources().getColor(R.color.colorShape));
            everyone_radio.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            mycontact_radio.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            exceptcontact_radio.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            nonecontact_radio.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            everyone_group.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            mygroup_radio.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            exceptgroup_radio.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            nonegroup_radio.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        } else {
            groups_group.setTextColor(getResources().getColor(R.color.colorBlack));
            groups_group.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            contact_group.setTextColor(getResources().getColor(R.color.colorWhite));
            contact_group.setBackgroundColor(getResources().getColor(R.color.colorTools));
            list_info_text.setBackgroundColor(getResources().getColor(R.color.colorTools));
            info_txt.setBackgroundColor(getResources().getColor(R.color.colorTools));
            llOne.setBackgroundColor(getResources().getColor(R.color.adColor));
            everyone_radio.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            mycontact_radio.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            exceptcontact_radio.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            nonecontact_radio.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            everyone_group.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            mygroup_radio.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            exceptgroup_radio.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            nonegroup_radio.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
        }

        addcontact_btn.setOnClickListener(view15 -> {
            if (!Preference.getstring(FragmentContact.this.getActivity(), "main_group", "contacts_groups", "contacts_group").equals("contacts_group")) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireActivity());
                View inflate = LayoutInflater.from(FragmentContact.this.getActivity()).inflate(R.layout.text_update_dialog, null);
                bottomSheetDialog.setContentView(inflate);

                RelativeLayout rlAutoReplay = inflate.findViewById(R.id.rlAutoReplay);
                TextView txt_header = inflate.findViewById(R.id.txt_header);
                EditText editText = inflate.findViewById(R.id.editreply_text);
                TextInputLayout text_input1 = inflate.findViewById(R.id.text_input1);

                if (Preference.getBooleanTheme(false)){
                    rlAutoReplay.setBackgroundColor(ContextCompat.getColor(context, R.color.darkBlack));
                    txt_header.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                    editText.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                    text_input1.setDefaultHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorWhite)));
                }

                ((TextView) inflate.findViewById(R.id.txt_header)).setText("Create group name");

                (inflate.findViewById(R.id.close_dialog)).setOnClickListener(view1 -> bottomSheetDialog.dismiss());


                editText.setSelection(editText.getText().length());
                (inflate.findViewById(R.id.updatebtn)).setOnClickListener(view12 -> {
                    if (editText.getText().toString().length() > 0) {
                        groupDatabaseHelper = new GroupDatabaseHelper(getActivity());
                        groupDatabaseHelper.insertData(editText.getText().toString());
                        groupDatabaseHelper.close();
                        Toast.makeText(getActivity(), "Group name added", Toast.LENGTH_SHORT).show();
                        groupModelModels = new ArrayList<>();
                        groupModelModels = GetGroupList();
                        if (groupModelModels.size() > 0) {
                            selected_contact_list.setVisibility(0);
                            info_txt.setVisibility(8);
                            selected_contact_list.setAdapter(new GroupListAdapter(groupModelModels));
                        } else {
                            info_txt.setVisibility(0);
                            info_txt.setText("Add group name to make list here");
                            selected_contact_list.setVisibility(8);
                        }
                        bottomSheetDialog.dismiss();
                        return;
                    }
                    Toast.makeText(getActivity(), "Add group name", Toast.LENGTH_SHORT).show();
                });

                bottomSheetDialog.show();
            } else if (Build.VERSION.SDK_INT < 23) {
                Intent intent = new Intent(getActivity(), ActivityContactsPicker.class);
                startActivityForResult(intent, CONTACT_PICK_REQUEST);
            } else if (context.checkSelfPermission("android.permission.READ_CONTACTS") != 0) {
                askpermission(103);
            } else {
                Intent intent2 = new Intent(getActivity(), ActivityContactsPicker.class);
                startActivityForResult(intent2, CONTACT_PICK_REQUEST);
            }
        });

        selected_contact_list.setLayoutManager(new LinearLayoutManager(getActivity()));

        contact_group.setOnClickListener(view13 -> {
            if (Preference.getBooleanTheme(false)) {
                groups_group.setBackgroundColor(getResources().getColor(R.color.colorShape));
                contact_group.setBackgroundColor(getResources().getColor(R.color.colorTools));
                groups_group.setTextColor(getResources().getColor(R.color.colorWhite));
                contact_group.setTextColor(getResources().getColor(R.color.colorWhite));
            } else {
                groups_group.setTextColor(getResources().getColor(R.color.colorBlack));
                groups_group.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                contact_group.setTextColor(getResources().getColor(R.color.colorWhite));
                contact_group.setBackgroundColor(getResources().getColor(R.color.colorTools));
            }

            Preference.setstring(getActivity(), "main_group", "contacts_groups", "contacts_group");
            contacts_radio_group.setVisibility(0);
            groups_radio_group.setVisibility(8);
            list_info_text.setText("Contact list");
            checkingselection();
        });

        groups_group.setOnClickListener(view14 -> {
            if (Preference.getBooleanTheme(false)) {
                groups_group.setBackgroundColor(getResources().getColor(R.color.colorTools));
                contact_group.setBackgroundColor(getResources().getColor(R.color.colorShape));
                groups_group.setTextColor(getResources().getColor(R.color.colorWhite));
                contact_group.setTextColor(getResources().getColor(R.color.colorWhite));
            } else {
                groups_group.setTextColor(getResources().getColor(R.color.colorWhite));
                groups_group.setBackgroundColor(getResources().getColor(R.color.colorTools));
                contact_group.setTextColor(getResources().getColor(R.color.colorBlack));
                contact_group.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            }

            Preference.setstring(getActivity(), "main_group", "contacts_groups", "groups_group");
            contacts_radio_group.setVisibility(8);
            groups_radio_group.setVisibility(0);
            list_info_text.setText("Group list");
            checkingselection();
        });

        checkingselection();

        groups_radio_group.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i) {
                case R.id.everyone_group /*{ENCODED_INT: 2131230910}*/:
                    Preference.setstring(getActivity(), "groups_radio", "groups_radio_group", "everyone_group");
                    info_txt.setVisibility(0);
                    info_txt.setText("Auto response to every groups");
                    addcontact_btn.setVisibility(8);
                    selected_contact_list.setVisibility(8);
                    return;
                case R.id.exceptgroup_radio /*{ENCODED_INT: 2131230914}*/:
                    Preference.setstring(getActivity(), "groups_radio", "groups_radio_group", "exceptgroup_radio");
                    info_txt.setVisibility(8);
                    addcontact_btn.setVisibility(0);
                    selected_contact_list.setVisibility(0);
                    if (groupModelModels == null) {
                        return;
                    }
                    if (groupModelModels.size() > 0) {
                        selected_contact_list.setVisibility(0);
                        info_txt.setVisibility(8);
                        selected_contact_list.setAdapter(new GroupListAdapter(groupModelModels));
                        return;
                    }
                    info_txt.setVisibility(0);
                    info_txt.setText("Add group name to make list here");
                    selected_contact_list.setVisibility(8);
                    return;
                case R.id.mygroup_radio /*{ENCODED_INT: 2131231019}*/:
                    Preference.setstring(getActivity(), "groups_radio", "groups_radio_group", "mygroup_radio");
                    info_txt.setVisibility(8);
                    addcontact_btn.setVisibility(0);
                    selected_contact_list.setVisibility(0);
                    if (groupModelModels == null) {
                        return;
                    }
                    if (groupModelModels.size() > 0) {
                        selected_contact_list.setVisibility(0);
                        info_txt.setVisibility(8);
                        selected_contact_list.setAdapter(new GroupListAdapter(groupModelModels));
                        return;
                    }
                    info_txt.setVisibility(0);
                    info_txt.setText("Add group name to make list here");
                    selected_contact_list.setVisibility(8);
                    return;
                case R.id.nonegroup_radio /*{ENCODED_INT: 2131231036}*/:
                    Preference.setstring(getActivity(), "groups_radio", "groups_radio_group", "nonegroup_radio");
                    info_txt.setVisibility(0);
                    info_txt.setText("Auto response to none groups");
                    addcontact_btn.setVisibility(8);
                    selected_contact_list.setVisibility(8);
                    return;
                default:
            }
        });

        contacts_radio_group.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i) {
                case R.id.everyone_radio:
                    Preference.setstring(getActivity(), "contacts_group", "contacts_radio_group", "everyone_radio");
                    info_txt.setVisibility(0);
                    info_txt.setText("Auto response to everyone");
                    addcontact_btn.setVisibility(8);
                    selected_contact_list.setVisibility(8);
                    return;
                case R.id.exceptcontact_radio:
                    info_txt.setVisibility(8);
                    addcontact_btn.setVisibility(0);
                    selected_contact_list.setVisibility(0);
                    Preference.setstring(getActivity(), "contacts_group", "contacts_radio_group", "exceptcontact_radio");
                    if (contactModels.size() > 0) {
                        selected_contact_list.setVisibility(0);
                        info_txt.setVisibility(8);
                        selected_contact_list.setAdapter(new ContactListAdapter(contactModels));
                        return;
                    }
                    info_txt.setVisibility(0);
                    info_txt.setText("Select contacts to add here");
                    selected_contact_list.setVisibility(8);
                    return;
                case R.id.mycontact_radio:
                    info_txt.setVisibility(8);
                    addcontact_btn.setVisibility(0);
                    selected_contact_list.setVisibility(0);
                    Preference.setstring(getActivity(), "contacts_group", "contacts_radio_group", "mycontact_radio");
                    if (contactModels.size() > 0) {
                        selected_contact_list.setVisibility(0);
                        info_txt.setVisibility(8);
                        selected_contact_list.setAdapter(new ContactListAdapter(contactModels));
                        return;
                    }
                    info_txt.setVisibility(0);
                    info_txt.setText("Select contacts to add here");
                    selected_contact_list.setVisibility(8);
                    return;
                case R.id.nonecontact_radio:
                    Preference.setstring(getActivity(), "contacts_group", "contacts_radio_group", "nonecontact_radio");
                    info_txt.setVisibility(0);
                    info_txt.setText("Auto response to no one");
                    addcontact_btn.setVisibility(8);
                    selected_contact_list.setVisibility(8);
                    return;
                default:
            }
        });
        return view;
    }

    @SuppressLint("WrongConstant")
    public void checkingselection() {
        if (Preference.getstring(getActivity(), "main_group", "contacts_groups", "contacts_group").equals("contacts_group")) {
            contactModels = GetContactList();
            contacts_radio_group.setVisibility(0);
            groups_radio_group.setVisibility(8);
            list_info_text.setText("Contact list");
            switch (Preference.getstring(getActivity(), "contacts_group", "contacts_radio_group", "everyone_radio")) {
                case "everyone_radio":
                    contacts_radio_group.check(R.id.everyone_radio);
                    info_txt.setVisibility(0);
                    info_txt.setText("Auto response to everyone");
                    addcontact_btn.setVisibility(8);
                    selected_contact_list.setVisibility(8);
                    break;
                case "mycontact_radio":
                    contacts_radio_group.check(R.id.mycontact_radio);
                    info_txt.setVisibility(8);
                    addcontact_btn.setVisibility(0);
                    selected_contact_list.setVisibility(0);
                    if (contactModels.size() > 0) {
                        selected_contact_list.setVisibility(0);
                        info_txt.setVisibility(8);
                        selected_contact_list.setAdapter(new ContactListAdapter(contactModels));
                        return;
                    }
                    info_txt.setVisibility(0);
                    info_txt.setText("Select contacts to add here");
                    selected_contact_list.setVisibility(8);
                    break;
                case "exceptcontact_radio":
                    contacts_radio_group.check(R.id.exceptcontact_radio);
                    info_txt.setVisibility(8);
                    addcontact_btn.setVisibility(0);
                    selected_contact_list.setVisibility(0);
                    if (contactModels.size() > 0) {
                        selected_contact_list.setVisibility(0);
                        info_txt.setVisibility(8);
                        selected_contact_list.setAdapter(new ContactListAdapter(contactModels));
                        return;
                    }
                    info_txt.setVisibility(0);
                    info_txt.setText("Select contacts to add here");
                    selected_contact_list.setVisibility(8);
                    break;
                default:
                    contacts_radio_group.check(R.id.nonecontact_radio);
                    info_txt.setVisibility(0);
                    info_txt.setText("Auto response to no one");
                    addcontact_btn.setVisibility(8);
                    selected_contact_list.setVisibility(8);
                    break;
            }
        } else {
            groupModelModels = new ArrayList<>();
            groupModelModels = GetGroupList();
            list_info_text.setText("Group list");
            contacts_radio_group.setVisibility(8);
            groups_radio_group.setVisibility(0);
            if (Preference.getstring(getActivity(), "groups_radio", "groups_radio_group", "everyone_group").equals("everyone_group")) {
                groups_radio_group.check(R.id.everyone_group);
                info_txt.setVisibility(0);
                info_txt.setText("Auto response to every groups");
                addcontact_btn.setVisibility(8);
                selected_contact_list.setVisibility(8);
            } else if (Preference.getstring(getActivity(), "groups_radio", "groups_radio_group", "mygroup_radio").equals("mygroup_radio")) {
                groups_radio_group.check(R.id.mygroup_radio);
                info_txt.setVisibility(8);
                addcontact_btn.setVisibility(0);
                selected_contact_list.setVisibility(0);
                if (groupModelModels.size() > 0) {
                    selected_contact_list.setVisibility(0);
                    info_txt.setVisibility(8);
                    selected_contact_list.setAdapter(new GroupListAdapter(groupModelModels));
                    return;
                }
                info_txt.setVisibility(0);
                info_txt.setText("Add group name to make list here");
                selected_contact_list.setVisibility(8);
            } else if (Preference.getstring(getActivity(), "groups_radio", "groups_radio_group", "mygroup_radio").equals("exceptgroup_radio")) {
                groups_radio_group.check(R.id.exceptgroup_radio);
                info_txt.setVisibility(8);
                addcontact_btn.setVisibility(0);
                selected_contact_list.setVisibility(0);
                if (groupModelModels.size() > 0) {
                    selected_contact_list.setVisibility(0);
                    info_txt.setVisibility(8);
                    selected_contact_list.setAdapter(new GroupListAdapter(groupModelModels));
                    return;
                }
                info_txt.setVisibility(0);
                info_txt.setText("Add group name to make list here");
                selected_contact_list.setVisibility(8);
            } else {
                groups_radio_group.check(R.id.nonegroup_radio);
                info_txt.setVisibility(0);
                info_txt.setText("Auto response to every groups");
                addcontact_btn.setVisibility(8);
                selected_contact_list.setVisibility(8);
            }
        }
    }

    public ArrayList<ContactModelAuto> GetContactList() {
        Cursor allData = contactDatabaseHelper.getAllData();
        while (allData.moveToNext()) {
            contactModel = new ContactModelAuto();
            contactModel.setId(allData.getString(0));
            contactModel.setName(allData.getString(1));
            contactModels.add(contactModel);
        }
        return contactModels;
    }

    public ArrayList<com.whatscan.toolkit.forwa.GetSet.GroupModel> GetGroupList() {
        Cursor allData = groupDatabaseHelper.getAllData();
        groupModelModels = new ArrayList<>();
        while (allData.moveToNext()) {
            groupModel = new com.whatscan.toolkit.forwa.GetSet.GroupModel();
            groupModel.setId(allData.getString(0));
            groupModel.setName(allData.getString(1));
            groupModelModels.add(groupModel);
        }
        return groupModelModels;
    }


    public void askpermission(final int i) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), "android.permission.READ_CONTACTS")) {
            @SuppressLint("WrongConstant") AlertDialog create = new AlertDialog.Builder(getActivity()).setMessage(getString(R.string.apply_permission)).setPositiveButton(getString(R.string.open_settings), (dialogInterface, i1) -> {
                permission_alert.dismiss();
                Uri fromParts = Uri.fromParts("package", context.getPackageName(), null);
                Intent intent = new Intent();
                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.setData(fromParts);
                intent.addFlags(268435456);
                startActivityForResult(intent, i1);
            }).setNegativeButton(getString(R.string.cancel), (dialogInterface, i12) -> permission_alert.dismiss()).create();
            permission_alert = create;
            create.setCancelable(false);
            permission_alert.show();
            return;
        }
        requestPermissions(new String[]{"android.permission.READ_CONTACTS"}, i);
    }

    @Override
    public void onRequestPermissionsResult(int i, @NotNull String[] strArr, @NotNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (iArr.length > 0 && i == 103) {
            if (iArr[0] != 0) {
                recheckpermission();
            } else {
                startActivityForResult(new Intent(getActivity(), ActivityContactsPicker.class), CONTACT_PICK_REQUEST);
            }
        }
    }

    public void recheckpermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, "android.permission.READ_CONTACTS")) {
            Toast.makeText(getActivity(), "Please allow permission", Toast.LENGTH_SHORT).show();
            return;
        }
        @SuppressLint("WrongConstant") AlertDialog create = new AlertDialog.Builder(getActivity()).setMessage(getString(R.string.apply_permission)).setPositiveButton(getString(R.string.open_settings), (dialogInterface, i) -> {
            permission_alert.dismiss();
            Uri fromParts = Uri.fromParts("package", context.getPackageName(), null);
            Intent intent = new Intent();
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(fromParts);
            intent.addFlags(268435456);
            startActivityForResult(intent, 103);
        }).setNegativeButton(getString(R.string.cancel), (dialogInterface, i) -> permission_alert.dismiss()).create();
        permission_alert = create;
        create.setCancelable(false);
        permission_alert.show();
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == CONTACT_PICK_REQUEST) {
            contactModels = GetContactList();
            if (contactModels.size() > 0) {
                selected_contact_list.setVisibility(0);
                info_txt.setVisibility(8);
                selected_contact_list.setAdapter(new ContactListAdapter(contactModels));
                return;
            }
            info_txt.setVisibility(0);
            info_txt.setText("Select contacts to add here");
            selected_contact_list.setVisibility(8);
        }
    }

    public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ChatViewHolder> {
        ArrayList<ContactModelAuto> arrayList;
        ContactDatabaseHelper contactDatabaseHelper1;

        public ContactListAdapter(ArrayList<ContactModelAuto> arrayList2) {
            arrayList = arrayList2;
            contactDatabaseHelper1 = new ContactDatabaseHelper(getActivity());
        }

        @NonNull
        @Override
        public ChatViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ChatViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.selected_contact_item_layout, viewGroup, false));
        }

        public void onBindViewHolder(ChatViewHolder chatViewHolder, final int i) {
            if (Preference.getBooleanTheme(false)) {
                chatViewHolder.rlMain.setBackgroundColor(ContextCompat.getColor(context, R.color.colorShape));
                chatViewHolder.contactname.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            }

            chatViewHolder.contactname.setText(arrayList.get(chatViewHolder.getAbsoluteAdapterPosition()).getName());

            chatViewHolder.delete_btn.setOnClickListener(view -> {
                contactDatabaseHelper1 = new ContactDatabaseHelper(getActivity());
                contactDatabaseHelper1.deleteData(contactModels.get(chatViewHolder.getAbsoluteAdapterPosition()).getId());
                contactDatabaseHelper1.close();
                contactModels = new ArrayList<>();
                contactModels = GetContactList();
                if (contactModels.size() > 0) {
                    selected_contact_list.setVisibility(View.VISIBLE);
                    info_txt.setVisibility(View.GONE);
                    selected_contact_list.setAdapter(new ContactListAdapter(contactModels));
                    return;
                }
                info_txt.setVisibility(View.VISIBLE);
                info_txt.setText("Select contacts to add here");
                selected_contact_list.setVisibility(View.GONE);
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class ChatViewHolder extends RecyclerView.ViewHolder {
            TextView contactname;
            ImageView delete_btn;
            RelativeLayout rlMain;

            public ChatViewHolder(View view) {
                super(view);
                contactname = view.findViewById(R.id.contactname);
                delete_btn = view.findViewById(R.id.delete_btn);
                rlMain = view.findViewById(R.id.rlMain);
            }
        }
    }

    public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.ChatViewHolder> {
        GroupDatabaseHelper groupDatabaseHelper;
        ArrayList<com.whatscan.toolkit.forwa.GetSet.GroupModel> arrayList;

        public GroupListAdapter(ArrayList<com.whatscan.toolkit.forwa.GetSet.GroupModel> arrayList2) {
            arrayList = arrayList2;
            groupDatabaseHelper = new GroupDatabaseHelper(getActivity());
        }

        @NonNull
        @Override
        public ChatViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ChatViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.selected_group_item_layout, viewGroup, false));
        }

        public void onBindViewHolder(@NonNull ChatViewHolder chatViewHolder, final int i) {
            if (Preference.getBooleanTheme(false)) {
                chatViewHolder.rlMain.setBackgroundColor(ContextCompat.getColor(context, R.color.colorShape));
                chatViewHolder.contactname.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            }

            final com.whatscan.toolkit.forwa.GetSet.GroupModel groupModel = arrayList.get(i);
            chatViewHolder.contactname.setText(groupModel.getName());

            chatViewHolder.delete_btn.setOnClickListener(view -> {
                groupDatabaseHelper = new GroupDatabaseHelper(getActivity());
                groupDatabaseHelper.deleteData(groupModelModels.get(i).getId());
                groupDatabaseHelper.close();
                groupModelModels = new ArrayList<>();
                groupModelModels = GetGroupList();
                if (groupModelModels.size() > 0) {
                    selected_contact_list.setVisibility(View.VISIBLE);
                    info_txt.setVisibility(View.GONE);
                    selected_contact_list.setAdapter(new GroupListAdapter(groupModelModels));
                    return;
                }
                info_txt.setVisibility(View.VISIBLE);
                info_txt.setText("Add group name to make list here");
                selected_contact_list.setVisibility(View.GONE);
            });

            chatViewHolder.edit_btn.setOnClickListener(view -> {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
                View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.text_update_dialog, null);
                bottomSheetDialog.setContentView(inflate);

                RelativeLayout rlAutoReplay = inflate.findViewById(R.id.rlAutoReplay);
                TextView txt_header = inflate.findViewById(R.id.txt_header);
                EditText editText = inflate.findViewById(R.id.editreply_text);
                TextInputLayout text_input1 = inflate.findViewById(R.id.text_input1);

                if (Preference.getBooleanTheme(false)){
                    rlAutoReplay.setBackgroundColor(ContextCompat.getColor(context, R.color.darkBlack));
                    txt_header.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                    editText.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                    text_input1.setDefaultHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorWhite)));
                }

                ((TextView) inflate.findViewById(R.id.txt_header)).setText("Update group name");

                (inflate.findViewById(R.id.close_dialog)).setOnClickListener(view1 -> bottomSheetDialog.dismiss());

                editText.setText(groupModel.getName());
                editText.setSelection(editText.getText().length());
                (inflate.findViewById(R.id.updatebtn)).setOnClickListener(view12 -> {
                    if (editText.getText().toString().length() > 0) {
                        groupDatabaseHelper = new GroupDatabaseHelper(getActivity());
                        groupDatabaseHelper.updateData(groupModel.getId(), editText.getText().toString());
                        groupDatabaseHelper.close();
                        Toast.makeText(getActivity(), "Group name updated", Toast.LENGTH_SHORT).show();
                        groupModelModels = new ArrayList<>();
                        groupModelModels = GetGroupList();
                        if (groupModelModels.size() > 0) {
                            selected_contact_list.setVisibility(View.VISIBLE);
                            info_txt.setVisibility(View.GONE);
                            selected_contact_list.setAdapter(new GroupListAdapter(groupModelModels));
                        } else {
                            info_txt.setVisibility(View.VISIBLE);
                            info_txt.setText("Add group name to make list here");
                            selected_contact_list.setVisibility(View.GONE);
                        }
                        bottomSheetDialog.dismiss();
                        return;
                    }
                    Toast.makeText(getActivity(), "Add group name", Toast.LENGTH_SHORT).show();
                });

                bottomSheetDialog.show();
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class ChatViewHolder extends RecyclerView.ViewHolder {
            RelativeLayout rlMain;
            TextView contactname;
            ImageView delete_btn;
            ImageView edit_btn;

            public ChatViewHolder(View view) {
                super(view);
                rlMain = view.findViewById(R.id.rlMain);
                contactname = view.findViewById(R.id.contactname);
                delete_btn = view.findViewById(R.id.delete_btn);
                edit_btn = view.findViewById(R.id.edit_btn);
            }
        }
    }
}