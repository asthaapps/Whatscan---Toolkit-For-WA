package com.whatscan.toolkit.forwa.BulkSender;

import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.Adapter.CountryCodeAdapter;
import com.whatscan.toolkit.forwa.GetSet.Country;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.DefaultTextWatcher;
import com.whatscan.toolkit.forwa.Util.Preference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class BulkActivityCountrySelection extends AppCompatActivity {
    public ImageView la_back;
    public TextView tv_toolbar;
    public RecyclerView recyclerView;
    public SearchView searchView;
    public EditText edSearch;
    public LinearLayoutManager linearLayoutManager;
    public ArrayList<Country> countryArrayList = new ArrayList<>();
    public CountryCodeAdapter countryCodeAdapter;
    public RelativeLayout rlCountry;
    public LinearLayout llSearch;
    public View ic_include;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Constant.adjustFontScale(getResources().getConfiguration(), BulkActivityCountrySelection.this);
        setContentView(R.layout.activity_country_selection);

        la_back = findViewById(R.id.la_back);
        tv_toolbar = findViewById(R.id.tv_toolbar);
        recyclerView = findViewById(R.id.recyclerview);
        searchView = findViewById(R.id.search_view);
        edSearch = findViewById(R.id.edSearch);
        rlCountry = findViewById(R.id.rlCountry);
        ic_include = findViewById(R.id.ic_include);
        llSearch = findViewById(R.id.llSearch);

        tv_toolbar.setText(Html.fromHtml("<small>" + getResources().getString(R.string.select_country) + "</small>"));

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rlCountry.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            llSearch.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_black));
            edSearch.setBackgroundColor(ContextCompat.getColor(this, R.color.colorShape));
            edSearch.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            edSearch.setHintTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        } else {
            setStatusBar();
            rlCountry.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            llSearch.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_white));
            edSearch.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            edSearch.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            edSearch.setHintTextColor(ContextCompat.getColor(this, R.color.colorBlack));
        }

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        countryCodeAdapter = new CountryCodeAdapter(this);
        recyclerView.setAdapter(countryCodeAdapter);

        searchView.setSearchableInfo(((SearchManager) getSystemService(Context.SEARCH_SERVICE)).getSearchableInfo(getComponentName()));
        searchView.setQueryHint(getString(R.string.search_country));
        SearchView.SearchAutoComplete searchAutoComplete = searchView.findViewById(R.id.search_src_text);

        edSearch.addTextChangedListener(new DefaultTextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (countryCodeAdapter == null) {
                    Toast.makeText(BulkActivityCountrySelection.this, "No Data Found..", Toast.LENGTH_SHORT).show();
                } else {
                    countryCodeAdapter.filter(String.valueOf(s));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        searchView.setOnQueryTextFocusChangeListener((view, z) -> {
            if (!z) {
                countryCodeAdapter.filter("");
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String str) {
                if (countryCodeAdapter == null) {
                    return false;
                }
                countryCodeAdapter.filter(str);
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String str) {
                return false;
            }
        });

        try {
            Field declaredField = TextView.class.getDeclaredField("mCursorDrawableRes");
            declaredField.setAccessible(true);
            declaredField.set(searchAutoComplete, 0);
        } catch (Exception unused) {
            unused.printStackTrace();
        }

        try {
            getCountryCodes();
        } catch (Exception e) {
            e.printStackTrace();
        }

        la_back.setOnClickListener(v -> onBackPressed());
    }

    private void getCountryCodes() throws Exception {
        try {
            JSONArray jSONArray = new JSONArray(loadJSONFromAsset());
            for (int i = 0; i < jSONArray.length(); i++) {
                try {
                    JSONObject jSONObject = jSONArray.getJSONObject(i);
                    Country country = new Country();
                    country.setCountryCode(jSONObject.getString("dial_code"));
                    country.setCountryName(jSONObject.getString("name"));
                    countryArrayList.add(country);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        countryCodeAdapter.setCountryList(countryArrayList);
        countryCodeAdapter.notifyDataSetChanged();
    }

    public String loadJSONFromAsset() {
        try {
            InputStream open = getAssets().open("CountryCodes.json");
            byte[] bArr = new byte[open.available()];
            open.read(bArr);
            open.close();
            return new String(bArr, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setStatusBar() {
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= 23) {
            window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorWhite));
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.colorWhite));
        } else if (Build.VERSION.SDK_INT == 21 || Build.VERSION.SDK_INT == 22) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorBlack));
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.colorBlack));
        } else {
            window.clearFlags(0);
        }
    }

    public void setStatusBarTheme() {
        View view = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.setSystemUiVisibility(view.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.darkBlack));
    }
}