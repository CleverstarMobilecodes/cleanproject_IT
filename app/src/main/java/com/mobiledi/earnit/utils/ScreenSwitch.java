package com.mobiledi.earnit.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobiledi.earnit.R;
import com.mobiledi.earnit.activity.AddChild;
import com.mobiledi.earnit.activity.AddTask;
import com.mobiledi.earnit.activity.Balance;
import com.mobiledi.earnit.activity.ChildDashboard;
import com.mobiledi.earnit.activity.ChildMessage;
import com.mobiledi.earnit.activity.ChildRequestTaskApproval;
import com.mobiledi.earnit.activity.GoalActivity;
import com.mobiledi.earnit.activity.InitialParentProfile;
import com.mobiledi.earnit.activity.LoginScreen;
import com.mobiledi.earnit.activity.ParentCheckInChildDashboard;
import com.mobiledi.earnit.activity.ParentDashboard;
import com.mobiledi.earnit.activity.ParentProfile;
import com.mobiledi.earnit.activity.ParentTaskApproval;
import com.mobiledi.earnit.model.Child;
import com.mobiledi.earnit.model.Goal;
import com.mobiledi.earnit.model.Parent;
import com.mobiledi.earnit.model.Tasks;
import com.mobiledi.earnit.service.SendMessageService;
import com.mobiledi.earnit.service.UpdateFcmToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by mobile-di on 12/10/17.
 */

public class ScreenSwitch {
    private final String TAG = "ScreenSwitch";
    Activity activity;
    ArrayList<Goal> goalList = new ArrayList<>();

    public ScreenSwitch(Activity activity){
        this.activity = activity;
    }


