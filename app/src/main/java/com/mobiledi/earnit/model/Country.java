package com.mobiledi.earnit.model;

import java.io.Serializable;

/**
 * Created by mobile-di on 31/10/17.
 */

public class Country implements Serializable {
    String countryName;
    String countryDialCode;
    String countryCode;

    public Country(){}

    public void Country(String countryName, String countryDialCode, String countryCode){
        this.countryName = countryName;
        this.countryDialCode = countryDialCode;
        this.countryCode = countryCode;
    }
    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryDialCode() {
        return countryDialCode;
    }

    public void setCountryDialCode(String countryDialCode) {
        this.countryDialCode = countryDialCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
