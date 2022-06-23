package com.whatscan.toolkit.forwa.ReferCoin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.whatscan.toolkit.forwa.GetSet.CheckRedemptionList;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Preference;

import java.util.List;

public class RedemptionAdapter extends RecyclerView.Adapter<RedemptionAdapter.RedemptionHolder> {
    public Context context;
    public List<CheckRedemptionList> checkRedemptionLists;

    public RedemptionAdapter(Context context, List<CheckRedemptionList> list) {
        this.context = context;
        this.checkRedemptionLists = list;
    }

    @NonNull
    @Override
    public RedemptionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RedemptionHolder(LayoutInflater.from(context).inflate(R.layout.item_redemption, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RedemptionHolder holder, int i) {
        if (Preference.getBooleanTheme(false)) {
            holder.rlTop.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_redemption_w));
            holder.txtCouponName.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            holder.txtCouponDate.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            holder.txtAdFeaturs.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            holder.txtAdFeatursDay.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            holder.txtValidDate.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            holder.txtSeven.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            holder.txtEight.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            holder.txtNine.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            holder.txtTen.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            holder.imgOne.setImageResource(R.drawable.ic_ticket_new_w);
        }

        holder.txtCouponName.setText(checkRedemptionLists.get(i).getCoupon());
        holder.txtCouponDate.setText(checkRedemptionLists.get(i).getStartingDate());
        holder.txtValidDate.setText(checkRedemptionLists.get(i).getEndingDate());
        holder.txtAdFeaturs.setText(checkRedemptionLists.get(i).getCoupon());
        holder.txtRedeemCoin.setText("-" + checkRedemptionLists.get(i).getCoin());

