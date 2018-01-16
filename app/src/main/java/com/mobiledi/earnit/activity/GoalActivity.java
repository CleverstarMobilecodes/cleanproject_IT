package com.mobiledi.earnit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.mobiledi.earnit.R;
import com.mobiledi.earnit.model.Child;
import com.mobiledi.earnit.model.Goal;
import com.mobiledi.earnit.model.Parent;
import com.mobiledi.earnit.model.Tasks;
import com.mobiledi.earnit.utils.AppConstant;
import com.mobiledi.earnit.utils.FloatingMenu;
import com.mobiledi.earnit.utils.ScreenSwitch;
import com.mobiledi.earnit.utils.Utils;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

/**
 * Created by mobile-di on 11/8/17.
 */

public class GoalActivity extends BaseActivity implements View.OnClickListener, Validator.ValidationListener {

    GoalActivity goalActivity;
    public Parent parentObject;
    public Child childObject, otherChild;
    Button save, cancel;
    Tasks tasks;
    EditText goalName;

    @Length(min = 2, max = 10)
    EditText goalValue;
    final int GOAL_MAX_LENGTH = 20;
    String valueType = AppConstant.CASH;
    Intent intent;
    CircularImageView childAvatar;
    TextView toolbarHeader;
    Validator validator;
    RelativeLayout progressBar;
    Goal goal;
    String goalMode, fromScreen;
    private final String TAG = "GoalActivity";
    ScreenSwitch screenSwitch;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goal_layout);
        goalActivity = this;
        screenSwitch = new ScreenSwitch(goalActivity);
        setViewsId();
        intent = getIntent();
        parentObject = (Parent) intent.getSerializableExtra(AppConstant.PARENT_OBJECT);
        childObject = (Child) intent.getSerializableExtra(AppConstant.CHILD_OBJECT);
        otherChild = (Child) intent.getSerializableExtra(AppConstant.OTHER_CHILD_OBJECT);
        tasks = (Tasks) intent.getSerializableExtra(AppConstant.TO_EDIT);
        goalMode = intent.getStringExtra(AppConstant.MODE);
        fromScreen = intent.getStringExtra(AppConstant.FROM_SCREEN);
        if (goalMode.equalsIgnoreCase(AppConstant.UPDATE)) {
            save.setText(AppConstant.UPDATE);
            goal = (Goal) intent.getSerializableExtra(AppConstant.GOAL);
            goalName.setText(goal.getGoalName());
            goalValue.setText(String.valueOf(goal.getAmount()));
            toolbarHeader.setText(getResources().getString(R.string.edit_goal));
        } else {
            save.setText(AppConstant.SAVE);
            toolbarHeader.setText(getResources().getString(R.string.add_goal));
        }

        try {
            Picasso.with(goalActivity).load(childObject.getAvatar()).error(R.drawable.default_avatar).into(childAvatar);
        } catch (Exception e) {
            Picasso.with(goalActivity).load(R.drawable.default_avatar).into(childAvatar);
            e.printStackTrace();
        }
        goalName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > GOAL_MAX_LENGTH) {
                    goalName.setError(getResources().getString(R.string.goal_name_length));
                    goalName.setFilters(new InputFilter[]{
                            new InputFilter.LengthFilter(GOAL_MAX_LENGTH)
                    });
                } else {
                    goalName.setFilters(new InputFilter[]{});
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        validator = new Validator(goalActivity);
        validator.setValidationListener(goalActivity);
        setCursorPosition();
    }

    private void setCursorPosition() {
        Utils.SetCursorPosition(goalName);
        Utils.SetCursorPosition(goalValue);
    }

    private void setViewsId() {
        childAvatar = (CircularImageView) findViewById(R.id.goal_avatar);
        toolbarHeader = (TextView) findViewById(R.id.goal_header);
        progressBar = (RelativeLayout) findViewById(R.id.loadingPanel);
        save = (Button) findViewById(R.id.save);
        cancel = (Button) findViewById(R.id.cancel);
        goalName = (EditText) findViewById(R.id.goal_name);
        goalValue = (EditText) findViewById(R.id.goal_value);

        save.setOnClickListener(goalActivity);
        cancel.setOnClickListener(goalActivity);
        childAvatar.setOnClickListener(goalActivity);
//        cash.setOnClickListener(goalActivity);
//        points.setOnClickListener(goalActivity);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                if (goalName.getText().toString().trim().length() > 0) {
                    if (goalName.getText().toString().trim().length() <= GOAL_MAX_LENGTH)
                        validator.validate();
                    else
                        goalName.setError(getResources().getString(R.string.goal_too_long));
                } else
                    goalName.setError(getResources().getString(R.string.enter_goal));
                break;
            case R.id.cancel:
                onCancelAndBack();
                break;
            case R.id.goal_avatar:
                new FloatingMenu(goalActivity).fetchAvatarDimension(childAvatar, childObject, otherChild, parentObject, AppConstant.GOAL_SCREEN, progressBar, null);

                break;
        }
    }

    @Override
    public void onBackPressed() {
        onCancelAndBack();
    }

    public void onCancelAndBack() {
        Intent intent;
        if (fromScreen.equalsIgnoreCase(AppConstant.PARENT_DASHBOARD)) {
            intent = new Intent(goalActivity, ParentDashboard.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra(AppConstant.PARENT_OBJECT, parentObject);
            startActivity(intent);
        } else if (fromScreen.equalsIgnoreCase(AppConstant.CHECKED_IN_SCREEN)) {
            if (otherChild.getTasksArrayList().size() > 0)
                screenSwitch.moveToAllTaskScreen(childObject, otherChild, fromScreen, parentObject, AppConstant.GOAL_SCREEN);
            else
                Toast.makeText(goalActivity, getResources().getString(R.string.no_task_available), Toast.LENGTH_LONG).show();
        } else if (fromScreen.equalsIgnoreCase(AppConstant.CHECKED_IN_TASK_APPROVAL__SCREEN)) {
            if (childObject.getTasksArrayList().size() > 0)
                screenSwitch.moveToAllTaskScreen(childObject, otherChild, fromScreen, parentObject, AppConstant.GOAL_SCREEN);
            else
                Toast.makeText(goalActivity, getResources().getString(R.string.no_task_for_approval), Toast.LENGTH_LONG).show();
        } else if (fromScreen.equalsIgnoreCase(AppConstant.ADD_TASK)) {
            screenSwitch.moveToAddTask(childObject, otherChild, parentObject, fromScreen, tasks);
        } else if (fromScreen.equalsIgnoreCase(AppConstant.BALANCE_SCREEN)) {
            screenSwitch.checkBalance(childObject, otherChild, parentObject, AppConstant.ADD_TASK, tasks, progressBar, AppConstant.PARENT);
        } else {
            screenSwitch.moveToAllTaskScreen(childObject, otherChild, AppConstant.CHECKED_IN_SCREEN, parentObject, AppConstant.GOAL_SCREEN);
        }
    }

    @Override
    public void onValidationSucceeded() {
        addEditGoal();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void addEditGoal() {

        JSONObject signInJson = new JSONObject();
        try {
            if (goalMode.equalsIgnoreCase(AppConstant.UPDATE)) {
                signInJson.put(AppConstant.ID, goal.getId());
                signInJson.put(AppConstant.UPDATE_DATE, new DateTime().getMillis());
            } else signInJson.put(AppConstant.CREATE_DATE, new DateTime().getMillis());


            signInJson.put(AppConstant.CHILDREN, new JSONObject().put(AppConstant.ID, childObject.getId()));
            signInJson.put(AppConstant.NAME, goalName.getText().toString().trim());
            signInJson.put(AppConstant.AMOUNT, goalValue.getText().toString().trim());
            StringEntity entity = new StringEntity(signInJson.toString());
            Utils.logDebug("GoalJson", signInJson.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, AppConstant.APPLICATION_JSON));
            AsyncHttpClient httpClient = new AsyncHttpClient();
            httpClient.setBasicAuth(parentObject.getEmail(), parentObject.getPassword());
            PersistentCookieStore myCookieStore = new PersistentCookieStore(goalActivity);
            httpClient.setCookieStore(myCookieStore);
            httpClient.post(goalActivity, AppConstant.BASE_URL + AppConstant.ADD_GOAL, entity, AppConstant.APPLICATION_JSON, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    if (goalMode.equalsIgnoreCase(AppConstant.UPDATE))
                        showToast("Goal updated for " + childObject.getFirstName());
                    else
                        showToast("Goal added for " + childObject.getFirstName());
                    onCancelAndBack();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    if (goalMode.equalsIgnoreCase(AppConstant.UPDATE))
                        showToast("Goal updated for " + childObject.getFirstName());
                    else
                        showToast("Goal added for " + childObject.getFirstName());
                    onCancelAndBack();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    unLockScreen();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
