package com.mobiledi.earnit.model;

import com.mobiledi.earnit.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by praks on 07/07/17.
 */

public class Child implements Serializable {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String avatar;
    private String phone;
    private String password;
    private long createDate;
    private long updateDate;
    private String message;
    private String fcmToken;

    public String getUserType() {
        return Utils.checkIsNUll(userType);
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    private String userType;
    Account account;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getFcmToken() {
        return Utils.checkIsNUll(fcmToken);
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    private ArrayList<Tasks> tasksArrayList;

    public Child() {
    }

    public Child(int id, String firstName, String lastName, String email, String avatar, String phone, long createDate, ArrayList<Tasks> tasksArrayList, long updateDate, String message) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.avatar = avatar;
        this.phone = phone;
        this.createDate = createDate;
        this.tasksArrayList = tasksArrayList;
        this.updateDate = updateDate;
        this.message = message;
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

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(long updateDate) {
        this.updateDate = updateDate;
    }

    public String getMessage() {
        return Utils.checkIsNUll(message);
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPassword() {
        return Utils.checkIsNUll(password);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Tasks> getTasksArrayList() {
        return tasksArrayList;
    }

    public void setTasksArrayList(ArrayList<Tasks> tasksArrayList) {
        this.tasksArrayList = tasksArrayList;
    }



}