    public void sendMessage(View view, final Child childObject){
        final Dialog dialog = new Dialog(view.getRootView().getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha=0.9f;
        window.setAttributes(lp);
        window.setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.message_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        TextView messageTitle = (TextView) dialog.findViewById(R.id.message_title_id);
        messageTitle.setText("Message to "+childObject.getFirstName()+":");
        final EditText messageBox = (EditText) dialog.findViewById(R.id.message_box);
        Button declineButton = (Button) dialog.findViewById(R.id.message_cancel);
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        Button acceptButton = (Button) dialog.findViewById(R.id.message_send);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(messageBox.getText().toString().trim().length() > 0){
                    Intent sendMessage = new Intent(activity, SendMessageService.class);
                    sendMessage.putExtra(AppConstant.CHILD_OBJECT, childObject);
                    sendMessage.putExtra(AppConstant.MESSAGE, messageBox.getText().toString());
                    activity.startService(sendMessage);
                    dialog.dismiss();
                }else
                    Toast.makeText(activity, "Please enter message first", Toast.LENGTH_LONG).show();
            }
        });
        dialog.show();
    }

    public void moveToAllTaskScreen(Child child, Child otherChild, String onScreen, Parent parent, String fromScreen){
        Intent parentCheckinChildDashboard = new Intent(activity, ParentCheckInChildDashboard.class);
        parentCheckinChildDashboard.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        parentCheckinChildDashboard.putExtra(AppConstant.CHILD_OBJECT, child);
        parentCheckinChildDashboard.putExtra(AppConstant.OTHER_CHILD_OBJECT, otherChild);
        parentCheckinChildDashboard.putExtra(AppConstant.PARENT_OBJECT, parent);
        parentCheckinChildDashboard.putExtra(AppConstant.SCREEN, onScreen);
        parentCheckinChildDashboard.putExtra(AppConstant.FROM_SCREEN, fromScreen);
        activity.startActivity(parentCheckinChildDashboard);
    }

    public void moveToAddTask(Child child, Child childWithAllTask, Parent parent, String screen, Tasks tasks){
        Intent addTask = new Intent(activity, AddTask.class);
        addTask.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        addTask.putExtra(AppConstant.CHILD_OBJECT, child);
        addTask.putExtra(AppConstant.OTHER_CHILD_OBJECT, childWithAllTask);
        addTask.putExtra(AppConstant.FROM_SCREEN, screen);
        addTask.putExtra(AppConstant.PARENT_OBJECT, parent);
        if(tasks != null) {
            addTask.putExtra(AppConstant.TO_EDIT, tasks);
            addTask.putExtra(AppConstant.TASK_STATUS, AppConstant.EDIT);
        }else addTask.putExtra(AppConstant.TASK_STATUS, AppConstant.ADD);
        activity.startActivity(addTask);
    }

    public void isGoalExists(final Child child, final Child otherChild, final Parent parent, final RelativeLayout progressBar, final String fromScreen, final Tasks tasks){

        try {
            AsyncHttpClient client = new AsyncHttpClient();
            client.setBasicAuth(parent.getEmail(), parent.getPassword());
            client.get(AppConstant.BASE_URL + AppConstant.GOAL_API +child.getId(), null, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Intent goalDashboard = new Intent(activity, GoalActivity.class);
                    goalDashboard.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    goalDashboard.putExtra(AppConstant.CHILD_OBJECT, child);
                    goalDashboard.putExtra(AppConstant.OTHER_CHILD_OBJECT, otherChild);
                    goalDashboard.putExtra(AppConstant.PARENT_OBJECT, parent);
                    goalDashboard.putExtra(AppConstant.FROM_SCREEN, fromScreen );
                    goalDashboard.putExtra(AppConstant.TO_EDIT, tasks);


                    try {
                        if(response.length() > 0 && (response.getJSONObject(0).get(AppConstant.ID) instanceof Integer)){

                            for(int i=0; i<response.length(); i++){
                                try {
                                    JSONObject object = response.getJSONObject(i);
                                    if(object.has(AppConstant.ID)){
                                        Goal goal = new GetObjectFromResponse().getGoalObject(object);
                                        goalList.add(goal);
                                        Log.i("goal-responsel1", String.valueOf(goalList.size()));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            goalDashboard.putExtra(AppConstant.MODE, AppConstant.UPDATE);
                            goalDashboard.putExtra(AppConstant.GOAL, goalList.get(0));
                        }else{
                            goalDashboard.putExtra(AppConstant.MODE, AppConstant.SAVE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    activity.startActivity(goalDashboard);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }



                @Override
                public void onStart() {
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFinish() {
                    progressBar.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkBalance(final Child child, final Child otherChild, final Parent parent, final String fromScreen, final Tasks tasks, final RelativeLayout progressBar, final String userType){
        AsyncHttpClient client = new AsyncHttpClient();
        client.setBasicAuth(child.getEmail(), child.getPassword());
        client.get(AppConstant.BASE_URL + AppConstant.GOAL_API +child.getId(), null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                progressBar.setVisibility(View.GONE);
                if(response.length() > 0){
                    for(int i=0; i<response.length(); i++) {
                        try {
                            JSONObject object = response.getJSONObject(i);
                            Goal goal  = new GetObjectFromResponse().getGoalObject(object);
                            if(goal.getTally() > 0 || goal.getCash() > 0 || goal.getAmount() > 0)
                                moveToBalance(child, otherChild, parent, fromScreen, tasks, goal, userType);
                            else
                                Utils.showToast(activity, activity.getResources().getString(R.string.no_balance));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Utils.logDebug(TAG, "Child error response: "+ errorResponse.toString());

            }

            @Override
            public void onStart() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void moveToBalance(Child child, Child otherChild, Parent parent, String fromScreen, Tasks tasks, Goal goal, String userType){
        Intent moveToBalance = new Intent(activity, Balance.class);
        moveToBalance.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        moveToBalance.putExtra(AppConstant.CHILD_OBJECT, child);
        moveToBalance.putExtra(AppConstant.OTHER_CHILD_OBJECT, otherChild);
        moveToBalance.putExtra(AppConstant.PARENT_OBJECT, parent);
        moveToBalance.putExtra(AppConstant.FROM_SCREEN, fromScreen );
        moveToBalance.putExtra(AppConstant.TO_EDIT, tasks);
        moveToBalance.putExtra(AppConstant.GOAL_OBJECT, goal);
        moveToBalance.putExtra(AppConstant.TYPE, userType);
        activity.startActivity(moveToBalance);
    }

    public void moveToParentDashboard(Parent parent){
        Intent intent = new Intent(activity, ParentDashboard.class);
        intent.putExtra(AppConstant.PARENT_OBJECT, parent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }


    public void moveToChildDashboard(Child childObject, RelativeLayout progressBar){
        new RestCall(activity).authenticateUser(childObject.getEmail(), childObject.getPassword(),null, AppConstant.CHILD_DASHBOARD_SCREEN, progressBar);

    }


    public void moveToParentProfile(int childId, Parent parentObject, String switchFrom) {
        Intent moveToLogin = new Intent(activity, ParentProfile.class);
        moveToLogin.putExtra(AppConstant.PARENT_OBJECT, parentObject);
        moveToLogin.putExtra(AppConstant.SCREEN, switchFrom);
        moveToLogin.putExtra(AppConstant.CHILD_ID, childId);
        moveToLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(moveToLogin);
    }

    public void updateFCMToken(Child child, String token ){
        Intent updateToken = new Intent(activity, UpdateFcmToken.class);
        updateToken.putExtra(AppConstant.CHILD_OBJECT, child);
        updateToken.putExtra(AppConstant.MODE, AppConstant.CHILD);
        updateToken.putExtra(AppConstant.FCM_TOKEN, token);
        activity.startService(updateToken);
    }

    public void moveTOChildDashboard(Child child){
        Intent childDashboard = new Intent(activity, ChildDashboard.class);
        childDashboard.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        childDashboard.putExtra(AppConstant.CHILD_OBJECT, child);
        activity.startActivity(childDashboard);
    }

    public void sendMessageToChild(String message, Child childObject){
        Intent sendMessage = new Intent(activity, SendMessageService.class);
        sendMessage.putExtra(AppConstant.CHILD_OBJECT, childObject);
        sendMessage.putExtra(AppConstant.MESSAGE, message);
        activity.startService(sendMessage);
    }


    public void moveToAddChild(Parent parentObject, int childID, String switchFrom, String mode, Child child){
        Intent moveToAddChild = new Intent(activity, AddChild.class);
        moveToAddChild.putExtra(AppConstant.MODE, mode);
        moveToAddChild.putExtra(AppConstant.PARENT_OBJECT, parentObject);
        moveToAddChild.putExtra(AppConstant.SCREEN, switchFrom);
        moveToAddChild.putExtra(AppConstant.CHILD_OBJECT, child);
        moveToAddChild.putExtra(AppConstant.CHILD_ID, childID);
        moveToAddChild.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(moveToAddChild);
    }


    public void moveToLogin() {
        Intent moveToLogin = new Intent(activity, LoginScreen.class);
        moveToLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(moveToLogin);
    }

    public void moveToInitialParentProfile(Parent parent){
        Intent intent = new Intent(activity, InitialParentProfile.class);
        intent.putExtra(AppConstant.PARENT_OBJECT, parent);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }


    public void moveToMessage(Child child){
        Intent childDashboard = new Intent(activity, ChildMessage.class);
        childDashboard.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        childDashboard.putExtra(AppConstant.CHILD_OBJECT, child);
        activity.startActivity(childDashboard);
    }

    public void moveToRequestTaskApproval(Child child, Tasks task){
        Intent requestTaskApproval = new Intent(activity, ChildRequestTaskApproval.class);
        requestTaskApproval.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        requestTaskApproval.putExtra(AppConstant.CHILD_OBJECT, child);
        requestTaskApproval.putExtra(AppConstant.TASK_OBJECT, task);
        activity.startActivity(requestTaskApproval);
    }

    public void moveToTaskApproval(Child child, Child otherChild, Parent parent, String fromScreen, Tasks tasks){
        Intent moveToTaskApproval = new Intent(activity, ParentTaskApproval.class);
        moveToTaskApproval.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        moveToTaskApproval.putExtra(AppConstant.CHILD_OBJECT, child);
        moveToTaskApproval.putExtra(AppConstant.OTHER_CHILD_OBJECT, otherChild);
        moveToTaskApproval.putExtra(AppConstant.PARENT_OBJECT, parent);
        moveToTaskApproval.putExtra(AppConstant.FROM_SCREEN, fromScreen );
        moveToTaskApproval.putExtra(AppConstant.TASK_OBJECT, tasks);
        activity.startActivity(moveToTaskApproval);
    }

}
