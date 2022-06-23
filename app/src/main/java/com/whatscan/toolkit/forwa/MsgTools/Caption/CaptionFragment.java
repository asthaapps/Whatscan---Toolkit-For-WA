package com.whatscan.toolkit.forwa.MsgTools.Caption;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import com.whatscan.toolkit.forwa.Api.ApiInterface;
import com.whatscan.toolkit.forwa.Api.RetroClient;
import com.whatscan.toolkit.forwa.BuildConfig;
import com.whatscan.toolkit.forwa.GetSet.CaptionCatagery;
import com.whatscan.toolkit.forwa.GetSet.CaptionData;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CaptionFragment extends Fragment {
    public RecyclerView cat_recyview;
    public CaptionStatusAdapter captionStatusAdapter;
    public List<CaptionCatagery> getCaptionData;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_caption, container, false);

        Constant.IntProgress(getContext());

        cat_recyview = view.findViewById(R.id.cat_recyview);
        cat_recyview.setLayoutManager(new GridLayoutManager(getContext(), 2));
        getCaptionData = new ArrayList<>();

        GetCateData();

        return view;
    }

    private void GetCateData() {
        if (getCaptionData.size() == 0) {
            Constant.ShowProgress();
        }
        ApiInterface apiInterface = RetroClient.getRetrofitInstance().create(ApiInterface.class);
        apiInterface.getCaptionCategory(Preference.getCaption_Cat(), "com.astha.whats.scan", BuildConfig.VERSION_CODE).enqueue(new Callback<CaptionData>() {
            @Override
            public void onResponse(@NotNull Call<CaptionData> call, @NotNull Response<CaptionData> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    Constant.DismissProgress();
                    getCaptionData = response.body().getCaptionCatagery();
                    captionStatusAdapter = new CaptionStatusAdapter(getContext(), getCaptionData);
                    cat_recyview.setAdapter(captionStatusAdapter);
                } else {
                    Toast.makeText(getActivity(), "Something wrong! try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<CaptionData> call, @NotNull Throwable t) {
                Constant.DismissProgress();
            }
        });
    }
}