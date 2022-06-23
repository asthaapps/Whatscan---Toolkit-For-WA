package com.whatscan.toolkit.forwa.WChatLocker;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.whatscan.toolkit.forwa.Adapter.ChatLockAdapter;
import com.whatscan.toolkit.forwa.DataBaseHelper.DatabaseHandler;
import com.whatscan.toolkit.forwa.GetSet.ChatLockModel;
import com.whatscan.toolkit.forwa.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FragmentChatLock extends Fragment {
    private static final String ARG_COLUMN_COUNT = "column-count";
    public static List<ChatLockModel> ITEMS = new ArrayList<>();
    public RecyclerView recyclerView;
    public ChatLockAdapter adapter;
    public DatabaseHandler db;
    public int mColumnCount = 1;
    public OnListFragmentInteractionListener mListener;

    public static FragmentChatLock newInstance(int i) {
        FragmentChatLock homeFragment = new FragmentChatLock();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_COLUMN_COUNT, i);
        homeFragment.setArguments(bundle);
        return homeFragment;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_chat_lock, viewGroup, false);

        recyclerView = inflate.findViewById(R.id.chatLockview);

        int i = mColumnCount;
        if (i <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), i));
        }

        ITEMS.clear();
        db = new DatabaseHandler(getActivity());
        ITEMS.addAll(db.getAllChatLock());
        adapter = new ChatLockAdapter(ITEMS, mListener);
        recyclerView.setAdapter(adapter);

        return inflate;
    }

    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        }
    }

    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(ChatLockModel chatLockModel);
    }
}