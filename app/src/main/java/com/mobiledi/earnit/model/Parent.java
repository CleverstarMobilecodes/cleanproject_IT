package com.mobiledi.earnit.model;

import com.mobiledi.earnit.utils.Utils;

import java.io.Serializable;

/**
 * Created by mradul on 7/5/17.
 */

public class Parent implements Serializable {
    int id;
    String firstName;
    String lastName;
    String email;
    String password;
    String avatar;
    String phone;
    String createDate;
    String updateDate;
    String userType;

    public String getFcmToken() {
        return Utils.checkIsNUll(fcmToken);
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    String fcmToken;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    Account account;

    public Parent() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return Utils.checkIsNUll(firstName);
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return Utils.checkIsNUll(lastName);
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return Utils.checkIsNUll(email);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return Utils.checkIsNUll(password);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return Utils.checkIsNUll(avatar);
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhone() {
        return Utils.checkIsNUll(phone);
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getUserType() {
        return Utils.checkIsNUll(userType);
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}