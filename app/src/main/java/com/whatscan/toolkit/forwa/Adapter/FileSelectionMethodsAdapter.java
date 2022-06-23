package com.whatscan.toolkit.forwa.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Event;
import com.whatscan.toolkit.forwa.Util.Preference;

import java.util.ArrayList;
import java.util.List;

public class FileSelectionMethodsAdapter extends RecyclerView.Adapter<FileSelectionMethodsAdapter.ViewHolder> {
    public OnFileChooseMethodSelected mOnItemSelected;
    public List<ToolModel> mToolList = new ArrayList<>();
    Context context;

    public FileSelectionMethodsAdapter(Context context1, OnFileChooseMethodSelected onFileChooseMethodSelected) {
        context = context1;
        mOnItemSelected = onFileChooseMethodSelected;
        mToolList.add(new ToolModel(context.getString(R.string.images), R.drawable.ic_image, Event.IMAGE));
        mToolList.add(new ToolModel(context.getString(R.string.videos), R.drawable.ic_videocam, Event.VIDEO));
        mToolList.add(new ToolModel(context.getString(R.string.pdf), R.drawable.ic_pdf, Event.PDF));
        mToolList.add(new ToolModel(context.getString(R.string.audios), R.drawable.ic_headset, Event.AUDIO));
    }

    @Override
    public int getItemCount() {
        return mToolList.size();
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if (Preference.getBooleanTheme(false)){
            viewHolder.b.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        }
        viewHolder.b.setText(mToolList.get(i).mToolName);
        viewHolder.a.setImageResource(mToolList.get(i).mToolIcon);
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_file_selection_tool, viewGroup, false));
    }

    public interface OnFileChooseMethodSelected {
        void onFileSelectionClicked(Event fileType);
    }

    public static class ToolModel {
        public Event mFileType;
        public int mToolIcon;
        public String mToolName;

        ToolModel(String str, int i, Event fileType) {
            mToolName = str;
            mToolIcon = i;
            mFileType = fileType;
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView a;
        TextView b;

        ViewHolder(View view) {
            super(view);
            a = view.findViewById(R.id.imgToolIcon);
            b = view.findViewById(R.id.txtTool);

            view.setOnClickListener(view1 -> FileSelectionMethodsAdapter.this.mOnItemSelected.onFileSelectionClicked(FileSelectionMethodsAdapter.this.mToolList.get(ViewHolder.this.getLayoutPosition()).mFileType));
        }
    }
}
