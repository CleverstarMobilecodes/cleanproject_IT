package com.mobiledi.earnit.model;

import com.mobiledi.earnit.utils.Utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by praks on 07/07/17.
 */

public class Tasks implements Serializable {
    private int id;
    private int childId;
    private double allowance;
    private long createDate;
    private long dueDate;
    private String name;
    private ArrayList<TaskComment> taskComments;

    public ArrayList<TaskComment> getTaskComments() {
        return taskComments;
    }

    public void setTaskComments(ArrayList<TaskComment> taskComments) {
        this.taskComments = taskComments;
    }

    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    private Goal goal;

    public String getDetails() {
        return Utils.checkIsNUll(details);
    }

    public void setDetails(String details) {
        this.details = details;
    }

    private String details;
    private int pictureRequired;
    private String status;
    private long updateDate;

    public RepititionSchedule getRepititionSchedule() {
        return repititionSchedule;
    }

    public void setRepititionSchedule(RepititionSchedule repititionSchedule) {
        this.repititionSchedule = repititionSchedule;
    }

    private RepititionSchedule repititionSchedule;

    public Tasks() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAllowance() {
        return allowance;
    }

    public void setAllowance(double allowance) {
        this.allowance = allowance;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getDueDate() {
        return dueDate;
    }

    public String getDueDateAsString() {
        if(getDueDate() == 0){
            return null;
        }else {
            DateTime dt = new DateTime(getDueDate());
            DateTimeFormatter fmt = DateTimeFormat.forPattern("h:mm a");
            return fmt.print(dt);
        }
    }

    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }

    public String getName() {
        return Utils.checkIsNUll(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPictureRequired() {
        return pictureRequired;
    }

    public void setPictureRequired(int pictureRequired) {
        this.pictureRequired = pictureRequired;
    }

    public String getStatus() {
        return Utils.checkIsNUll(status);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(long updateDate) {
        this.updateDate = updateDate;
    }

    public int getChildId() {
        return childId;
    }

    public void setChildId(int childId) {
        this.childId = childId;
    }


}
