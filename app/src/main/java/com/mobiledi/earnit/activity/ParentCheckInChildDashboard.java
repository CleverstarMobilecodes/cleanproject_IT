package com.mobiledi.earnit.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.mobiledi.earnit.R;
import com.mobiledi.earnit.adapter.ChildViewDateAdapter;
import com.mobiledi.earnit.model.Child;
import com.mobiledi.earnit.model.ChildsTaskObject;
import com.mobiledi.earnit.model.Parent;
import com.mobiledi.earnit.model.Tasks;
import com.mobiledi.earnit.utils.AppConstant;
import com.mobiledi.earnit.utils.FloatingMenu;
import com.mobiledi.earnit.utils.GetObjectFromResponse;
import com.mobiledi.earnit.utils.NavigationDrawer;
import com.mobiledi.earnit.utils.RestCall;
import com.mobiledi.earnit.utils.ScreenSwitch;
import com.mobiledi.earnit.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by mradul on 7/18/17.
 */

public class ParentCheckInChildDashboard extends BaseActivity implements View.OnClickListener {

    CircularImageView tChildImage;
    TextView tHeaderName, addTask, readyLabel;
    private Toolbar parentCheckinToolbar;
    public Child childObject;
    Child otherChild;
    Parent parentObject;
    RecyclerView parentCheckinRecycler;
    ChildViewDateAdapter parentCheckInTaskAdapter;
    ParentCheckInChildDashboard parentCheckInChildDashboard;
    Button notifyChildButton;
    private ImageButton drawerToggle;
    RelativeLayout progressBar;
    Handler handler;
    Runnable runnable;
    String onScreen, fromScreen;
    LinearLayout showAllBlock, addTaskBlock;
    TextView addTaskTextview, showAllTextView;
    ImageView showAllAddTask;
    boolean alreadyClick = false;
    private final String TAG = "ParentCheckInDashboard";
    ScreenSwitch screenSwitch;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_checkin_child_layout);
        parentCheckinToolbar = (Toolbar) findViewById(R.id.toolbar_child);
        setSupportActionBar(parentCheckinToolbar);
        getSupportActionBar().setTitle(null);
        parentCheckInChildDashboard = this;
        screenSwitch = new ScreenSwitch(parentCheckInChildDashboard);
        tChildImage = (CircularImageView) findViewById(R.id.user_image);
        tHeaderName = (TextView) parentCheckinToolbar.findViewById(R.id.textView_child);
        addTask = (TextView) findViewById(R.id.add_task);
        readyLabel = (TextView) findViewById(R.id.approval_label);
        drawerToggle = (ImageButton) parentCheckinToolbar.findViewById(R.id.calendarBtn_child);
        drawerToggle.setImageResource(R.drawable.ic_menu_pink_36dp);
        //SERIALIZE OBJECT FROM INTENT OBJECT
        Intent intent = getIntent();
        onScreen = intent.getStringExtra(AppConstant.SCREEN);
        fromScreen = intent.getStringExtra(AppConstant.FROM_SCREEN);
        Utils.logDebug(TAG, " onscreen :>"+onScreen+ " fromScreen :>"+fromScreen);
        childObject = (Child) intent.getSerializableExtra(AppConstant.CHILD_OBJECT);
        otherChild = (Child) intent.getSerializableExtra(AppConstant.OTHER_CHILD_OBJECT);
        parentObject = (Parent) intent.getSerializableExtra(AppConstant.PARENT_OBJECT);
        //SET PROFILE IMAGE
        try {
            Picasso.with(getApplicationContext()).load(childObject.getAvatar()).error(R.drawable.default_avatar).into(tChildImage);
        } catch (Exception e) {
            Picasso.with(getApplicationContext()).load(R.drawable.default_avatar).into(tChildImage);
            e.printStackTrace();
        }
        tHeaderName.setText(childObject.getFirstName().substring(0,1).toUpperCase()+childObject.getFirstName().substring(1)+"'s Tasks");
        progressBar = (RelativeLayout) findViewById(R.id.loadingPanel);
        showAllBlock = (LinearLayout) findViewById(R.id.show_all_block);
        addTaskBlock = (LinearLayout) findViewById(R.id.add_task_block);
        addTaskTextview = (TextView) findViewById(R.id.add_task);
        showAllTextView = (TextView) findViewById(R.id.show_all);
        showAllAddTask = (ImageView) findViewById(R.id.add_task_show_all_id);

        if (onScreen.equalsIgnoreCase(AppConstant.CHECKED_IN_SCREEN))
            addTaskBlock.setVisibility(View.VISIBLE);
        else
            showAllBlock.setVisibility(View.VISIBLE);

        notifyChildButton = (Button) findViewById(R.id.message_to_child);
        parentCheckinRecycler = (RecyclerView) findViewById(R.id.parent_check_in_child_recycler);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        parentCheckinRecycler.setLayoutManager(mLayoutManager);
        parentCheckinRecycler.setItemAnimator(new DefaultItemAnimator());
        List<ChildsTaskObject> removeDeclineTask = removeDeclineTask();
        if(onScreen.equalsIgnoreCase(AppConstant.CHECKED_IN_SCREEN))
            parentCheckInTaskAdapter = new ChildViewDateAdapter(progressBar, parentCheckInChildDashboard, new GetObjectFromResponse().getChildTaskListObject(otherChild, AppConstant.PARENT, onScreen), otherChild, parentObject, AppConstant.PARENT, onScreen, false, childObject);
        else
            parentCheckInTaskAdapter = new ChildViewDateAdapter(progressBar, parentCheckInChildDashboard, removeDeclineTask, childObject, parentObject, AppConstant.PARENT, onScreen, false, otherChild);

        parentCheckinRecycler.setAdapter(parentCheckInTaskAdapter);
        notifyChildButton.setText("Send a message to " + childObject.getFirstName());
        notifyChildButton.setOnClickListener(parentCheckInChildDashboard);
        addTaskTextview.setOnClickListener(parentCheckInChildDashboard);
        showAllTextView.setOnClickListener(parentCheckInChildDashboard);
        showAllAddTask.setOnClickListener(parentCheckInChildDashboard);
        tChildImage.setOnClickListener(parentCheckInChildDashboard);

        new NavigationDrawer(parentCheckInChildDashboard, parentObject,parentCheckinToolbar,drawerToggle, onScreen, childObject.getId());
        callApi();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        screenSwitch.moveToParentDashboard(parentObject);
    }

    public void callApi(){
         handler = new Handler();
        runnable = new Runnable()
        {
            public void run()
            {

                try {
                    new RestCall(parentCheckInChildDashboard).fetchUpdatedChild(parentObject, childObject.getEmail(), progressBar, onScreen);
                } catch (Exception e) {
                }            }
        };
        handler.postDelayed(runnable, AppConstant.REFRESH_INTERVAL);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.message_to_child:
                notifyChildByMessage(v);
                break;
            case R.id.add_task:
                screenSwitch.moveToAddTask(childObject, otherChild, parentObject, onScreen, null);

                break;
            case R.id.show_all:
                if(!alreadyClick){
                    alreadyClick = true;

                parentCheckInTaskAdapter = new ChildViewDateAdapter(progressBar, parentCheckInChildDashboard, removeDeclineTask(), childObject, parentObject, AppConstant.PARENT, onScreen, true, otherChild);
                parentCheckinRecycler.setAdapter(parentCheckInTaskAdapter);
                }else{
                    showToast(getResources().getString(R.string.no_more_task));

                }
                break;
            case R.id.add_task_show_all_id:
                screenSwitch.moveToAddTask(childObject, otherChild, parentObject, AppConstant.CHECKED_IN_TASK_APPROVAL__SCREEN, null);
                break;

            case R.id.user_image:
                new FloatingMenu(parentCheckInChildDashboard).fetchAvatarDimension(tChildImage, childObject, otherChild, parentObject, onScreen, progressBar, null);

                break;
        }
    }

    public void notifyChildByMessage(final View view) {
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
                    screenSwitch.sendMessageToChild(messageBox.getText().toString(), childObject);
                    dialog.dismiss();
                }else
                    showToast("Please enter message first");
            }
        });
        dialog.show();
    }
    public List<ChildsTaskObject> removeDeclineTask(){
        List<ChildsTaskObject> fetchTaskList = new GetObjectFromResponse().getChildTaskListObject(childObject, AppConstant.PARENT, onScreen);
        int approvalTaskCount = 0;
        for(int i = 0; i<fetchTaskList.size(); i++){
            ChildsTaskObject taskObject = fetchTaskList.get(i);
            List<Tasks> tasksList = taskObject.getTasks();
            for(int j=0; j<tasksList.size(); j++){
                Tasks task = tasksList.get(j);
                if(task.getStatus().equalsIgnoreCase(AppConstant.DECLINED)){
                    tasksList.remove(j);
                }else if(task.getStatus().equalsIgnoreCase(AppConstant.COMPLETED))
                    approvalTaskCount++;
            }
            if(tasksList.size() == 0){
                fetchTaskList.remove(i);
            }
        }
        if(approvalTaskCount > 0)
            readyLabel.setText(getResources().getString(R.string.ready_approval));
        return fetchTaskList;
    }
}
