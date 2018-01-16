package com.mobiledi.earnit.adapter;


import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.mobiledi.earnit.R;
import com.mobiledi.earnit.activity.AddTask;
import com.mobiledi.earnit.model.Child;
import com.mobiledi.earnit.model.ChildsTaskObject;
import com.mobiledi.earnit.model.Parent;
import com.mobiledi.earnit.model.Tasks;
import com.mobiledi.earnit.utils.AppConstant;
import com.mobiledi.earnit.utils.ScreenSwitch;
import com.mobiledi.earnit.utils.Utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

public class ChildViewTaskAdapter extends BaseAdapter {

    ArrayList<Tasks> tasksList = new ArrayList<>();
    Child childObject;
    Child otherChild;
    Parent parentobject;
    LayoutInflater inflater;
    Activity context;
    List<ChildsTaskObject> childsTaskObjectList;
    ChildViewDateAdapter childViewDateAdapter;
    int parentPosition;
    private String userType;
    private String onScreen;
    RelativeLayout progress;
    final String TAG = "ChildViewTaskAdapter";


    public ChildViewTaskAdapter(RelativeLayout progress, Activity context, List<ChildsTaskObject> childsTaskObjectList, ChildViewDateAdapter childViewDateAdapter, int parentPosition, Child childObject, Parent parentobject, String type, String onScreen, Child otherChild) {
        this.parentPosition = parentPosition;
        this.tasksList = childsTaskObjectList.get(parentPosition).getTasks();
        this.parentobject = parentobject;
        this.context = context;
        this.childObject = childObject;
        this.otherChild = otherChild;
        this.childsTaskObjectList = childsTaskObjectList;
        this.childViewDateAdapter = childViewDateAdapter;
        userType = type;
        inflater = LayoutInflater.from(this.context);
        this.progress = progress;
        this.onScreen = onScreen;
    }

