package com.mobiledi.earnit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobiledi.earnit.R;
import com.mobiledi.earnit.model.Child;
import com.mobiledi.earnit.model.Parent;
import com.mobiledi.earnit.model.TaskComment;
import com.mobiledi.earnit.model.Tasks;
import com.mobiledi.earnit.utils.AppConstant;
import com.mobiledi.earnit.utils.GetObjectFromResponse;
import com.mobiledi.earnit.utils.ScreenSwitch;
import com.mobiledi.earnit.utils.Utils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class ParentTaskApproval  extends BaseActivity implements View.OnClickListener {
    TextView tTaskName, taskDetails, taskDueDate, taskAllowance, taskRepeat, taskPicture, commentLabel;
    Child childObject, otherChildObject;
    Tasks taskObject;
    Parent parentObject;
    ParentTaskApproval parentTaskApproval;
    ImageView postedImage;
    EditText commentBox;
    ImageButton cancel;
    Button approval, declined;
    RelativeLayout progressBar;
    private final String TAG = "ParentTaskApproval";
    String fromScreen;
    ScreenSwitch screenSwitch;
    ArrayList<Tasks> completedTaskList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_approval_view);
        parentTaskApproval = this;
        screenSwitch = new ScreenSwitch(parentTaskApproval);
        settingViewIds();

        //SERIALIZE OBJECT FROM INTENT OBJECT
        Intent intent = getIntent();
        childObject = (Child) intent.getSerializableExtra(AppConstant.CHILD_OBJECT);
        otherChildObject = (Child) intent.getSerializableExtra(AppConstant.OTHER_CHILD_OBJECT);
        parentObject = (Parent) intent.getSerializableExtra(AppConstant.PARENT_OBJECT);
        fromScreen = intent.getStringExtra(AppConstant.FROM_SCREEN);
        Utils.logDebug(TAG, "comming from >"+ fromScreen);
        if(intent.getSerializableExtra(AppConstant.TASK_OBJECT) != null){
            taskObject = (Tasks) intent.getSerializableExtra(AppConstant.TASK_OBJECT);
            autoFillTaskDetails(taskObject);
        }else {
            try {
                completedTaskList = fetchComletedTaskList(childObject);
                if (completedTaskList.size() > 0){
                    taskObject = completedTaskList.get(0);
                    autoFillTaskDetails(taskObject);}
                else
                    screenSwitch.moveToParentDashboard(parentObject);
            }catch (NullPointerException e){}
        }
    }

    public void settingViewIds() {
        progressBar = (RelativeLayout) findViewById(R.id.loadingPanel);
        tTaskName = (TextView) findViewById(R.id.task_name);
        taskDetails = (TextView) findViewById(R.id.task_description);
        taskDueDate = (TextView) findViewById(R.id.task_due_date);
        commentBox = (EditText) findViewById(R.id.comment_box);
        postedImage = (ImageView) findViewById(R.id.posted_imageview);
        declined = (Button) findViewById(R.id.decline);
        approval = (Button) findViewById(R.id.approve);
        cancel = (ImageButton) findViewById(R.id.back_arrow);
        taskAllowance = (TextView) findViewById(R.id.task_allowance);
        taskPicture = (TextView) findViewById(R.id.task_photo_required);
        taskRepeat = (TextView) findViewById(R.id.task_repeat);
        commentLabel = (TextView) findViewById(R.id.comment_label);
        declined.setOnClickListener(parentTaskApproval);
        approval.setOnClickListener(parentTaskApproval);
        cancel.setOnClickListener(parentTaskApproval);


    }

    private void autoFillTaskDetails(Tasks task){
        if(!task.getName().isEmpty())
            tTaskName.setText(taskObject.getName());

        if(taskObject.getDetails().isEmpty())
            taskDetails.setText(getResources().getString(R.string.task_despription));
        else
            taskDetails.setText(taskObject.getDetails());


       taskAllowance.setText("$ " + String.valueOf(Utils.roundOff(task.getAllowance(), 2)));
        if(task.getPictureRequired() == 0){
            taskPicture.setText(AppConstant.NO);
            postedImage.setVisibility(View.GONE);
        }
        else {
            postedImage.setVisibility(View.VISIBLE);
            taskPicture.setText(AppConstant.YES);
        }

        if(task.getRepititionSchedule() != null){
            String repeatFrequency = task.getRepititionSchedule().getRepeat();
            taskRepeat.setText(repeatFrequency.substring(0,1).toUpperCase()+ repeatFrequency.substring(1));
        }else taskRepeat.setText(AppConstant.NO);

        if(task.getTaskComments().size() > 0){
            TaskComment comment = task.getTaskComments().get(task.getTaskComments().size() - 1);
            if(comment.getPictureUrl().isEmpty()){
                postedImage.setVisibility(View.GONE);
            }else{
                progressBar.setVisibility(View.VISIBLE);
                Picasso.with(parentTaskApproval).load(comment.getPictureUrl()).into(postedImage, new Callback() {
                                @Override
                                public void onSuccess() {
                                    progressBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onError() {
                                    progressBar.setVisibility(View.GONE);
                                }

                            });
            }

            if(comment.getComment().isEmpty()){
                commentBox.setVisibility(View.GONE);
                commentLabel.setVisibility(View.GONE);
            }else {
                commentBox.setText(comment.getComment());
                commentBox.setKeyListener(null);
            }
        }
        DateTime dt = new DateTime(taskObject.getDueDate());
        DateTimeFormatter fmt = DateTimeFormat.forPattern(AppConstant.DATE_FORMAT);
        String toPrintDate = fmt.print(dt);
        taskDueDate.setText(toPrintDate);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_arrow:
                goBack();
                break;

            case R.id.decline:
                updateTaskStatus(taskObject, AppConstant.DECLINED);
                break;

            case R.id.approve:
                updateTaskStatus(taskObject, AppConstant.APPROVED);
                break;
        }
    }

    private void updateTaskStatus(Tasks selectedTask, String changedStatus) {
        JSONObject taskJson = new JSONObject();
        try {
            taskJson.put(AppConstant.CHILDREN, new JSONObject().put(AppConstant.ID, selectedTask.getChildId()));
            taskJson.put(AppConstant.ID, selectedTask.getId());
            taskJson.put(AppConstant.NAME, selectedTask.getName());
            taskJson.put(AppConstant.DUE_DATE, selectedTask.getDueDate());
            taskJson.put(AppConstant.CREATE_DATE, selectedTask.getCreateDate());
            taskJson.put(AppConstant.DESCRIPTION, selectedTask.getDetails());
            taskJson.put(AppConstant.STATUS, changedStatus);
            taskJson.put(AppConstant.UPDATE_DATE, new DateTime().getMillis());
            taskJson.put(AppConstant.ALLOWANCE, selectedTask.getAllowance());

            if (selectedTask.getRepititionSchedule() == null)
                Utils.logDebug(TAG, "repeat is none");
            else{
                JSONObject repeatSchedule = new JSONObject();
                repeatSchedule.put(AppConstant.ID, selectedTask.getRepititionSchedule().getId());
                repeatSchedule.put(AppConstant.REPEAT, selectedTask.getRepititionSchedule().getRepeat());
                taskJson.put(AppConstant.REPITITION_SCHEDULE, repeatSchedule);

            }

            if (selectedTask.getPictureRequired() == 1)
                taskJson.put(AppConstant.PICTURE_REQUIRED, selectedTask.getPictureRequired());
            else{
                taskJson.put(AppConstant.PICTURE_REQUIRED, 0);
                Utils.logDebug(TAG, "picture required not checked");
            }

            if (selectedTask.getGoal() != null)
                taskJson.put(AppConstant.GOAL, new JSONObject().put(AppConstant.ID, selectedTask.getGoal().getId()));
            Utils.logDebug(TAG, " child-update-task : "+  taskJson.toString());
            StringEntity entity = new StringEntity(taskJson.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, AppConstant.APPLICATION_JSON));
            AsyncHttpClient httpClient = new AsyncHttpClient();
            httpClient.setBasicAuth(parentObject.getEmail(), parentObject.getPassword());

            httpClient.put(parentTaskApproval, AppConstant.BASE_URL + AppConstant.TASKS_API, entity, AppConstant.APPLICATION_JSON, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Utils.logDebug(TAG, " onSuccessOU : "+  response.toString());
                    progressBar.setVisibility(View.GONE);
                    lockScreen();
                    fetchChildTaskList();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Utils.logDebug(TAG, " onSuccessAU : "+  response.toString());
                    progressBar.setVisibility(View.GONE);
                    lockScreen();
                    fetchChildTaskList();
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    showToast(getResources().getString(R.string.api_calling_failed));
                    Utils.logDebug(TAG, " onFailureU : "+  errorResponse.toString());
                    unLockScreen();

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    showToast(getResources().getString(R.string.api_calling_failed));
                    Utils.logDebug(TAG, " onFailureU : "+  errorResponse.toString());
                    unLockScreen();

                }

                @Override
                public void onStart() {
                    progressBar.setVisibility(View.VISIBLE);
                    lockScreen();


                }

                @Override
                public void onFinish() {
                    progressBar.setVisibility(View.GONE);
                    unLockScreen();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goBack();
    }


    public void fetchChildTaskList() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setBasicAuth(parentObject.getEmail(), parentObject.getPassword());
        client.get(AppConstant.BASE_URL + AppConstant.CHILDREN_API + parentObject.getAccount().getId(), null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                unLockScreen();
                Utils.logDebug(TAG, "Child Success response: "+response.toString());
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject childObj = response.getJSONObject(i);

                        //child with non-approval task
                        if(childObj.getInt(AppConstant.ID) == childObject.getId()){
                            Child child = new GetObjectFromResponse().getChildObject(childObj);
                            Child otherChild = new GetObjectFromResponse().getChildObject(childObj);

                            //TASKS
                            ArrayList<Tasks> taskList = new ArrayList<>();
                            ArrayList<Tasks> otherTaskList = new ArrayList<>();
                            JSONArray taskArray = childObj.getJSONArray(AppConstant.TASKS);
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
                            new ScreenSwitch(parentTaskApproval).moveToTaskApproval( child, otherChild, parentObject, fromScreen, null);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Utils.logDebug(TAG, "Child error response: "+ errorResponse.toString());
                unLockScreen();
            }

            @Override
            public void onStart() {
                progressBar.setVisibility(View.VISIBLE);
                lockScreen();
            }

            @Override
            public void onFinish() {
                progressBar.setVisibility(View.GONE);
                unLockScreen();
            }
        });

    }

    private ArrayList<Tasks> fetchComletedTaskList(Child child){
        ArrayList<Tasks> toReturn = new ArrayList<>();
        for (Tasks task : child.getTasksArrayList()) {
            if (task.getStatus().equalsIgnoreCase(AppConstant.COMPLETED))
                toReturn.add(task);
        }
        return toReturn;
    }

    private void goBack(){
        screenSwitch.moveToAllTaskScreen( childObject, otherChildObject ,fromScreen, parentObject, AppConstant.TASK_APPROVAL_SCREEN);

    }

}
