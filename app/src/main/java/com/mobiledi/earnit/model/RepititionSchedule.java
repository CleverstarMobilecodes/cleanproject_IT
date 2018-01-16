package com.mobiledi.earnit.model;

import com.mobiledi.earnit.utils.Utils;

import java.io.Serializable;

/**
 * Created by praks on 07/07/17.
 */

public class RepititionSchedule implements Serializable {
    private int id;
    private Long expiryDate;
    private String repeat;

    public RepititionSchedule(){}

    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;

    }

    public Long getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Long expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getRepeat() {
        return Utils.checkIsNUll(repeat);
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }


}