    @Override
    public int getCount() {
        return tasksList.size();
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final MyViewHolder mViewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.single_task_view_detail, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }
        final Tasks currentTask = getItem(position);
       mViewHolder.childTaskDetail.setText(currentTask.getName());
        DateTime dt = new DateTime(currentTask.getDueDate());
        DateTimeFormatter fmt = DateTimeFormat.forPattern(AppConstant.DATE_FORMAT);
        String toPrintDate = fmt.print(dt);
        mViewHolder.childTaskDueDate.setText(toPrintDate);
        DateTime currentDate = new DateTime();
        if (userType.equals(AppConstant.CHILD)) {
            mViewHolder.childTaskCheckbox.setVisibility(View.GONE);
            mViewHolder.thumbUp.setVisibility(View.GONE);
        } else {
            mViewHolder.childTaskCheckbox.setVisibility(View.GONE);
            mViewHolder.thumbUp.setBackground(new IconDrawable(context, FontAwesomeIcons.fa_eye)
                    .colorRes(R.color.main_font)
                    .actionBarSize());
        }
        mViewHolder.right_arrow.setVisibility(View.INVISIBLE);
            if (new DateTime(currentTask.getDueDate()).withTimeAtStartOfDay().equals(currentDate.withTimeAtStartOfDay())) {
                if (currentTask.getStatus().equalsIgnoreCase(AppConstant.COMPLETED)) {
                    mViewHolder.childTaskStatus.setBackgroundResource(R.drawable.completed_status);
                    if(userType.equals(AppConstant.PARENT))
                     mViewHolder.thumbUp.setVisibility(View.VISIBLE);
                } else {
                    if (currentDate.isAfter(currentTask.getDueDate())) {
                        mViewHolder.childTaskStatus.setBackgroundResource(R.drawable.pink_status);
                        if(userType.equals(AppConstant.PARENT))
                            mViewHolder.thumbUp.setVisibility(View.GONE);
                    } else {
                        mViewHolder.childTaskStatus.setBackgroundResource(R.drawable.yellow_status);
                        if(userType.equals(AppConstant.PARENT))
                            mViewHolder.thumbUp.setVisibility(View.GONE);
                    }
                }
            } else if (currentDate.isAfter(currentTask.getDueDate())) {
                if (currentTask.getStatus().equalsIgnoreCase(AppConstant.COMPLETED)) {
                    mViewHolder.childTaskStatus.setBackgroundResource(R.drawable.completed_status);
                    if(userType.equals(AppConstant.PARENT))
                        mViewHolder.thumbUp.setVisibility(View.VISIBLE);
                } else {
                    mViewHolder.childTaskStatus.setBackgroundResource(R.drawable.pink_status);
                    if(userType.equals(AppConstant.PARENT))
                        mViewHolder.thumbUp.setVisibility(View.GONE);
                }
            } else {
                if (currentTask.getStatus().equalsIgnoreCase(AppConstant.COMPLETED)) {
                    mViewHolder.childTaskStatus.setBackgroundResource(R.drawable.completed_status);
                    if(userType.equals(AppConstant.PARENT))
                        mViewHolder.thumbUp.setVisibility(View.VISIBLE);
                } else {
                    mViewHolder.childTaskStatus.setBackgroundResource(R.drawable.gray_status);
                    if(userType.equals(AppConstant.PARENT))
                        mViewHolder.thumbUp.setVisibility(View.GONE);
                }
            }
            mViewHolder.thumbUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callTaskApproval(currentTask);

                }
            });

        mViewHolder.task_description_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userType.equalsIgnoreCase(AppConstant.CHILD)) {
                    if(currentTask.getStatus().equalsIgnoreCase(AppConstant.COMPLETED))
                        Utils.showToast(context, context.getResources().getString(R.string.already_submit));
                    else
                        new ScreenSwitch(context).moveToRequestTaskApproval(childObject, currentTask);
                }else {
                    if (!currentTask.getStatus().equalsIgnoreCase(AppConstant.COMPLETED))
                        editTaskListener(currentTask);
                    else
                        callTaskApproval(currentTask);
                }
            }
        });


        return convertView;
    }

    public void editTaskListener(Tasks currentTask){

        Intent addTask = new Intent(context, AddTask.class);
        addTask.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        addTask.putExtra(AppConstant.NAME, childObject.getFirstName());
        addTask.putExtra(AppConstant.AVATAR, childObject.getAvatar());
        addTask.putExtra(AppConstant.ID, childObject.getId());
        addTask.putExtra(AppConstant.SCREEN, AppConstant.CHECKED_IN_SCREEN);
        addTask.putExtra(AppConstant.FROM_SCREEN, AppConstant.CHECKED_IN_SCREEN);
        if(onScreen.equalsIgnoreCase(AppConstant.CHECKED_IN_SCREEN)) {
            addTask.putExtra(AppConstant.CHILD_OBJECT, otherChild);
            addTask.putExtra(AppConstant.OTHER_CHILD_OBJECT, childObject);
        }else{
            addTask.putExtra(AppConstant.CHILD_OBJECT, childObject);
            addTask.putExtra(AppConstant.OTHER_CHILD_OBJECT, otherChild);
        }
        addTask.putExtra(AppConstant.PARENT_OBJECT, parentobject);
        addTask.putExtra(AppConstant.TO_EDIT, currentTask);
        addTask.putExtra(AppConstant.TASK_STATUS, AppConstant.EDIT);
        context.startActivity(addTask);
    }

    private class MyViewHolder {
        TextView childTaskDetail, childTaskDueDate, childTaskCheckbox;
        Button childTaskStatus;
        ImageView thumbUp, right_arrow;
        LinearLayout task_details_layout, task_description_layout;

        public MyViewHolder(View item) {
            childTaskDetail = (TextView) item.findViewById(R.id.child_task_detail);
            childTaskDueDate = (TextView) item.findViewById(R.id.child_task_due);
            childTaskCheckbox = (TextView) item.findViewById(R.id.child_task_checkbox);
            childTaskStatus = (Button) item.findViewById(R.id.child_task_staus);
            thumbUp = (ImageView) item.findViewById(R.id.thumb_up_id);
            right_arrow = (ImageView) item.findViewById(R.id.child_task_right);
            task_details_layout = (LinearLayout) item.findViewById(R.id.task_details_layout);
            task_description_layout = (LinearLayout) item.findViewById(R.id.task_description_layout);
        }


    }

    private void callTaskApproval(Tasks currentTask){
        if(onScreen.equalsIgnoreCase(AppConstant.CHECKED_IN_SCREEN))
             new ScreenSwitch(context).moveToTaskApproval(otherChild,childObject, parentobject, onScreen, currentTask);
        else
            new ScreenSwitch(context).moveToTaskApproval(childObject,otherChild, parentobject, onScreen, currentTask);

    }

}