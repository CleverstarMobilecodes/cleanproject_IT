package com.mobiledi.earnit.adapter;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mobiledi.earnit.R;
import com.mobiledi.earnit.model.Child;
import com.mobiledi.earnit.model.Parent;
import com.mobiledi.earnit.model.Tasks;
import com.mobiledi.earnit.utils.AppConstant;
import com.mobiledi.earnit.utils.ScreenSwitch;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;

public class TaskAdapter extends BaseAdapter {

    ArrayList<Tasks> tasksList = new ArrayList<>();
    LayoutInflater inflater;
    Activity context;
    boolean isNone= false;
    Child childObject;
    Child otherTaskList;
    Parent parentObject;
    private final String TAG = "TaskAdapter";


    public TaskAdapter(Activity context, ArrayList<Tasks> tasksList, Child childObject, Child otherTaskList, Parent parentObject) {
        this.tasksList = tasksList;
        this.context = context;
        this.childObject = childObject;
        this.parentObject = parentObject;
        this.otherTaskList = otherTaskList;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        if(tasksList.size() == 0){
            isNone = true;
            return 1;
        }else{
            if (tasksList.size() <= 3){
                return tasksList.size();
            }
            else{
                return 4;
            }
        }
    }

    @Override
    public Tasks getItem(int position) {
        return tasksList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.task_view, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }
        if(isNone){
            mViewHolder.taskName.setText("None");
        }else if(position == 3){
            mViewHolder.taskName.setText("More..");
        }else{
            Tasks currentTask = getItem(position);
            String taskName = currentTask.getName();
            String task_name = taskName.substring(0, 1).toUpperCase() + taskName.substring(1);
            mViewHolder.taskName.setText(task_name);

            DateTime dt = new DateTime(currentTask.getDueDate());
            DateTimeFormatter fmt = DateTimeFormat.forPattern("h:mm a");
            String toPrintDate = fmt.print(dt);

            mViewHolder.taskDueDate.setText(toPrintDate);
        }


        mViewHolder.taskName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isNone){
                    new ScreenSwitch(context).moveToAllTaskScreen( childObject, otherTaskList, AppConstant.CHECKED_IN_TASK_APPROVAL__SCREEN, parentObject, AppConstant.PARENT_DASHBOARD);
                }
            }
        });

        return convertView;
    }

    private class MyViewHolder {
        TextView taskName, taskDueDate;

        public MyViewHolder(View item) {
            taskName = (TextView) item.findViewById(R.id.taskName);
            taskDueDate = (TextView) item.findViewById(R.id.childtaskName);
        }
    }
}