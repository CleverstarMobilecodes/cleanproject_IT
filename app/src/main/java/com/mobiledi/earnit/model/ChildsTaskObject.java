package com.mobiledi.earnit.model;

import org.joda.time.DateTime;

import java.io.DataInputStream;
import java.util.ArrayList;

/**
 * Created by praks on 13/07/17.
 */

public class ChildsTaskObject {
    String dueDate;
    ArrayList<Tasks> tasks;

    public ChildsTaskObject(){

    }

    public ChildsTaskObject(String dueDate, ArrayList<Tasks> tasks) {
        this.dueDate = dueDate;
        this.tasks = tasks;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public ArrayList<Tasks> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Tasks> tasks) {
        this.tasks = tasks;
    }
}
