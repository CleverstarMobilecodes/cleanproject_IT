package com.mobiledi.earnit.utils;

import com.mobiledi.earnit.model.Account;
import com.mobiledi.earnit.model.Child;
import com.mobiledi.earnit.model.ChildsTaskObject;
import com.mobiledi.earnit.model.Goal;
import com.mobiledi.earnit.model.Parent;
import com.mobiledi.earnit.model.RepititionSchedule;
import com.mobiledi.earnit.model.TaskComment;
import com.mobiledi.earnit.model.Tasks;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by mobile-di on 26/8/17.
 */

public class GetObjectFromResponse {
    public static final String TAG = "GetObjectFromResponse";
    public static  String PENDING_APPROVAL_DATE;
    public static  String PAST_DUE_DATE;

    public Parent getParentObject(JSONObject response) {

        Parent parent = new Parent();
        try {
            parent.setId(response.getInt(AppConstant.ID));
            parent.setAvatar(response.getString(AppConstant.AVATAR));
            parent.setFirstName(response.getString(AppConstant.FIRST_NAME));
            parent.setLastName(response.getString(AppConstant.LAST_NAME));
            parent.setEmail(response.getString(AppConstant.EMAIL));
            parent.setPassword(response.getString(AppConstant.PASSWORD));
            parent.setUserType(response.getString(AppConstant.TYPE));
            parent.setPhone(response.getString(AppConstant.PHONE));
            parent.setFcmToken(response.getString(AppConstant.FCM_TOKEN));


            JSONObject account = response.getJSONObject(AppConstant.ACCOUNT);
            Account parentAcount = getAccountObject(account);
            parent.setAccount(parentAcount);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return parent;
    }

    public Child getChildObject(JSONObject response) {
        Child child = new Child();
        try {
            child.setId(response.getInt(AppConstant.ID));
            child.setAvatar(response.getString(AppConstant.AVATAR));
            child.setFirstName(response.getString(AppConstant.FIRST_NAME));
            child.setLastName(response.getString(AppConstant.LAST_NAME));
            child.setEmail(response.getString(AppConstant.EMAIL));
            child.setPassword(response.getString(AppConstant.PASSWORD));
            child.setFcmToken(response.getString(AppConstant.FCM_TOKEN));
            child.setMessage(response.getString(AppConstant.MESSAGE));
            child.setPhone(response.getString(AppConstant.PHONE));
            child.setUserType(response.getString(AppConstant.TYPE));
            JSONObject account = response.getJSONObject(AppConstant.ACCOUNT);
            Account childAccount = getAccountObject(account);
            child.setAccount(childAccount);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return child;
    }

    public Tasks getTaskObject(JSONObject taskObject, int childId) {
        Tasks task = new Tasks();
        try {
            task.setId(taskObject.getInt(AppConstant.ID));
            task.setChildId(childId);
            task.setName(taskObject.getString(AppConstant.NAME));
            task.setDueDate(taskObject.getLong(AppConstant.DUE_DATE));
            task.setCreateDate(taskObject.getLong(AppConstant.CREATE_DATE));
            task.setStatus(taskObject.getString(AppConstant.STATUS));
            task.setAllowance(taskObject.getDouble(AppConstant.ALLOWANCE));
            task.setDetails(taskObject.getString(AppConstant.DESCRIPTION));
            if (taskObject.get(AppConstant.PICTURE_REQUIRED) instanceof Integer)
                task.setPictureRequired(taskObject.getInt(AppConstant.PICTURE_REQUIRED));
            else
                task.setPictureRequired(0);

            if (taskObject.has(AppConstant.GOAL)) {
                if (!taskObject.isNull(AppConstant.GOAL)) {
                    JSONObject goalObject = taskObject.getJSONObject(AppConstant.GOAL);
                    Goal goal = getGoalObject(goalObject);
                    task.setGoal(goal);
                }
            }

            if(taskObject.has(AppConstant.REPITITION_SCHEDULE)){
                if(!taskObject.isNull(AppConstant.REPITITION_SCHEDULE)){
                    JSONObject scheduleObject = taskObject.getJSONObject(AppConstant.REPITITION_SCHEDULE);
                    RepititionSchedule schedule = getRepititionSchedule(scheduleObject);
                    task.setRepititionSchedule(schedule);
                }
            }

            JSONArray taskCommentArray = taskObject.getJSONArray(AppConstant.TASK_COMMENTS);
            ArrayList<TaskComment> comments = new ArrayList<TaskComment>();
            if (taskCommentArray != null && taskCommentArray.length() > 0) {
                for (int j = 0; j < taskCommentArray.length(); j++) {
                    JSONObject singleComment = taskCommentArray.getJSONObject(j);
                    TaskComment comment = getCommentObject(singleComment);
                    comments.add(comment);
                }
            } else {
                Utils.logDebug(TAG, "Task comment not available");
            }
            task.setTaskComments(comments);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return task;
    }

    public Goal getGoalObject(JSONObject goalObject) {
        Goal goal = new Goal();
        try {
            if(goalObject.get(AppConstant.ID) instanceof Integer)
                goal.setId(goalObject.getInt(AppConstant.ID));
            else
                goal.setId(0);
            goal.setAmount(goalObject.getInt(AppConstant.AMOUNT));
            goal.setGoalName(goalObject.getString(AppConstant.GOAL_NAME));
            goal.setTally(goalObject.getInt(AppConstant.TALLY));
            goal.setTallyPercent(Float.parseFloat(String.valueOf(goalObject.getDouble(AppConstant.TALLY_PERCENT))));
            goal.setCash(goalObject.getInt(AppConstant.CASH_BALANCE));
//            goal.setCreateDate(goalObject.getLong(AppConstant.CREATE_DATE));
//            goal.setUpdateDate(goalObject.getLong(AppConstant.UPDATE_DATE));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return goal;
    }

    public RepititionSchedule getRepititionSchedule(JSONObject scheduleObject) {

        RepititionSchedule schedule = new RepititionSchedule();
        try {
            schedule.setId(scheduleObject.getInt(AppConstant.ID));
            schedule.setRepeat(scheduleObject.getString(AppConstant.REPEAT));
//            schedule.setExpiryDate(scheduleObject.getLong(AppConstant.EXPIRY_DATE));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return schedule;
    }

    public Account getAccountObject(JSONObject account) {
        Account childAccount = new Account();
        try {

            childAccount.setId(account.getInt(AppConstant.ID));
            childAccount.setAccountCode(account.getString(AppConstant.ACCOUNT_CODE));
            childAccount.setCreateDate(account.getLong(AppConstant.CREATE_DATE));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return childAccount;
    }

    public TaskComment getCommentObject(JSONObject singleComment) {
        TaskComment comment = new TaskComment();
        try {
            comment.setId(singleComment.getInt(AppConstant.ID));
            comment.setComment(singleComment.getString(AppConstant.COMMENT));
            comment.setPictureUrl(singleComment.getString(AppConstant.PICTURE_URL));
            comment.setReadStatus(singleComment.getInt(AppConstant.READ_STATUS));
//            comment.setCreateDate(singleComment.getLong(AppConstant.CREATE_DATE));
//            comment.setUpdateDate(singleComment.getLong(AppConstant.UPDATE_DATE));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return comment;
    }

    List<ChildsTaskObject> childsTaskObjectList = new ArrayList<>();

    Map<String, ArrayList<Tasks>> map = new TreeMap<>();

    public List<ChildsTaskObject> getChildTaskListObject(Child childObject, String type, String screen) {
        //Get Unique Dates
        PENDING_APPROVAL_DATE = new DateTime().plusDays(-2).withTimeAtStartOfDay().toString();
        PAST_DUE_DATE = new DateTime().plusDays(-1).withTimeAtStartOfDay().toString();
        for (Tasks task : childObject.getTasksArrayList()) {
            String key = new DateTime(task.getDueDate()).withTimeAtStartOfDay().toString();
            if(new DateTime().withTimeAtStartOfDay().isAfter(new DateTime(task.getDueDate()).withTimeAtStartOfDay())){
                key = PAST_DUE_DATE;
            }if (!task.getStatus().equals(AppConstant.APPROVED)) {
                 if (task.getStatus().equalsIgnoreCase(AppConstant.COMPLETED) && screen.equalsIgnoreCase(AppConstant.CHECKED_IN_SCREEN))
                    addToMap(task, PENDING_APPROVAL_DATE, screen);
                addToMap(task, key, screen);
            }
        }

        for(String key : map.keySet()){
            childsTaskObjectList.add(new ChildsTaskObject(key, map.get(key)));
        }
        return childsTaskObjectList;
    }

    private void addToMap(Tasks task, String key, String screen) {
        if(!key.equals(PENDING_APPROVAL_DATE) && task.getStatus().equalsIgnoreCase(AppConstant.COMPLETED) && screen.equalsIgnoreCase(AppConstant.CHECKED_IN_SCREEN))
            return;

        if (!map.containsKey(key))
            map.put(key, new ArrayList<Tasks>());
        map.get(key).add(task);
    }

    public void getObjectList(Tasks task) {
        boolean exists = false;
        for (ChildsTaskObject childsTaskObject : childsTaskObjectList) {
            if (new DateTime().withTimeAtStartOfDay().isAfter(new DateTime(task.getDueDate()).withTimeAtStartOfDay())) {
                ArrayList<Tasks> taskList = childsTaskObject.getTasks();
                taskList.add(task);
                childsTaskObject.setTasks(taskList);
                exists = true;
                break;
            } else if (new DateTime(task.getDueDate()).withTimeAtStartOfDay()
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
