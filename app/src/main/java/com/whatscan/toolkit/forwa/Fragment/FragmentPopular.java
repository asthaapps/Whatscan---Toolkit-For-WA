package com.whatscan.toolkit.forwa.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.whatscan.toolkit.forwa.Ads.Advertisement;
import com.whatscan.toolkit.forwa.Cleaner.ActivityCleaner;
import com.whatscan.toolkit.forwa.Other.ActivityProfile;
import com.whatscan.toolkit.forwa.Other.ActivitySearch;
import com.whatscan.toolkit.forwa.Prank.FakeChat.ActivityFakeChat;
import com.whatscan.toolkit.forwa.Prank.FakeProfile.ActivityFakeProfile;
import com.whatscan.toolkit.forwa.Prank.FakeStory.ActivityFakeStory;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.User.ActivityLogin;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.WSticker.ActivitySticker;
import com.whatscan.toolkit.forwa.WSticker.ActivityStickerMakerList;

public class FragmentPopular extends Fragment {
    public Context context;
    public ImageView imgProfile;
    public TextView txtTitle, txtSearch, tv_cleaner, tv_text50, tv_sticker, tv_stickerM, tv_fake_story, tv_fake_chat, tv_fake_profile;
    public LinearLayout ll_cleaner, llSearch, llFCPS, iv_search;
    public RelativeLayout cardSticker, cardStickerCreate, llMain;
    public RelativeLayout card_fakeChat, card_fakeProfile, card_fakeStory,cvProfile;
    FrameLayout fl_native;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewP = inflater.inflate(R.layout.fragment_popular, container, false);

        context = getContext();
        llMain = viewP.findViewById(R.id.llMain);
        ll_cleaner = viewP.findViewById(R.id.ll_cleaner);
        llFCPS = viewP.findViewById(R.id.llFCPS);
        card_fakeChat = viewP.findViewById(R.id.card_fakeChat);
        card_fakeProfile = viewP.findViewById(R.id.card_fakeProfile);
        card_fakeStory = viewP.findViewById(R.id.card_fakeStory);
        cardSticker = viewP.findViewById(R.id.cardSticker);
        cardStickerCreate = viewP.findViewById(R.id.cardStickerCreate);
        imgProfile = viewP.findViewById(R.id.imgProfile);
        llSearch = viewP.findViewById(R.id.llSearch);
        iv_search = viewP.findViewById(R.id.iv_search);
        txtTitle = viewP.findViewById(R.id.txtTitle);
        txtSearch = viewP.findViewById(R.id.txtSearch);
        tv_cleaner = viewP.findViewById(R.id.tv_cleaner);
        tv_text50 = viewP.findViewById(R.id.tv_text50);
        tv_sticker = viewP.findViewById(R.id.tv_sticker);
        tv_stickerM = viewP.findViewById(R.id.tv_stickerM);
        tv_fake_story = viewP.findViewById(R.id.tv_fake_story);
        tv_fake_chat = viewP.findViewById(R.id.tv_fake_chat);
        tv_fake_profile = viewP.findViewById(R.id.tv_fake_profile);
        cvProfile = viewP.findViewById(R.id.cvProfile);

        TextView tv_username = viewP.findViewById(R.id.tv_username);
        if (!Preference.getUserName().isEmpty()) {
            tv_username.setText(Preference.getUserName());
        } else {
            tv_username.setVisibility(View.GONE);
        }

        fl_native = viewP.findViewById(R.id.fl_native);
        Advertisement.showNativeAds(getActivity(), fl_native);

        if (Preference.getProfile().isEmpty()) {
            Glide.with(context).load(R.drawable.ic_profile).placeholder(R.drawable.ic_profile).into(imgProfile);
        } else {
            Glide.with(context).load(Preference.getProfile()).placeholder(R.drawable.ic_profile).into(imgProfile);
        }

