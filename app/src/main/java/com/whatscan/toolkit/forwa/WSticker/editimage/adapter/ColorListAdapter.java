package com.whatscan.toolkit.forwa.WSticker.editimage.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.WSticker.editimage.fragment.PaintFragment;

import org.jetbrains.annotations.NotNull;

public class ColorListAdapter extends RecyclerView.Adapter<ViewHolder> {
    public static final int TYPE_COLOR = 1;
    public static final int TYPE_MORE = 2;
    public PaintFragment mContext;
    public int[] colorsData;
    public IColorListAction mCallback;
    public ColorListAdapter(PaintFragment frg, int[] colors, IColorListAction action) {
        super();
        this.mContext = frg;
        this.colorsData = colors;
        this.mCallback = action;
    }

    @Override
    public int getItemCount() {
        return colorsData.length + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return colorsData.length == position ? TYPE_MORE : TYPE_COLOR;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View v = null;
        ViewHolder viewHolder = null;
        if (viewType == TYPE_COLOR) {
            v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.view_color_panel, parent, false);
            viewHolder = new ColorViewHolder(v);
        } else if (viewType == TYPE_MORE) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_color_more_panel, parent, false);
            viewHolder = new MoreViewHolder(v);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {
        int type = getItemViewType(position);
        if (type == TYPE_COLOR) {
            onBindColorViewHolder((ColorViewHolder) holder, position);
        } else if (type == TYPE_MORE) {
            onBindColorMoreViewHolder((MoreViewHolder) holder, position);
        }
    }

    private void onBindColorViewHolder(final ColorViewHolder holder, final int position) {
        holder.colorPanelView.setBackgroundColor(colorsData[position]);
        holder.colorPanelView.setOnClickListener(v -> {
            if (mCallback != null) {
                mCallback.onColorSelected(position, colorsData[position]);
            }
        });
    }

    private void onBindColorMoreViewHolder(final MoreViewHolder holder, final int position) {
        holder.moreBtn.setOnClickListener(v -> {
            if (mCallback != null) {
                mCallback.onMoreSelected(position);
            }
        });
    }

    public interface IColorListAction {
        void onColorSelected(final int position, final int color);

        void onMoreSelected(final int position);
    }

    public static class ColorViewHolder extends ViewHolder {
        View colorPanelView;
        public ColorViewHolder(View itemView) {
            super(itemView);
            this.colorPanelView = itemView.findViewById(R.id.color_panel_view);
        }
    }

    public static class MoreViewHolder extends ViewHolder {
        View moreBtn;
        public MoreViewHolder(View itemView) {
            super(itemView);
            this.moreBtn = itemView.findViewById(R.id.color_panel_more);
        }
    }
}