        if (checkRedemptionLists.get(i).getCoupon().equals("Ads 1Day") || checkRedemptionLists.get(i).getCoupon().equals("Ads 7Day") ||
                checkRedemptionLists.get(i).getCoupon().equals("Ads 3Day") || checkRedemptionLists.get(i).getCoupon().equals("Ads 30Day")) {
            holder.imgAdFeaturs.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.d_no_ads));
            holder.txtAdFeatursDay.setText("Ad-Free Viewing for" + checkRedemptionLists.get(i).getDay() + " day");
        } else {
            switch (checkRedemptionLists.get(i).getCoupon()) {
                case "Bulk Sender 1Day":
                case "Bulk Sender 7Day":
                    holder.imgAdFeaturs.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.w_bulk));
                    holder.txtAdFeatursDay.setText("Use Tools for" + checkRedemptionLists.get(i).getDay() + " day");
                    holder.txtOne.setText(Html.fromHtml("• Valid on all section for whole application"));
                    holder.txtTwo.setText(Html.fromHtml("• Will expire after the indicated date"));
                    holder.txtThree.setText(Html.fromHtml("• After the coupon is redeemed successfully. you will automatically get the free use of above tools When using tools, you can use tool without any limitation"));
                    holder.txtFour.setText(Html.fromHtml("• add plan or membership on your login account to use free of cost using tools"));
                    holder.txtFive.setText(Html.fromHtml("• While redeeming only that tools free and no other tools"));
                    holder.txtSix.setText(Html.fromHtml("• All benefits like pro and paid user"));
                    break;
                case "Auto Response 1Day":
                case "Auto Response 7Day":
                    holder.imgAdFeaturs.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.w_auto));
                    holder.txtAdFeatursDay.setText("Use Tools for" + checkRedemptionLists.get(i).getDay() + " day");
                    holder.txtOne.setText(Html.fromHtml("• Valid on all section for whole application"));
                    holder.txtTwo.setText(Html.fromHtml("• Will expire after the indicated date"));
                    holder.txtThree.setText(Html.fromHtml("• After the coupon is redeemed successfully. you will automatically get the free use of above tools When using tools, you can use tool without any limitation"));
                    holder.txtFour.setText(Html.fromHtml("• add plan or membership on your login account to use free of cost using tools"));
                    holder.txtFive.setText(Html.fromHtml("• While redeeming only that tools free and no other tools"));
                    holder.txtSix.setText(Html.fromHtml("• All benefits like pro and paid user"));
                    break;
                case "Import excel 1Day":
                case "Import excel 7Day":
                    holder.imgAdFeaturs.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.w_export));
                    holder.txtAdFeatursDay.setText("Use Tools for" + checkRedemptionLists.get(i).getDay() + " day");
                    holder.txtOne.setText(Html.fromHtml("• Valid on all section for whole application"));
                    holder.txtTwo.setText(Html.fromHtml("• Will expire after the indicated date"));
                    holder.txtThree.setText(Html.fromHtml("• After the coupon is redeemed successfully. you will automatically get the free use of above tools When using tools, you can use tool without any limitation"));
                    holder.txtFour.setText(Html.fromHtml("• add plan or membership on your login account to use free of cost using tools"));
                    holder.txtFive.setText(Html.fromHtml("• While redeeming only that tools free and no other tools"));
                    holder.txtSix.setText(Html.fromHtml("• All benefits like pro and paid user"));
                    break;
            }
        }

        if (checkRedemptionLists.get(i).getExpire().equals("false")) {
            holder.txtActiveExpired.setText("Expired");
            holder.bt_active.setText("Expired");
            holder.bt_active.setBackground(ContextCompat.getDrawable(context, R.drawable.button_red));
        } else {
            holder.txtActiveExpired.setText("Activated");
        }

        holder.bt_active.setOnClickListener(v -> {
            if (!checkRedemptionLists.get(i).getExpire().equals("false")) {
                Toast.makeText(context, "Already activated!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return checkRedemptionLists.size();
    }

    public static class RedemptionHolder extends RecyclerView.ViewHolder {
        public RelativeLayout rlTop;
        public TextView txtCouponName, txtCouponDate, txtAdFeaturs, txtAdFeatursDay, txtActiveExpired;
        public TextView txtValidDate, txtRedeemCoin;
        public TextView txtOne, txtTwo, txtThree, txtFour, txtFive, txtSix, txtSeven, txtEight, txtNine, txtTen;
        public AppCompatButton bt_active;
        public ImageView imgOne, imgAdFeaturs;

        public RedemptionHolder(@NonNull View itemView) {
            super(itemView);
            rlTop = itemView.findViewById(R.id.rlTop);
            txtActiveExpired = itemView.findViewById(R.id.txtActiveExpired);
            txtCouponName = itemView.findViewById(R.id.txtCouponName);
            txtCouponDate = itemView.findViewById(R.id.txtCouponDate);
            txtAdFeaturs = itemView.findViewById(R.id.txtAdFeaturs);
            txtAdFeatursDay = itemView.findViewById(R.id.txtAdFeatursDay);
            txtValidDate = itemView.findViewById(R.id.txtValidDate);
            txtRedeemCoin = itemView.findViewById(R.id.txtRedeemCoin);
            imgAdFeaturs = itemView.findViewById(R.id.imgAdFeaturs);
            bt_active = itemView.findViewById(R.id.bt_active);
            imgOne = itemView.findViewById(R.id.imgOne);
            txtOne = itemView.findViewById(R.id.txtOne);
            txtTwo = itemView.findViewById(R.id.txtTwo);
            txtThree = itemView.findViewById(R.id.txtThree);
            txtFour = itemView.findViewById(R.id.txtFour);
            txtFive = itemView.findViewById(R.id.txtFive);
            txtSix = itemView.findViewById(R.id.txtSix);
            txtSeven = itemView.findViewById(R.id.txtSeven);
            txtEight = itemView.findViewById(R.id.txtEight);
            txtNine = itemView.findViewById(R.id.txtNine);
            txtTen = itemView.findViewById(R.id.txtTen);
        }
    }
}
