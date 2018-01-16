package com.mobiledi.earnit.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.mobiledi.earnit.R;
import com.mobiledi.earnit.model.Child;
import com.mobiledi.earnit.model.Parent;
import com.mobiledi.earnit.model.Tasks;
import com.mobiledi.earnit.service.UpdateFcmToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by mobile-di on 12/8/17.
 */

public class RestCall {
    private Activity activity;
    String token;
    final String TAG = "RestCall";
    ScreenSwitch screenSwitch;

    public RestCall(Activity activity){
        this.activity = activity;
        screenSwitch = new ScreenSwitch(this.activity);
    }

    public void authenticateUser(String username, final String password, final EditText editPassword, final String from, final RelativeLayout progressBar) {

        SharedPreferences shareToken = activity.getSharedPreferences(AppConstant.FIREBASE_PREFERENCE, MODE_PRIVATE);
        Utils.logDebug(TAG," GeneratedTokenI "+ shareToken.getString(AppConstant.TOKEN_ID, null) );
        token = shareToken.getString(AppConstant.TOKEN_ID, null);

        JSONObject signInJson = new JSONObject();
        try {
            signInJson.put(AppConstant.EMAIL, username.trim());
            signInJson.put(AppConstant.PASSWORD, password.trim());
            StringEntity entity = new StringEntity(signInJson.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, AppConstant.APPLICATION_JSON));
            AsyncHttpClient httpClient = new AsyncHttpClient();
            PersistentCookieStore myCookieStore = new PersistentCookieStore(activity);
            httpClient.setCookieStore(myCookieStore);
            Utils.logDebug(TAG," login-Rquest "+ AppConstant.BASE_URL + AppConstant.LOGIN_API );
            Utils.logDebug(TAG, " login-Rquest "+ signInJson.toString());
            httpClient.post(activity, AppConstant.BASE_URL + AppConstant.LOGIN_API, entity, AppConstant.APPLICATION_JSON, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        Utils.logDebug(TAG, " login-Rquest onSuccess"+response.toString());

                            SharedPreferences shareToken = activity.getSharedPreferences(AppConstant.FIREBASE_PREFERENCE, MODE_PRIVATE);
                            SharedPreferences.Editor editor = shareToken.edit();
                            editor.putString(AppConstant.EMAIL, response.getString(AppConstant.EMAIL));
                            editor.putString(AppConstant.PASSWORD, response.getString(AppConstant.PASSWORD));
                            editor.commit();

                        Intent updateToken = new Intent(activity, UpdateFcmToken.class);
                        updateToken.putExtra(AppConstant.IS_LOGOUT, false);
                        if (response.getString(AppConstant.TYPE).equals(AppConstant.PARENT)) {
                            Parent parent = new GetObjectFromResponse().getParentObject(response);

                            // token update for child
                            updateToken.putExtra(AppConstant.PARENT_OBJECT, parent);
                            updateToken.putExtra(AppConstant.MODE, AppConstant.PARENT);
                            if (parent.getFcmToken().isEmpty()) {
                                Utils.logDebug(TAG, " parent-FCM not available");
                                updateToken.putExtra(AppConstant.FCM_TOKEN, token);
                                activity.startService(updateToken);
                            }else {
                                if(!parent.getFcmToken().equalsIgnoreCase(token)){
                                    Utils.logDebug(TAG, "parent-FCM updating");
                                    updateToken.putExtra(AppConstant.FCM_TOKEN, token);
                                    activity.startService(updateToken);
                                }
                            }

                            if(parent.getFirstName().isEmpty() ||
                                    parent.getLastName().isEmpty() || parent.getPhone().isEmpty()){
                                screenSwitch.moveToInitialParentProfile(parent);
                            }else{
                                screenSwitch.moveToParentDashboard(parent);
                            }



                        } else if (response.getString(AppConstant.TYPE).equals(AppConstant.CHILD)) {


                            Child child = new GetObjectFromResponse().getChildObject(response);
                            //TASKS
                            ArrayList<Tasks> taskList = new ArrayList<>();

                            JSONArray taskArray = response.getJSONArray(AppConstant.TASKS);
                            for (int taskIndex = 0; taskIndex < taskArray.length(); taskIndex++) {
                                JSONObject taskObject = taskArray.getJSONObject(taskIndex);
                                Tasks task = new GetObjectFromResponse().getTaskObject(taskObject, child.getId());
                                taskList.add(task);
                            }

                            child.setTasksArrayList(taskList);
                            if (child.getMessage().isEmpty()) {
                                updateToken.putExtra(AppConstant.CHILD_OBJECT, child);
                                updateToken.putExtra(AppConstant.MODE, AppConstant.CHILD);
                                if (child.getFcmToken().isEmpty()) {
                                    Utils.logDebug(TAG,"child-FCM not available");
                                    updateToken.putExtra(AppConstant.FCM_TOKEN, token);
                                    activity.startService(updateToken);
                                }else {
                                    if(!child.getFcmToken().equalsIgnoreCase(token)){
                                        Utils.logDebug(TAG,"child-FCM updating");
                                        updateToken.putExtra(AppConstant.FCM_TOKEN, token);
                                        activity.startService(updateToken);
                                    }
                                }
                                //LOAD CHILD ACTIVITY
                                screenSwitch.moveTOChildDashboard(child);
                            }else {
                                //LOAD message ACTIVITY
                                screenSwitch.moveToMessage(child);
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }


                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Utils.logDebug(TAG," login-Rquest onFailureO  "+ errorResponse.toString());

                    clearEdittext(from, editPassword);
                    Utils.unLockScreen(activity.getWindow());


                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    Utils.logDebug(TAG," login-Rquest onFailureA"+ errorResponse.toString());
                    clearEdittext(from, editPassword);
                    Utils.unLockScreen(activity.getWindow());

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Utils.logDebug(TAG," login-Rquest onSuccessA"+response.toString());
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Utils.logDebug(TAG," login-Rquest onFailureS"+ responseString.toString());
                    clearEdittext(from, editPassword);
                }

                @Override
                public void onStart() {
                    progressBar.setVisibility(View.VISIBLE);
                    Utils.lockScreen(activity.getWindow());
                }

                @Override
                public void onFinish() {
                    progressBar.setVisibility(View.GONE);
                    Utils.unLockScreen(activity.getWindow());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearEdittext(String from, EditText editPassword){
        if(from.equalsIgnoreCase(AppConstant.LOGIN_SCREEN)){
            editPassword.setText("");
        }
        Utils.showToast(activity, activity.getResources().getString(R.string.login_failed));
}
    public void fetchUpdatedChild(final Parent parentObject , final String childEmail, final RelativeLayout progressBar, final String onScreen){

            final AsyncHttpClient client = new AsyncHttpClient();
            client.setBasicAuth(parentObject.getEmail(), parentObject.getPassword());
            client.get(AppConstant.BASE_URL + AppConstant.CHILDREN_API +parentObject.getAccount().getId(), null, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject object = response.getJSONObject(i);
                            if (object.getString(AppConstant.EMAIL).equals(childEmail)) {
                                Child child = new GetObjectFromResponse().getChildObject(object);
                                Child otherChild = new GetObjectFromResponse().getChildObject(object);

                                //TASKS
                                ArrayList<Tasks> taskList = new ArrayList<>();
                                ArrayList<Tasks> otherTaskList = new ArrayList<>();
                                JSONArray taskArray = object.getJSONArray(AppConstant.TASKS);
                                Utils.logDebug(TAG, "Fetch child list for "+ onScreen);
                                if(onScreen.equalsIgnoreCase(AppConstant.CHECKED_IN_SCREEN) || onScreen.equalsIgnoreCase(AppConstant.CHECKED_IN_TASK_APPROVAL__SCREEN)){
                                    for (int taskIndex = 0; taskIndex < taskArray.length(); taskIndex++) {
                                        JSONObject taskObject = taskArray.getJSONObject(taskIndex);
                                        if(!taskObject.getString(AppConstant.STATUS).equals(AppConstant.APPROVED)){
                                            Tasks task = new GetObjectFromResponse().getTaskObject(taskObject,child.getId());
                                            taskList.add(task);
                                        }

                                        JSONObject othertaskObject = taskArray.getJSONObject(taskIndex);
                                        if(othertaskObject.getString(AppConstant.STATUS).equals(AppConstant.COMPLETED)){
                                            Tasks task = new GetObjectFromResponse().getTaskObject(othertaskObject,child.getId());
                                            otherTaskList.add(task);
                                        }
                                    }
                                }
                                child.setTasksArrayList(taskList);
                                otherChild.setTasksArrayList(otherTaskList);
                                screenSwitch.moveToAllTaskScreen( child, otherChild, onScreen, parentObject, onScreen);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e){e.printStackTrace();}
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Utils.logDebug(TAG," Child error response:"+ errorResponse.toString());

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


}
