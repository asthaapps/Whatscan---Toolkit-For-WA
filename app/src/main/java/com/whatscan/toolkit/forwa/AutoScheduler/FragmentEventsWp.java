package com.whatscan.toolkit.forwa.AutoScheduler;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.whatscan.toolkit.forwa.DataBaseHelper.DBHelperScheduler;
import com.whatscan.toolkit.forwa.GetSet.EventModel;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.RecyclerTouchListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class FragmentEventsWp extends Fragment {
    public static EventsAdapterWp event_adapter;
    public static ArrayList<EventModel> eventlistforwp = new ArrayList<>();
    public RecyclerView all_event_list;
    public Date date1, date2;
    public DBHelperScheduler dbHelper;
    public RelativeLayout emptyview, main_menu;
    public RecyclerTouchListener onTouchListener;

    @SuppressLint({"NewApi", "SimpleDateFormat"})
    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_events_wp, viewGroup, false);

        all_event_list = inflate.findViewById(R.id.rv_all_eventsforwp);
        emptyview = inflate.findViewById(R.id.emptyview);
        main_menu = inflate.findViewById(R.id.all_event_relativelayout);

        dbHelper = new DBHelperScheduler(getActivity());
        eventlistforwp = dbHelper.getAllCotacts("com.whatsapp");

        if (eventlistforwp.size() == 0) {
            all_event_list.setVisibility(View.GONE);
            emptyview.setVisibility(View.VISIBLE);
        } else {
            all_event_list.setVisibility(View.VISIBLE);
            emptyview.setVisibility(View.GONE);
        }

        if (eventlistforwp != null && eventlistforwp.size() != 0) {
            Collections.sort(eventlistforwp, (event_model, event_model2) -> {
                String str = event_model2.getDate() + "-" + event_model2.getTime();
                try {
                    date1 = new SimpleDateFormat("dd-MM-yyyy-hh:mm").parse(event_model.getDate() + "-" + event_model.getTime());
                    date2 = new SimpleDateFormat("dd-MM-yyyy-hh:mm").parse(str);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return date1.compareTo(date2);
            });

            all_event_list.setVisibility(View.VISIBLE);
            event_adapter = new EventsAdapterWp(getActivity(), eventlistforwp);
            all_event_list.setLayoutManager(new LinearLayoutManager(getActivity()));
            all_event_list.setItemAnimator(new DefaultItemAnimator());
            all_event_list.setAdapter(event_adapter);
        }

        onTouchListener = new RecyclerTouchListener(getActivity(), all_event_list);
        all_event_list.addOnItemTouchListener(onTouchListener);
        onTouchListener.setClickable(new RecyclerTouchListener.OnRowClickListener() {
            public void onIndependentViewClicked(int i, int i2) {
            }

            public void onRowClicked(int i) {
                ActivityAllEventScheduler.sendcontactname = null;
                EventModel event_model = eventlistforwp.get(i);
                Intent intent = new Intent(getActivity(), ActivityScheduleUpdate.class);
                intent.putExtra("name", event_model.getName());
                intent.putExtra("dayofweek", event_model.getDay());
                intent.putExtra(DBHelperScheduler.EVENTS_COLUMN_DATE, event_model.getDate());
                intent.putExtra(DBHelperScheduler.EVENTS_COLUMN_TIME, event_model.getTime());
                intent.putExtra(DBHelperScheduler.EVENTS_COLUMN_MESSAGE, event_model.getMessage());
                intent.putExtra(DBHelperScheduler.EVENTS_COLUMN_ID, event_model.getId());
                intent.putExtra(DBHelperScheduler.EVENTS_COLUMN_REQUESTCODE, String.valueOf(event_model.getRequestcode()));
                intent.putExtra("PackageName", String.valueOf(event_model.getPackagename()));
                intent.putExtra("fragmentcode", 0);
                startActivity(intent);
            }
        }).setLongClickable(true, i -> {
        });

        return inflate;
    }

    @SuppressLint({"NewApi", "SimpleDateFormat"})
    @Override
    public void onStart() {
        super.onStart();
        eventlistforwp = dbHelper.getAllCotacts("com.whatsapp");
        if (eventlistforwp.size() == 0) {
            all_event_list.setVisibility(View.GONE);
            emptyview.setVisibility(View.VISIBLE);
        } else {
            all_event_list.setVisibility(View.VISIBLE);
            emptyview.setVisibility(View.GONE);
        }

        if (eventlistforwp != null && eventlistforwp.size() != 0) {
            if (ActivityAllEventScheduler.upswapdt) {
                Collections.sort(eventlistforwp, (event_model, event_model2) -> {
                    String str = event_model2.getDate() + "-" + event_model2.getTime();
                    try {
                        date1 = new SimpleDateFormat("dd-MM-yyyy-hh:mm").parse(event_model.getDate() + "-" + event_model.getTime());
                        date2 = new SimpleDateFormat("dd-MM-yyyy-hh:mm").parse(str);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return date2.compareTo(date1);
                });
            } else {
                Collections.sort(eventlistforwp, (event_model, event_model2) -> {
                    String str = event_model2.getDate() + "-" + event_model2.getTime();
                    try {
                        date1 = new SimpleDateFormat("dd-MM-yyyy-hh:mm").parse(event_model.getDate() + "-" + event_model.getTime());
                        date2 = new SimpleDateFormat("dd-MM-yyyy-hh:mm").parse(str);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return date1.compareTo(date2);
                });
            }
            all_event_list.setVisibility(View.VISIBLE);
            event_adapter = new EventsAdapterWp(getActivity(), eventlistforwp);
            all_event_list.setLayoutManager(new LinearLayoutManager(getActivity()));
            all_event_list.setItemAnimator(new DefaultItemAnimator());
            all_event_list.setAdapter(event_adapter);
        }
    }
}