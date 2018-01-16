package com.mobiledi.earnit.activity;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.mobiledi.earnit.R;
import com.mobiledi.earnit.adapter.ItemAdapter;
import com.mobiledi.earnit.model.Child;
import com.mobiledi.earnit.model.Goal;
import com.mobiledi.earnit.model.Item;
import com.mobiledi.earnit.model.Parent;
import com.mobiledi.earnit.model.Tasks;
import com.mobiledi.earnit.utils.AppConstant;
import com.mobiledi.earnit.utils.FloatingMenu;
import com.mobiledi.earnit.utils.GetObjectFromResponse;
import com.mobiledi.earnit.utils.ScreenSwitch;
import com.mobiledi.earnit.utils.Utils;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.apache.commons.math3.util.Precision;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

import static android.text.InputType.TYPE_TEXT_FLAG_CAP_SENTENCES;
import static android.text.InputType.TYPE_TEXT_FLAG_CAP_WORDS;

/**
 * Created by mradul on 7/13/17.
 */

public class AddTask extends BaseActivity implements View.OnClickListener{

    public Parent parentObject;
    public Child childObject, otherChild;
    Button save, cancel, checkbox, assignTo;
    EditText taskName, amount, taskDetails;
    TextView date_time_textview;
    TextView childName;
    CircularImageView childAvatar;
    ImageButton dueDate;
    String actionBarText = "New Task for ";
    String NONE = "None";
    AddTask addTask;
    Map<Integer, String> childs;
    List<String> assignChild;
    int dom = 0;
    int month = 0;
    int year = 0;
    int hour = 00;
    int min = 00;
    final int TASK_NAME_LENGTH = 40;
    Intent intent;
    boolean checkboxStatus = false;
    boolean IS_EDITING_TASK = false;
    private final String TAG = "AddTask";
    TextView repeatSpinner, repeatTask;
    RelativeLayout progressBar;
    String screen_name;
    private String goalName;
    private String repeat;
    ArrayList<String> list;
    String repeats[];
    ArrayList<Goal> goalList = new ArrayList<>();
    Tasks currentTask;
    DateTimeFormatter fmt;
    ScreenSwitch screenSwitch;
    int repeatCount = 0;
    ArrayList<Item> repeatList, goalsList;
    private BottomSheetDialog mBottomSheetDialog;
    int fetchGoalId = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task_layout);
        addTask = this;
        screenSwitch = new ScreenSwitch(addTask);
        save = (Button) findViewById(R.id.save);
        cancel = (Button) findViewById(R.id.cancel);
        taskDetails = (EditText) findViewById(R.id.task_detail);
        taskName = (EditText) findViewById(R.id.task_name);
        amount = (EditText) findViewById(R.id.amount_id);
        progressBar = (RelativeLayout) findViewById(R.id.loadingPanel);
        date_time_textview = (TextView) findViewById(R.id.date_time_textview);
        intent = getIntent();
        screen_name = intent.getStringExtra(AppConstant.FROM_SCREEN);
        parentObject = (Parent) intent.getSerializableExtra(AppConstant.PARENT_OBJECT);

        childObject = (Child) intent.getSerializableExtra(AppConstant.CHILD_OBJECT);
        otherChild = (Child) intent.getSerializableExtra(AppConstant.OTHER_CHILD_OBJECT);


        fmt = DateTimeFormat.forPattern(AppConstant.DATE_FORMAT);
        list = new ArrayList<>();
        list.add(NONE);
        repeatList = new ArrayList<>();
        goalsList = new ArrayList<>();
        goalsList.add(new Item(0,NONE));

        repeats = getResources().getStringArray(R.array.repeat_frequency);

        for(String s : repeats) {
            repeatList.add(new Item(repeatCount, s));
            repeatCount++;
        }

        callGoalService(parentObject.getEmail(), parentObject.getPassword(), childObject.getId());
        childName = (TextView) findViewById(R.id.add_task_header);
        childAvatar = (CircularImageView) findViewById(R.id.add_task_avatar);
        dueDate = (ImageButton) findViewById(R.id.due_date);
        checkbox = (Button) findViewById(R.id.checkbox);
        assignTo = (Button) findViewById(R.id.assign_to_id);
        try {
            Picasso.with(addTask).load(childObject.getAvatar()).error(R.drawable.default_avatar).into(childAvatar);
        } catch (Exception e) {
            Picasso.with(addTask).load(R.drawable.default_avatar).into(childAvatar);
            e.printStackTrace();
        }

        repeatSpinner = (TextView) findViewById(R.id.apply_to_goal_spinner);
        repeatTask = (TextView) findViewById(R.id.repeat_frequency);
        childs = (Map<Integer, String>) intent.getSerializableExtra(AppConstant.CHILD_MAP);
        assignChild = new LinkedList<>(Arrays.asList(childObject.getFirstName()));
        assignTo.setText(childObject.getFirstName());
        save.setOnClickListener(addTask);
        cancel.setOnClickListener(addTask);
        dueDate.setOnClickListener(addTask);
        checkbox.setOnClickListener(addTask);
        childAvatar.setOnClickListener(addTask);
        assignTo.setOnClickListener(addTask);
        repeatSpinner.setOnClickListener(addTask);
        repeatTask.setOnClickListener(addTask);

        taskName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length() > TASK_NAME_LENGTH){
                    taskName.setError(getResources().getString(R.string.task_name_length));
                    taskName.setFilters(new InputFilter[] {
                            new InputFilter.LengthFilter(TASK_NAME_LENGTH)
                    });
                }else {
                    taskName.setFilters(new InputFilter[] {});
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        InputFilter filter = new InputFilter() {
            final int maxDigitsBeforeDecimalPoint = 6;
            final int maxDigitsAfterDecimalPoint = 2;

            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                StringBuilder builder = new StringBuilder(dest);
                builder.replace(dstart, dend, source
                        .subSequence(start, end).toString());
                if (!builder.toString().matches(
                        "(([1-9]{1})([0-9]{0," + (maxDigitsBeforeDecimalPoint - 1) + "})?)?(\\.[0-9]{0," + maxDigitsAfterDecimalPoint + "})?"

                )) {
                    if (source.length() == 0)
                        return dest.subSequence(dstart, dend);
                    return "";
                }

                return null;

            }
        };

        amount.setFilters(new InputFilter[]{filter});

        //CHECK IF IT IS A EDIT TASK REQUEST
        if (intent.getSerializableExtra(AppConstant.TO_EDIT) != null && !screen_name.equalsIgnoreCase(AppConstant.ADD_TASK)) {
            currentTask = (Tasks) intent.getSerializableExtra(AppConstant.TO_EDIT);
            IS_EDITING_TASK = true;
            autoFill(currentTask);
            if(currentTask.getGoal() != null){
                fetchGoalId = currentTask.getGoal().getId();
                goalName = currentTask.getGoal().getGoalName();
                repeatSpinner.setText(goalName.substring(0,1).toUpperCase()+goalName.substring(1));
            }else{
                fetchGoalId = 0;
                repeatSpinner.setText(NONE);}
            if(currentTask.getRepititionSchedule() != null){
                repeat = currentTask.getRepititionSchedule().getRepeat();
                repeatTask.setText(repeat.substring(0,1).toUpperCase()+repeat.substring(1));
            }else{
                repeat = NONE;
                repeatTask.setText(NONE);
            }

        }else{
            currentTask = null;
            childName.setText(actionBarText + childObject.getFirstName());
            repeatTask.setText(NONE);
            repeatSpinner.setText(NONE);
        }
        setCursorPosition();
        taskDetails.setImeOptions(EditorInfo.IME_ACTION_DONE);
        taskDetails.setRawInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_CLASS_TEXT);

        taskDetails.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId==EditorInfo.IME_ACTION_DONE){
                    taskDetails.setCursorVisible(false);

                }
                return false;
            }
        });
    }
    private void setCursorPosition() {
        Utils.SetCursorPosition(taskName);
        Utils.SetCursorPosition(amount);
        Utils.SetCursorPosition(taskDetails);
    }
    private void autoFill(Tasks currentTask) {

        childName.setText(currentTask.getName());
        taskDetails.setText(currentTask.getDetails());
        taskName.setText(currentTask.getName());
        amount.setText(String.valueOf(Precision.round(currentTask.getAllowance(), 2)));
        DateTime dt = new DateTime(currentTask.getDueDate());
        String toPrintDate = fmt.print(dt);
        date_time_textview.setText(toPrintDate);

        if (currentTask.getPictureRequired() == 1) {
            checkbox.setText("✔");
            checkboxStatus = true;
        } else {
            checkboxStatus = false;
        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.due_date:
                showDateTimePicker();
                break;

            case R.id.cancel:
                onCancelAndBack(childObject, otherChild);
                break;

            case R.id.checkbox:
                if (checkboxStatus) {
                    checkbox.setText("");
                    checkboxStatus = false;
                } else {
                    checkbox.setText("✔");
                    checkboxStatus = true;
                }
                break;

            case R.id.save:
                if (taskName.getText().toString().trim().length() > 0){
                    if(taskName.getText().toString().trim().length() <= TASK_NAME_LENGTH){
                        if(dom != 0 ? true: false )
                            saveTask();
                        else{
                            if(intent.getStringExtra(AppConstant.TASK_STATUS).equals(AppConstant.EDIT) && currentTask.getDueDate() != 0)
                                saveTask();
                            else
                                showToast(getResources().getString(R.string.select_due_date));
                        }
                    }
                    else
                        showToast(getResources().getString(R.string.task_name_too_long));
                }
                else
                    showToast(getResources().getString(R.string.please_enter_task_name));
                break;

            case R.id.add_task_avatar:
                if (currentTask != null)
                    new FloatingMenu(addTask).fetchAvatarDimension(childAvatar,childObject, otherChild, parentObject, AppConstant.ADD_TASK, progressBar,currentTask );
                else
                    new FloatingMenu(addTask).fetchAvatarDimension(childAvatar,childObject, otherChild, parentObject, AppConstant.ADD_TASK, progressBar, null);
                break;

            case R.id.apply_to_goal_spinner:
                showBottomSheetDialog(goalsList, repeatSpinner, AppConstant.GOAL);
                break;

             case R.id.repeat_frequency:
                showBottomSheetDialog(repeatList, repeatTask, AppConstant.REPEAT);
                break;


        }

    }

    private void saveTask() {
        DateTime dDate = null;
        if(intent.getStringExtra(AppConstant.TASK_STATUS).equals(AppConstant.EDIT)){
            if(dom == 0){
                dDate = new DateTime(currentTask.getDueDate());

            }
            else
                dDate = new DateTime(year, month + 1, dom, hour, min, 00);
        }else{
            if (dom != 0)
                dDate = new DateTime(year, month + 1, dom, hour, min, 00);
        }

        JSONObject signInJson = new JSONObject();
        try {
            if (IS_EDITING_TASK)
                signInJson.put(AppConstant.ID, currentTask.getId());

            signInJson.put(AppConstant.CHILDREN, new JSONObject().put(AppConstant.ID, childObject.getId()));

            if (fetchGoalId != 0)
                signInJson.put(AppConstant.GOAL, new JSONObject().put(AppConstant.ID, fetchGoalId));

            signInJson.put(AppConstant.ALLOWANCE, amount.getText().length() > 0 ? Float.parseFloat(amount.getText().toString()) : Float.parseFloat("0"));
            signInJson.put(AppConstant.CREATE_DATE, new DateTime().getMillis());

            if(intent.getStringExtra(AppConstant.TASK_STATUS).equals(AppConstant.EDIT))
                signInJson.put(AppConstant.DUE_DATE, dDate.getMillis());
            else
                signInJson.put(AppConstant.DUE_DATE, dom != 0 ? dDate.getMillis() : new DateTime().plusDays(1).getMillis());

            signInJson.put(AppConstant.NAME, taskName.getText().toString().trim());
            signInJson.put(AppConstant.DESCRIPTION, taskDetails.getText());
            signInJson.put(AppConstant.PICTURE_REQUIRED, checkboxStatus ? 1 : 0);
            signInJson.put(AppConstant.STATUS, AppConstant.DUE);
            signInJson.put(AppConstant.UPDATE_DATE, 0);
            signInJson.put(AppConstant.TASK_COMMENTS, new JSONArray());
            Utils.logDebug(TAG, "add_task_json : R"+ repeat);

            if(!repeat.equalsIgnoreCase(NONE)){
                JSONObject repeatObject = new JSONObject();
                if(IS_EDITING_TASK){
                    if(currentTask.getRepititionSchedule() == null){
                        repeatObject.put(AppConstant.REPEAT, repeat.toLowerCase());
                        repeatObject.put(AppConstant.EXPIRY_DATE,new DateTime().getMillis());
                        signInJson.put(AppConstant.REPITITION_SCHEDULE, repeatObject);
                    }else{
                        repeatObject.put(AppConstant.ID, currentTask.getRepititionSchedule().getId());
                        repeatObject.put(AppConstant.REPEAT, repeat.toLowerCase());
                        signInJson.put(AppConstant.REPITITION_SCHEDULE, repeatObject);
                    }
                }else{
                    repeatObject.put(AppConstant.REPEAT, repeat.toLowerCase());
                    repeatObject.put(AppConstant.EXPIRY_DATE,new DateTime().getMillis());
                    signInJson.put(AppConstant.REPITITION_SCHEDULE, repeatObject);
                }
            }

            Utils.logDebug(TAG, "add_task_json :"+ String.valueOf(signInJson));

            StringEntity entity = new StringEntity(signInJson.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, AppConstant.APPLICATION_JSON));
            AsyncHttpClient httpClient = new AsyncHttpClient();
            httpClient.setBasicAuth(parentObject.getEmail(), parentObject.getPassword());
            PersistentCookieStore myCookieStore = new PersistentCookieStore(addTask);
            httpClient.setCookieStore(myCookieStore);
            if (IS_EDITING_TASK){
                Utils.logDebug(TAG, "calling task edit api");
                httpClient.put(addTask, AppConstant.BASE_URL + AppConstant.TASKS_API, entity, AppConstant.APPLICATION_JSON, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Utils.logDebug(TAG, "calling success : "+response);
                        fetchChildTaskList();

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        Utils.logDebug(TAG, "calling success : "+response);
                        fetchChildTaskList();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Utils.logDebug(TAG, "calling onFailure : "+errorResponse.toString());
                        unLockScreen();

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        Utils.logDebug(TAG, "calling onFailure : "+errorResponse.toString());
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
            else{
                Utils.logDebug(TAG, "calling task add api");
                httpClient.post(addTask, AppConstant.BASE_URL + AppConstant.TASKS_API, entity, AppConstant.APPLICATION_JSON, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Utils.logDebug(TAG, "calling success : "+response);
                        fetchChildTaskList();

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        Utils.logDebug(TAG, "calling success : "+response);
                        fetchChildTaskList();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Utils.logDebug(TAG, "calling onFailure : "+errorResponse.toString());
                        unLockScreen();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        Log.i(TAG, "calling onFailure : "+errorResponse.toString());
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
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e){e.printStackTrace();}
    }

    private void showDateTimePicker() {
        Calendar now = Calendar.getInstance();

        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog datePickerDialog, int i, int i1, int i2) {
                        year = i;
                        month = i1;
                        dom = i2;
                        DateTime dDate = new DateTime(year, month + 1, dom, hour, min, 00);
                        DateTimeFormatter fmt = DateTimeFormat.forPattern(AppConstant.DATE_FORMAT);
                        String toPrintDate = fmt.print(dDate);
                        date_time_textview.setText(toPrintDate);
                        timePicker();

                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setMinDate(Calendar.getInstance());
        dpd.show(getFragmentManager(), AppConstant.DATE_PICKER_DIALOG);



    }

    public void timePicker() {
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog    Theme_DeviceDefault_Light_Dialog_Alert
        TimePickerDialog timePickerDialog = new TimePickerDialog(addTask, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        hour = hourOfDay;
                        min = minute;
                        DateTime dDate = new DateTime(year, month + 1, dom, hour, min, 00);
                        DateTimeFormatter fmt = DateTimeFormat.forPattern(AppConstant.DATE_FORMAT);
                        String toPrintDate = fmt.print(dDate);
                        date_time_textview.setText(toPrintDate);


                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
            onCancelAndBack(childObject, otherChild);
    }

    public void onCancelAndBack(Child child, Child otherChild) {
        Utils.logDebug(TAG, "calling onCancelAndBack method ");
        if(screen_name.equalsIgnoreCase(AppConstant.CHECKED_IN_SCREEN)){
            if(otherChild.getTasksArrayList().size() > 0)
                screenSwitch.moveToAllTaskScreen(child, otherChild, screen_name, parentObject, AppConstant.ADD_TASK);
            else
                Toast.makeText(addTask, getResources().getString(R.string.no_task_available), Toast.LENGTH_LONG).show();
        }else if(screen_name.equalsIgnoreCase(AppConstant.CHECKED_IN_TASK_APPROVAL__SCREEN)){
            if (child.getTasksArrayList().size() > 0)
                screenSwitch.moveToAllTaskScreen(child, otherChild, screen_name, parentObject, AppConstant.ADD_TASK);
            else
                Toast.makeText(addTask, getResources().getString(R.string.no_task_for_approval), Toast.LENGTH_LONG).show();
        }else if(screen_name.equalsIgnoreCase(AppConstant.PARENT_DASHBOARD))
            screenSwitch.moveToParentDashboard(parentObject);
       else if(screen_name.equalsIgnoreCase(AppConstant.GOAL_SCREEN))
           screenSwitch.isGoalExists(child, otherChild, parentObject, progressBar,screen_name,currentTask);
       else if(screen_name.equalsIgnoreCase(AppConstant.BALANCE_SCREEN))
            screenSwitch.checkBalance(child, otherChild, parentObject, AppConstant.ADD_TASK,currentTask, progressBar, AppConstant.PARENT);
       else {
           screen_name = AppConstant.PARENT_DASHBOARD;
            screenSwitch.moveToParentDashboard(parentObject);
       }
    }

    public void fetchChildTaskList() {

        Utils.logDebug(TAG, "calling fetch ChildTaskList api");
        final AsyncHttpClient client = new AsyncHttpClient();
        client.setBasicAuth(parentObject.getEmail(), parentObject.getPassword());
        client.get(AppConstant.BASE_URL + AppConstant.CHILDREN_API +parentObject.getAccount().getId(), null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Utils.logDebug(TAG, "calling fetch ChildTaskList api onSuccess :"+ response);

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        if (object.getString(AppConstant.EMAIL).equals(childObject.getEmail())) {
                            Child child = new GetObjectFromResponse().getChildObject(object);
                            Child otherChild = new GetObjectFromResponse().getChildObject(object);

                            //TASKS
                            ArrayList<Tasks> taskList = new ArrayList<>();
                            ArrayList<Tasks> otherTaskList = new ArrayList<>();
                            JSONArray taskArray = object.getJSONArray(AppConstant.TASKS);
                            Utils.logDebug(TAG, "Fetch child list for "+ screen_name);
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
                            if(IS_EDITING_TASK)
                                onCancelAndBack(child, otherChild);
                            else{
                                showDialogOnTaskAdded(child, otherChild);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e){e.printStackTrace();}
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Utils.logDebug(TAG, "calling fetch ChildTaskList api onFailure :"+ errorResponse);
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

    public void callGoalService(String email, String password, int childId) {

        try {
            AsyncHttpClient client = new AsyncHttpClient();
            client.setBasicAuth(email, password);
            client.get(AppConstant.BASE_URL + AppConstant.GOAL_API + childId, null, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject object = response.getJSONObject(i);
                            Goal goal = new GetObjectFromResponse().getGoalObject(object);

                            if (goal.getTally() < goal.getAmount())
                                goalList.add(goal);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    //UPDATE GOAL SPINNER IF IT IS A TASK EDIT
                    for (Goal goal : goalList) {
                        goalsList.add(new Item(goal.getId(), goal.getGoalName()));
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    unLockScreen();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showDialogOnTaskAdded(final Child child, final Child otherChild) {
        final Dialog dialog = new Dialog(addTask);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha=0.9f;
        window.setAttributes(lp);
        window.setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.dialog_box);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        final TextView message = (TextView) dialog.findViewById(R.id.dialog_message);
        message.setText(getResources().getString(R.string.add_another_task));
        Button declineButton = (Button) dialog.findViewById(R.id.cancel);
        declineButton.setText(AppConstant.NO);
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                onCancelAndBack(child, otherChild);

            }
        });
        Button acceptButton = (Button) dialog.findViewById(R.id.ok);
        acceptButton.setText(AppConstant.YES);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                screenSwitch.moveToAddTask(child, otherChild, parentObject, screen_name, null);
            }
        });
        dialog.show();
    }



    private void showBottomSheetDialog(ArrayList<Item> items, final TextView dropDownView, final String type) {
        mBottomSheetDialog = new BottomSheetDialog(this);
        final View view = getLayoutInflater().inflate(R.layout.sheet, null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ItemAdapter(items, new ItemAdapter.ItemListener() {
            @Override
            public void onItemClick(Item item) {
                if(type.equalsIgnoreCase(AppConstant.GOAL)){
                    fetchGoalId = item.getId();
                }else{
                    repeat = item.getmTitle();
                }
                showToast(item.getTitle());
                dropDownView.setText(item.getTitle());
                if (mBottomSheetDialog != null) {
                    mBottomSheetDialog.dismiss();
                }
            }
        }));

        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.show();
        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mBottomSheetDialog = null;
            }
        });
    }
}