        if (Preference.getBooleanTheme(false)) {
            llMain.setBackgroundColor(ContextCompat.getColor(context, R.color.darkBlack));
            llSearch.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_black));
            iv_search.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_black));
            ll_cleaner.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_black));
            cvProfile.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_black));
            cardSticker.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_black));
            cardStickerCreate.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_black));
            card_fakeChat.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_black));
            card_fakeProfile.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_black));
            card_fakeStory.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_black));

            txtTitle.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtSearch.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_cleaner.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_text50.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_sticker.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_stickerM.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_fake_story.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_fake_profile.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_fake_chat.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_username.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        } else {
            llMain.setBackgroundColor(ContextCompat.getColor(context, R.color.card_color));
            llSearch.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_white));
            iv_search.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_white));
            ll_cleaner.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_white));
            cvProfile.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_white));
            card_fakeChat.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_white));
            card_fakeProfile.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_white));
            card_fakeStory.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_white));
            cardSticker.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_sticker));
            cardStickerCreate.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_maker));
            txtTitle.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            txtSearch.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            tv_cleaner.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            tv_text50.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            tv_sticker.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            tv_stickerM.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            tv_fake_story.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            tv_fake_profile.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            tv_fake_chat.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            tv_username.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
        }

        llSearch.setOnClickListener(v -> startActivity(new Intent(context, ActivitySearch.class), ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle()));
        iv_search.setOnClickListener(v -> startActivity(new Intent(context, ActivitySearch.class), ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle()));

        imgProfile.setOnClickListener(v -> {
            if (Preference.getLogin_status().equals("Yes")) {
                startActivity(new Intent(context, ActivityProfile.class), ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle());
            } else {
                startActivity(new Intent(context, ActivityLogin.class), ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle());
            }
        });

        ll_cleaner.setOnClickListener(v -> {
            Advertisement.getInstance((Activity) context).showFullAds(() -> CleanerIntent(v));
        });

        card_fakeChat.setOnClickListener(view -> {
            Advertisement.getInstance((Activity) context).showFullAds(() -> CardFackChatIntent(view));
        });

        card_fakeProfile.setOnClickListener(view -> {
            Advertisement.getInstance((Activity) context).showFullAds(() -> CardFackProfileIntent(view));
        });

        card_fakeStory.setOnClickListener(view -> {
            Advertisement.getInstance((Activity) context).showFullAds(() -> CardFackStoryIntent(view));
        });

        cardSticker.setOnClickListener(view -> {
            Advertisement.getInstance((Activity) context).showFullAds(() -> CardStickerIntent(view));
        });

        cardStickerCreate.setOnClickListener(view -> startActivity(new Intent(context, ActivityStickerMakerList.class), ActivityOptionsCompat.makeScaleUpAnimation(view, (int) view.getX(), (int) view.getY(), view.getWidth(), view.getHeight()).toBundle()));

        return viewP;
    }

    private void CleanerIntent(View v) {
        startActivity(new Intent(context, ActivityCleaner.class), ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle());
    }

    private void CardFackStoryIntent(View view) {
        startActivity(new Intent(context, ActivityFakeStory.class), ActivityOptionsCompat.makeScaleUpAnimation(view, (int) view.getX(), (int) view.getY(), view.getWidth(), view.getHeight()).toBundle());
    }

    private void CardFackProfileIntent(View view) {
        startActivity(new Intent(context, ActivityFakeProfile.class), ActivityOptionsCompat.makeScaleUpAnimation(view, (int) view.getX(), (int) view.getY(), view.getWidth(), view.getHeight()).toBundle());
    }

    private void CardFackChatIntent(View view) {
        startActivity(new Intent(context, ActivityFakeChat.class), ActivityOptionsCompat.makeScaleUpAnimation(view, (int) view.getX(), (int) view.getY(), view.getWidth(), view.getHeight()).toBundle());
    }

    private void CardStickerIntent(View view) {
        startActivity(new Intent(context, ActivitySticker.class), ActivityOptionsCompat.makeScaleUpAnimation(view, (int) view.getX(), (int) view.getY(), view.getWidth(), view.getHeight()).toBundle());
    }
}
