package com.whatscan.toolkit.forwa.GetSet;

import androidx.annotation.Keep;

@Keep
public class Country {
    private String countryCode;
    private String countryName;

    public String getCountryCode() {
        return this.countryCode;
    }

    public void setCountryCode(String str) {
        this.countryCode = str;
    }

    public String getCountryName() {
        return this.countryName;
    }

    public void setCountryName(String str) {
        this.countryName = str;
    }
}
