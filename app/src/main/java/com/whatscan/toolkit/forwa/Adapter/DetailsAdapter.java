package com.whatscan.toolkit.forwa.Adapter;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.whatscan.toolkit.forwa.GetSet.Details;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.bumptech.glide.Glide;

import java.util.List;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.DetailsViewHolder> {
    public Context ctx;
    public List<Details> datalist;
    public OnItemClickListener listener;

    public DetailsAdapter(Context ctx, List<Details> datalist, OnItemClickListener listener) {
        this.ctx = ctx;
        this.datalist = datalist;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.item_details_cleaner, parent, false);
        return new DetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsViewHolder detailsViewHolder, int positions) {
        if (Preference.getBooleanTheme(false)) {
            detailsViewHolder.cardView.setCardBackgroundColor(ContextCompat.getColor(ctx, R.color.colorShape));
            detailsViewHolder.title.setTextColor(ContextCompat.getColor(ctx, R.color.colorWhite));
            detailsViewHolder.data.setTextColor(ContextCompat.getColor(ctx, R.color.colorWhite));
        } else {
            detailsViewHolder.cardView.setCardBackgroundColor(ContextCompat.getColor(ctx, R.color.colorWhite));
            detailsViewHolder.title.setTextColor(ContextCompat.getColor(ctx, R.color.colorBlack));
            detailsViewHolder.data.setTextColor(ContextCompat.getColor(ctx, R.color.colorBlack));
        }

        Details details = datalist.get(positions);
        detailsViewHolder.title.setText(details.getTitle());
        detailsViewHolder.data.setText(details.getData());
        Glide.with(ctx)
                .load(details.getImage())
                .transition(withCrossFade())
                .into(detailsViewHolder.image);

        detailsViewHolder.cardView.setOnClickListener(v -> {
            switch (detailsViewHolder.getAbsoluteAdapterPosition()) {
                case 0:
                    if (listener != null)
                        listener.onImagesClicked();
                    break;
                case 1:
                    if (listener != null)
                        listener.onDocumentsClicked();
                    break;
                case 2:
                    if (listener != null)
                        listener.onVideosClicked();
                    break;
                case 3:
                    if (listener != null)
                        listener.onStatusClicked();
                    break;
                case 4:
                    if (listener != null)
                        listener.onAudiosClicked();
                    break;
                case 5:
                    if (listener != null)
                        listener.onVoiceClicked();
                    break;
                case 6:
                    if (listener != null)
                        listener.onWallpapersClicked();
                    break;
                case 7:
                    if (listener != null)
                        listener.onGifsClicked();
                    break;
                default:
                    if (listener != null)
                        listener.onNonDefaultClicked(details.getPath());
                    break;
            }
        });
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }


    public interface OnItemClickListener {
        void onImagesClicked();

        void onDocumentsClicked();

        void onVideosClicked();

        void onStatusClicked();

        void onAudiosClicked();

        void onGifsClicked();

        void onWallpapersClicked();

        void onVoiceClicked();

        void onNonDefaultClicked(String path);
    }

    public static class DetailsViewHolder extends RecyclerView.ViewHolder {
        TextView title, data;
        ImageView image;
        CardView cardView;

        public DetailsViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            data = itemView.findViewById(R.id.data);
            image = itemView.findViewById(R.id.image);
            cardView = itemView.findViewById(R.id.recycler_view);
        }
    }
}