package com.whatscan.toolkit.forwa.DeletedMedia;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.DataBaseHelper.DBHelper;
import com.whatscan.toolkit.forwa.GetSet.NameModel;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.DefaultTextWatcher;
import com.whatscan.toolkit.forwa.Util.Preference;

import java.util.ArrayList;
import java.util.List;

public class FragmentChat extends Fragment {
    public static ActionMode actionMode;
    public DBHelper dbHelper;
    public RecyclerView rv_chat;
    public List<NameModel> nameModelList;
    public ActionModeCallback actionModeCallback;
    public AdapterChat adapterChat;
    public EditText edSearch;
    public SearchView searchView;
    public LinearLayout llSearch;

    private final BroadcastReceiver onNotice = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            adapterChat.notifyDataSetChanged();
        }
    };

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            updaterecycle();
        }
    };

    public FragmentChat() {
    }

    private void updaterecycle() {
        nameModelList.clear();
        for (int i = 0; i < dbHelper.getNames().size(); i++) {
            if (dbHelper.getNames().get(i).getName().substring(dbHelper.getNames().get(i).getName().lastIndexOf(" ") + 1).equalsIgnoreCase("Business") || dbHelper.getNames().get(i).getName().substring(dbHelper.getNames().get(i).getName().lastIndexOf(" ") + 1).equalsIgnoreCase("Whatsapp")) {
                nameModelList.add(dbHelper.getNames().get(i));
            }
        }
        adapterChat = new AdapterChat(requireActivity(), nameModelList);
        rv_chat.setAdapter(adapterChat);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        rv_chat = view.findViewById(R.id.rv_chat);
        edSearch = view.findViewById(R.id.edSearch);
        LottieAnimationView la_empty = view.findViewById(R.id.la_empty);
        TextView tv_empty = view.findViewById(R.id.tv_empty);
        searchView = view.findViewById(R.id.search_view);
        llSearch = view.findViewById(R.id.llSearch);

        actionModeCallback = new ActionModeCallback();
        dbHelper = new DBHelper(getActivity());
        nameModelList = new ArrayList<>();

        if (Preference.getBooleanTheme(false)) {
            llSearch.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.shape_black));
            edSearch.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorShape));
            edSearch.setHintTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite));
            edSearch.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite));
        } else {
            llSearch.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.shape_white));
            edSearch.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorWhite));
            edSearch.setHintTextColor(ContextCompat.getColor(requireContext(), R.color.colorBlack));
            edSearch.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorBlack));
        }

        for (int i = 0; i < dbHelper.getNames().size(); i++) {
            if (dbHelper.getNames().get(i).getName().substring(dbHelper.getNames().get(i).getName().lastIndexOf(" ") + 1).equalsIgnoreCase("Business") || dbHelper.getNames().get(i).getName().substring(dbHelper.getNames().get(i).getName().lastIndexOf(" ") + 1).equalsIgnoreCase("Whatsapp")) {
                nameModelList.add(dbHelper.getNames().get(i));
            }
        }

        if (nameModelList.isEmpty()) {
            la_empty.setVisibility(View.VISIBLE);
            tv_empty.setVisibility(View.VISIBLE);
            rv_chat.setVisibility(View.GONE);
            llSearch.setVisibility(View.GONE);
        } else {
            rv_chat.setVisibility(View.VISIBLE);
            la_empty.setVisibility(View.GONE);
            tv_empty.setVisibility(View.GONE);
            llSearch.setVisibility(View.VISIBLE);
            rv_chat.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapterChat = new AdapterChat(requireActivity(), nameModelList);
            rv_chat.setAdapter(adapterChat);
            LocalBroadcastManager.getInstance(requireContext()).registerReceiver(broadcastReceiver, new IntentFilter("updaterecycle"));
        }

        edSearch.addTextChangedListener(new DefaultTextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });

        return view;
    }

    private void filter(String text) {
        ArrayList<NameModel> filteredlist = new ArrayList<>();
        for (NameModel item : nameModelList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(getActivity(), "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            adapterChat.filterList(filteredlist);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(requireActivity()).unregisterReceiver(onNotice);
    }

    private class ActionModeCallback implements ActionMode.Callback {
        private ActionModeCallback() {
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            return true;
        }

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            adapterChat.clearSelections();
            FragmentChat.actionMode = null;
        }
    }
}