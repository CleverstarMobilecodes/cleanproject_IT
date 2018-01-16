package com.mobiledi.earnit.model;

import com.mobiledi.earnit.utils.Utils;

import java.io.Serializable;

/**
 * Created by mobile-di on 23/8/17.
 */

public class TaskComment implements Serializable {
    private int id;
    private int readStatus;
    private String comment;
    private long createDate;
    private long updateDate;
    private String pictureUrl;

    public long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(long updateDate) {
        this.updateDate = updateDate;
    }

    public TaskComment(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(int readStatus) {
        this.readStatus = readStatus;
    }

    public String getComment() {
        return Utils.checkIsNUll(comment);
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public String getPictureUrl() {
        return Utils.checkIsNUll(pictureUrl);
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
