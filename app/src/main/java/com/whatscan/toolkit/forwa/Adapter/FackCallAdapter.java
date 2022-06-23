package com.whatscan.toolkit.forwa.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.whatscan.toolkit.forwa.GetSet.StatusData;
import com.whatscan.toolkit.forwa.Prank.FakeChat.ActivityFackCallsShow;
import com.whatscan.toolkit.forwa.R;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FackCallAdapter extends RecyclerView.Adapter<FackCallAdapter.FackViewHolder> {
    public List<StatusData> statusDataArrayList;
    Context context;
    Activity activity;

    public FackCallAdapter(Context con, Activity activity1, List<StatusData> statusList) {
        context = con;
        activity = activity1;
        statusDataArrayList = statusList;
    }

    @NonNull
    @NotNull
    @Override
    public FackViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new FackViewHolder(LayoutInflater.from(context).inflate(R.layout.item_fack_call, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FackViewHolder holder, int position) {
        holder.tvName.setText(statusDataArrayList.get(position).getName());
        holder.tvTime.setText(statusDataArrayList.get(position).getTimeanddate());
        Glide.with(context).load(statusDataArrayList.get(position).getImageurl()).into(holder.profile_image);
        String profile_url = statusDataArrayList.get(position).getProfileurl();

        if (statusDataArrayList.get(position).getText().equals("a")) {
            holder.imgCallVideo.setImageResource(R.drawable.call);
        } else {
            holder.imgCallVideo.setImageResource(R.drawable.wp_video);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(activity, ActivityFackCallsShow.class);
            if (statusDataArrayList.get(position).getText().equals("a")) {
                intent.putExtra("NAME", statusDataArrayList.get(position).getName());
                intent.putExtra("PROFILEPIC", statusDataArrayList.get(position).getImageurl());
                intent.putExtra("TYPEONE", true);
            } else {
                intent.putExtra("NAMENEW", statusDataArrayList.get(position).getName());
                intent.putExtra("TYPETWO", true);
                intent.putExtra("PROFILEPIC", statusDataArrayList.get(position).getImageurl());
                intent.putExtra("PROFILEPICNEW", profile_url);
            }
            activity.startActivity(intent);
        });

        holder.itemView.setOnLongClickListener(v -> {
            statusDataArrayList.remove(position);
            SharedPreferences.Editor edit = context.getSharedPreferences("data", 0).edit();
            edit.putString("mycall", new Gson().toJson(statusDataArrayList));
            edit.apply();
            Toast.makeText(context, "Delete success", Toast.LENGTH_SHORT).show();
            notifyDataSetChanged();
            if (statusDataArrayList.size() == 0) {
                activity.finish();
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return statusDataArrayList.size();
    }

    public static class FackViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvTime;
        CircleImageView profile_image;
        ImageView imgCallVideo;

        public FackViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvTime = itemView.findViewById(R.id.tvTime);
            profile_image = itemView.findViewById(R.id.profile_image);
            imgCallVideo = itemView.findViewById(R.id.imgCallVideo);
        }
    }
}
