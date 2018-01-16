package com.mobiledi.earnit.model;

import com.mobiledi.earnit.utils.Utils;

import java.io.Serializable;

/**
 * Created by mradul on 8/5/17.
 */

public class Account implements Serializable {
    private int id;
    private String accountCode;
    private long createDate;

    public Account(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccountCode() {
        return Utils.checkIsNUll(accountCode);
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }
}
