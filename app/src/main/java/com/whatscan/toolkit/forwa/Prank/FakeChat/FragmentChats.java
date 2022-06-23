package com.whatscan.toolkit.forwa.Prank.FakeChat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.DataBaseHelper.DatabaseHelper;
import com.whatscan.toolkit.forwa.R;

import java.util.ArrayList;

public class FragmentChats extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private ArrayList<UserDetails> arrayList;
    private DatabaseHelper databaseHelper;
    private UserAdapter userAdapter;
    private ListView userList;

    public FragmentChats() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        LottieAnimationView la_empty = view.findViewById(R.id.la_empty);
        ImageView iv_add = view.findViewById(R.id.iv_add);

        databaseHelper = new DatabaseHelper(getActivity());
        userList = view.findViewById(R.id.rl_chat);
        CallList();

        if (arrayList.isEmpty()) {
            la_empty.setVisibility(View.VISIBLE);
        } else {
            la_empty.setVisibility(View.GONE);
        }

        iv_add.setOnClickListener(v -> startActivity(new Intent(getActivity(), ActivityChatAdd.class), ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle()));
        return view;
    }

    private void CallList() {
        GetUserDetails();
        userList.setOnItemClickListener(this);
        userList.setOnItemLongClickListener(this);
    }

    private void GetUserDetails() {
        Cursor objCursor = databaseHelper.ViewUserList();
        arrayList = new ArrayList<>();
        objCursor.moveToFirst();
        for (int i = 0; i < objCursor.getCount(); i++) {
            int id = objCursor.getInt(objCursor.getColumnIndex("uid"));
            String name = objCursor.getString(objCursor.getColumnIndex("uname"));
            String status = objCursor.getString(objCursor.getColumnIndex("ustatus"));
            String typing = objCursor.getString(objCursor.getColumnIndex("utyping"));
            String online = objCursor.getString(objCursor.getColumnIndex("uonline"));
            byte[] blob = objCursor.getBlob(objCursor.getColumnIndex("uprofile"));
            UserDetails userDetails = new UserDetails();
            userDetails.setUid(id);
            userDetails.setUname(name);
            userDetails.setUstatus(status);
            userDetails.setUonline(online);
            userDetails.setUtyping(typing);
            userDetails.setBytes(blob);
            arrayList.add(userDetails);
            objCursor.moveToNext();
            userAdapter = new UserAdapter(requireContext(), arrayList);
            userList.setAdapter(userAdapter);
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getActivity(), ActivityFackUserChat.class);
        UserDetails userlist = arrayList.get(i);
        intent.putExtra("USER_ID", userlist.getUid());
        intent.putExtra("USER_NAME", userlist.getUname());
        intent.putExtra("USER_ONLINE", userlist.getUonline());
        intent.putExtra("USER_TYPING", userlist.getUtyping());
        intent.putExtra("USER_PROFILE", i);
        startActivity(intent);
    }

    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        CallDialog(i);
        return true;
    }

    private void CallDialog(final int i) {
        final UserDetails userDetails = arrayList.get(i);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(getString(R.string.delete_conversation));
        alertDialog.setMessage(getString(R.string.delete_conversation_desc));
        alertDialog.setPositiveButton(getString(R.string.yes), (dialog, which) -> {
            databaseHelper.DeleteUserProfile(userDetails.getUid());
            arrayList.remove(i);
            userAdapter.notifyDataSetChanged();
        });
        alertDialog.setNegativeButton(getString(R.string.no), new btnDialogNoListner());
        alertDialog.setNeutralButton(getString(R.string.rate_us), new btnDialogRateUsListner());
        alertDialog.show();
    }

    private static class btnDialogNoListner implements DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int which) {
        }
    }

    private class btnDialogRateUsListner implements DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int which) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + requireContext().getPackageName())));
        }
    }
}