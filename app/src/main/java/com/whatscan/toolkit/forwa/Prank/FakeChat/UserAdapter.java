package com.whatscan.toolkit.forwa.Prank.FakeChat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.whatscan.toolkit.forwa.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

class UserAdapter extends BaseAdapter {
    public static ArrayList<UserDetails> userdetails;
    public Context context;
    public LayoutInflater inflater;

    UserAdapter(Context context, ArrayList<UserDetails> userdetailss) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        userdetails = userdetailss;
    }

    public int getCount() {
        return userdetails.size();
    }

    public Object getItem(int i) {
        return i;
    }

    public long getItemId(int i) {
        return i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        UserDetails userlist = userdetails.get(i);
        Holder holder = new Holder();
        @SuppressLint("ViewHolder") View rowView = this.inflater.inflate(R.layout.item_fake_chat, null);

        holder.username = rowView.findViewById(R.id.tv_username);
        holder.visibility = rowView.findViewById(R.id.tv_message);
        holder.img = rowView.findViewById(R.id.avatar_user);
        holder.tv_chat_time = rowView.findViewById(R.id.tv_chat_time);
        holder.username.setText(userlist.getUname());
        if (userlist.getUtyping().equals("typing")) {
            holder.visibility.setText(Html.fromHtml("typing..."));
        } else {
            holder.visibility.setText("");
        }
        holder.img.setImageBitmap(getImagefromdatabase(userlist.getBytes()));

        holder.tv_chat_time.setText(new SimpleDateFormat("HH:mm", Locale.US).format(new Date()));
        return rowView;
    }

    private Bitmap getImagefromdatabase(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static class Holder {
        CircleImageView img;
        TextView username;
        TextView visibility;
        TextView tv_chat_time;
    }
}