package com.mobiledi.earnit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.mobiledi.earnit.R;
import com.mobiledi.earnit.model.Child;
import com.mobiledi.earnit.model.Goal;
import com.mobiledi.earnit.model.Parent;
import com.mobiledi.earnit.model.Tasks;
import com.mobiledi.earnit.utils.AppConstant;
import com.mobiledi.earnit.utils.FloatingMenu;
import com.mobiledi.earnit.utils.ScreenSwitch;
import com.mobiledi.earnit.utils.Utils;
import com.squareup.picasso.Picasso;

import org.apache.commons.math3.util.Precision;

/**
 * Created by mobile-di on 7/10/17.
 */

public class Balance extends BaseActivity implements View.OnClickListener{

    TextView totalBalance, goalBalance, cashBalance, goalTelly;
    ImageView back;
    CircularImageView avatar;
    Button home;
    Balance balance;
    public Parent parentObject;
    public Child childObject, otherChild;
    Goal goal;
    Tasks tasks;
    String fromScreen, userType;
    private final String TAG = "BalanceActivity";
    Intent intent;
    RelativeLayout progressBar;
    ScreenSwitch screenSwitch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.balance_layout);
        setViewIds();
        screenSwitch = new ScreenSwitch(balance);
        intent = getIntent();
        parentObject = (Parent) intent.getSerializableExtra(AppConstant.PARENT_OBJECT);
        childObject = (Child) intent.getSerializableExtra(AppConstant.CHILD_OBJECT);
        otherChild = (Child) intent.getSerializableExtra(AppConstant.OTHER_CHILD_OBJECT);
        goal = (Goal) intent.getSerializableExtra(AppConstant.GOAL_OBJECT);
        tasks = (Tasks) intent.getSerializableExtra(AppConstant.TO_EDIT);
        fromScreen = intent.getStringExtra(AppConstant.FROM_SCREEN);
        userType = intent.getStringExtra(AppConstant.TYPE);

        try {
            Picasso.with(balance).load(childObject.getAvatar()).error(R.drawable.default_avatar).into(avatar);
        } catch (Exception e) {
            Picasso.with(balance).load(R.drawable.default_avatar).into(avatar);
            e.printStackTrace();
        }
        totalBalance.setText(getResources().getString(R.string.total_account_balance)+(goal.getCash()+goal.getTally()));
        cashBalance.setText(getResources().getString(R.string.cash_text)+goal.getCash());
        goalBalance.setText(getResources().getString(R.string.goal_total)+goal.getTally());
        if(goal.getId() != 0)
            goalTelly.setText(goal.getGoalName()+": $"+goal.getTally()+ " of $"+goal.getAmount()+" / "+String.valueOf(Precision.round(goal.getTallyPercent(), 2)
            )+"%");
        else
            goalTelly.setText("No goal assigned yet!!");


    }

    public void setViewIds() {
        balance = this;
        totalBalance = (TextView) findViewById(R.id.toal_account_balance_id);
        cashBalance = (TextView) findViewById(R.id.cash_balance_id);
        goalBalance = (TextView) findViewById(R.id.goal_balance_id);
        goalTelly = (TextView) findViewById(R.id.goal_detail_id);
        back = (ImageView) findViewById(R.id.balance_back_id);
        avatar = (CircularImageView) findViewById(R.id.balance_avatar);
        home = (Button) findViewById(R.id.home);
        progressBar = (RelativeLayout) findViewById(R.id.loadingPanel);

        back.setOnClickListener(balance);
        avatar.setOnClickListener(balance);
        home.setOnClickListener(balance);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.balance_back_id:
                if(userType.equalsIgnoreCase(AppConstant.PARENT))
                    onCancelAndBack();
                else
                    screenSwitch.moveToChildDashboard(childObject, progressBar);
                break;

            case R.id.balance_avatar:
                if(userType.equalsIgnoreCase(AppConstant.PARENT))
                    new FloatingMenu(balance).fetchAvatarDimension(avatar,childObject, otherChild, parentObject, AppConstant.BALANCE_SCREEN, progressBar,null );
                else
                    new FloatingMenu(balance).fetchAvatarDimension(avatar, childObject, AppConstant.BALANCE_SCREEN, progressBar);
                break;

            case R.id.home:
                if(userType.equalsIgnoreCase(AppConstant.PARENT))
                    screenSwitch.moveToParentDashboard(parentObject);
                else
                    screenSwitch.moveToChildDashboard(childObject, progressBar);
                break;

        }
    }

    @Override
    public void onBackPressed() {
        if(userType.equalsIgnoreCase(AppConstant.PARENT))
             onCancelAndBack();
        else
            screenSwitch.moveToChildDashboard(childObject, progressBar);
    }
    public void onCancelAndBack() {
        if(fromScreen.equalsIgnoreCase(AppConstant.PARENT_DASHBOARD)) {
            screenSwitch.moveToParentDashboard(parentObject);
        }else if(fromScreen.equalsIgnoreCase(AppConstant.CHECKED_IN_SCREEN)){
            if(otherChild.getTasksArrayList().size() > 0)
                screenSwitch.moveToAllTaskScreen(childObject, otherChild,fromScreen, parentObject,AppConstant.BALANCE_SCREEN );
            else
                Utils.showToast(balance, getResources().getString(R.string.no_task_available));
        }else if(fromScreen.equalsIgnoreCase(AppConstant.CHECKED_IN_TASK_APPROVAL__SCREEN)){
            if (childObject.getTasksArrayList().size() > 0)
                screenSwitch.moveToAllTaskScreen(childObject, otherChild,fromScreen, parentObject,AppConstant.BALANCE_SCREEN );
            else
                Utils.showToast(balance, getResources().getString(R.string.no_task_for_approval));
        }else if(fromScreen.equalsIgnoreCase(AppConstant.ADD_TASK))
            screenSwitch.moveToAddTask(childObject, otherChild, parentObject, fromScreen, tasks);
        else if(fromScreen.equalsIgnoreCase(AppConstant.GOAL_SCREEN))
            screenSwitch.isGoalExists(childObject, otherChild, parentObject, progressBar,fromScreen,tasks);
        else
            screenSwitch.moveToAllTaskScreen(childObject, otherChild,AppConstant.CHECKED_IN_SCREEN, parentObject,AppConstant.BALANCE_SCREEN );

    }

}
