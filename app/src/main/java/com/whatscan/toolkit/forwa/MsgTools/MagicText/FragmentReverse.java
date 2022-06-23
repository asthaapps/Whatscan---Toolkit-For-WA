package com.whatscan.toolkit.forwa.MsgTools.MagicText;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.whatscan.toolkit.forwa.Adapter.MagicTextAdapter;
import com.whatscan.toolkit.forwa.Api.ApiInterface;
import com.whatscan.toolkit.forwa.Api.RetroClient;
import com.whatscan.toolkit.forwa.GetSet.GetSetMagic;
import com.whatscan.toolkit.forwa.GetSet.ResponseDecorativeMagic;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentReverse extends Fragment {
    TextInputEditText txt_name;
    List<GetSetMagic> getSetMagics = new ArrayList<>();
    MagicTextAdapter magicTextAdapter;

    public FragmentReverse() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_magic, container, false);

        txt_name = view.findViewById(R.id.txt_name);
        RecyclerView rv_text = view.findViewById(R.id.rv_text);
        RelativeLayout rl_view = view.findViewById(R.id.rl_view);
        TextInputLayout ti_edit = view.findViewById(R.id.ti_edit);
        TextView txtNoData = view.findViewById(R.id.txtNoData);

        if (Preference.getBooleanTheme(false)) {
            ti_edit.setDefaultHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.colorWhite)));
            txt_name.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite));
            txtNoData.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite));
        }

        if (getSetMagics.size() == 0) {
            rl_view.setVisibility(View.VISIBLE);
        } else {
            rl_view.setVisibility(View.GONE);
        }

        txt_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                rl_view.setVisibility(View.VISIBLE);
                rv_text.setVisibility(View.GONE);
                ApiInterface apiInterface = RetroClient.getRetrofitInstance().create(ApiInterface.class);
                apiInterface.getDecorativeMagic(Preference.getFont_Changer(), String.valueOf(s)).enqueue(new Callback<ResponseDecorativeMagic>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseDecorativeMagic> call, @NotNull Response<ResponseDecorativeMagic> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            List<GetSetMagic> getSetMagics = response.body().getGetTextData();
                            getSetMagics = response.body().getGetTextData();
                            if (getSetMagics.size() == 0) {
                                rl_view.setVisibility(View.VISIBLE);
                                rv_text.setVisibility(View.GONE);
                            } else {
                                rl_view.setVisibility(View.GONE);
                                rv_text.setVisibility(View.VISIBLE);
                            }
                            rv_text.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                            magicTextAdapter = new MagicTextAdapter(getContext(), getSetMagics);
                            rv_text.setAdapter(magicTextAdapter);
                        } else {
                            Toast.makeText(getContext(), "Something wrong! try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseDecorativeMagic> call, @NotNull Throwable t) {
                        Toast.makeText(getContext(), "Something wrong! try again", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }
}