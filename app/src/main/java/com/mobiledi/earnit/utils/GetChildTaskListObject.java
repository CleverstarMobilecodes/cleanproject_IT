package com.mobiledi.earnit.utils;

import com.mobiledi.earnit.model.Child;
import com.mobiledi.earnit.model.ChildsTaskObject;
import com.mobiledi.earnit.model.Tasks;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mradul on 7/29/17.
 */

public class GetChildTaskListObject {
    List<ChildsTaskObject> childsTaskObjectList = new ArrayList<>();

    public List<ChildsTaskObject> getlist(Child childObject, String type){
        //Get Unique Dates

        for (Tasks task : childObject.getTasksArrayList()) {
            if(type.equals(AppConstant.CHILD)){
                if (task.getStatus().equals(AppConstant.DUE) || task.getStatus().equals(AppConstant.OVER_DUE) || task.getStatus().equals(AppConstant.DECLINED))
                    getObjectList(task);
            }else {
                if (!task.getStatus().equals(AppConstant.APPROVED))
                    getObjectList(task);
            }
        }
        return childsTaskObjectList;
    }

    public void getObjectList(Tasks task){
        boolean exists = false;
        for (ChildsTaskObject childsTaskObject : childsTaskObjectList) {
            if(new DateTime().withTimeAtStartOfDay().isAfter(new DateTime(task.getDueDate()).withTimeAtStartOfDay())){
                ArrayList<Tasks> taskList = childsTaskObject.getTasks();
                taskList.add(task);
                childsTaskObject.setTasks(taskList);
                exists = true;
                break;
            }else if (new DateTime(task.getDueDate()).withTimeAtStartOfDay()
                    .equals(new DateTime(childsTaskObject.getDueDate()).withTimeAtStartOfDay()))

            {
                ArrayList<Tasks> taskList = childsTaskObject.getTasks();
                taskList.add(task);
                childsTaskObject.setTasks(taskList);
                exists = true;
                break;

            }
        }
        if (!exists) {
            ChildsTaskObject childsTaskObject = new ChildsTaskObject();
            childsTaskObject.setDueDate(new DateTime(task.getDueDate()).withTimeAtStartOfDay().toString());
            ArrayList<Tasks> taskList = new ArrayList<>();
            taskList.add(task);
            childsTaskObject.setTasks(taskList);
            childsTaskObjectList.add(childsTaskObject);
        }
    }


}
