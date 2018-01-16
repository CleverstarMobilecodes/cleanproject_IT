package com.mobiledi.earnit.adapter;


import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobiledi.earnit.R;
import com.mobiledi.earnit.customcontrol.ExpandableHeightListView;
import com.mobiledi.earnit.model.Child;
import com.mobiledi.earnit.model.ChildsTaskObject;
import com.mobiledi.earnit.model.Parent;
import com.mobiledi.earnit.model.Tasks;
import com.mobiledi.earnit.utils.AppConstant;
import com.mobiledi.earnit.utils.GetObjectFromResponse;
import com.mobiledi.earnit.utils.ScreenSwitch;
import com.mobiledi.earnit.utils.Utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class ChildViewDateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ChildsTaskObject> childsTaskObjectList;
    private ChildViewTaskAdapter taskChildAdapter;
    private Activity context;
    Child childObject;
    private Child otherChild;
    private Parent parentObject;
    private String userType;
    private String onScreen;
    private static final int TYPE_ITEM = 1;
    private static final int FEB_ICON_SIZE = 30 ;
    boolean enableShowAll;
    RelativeLayout progress;
    private final String TAG = "ChildViewDateAdapter";


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView dateHeader;
        public ImageView expendCollapse;
        public TextView expendCollapseStatus;
        public ExpandableHeightListView childrenTaskList;
        public ChildsTaskObject childsTaskObject;


        public MyViewHolder(View view) {
            super(view);
            dateHeader = (TextView) view.findViewById(R.id.dateHeader);
            childrenTaskList = (ExpandableHeightListView) view.findViewById(R.id.children_task_list);
            expendCollapse = (ImageView) view.findViewById(R.id.expend_collapse_id);
            expendCollapseStatus = (TextView) view.findViewById(R.id.expent_collapse_status_id);
        }
    }

    public ChildViewDateAdapter(RelativeLayout progress, Activity context, List<ChildsTaskObject> childsTaskObjectList, Child childObject, Parent parentObject, String type, String onScreen, boolean enableShowAll, Child otherChild) {
        this.childsTaskObjectList = childsTaskObjectList;
        this.childObject = childObject;
        this.otherChild = otherChild;
        this.parentObject = parentObject;
        this.context = context;
        this.progress = progress;
        userType = type;
        this.onScreen = onScreen;
        this.enableShowAll = enableShowAll;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.detail_task_view_header, parent, false);
            return new MyViewHolder(itemView);
        } else return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holders, int position) {
        Utils.logDebug(TAG, "on screen : " + onScreen + "POSITION at :" + position);

        if (childsTaskObjectList.size() == 0 && onScreen.equalsIgnoreCase(AppConstant.CHECKED_IN_TASK_APPROVAL__SCREEN)) {
            Utils.logDebug(TAG, "on task Approval " + "POSITION at :" + position);
            switchToCheckIn(childObject);
        } else {
            Utils.logDebug(TAG, "on MyViewHolder " + "POSITION at :" + position);
            final MyViewHolder holder = (MyViewHolder) holders;
            holder.childsTaskObject = childsTaskObjectList.get(position);
            if (new DateTime(holder.childsTaskObject.getDueDate()).withTimeAtStartOfDay().equals(new DateTime(GetObjectFromResponse.PENDING_APPROVAL_DATE).withTimeAtStartOfDay())) {
                holder.dateHeader.setText(AppConstant.NON_COMPLETED_APPROVED);
            } else if (new DateTime(holder.childsTaskObject.getDueDate()).withTimeAtStartOfDay().equals(new DateTime(GetObjectFromResponse.PAST_DUE_DATE).withTimeAtStartOfDay())) {
                holder.dateHeader.setText(AppConstant.PAST_DUE);
            } else if (new DateTime(holder.childsTaskObject.getDueDate()).withTimeAtStartOfDay().equals(new DateTime().withTimeAtStartOfDay())) {
                holder.dateHeader.setText("Today "+DateTimeFormat.forPattern("MM/dd").print(new DateTime()));
            } else {
                DateTime dt = new DateTime(holder.childsTaskObject.getDueDate());
                DateTimeFormatter fmt = DateTimeFormat.forPattern("EEE MM/dd");
                String toPrintDate = fmt.print(dt);
                holder.dateHeader.setText(toPrintDate);
            }
            holder.expendCollapse.setImageDrawable((new IconDrawable(context, FontAwesomeIcons.fa_angle_down)
                    .colorRes(R.color.main_font).sizeDp(30)));
            holder.expendCollapseStatus.setText(AppConstant.TRUE);

            holder.expendCollapse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.expendCollapseStatus.getText().toString().equalsIgnoreCase(AppConstant.TRUE)){
                        holder.expendCollapse.setImageDrawable((new IconDrawable(v.getContext(), FontAwesomeIcons.fa_angle_right)
                                .colorRes(R.color.main_font).sizeDp(FEB_ICON_SIZE)));
                        holder.childrenTaskList.setVisibility(View.GONE);
                        holder.expendCollapseStatus.setText(AppConstant.FALSE);
                    }else {
                        holder.expendCollapse.setImageDrawable((new IconDrawable(v.getContext(), FontAwesomeIcons.fa_angle_down)
                                .colorRes(R.color.main_font).sizeDp(FEB_ICON_SIZE)));
                        holder.childrenTaskList.setVisibility(View.VISIBLE);
                        Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
                        holder.childrenTaskList.setAnimation(animation);
                        holder.expendCollapseStatus.setText(AppConstant.TRUE);
                    }

                }
            });
            //TaskLIST

            taskChildAdapter = new ChildViewTaskAdapter(progress, context, childsTaskObjectList, ChildViewDateAdapter.this, position, childObject, parentObject, userType, onScreen, otherChild);
            holder.childrenTaskList.setAdapter(taskChildAdapter);
            holder.childrenTaskList.setExpanded(true);
            holder.childrenTaskList.setDivider(null);




        }
    }

    private void switchToCheckIn(final Child childObject) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setBasicAuth(parentObject.getEmail(), parentObject.getPassword());
        Utils.logDebug(TAG, "switchToCheckIn request: " + AppConstant.BASE_URL + AppConstant.CHILDREN_API + parentObject.getId());
        client.get(AppConstant.BASE_URL + AppConstant.CHILDREN_API + parentObject.getAccount().getId(), null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Utils.logDebug(TAG, "switchToCheckIn response: " + response.toString());

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        //child with non-approval task
                        if (childObject.getId() == object.getInt(AppConstant.ID)) {


                            Child child = new GetObjectFromResponse().getChildObject(object);
                            Child otherChild = new GetObjectFromResponse().getChildObject(object);

                            //TASKS
                            ArrayList<Tasks> taskList = new ArrayList<>();
                            ArrayList<Tasks> otherTaskList = new ArrayList<>();
                            JSONArray taskArray = object.getJSONArray(AppConstant.TASKS);
                            for (int taskIndex = 0; taskIndex < taskArray.length(); taskIndex++) {
                                JSONObject taskObject = taskArray.getJSONObject(taskIndex);
                                if(taskObject.getString(AppConstant.STATUS).equals(AppConstant.COMPLETED)){
                                    Tasks task = new GetObjectFromResponse().getTaskObject(taskObject,child.getId());
                                    taskList.add(task);
                                }

                                JSONObject othertaskObject = taskArray.getJSONObject(taskIndex);
                                if(!othertaskObject.getString(AppConstant.STATUS).equals(AppConstant.APPROVED)){
                                    Tasks task = new GetObjectFromResponse().getTaskObject(othertaskObject,child.getId());
                                    otherTaskList.add(task);
                                }
                            }
                            child.setTasksArrayList(taskList);
                            otherChild.setTasksArrayList(otherTaskList);
                           new ScreenSwitch(context).moveToAllTaskScreen( child, otherChild, AppConstant.CHECKED_IN_SCREEN, parentObject, onScreen);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Utils.logDebug(TAG, "switchToCheckIn response: " + errorResponse.toString());
                Utils.unLockScreen(context.getWindow());

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Utils.logDebug(TAG, "switchToCheckIn JSONObject response" + response.toString());

            }

            @Override
            public void onStart() {
                Utils.lockScreen(context.getWindow());

            }

            @Override
            public void onFinish() {
                Utils.unLockScreen(context.getWindow());

            }
        });
    }

    @Override
    public int getItemCount() {
        if (userType.equals(AppConstant.CHILD)) {
            return childsTaskObjectList.size();
        } else {
            if (enableShowAll)
                return childsTaskObjectList.size();
            else {
                if (onScreen.equalsIgnoreCase(AppConstant.CHECKED_IN_SCREEN))
                    return childsTaskObjectList.size();
                else
                    return 1;
            }

        }

    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }
}
