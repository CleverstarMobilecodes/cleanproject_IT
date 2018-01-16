package com.mobiledi.earnit.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.mobiledi.earnit.R;
import com.mobiledi.earnit.activity.LoginScreen;
import com.mobiledi.earnit.model.Child;
import com.mobiledi.earnit.model.Parent;
import com.mobiledi.earnit.model.Tasks;
import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * Created by mobile-di on 22/9/17.
 */

public class FloatingMenu {
    private final String  TAG = "FloatingMenu" ;
    int OFFSET_X = 0 ;
    int OFFSET_Y = 0 ;
    int popupWidth = 0;
    int popupHeight = 0;
    Activity activity;

    ScreenSwitch screenSwitch;


    public FloatingMenu(Activity activity){
        this.activity = activity;
        screenSwitch = new ScreenSwitch(activity);
    }

    public void fetchAvatarDimension(CircularImageView view, Child child, Child childWithAllTask, Parent parent, String fromScreen, RelativeLayout progressBar, Tasks tasks){

        int[] location = new int[2];
        view.getLocationOnScreen(location);
        Point p = new Point();
        p.x = location[0];
        p.y = location[1];
        showPopup(activity, p, child, childWithAllTask, parent, fromScreen, progressBar, tasks);
    }

    private void showPopup(Activity view, Point p, final Child child, final Child childWithAllTask, final Parent parent, final String fromScreen, final RelativeLayout progressBar, final Tasks tasks ) {

        // Inflate the popup_layout.xml
        LinearLayout viewGroup = (LinearLayout) view.findViewById(R.id.feb_menu_layout);
        LayoutInflater layoutInflater = (LayoutInflater) view.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.feb_menu_layout, viewGroup);

        checkDeviceResolution(fromScreen);
        // Creating the PopupWindow
        final PopupWindow popup = new PopupWindow(view);
        popup.setContentView(layout);
        popup.setWidth(popupWidth);
        popup.setHeight(popupHeight);
        popup.setFocusable(true);

        // Clear the default translucent background
        popup.setBackgroundDrawable(new BitmapDrawable());

        // Displaying the popup at the specified location, + offsets.
        popup.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);

        CircularImageView febIcon = (CircularImageView) layout.findViewById(R.id.fb_user_image);
        TextView addTask = (TextView) layout.findViewById(R.id.fb_add_task);
        TextView allTask = (TextView) layout.findViewById(R.id.fb_a11_task);
        TextView approvalTask = (TextView) layout.findViewById(R.id.fb_approve_task);
        TextView balance = (TextView) layout.findViewById(R.id.fb_balance);
        TextView goal = (TextView) layout.findViewById(R.id.fb_goal);
        TextView message = (TextView) layout.findViewById(R.id.fb_message);
        addTask.setCompoundDrawablesRelativeWithIntrinsicBounds((new IconDrawable(layout.getContext(), FontAwesomeIcons.fa_plus_circle)
                .colorRes(R.color.check_in).sizeDp(AppConstant.FEB_ICON_SIZE)), null, null, null);
        allTask.setCompoundDrawablesRelativeWithIntrinsicBounds((new IconDrawable(layout.getContext(), FontAwesomeIcons.fa_file_text_o)
                .colorRes(R.color.check_in).sizeDp(AppConstant.FEB_ICON_SIZE)), null, null, null);
        approvalTask.setCompoundDrawablesRelativeWithIntrinsicBounds((new IconDrawable(layout.getContext(), FontAwesomeIcons.fa_eye)
                .colorRes(R.color.check_in).sizeDp(AppConstant.FEB_ICON_SIZE)), null, null, null);
        balance.setCompoundDrawablesRelativeWithIntrinsicBounds((new IconDrawable(layout.getContext(), FontAwesomeIcons.fa_usd)
                .colorRes(R.color.check_in).sizeDp(AppConstant.FEB_ICON_SIZE)), null, null, null);
        goal.setCompoundDrawablesRelativeWithIntrinsicBounds((new IconDrawable(layout.getContext(), FontAwesomeIcons.fa_star)
                .colorRes(R.color.check_in).sizeDp(AppConstant.FEB_ICON_SIZE)), null, null, null);
        message.setCompoundDrawablesRelativeWithIntrinsicBounds((new IconDrawable(layout.getContext(), FontAwesomeIcons.fa_comment)
                .colorRes(R.color.check_in).sizeDp(AppConstant.FEB_ICON_SIZE)), null, null, null);
        try {
            Picasso.with(activity).load(child.getAvatar()).error(R.drawable.default_avatar).into(febIcon);
        } catch (Exception e) {
            e.printStackTrace();
        }
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               screenSwitch.moveToAddTask( child, childWithAllTask,  parent, fromScreen, tasks);
                popup.dismiss();
            }
        });
        allTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(childWithAllTask.getTasksArrayList().size() > 0)
                        screenSwitch.moveToAllTaskScreen( child, childWithAllTask, AppConstant.CHECKED_IN_SCREEN, parent, fromScreen);
                    else
                        Utils.showToast(activity, activity.getResources().getString(R.string.no_task_available));
                   popup.dismiss();
            }
        });
        approvalTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (child.getTasksArrayList().size() > 0)
                        screenSwitch.moveToAllTaskScreen( child, childWithAllTask, AppConstant.CHECKED_IN_TASK_APPROVAL__SCREEN, parent, fromScreen);
                    else
                        Utils.showToast(activity, activity.getResources().getString(R.string.no_task_for_approval));
                popup.dismiss();
            }
        });
        balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screenSwitch.checkBalance(child, childWithAllTask, parent, fromScreen,tasks, progressBar, AppConstant.PARENT);
                popup.dismiss();
            }
        });
        goal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screenSwitch.isGoalExists(child, childWithAllTask, parent, progressBar, fromScreen,tasks);
                popup.dismiss();
            }
        });
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screenSwitch.sendMessage(v, childWithAllTask);
                popup.dismiss();
            }
        });
        febIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });

    }
    public void fetchAvatarDimension(CircularImageView view, Child childObject, String onScreen, RelativeLayout progress){

        int[] location = new int[2];
        view.getLocationOnScreen(location);
        Point p = new Point();
        p.x = location[0];
        p.y = location[1];
        showChildPopUp(activity, p, childObject, onScreen, progress);
    }

    public void showChildPopUp(final Activity view, Point p, final Child childObject, final String onScreen, final RelativeLayout progress){


        // Inflate the popup_layout.xml
        LinearLayout viewGroup = (LinearLayout) view.findViewById(R.id.child_feb_menu_layout);
        LayoutInflater layoutInflater = (LayoutInflater) view.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.child_feb_menu_layout, viewGroup);

        checkDeviceResolutionChild();
        // Creating the PopupWindow
        final PopupWindow popup = new PopupWindow(view);
        popup.setContentView(layout);
        popup.setWidth(popupWidth);
        popup.setHeight(popupHeight);
        popup.setFocusable(true);


        // Clear the default translucent background
        popup.setBackgroundDrawable(new BitmapDrawable());

        // Displaying the popup at the specified location, + offsets.
        popup.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);

        CircularImageView febIcon = (CircularImageView) layout.findViewById(R.id.child_fb_user_image);
        TextView viewTask = (TextView) layout.findViewById(R.id.child_fb_view_task);
        TextView goalBalance = (TextView) layout.findViewById(R.id.child_fb_balance);
     /* TextView calendar = (TextView) layout.findViewById(R.id.child_fb_calendar);
        TextView profile = (TextView) layout.findViewById(R.id.child_fb_profile);*/
        TextView logout = (TextView) layout.findViewById(R.id.child_fb_logout);
        try {
            viewTask.setCompoundDrawablesRelativeWithIntrinsicBounds((new IconDrawable(layout.getContext(), FontAwesomeIcons.fa_file_text_o)
                    .colorRes(R.color.check_in).sizeDp(AppConstant.FEB_ICON_SIZE)), null, null, null);
            goalBalance.setCompoundDrawablesRelativeWithIntrinsicBounds((new IconDrawable(layout.getContext(), FontAwesomeIcons.fa_usd)
                    .colorRes(R.color.check_in).sizeDp(AppConstant.FEB_ICON_SIZE)), null, null, null);
            /*calendar.setCompoundDrawablesRelativeWithIntrinsicBounds((new IconDrawable(layout.getContext(), FontAwesomeIcons.fa_calendar)
                    .colorRes(R.color.check_in).sizeDp(AppConstant.FEB_ICON_SIZE)), null, null, null);
            profile.setCompoundDrawablesRelativeWithIntrinsicBounds((new IconDrawable(layout.getContext(), FontAwesomeIcons.fa_user)
                    .colorRes(R.color.check_in).sizeDp(AppConstant.FEB_ICON_SIZE)), null, null, null);*/
            logout.setCompoundDrawablesRelativeWithIntrinsicBounds((new IconDrawable(layout.getContext(), FontAwesomeIcons.fa_sign_out)
                    .colorRes(R.color.check_in).sizeDp(AppConstant.FEB_ICON_SIZE)), null, null, null);
        }catch (Exception e){e.printStackTrace();}
        try {
            Picasso.with(view).load(childObject.getAvatar()).error(R.drawable.default_avatar).into(febIcon);
        } catch (Exception e) {
            e.printStackTrace();
        }
        viewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RestCall(view).authenticateUser(childObject.getEmail(), childObject.getPassword()
                        ,null,onScreen,progress);
            }
        });
        goalBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screenSwitch.checkBalance(childObject, null, null, onScreen,null, progress, AppConstant.CHILD);
                popup.dismiss();
            }
        });
      /*  calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lMessage.showToast("TODO: Calendar");
                popup.dismiss();

            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lMessage.showToast("TODO :Profile");
            }
        });*/
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showToast(activity, "logout");
                SharedPreferences sharedPreferences = activity.getSharedPreferences(AppConstant.FIREBASE_PREFERENCE, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                updateDeviceFCM();
                Intent intentLogout = new Intent(activity, LoginScreen.class);
                intentLogout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                Utils.showToast(activity, activity.getResources().getString(R.string.logout));
                activity.startActivity(intentLogout);
                popup.dismiss();

            }
        });
        febIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });
    }

    public void checkDeviceResolution(String fromScreen){
        switch (activity.getResources().getDisplayMetrics().densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                Utils.logDebug(TAG, "dpi : ldpi");
                popupWidth = 450;
                popupHeight = 500;
                if(fromScreen.equalsIgnoreCase(AppConstant.PARENT_DASHBOARD)){
                    OFFSET_X = -300;
                    OFFSET_Y = -20;
                }else if(fromScreen.equalsIgnoreCase(AppConstant.ADD_TASK)){
                    OFFSET_X = -290;
                    OFFSET_Y = -10;
                }else if(fromScreen.equalsIgnoreCase(AppConstant.CHECKED_IN_SCREEN) || fromScreen.equalsIgnoreCase(AppConstant.CHECKED_IN_TASK_APPROVAL__SCREEN)){
                    OFFSET_X = -300;
                    OFFSET_Y = -10;
                }else if(fromScreen.equalsIgnoreCase(AppConstant.GOAL_SCREEN)){
                    OFFSET_X = -290;
                    OFFSET_Y = -15;
                }
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                Utils.logDebug(TAG, "dpi : mdpi");
                popupWidth = 450;
                popupHeight = 500;
                if(fromScreen.equalsIgnoreCase(AppConstant.PARENT_DASHBOARD)){
                    OFFSET_X = -300;
                    OFFSET_Y = -20;
                }else if(fromScreen.equalsIgnoreCase(AppConstant.ADD_TASK)){
                    OFFSET_X = -290;
                    OFFSET_Y = -10;
                }else if(fromScreen.equalsIgnoreCase(AppConstant.CHECKED_IN_SCREEN) || fromScreen.equalsIgnoreCase(AppConstant.CHECKED_IN_TASK_APPROVAL__SCREEN)){
                    OFFSET_X = -300;
                    OFFSET_Y = -10;
                }else if(fromScreen.equalsIgnoreCase(AppConstant.GOAL_SCREEN)){
                    OFFSET_X = -290;
                    OFFSET_Y = -15;
                }
                break;
            case DisplayMetrics.DENSITY_HIGH:
                Utils.logDebug(TAG, "dpi : hdpi");
                popupWidth = 360;
                popupHeight = 400;
                if(fromScreen.equalsIgnoreCase(AppConstant.PARENT_DASHBOARD)){
                    OFFSET_X = -260;
                    OFFSET_Y = -8;
                }else if(fromScreen.equalsIgnoreCase(AppConstant.ADD_TASK)){
                    OFFSET_X = -255;
                    OFFSET_Y = -10;
                }else if(fromScreen.equalsIgnoreCase(AppConstant.CHECKED_IN_SCREEN) || fromScreen.equalsIgnoreCase(AppConstant.CHECKED_IN_TASK_APPROVAL__SCREEN)){
                    OFFSET_X = -260;
                    OFFSET_Y = -10;
                }else if(fromScreen.equalsIgnoreCase(AppConstant.GOAL_SCREEN)){
                    OFFSET_X = -255;
                    OFFSET_Y = -15;
                }
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                Utils.logDebug(TAG, "dpi : xhdpi");
                 popupWidth = 450;
                 popupHeight = 500;
                if(fromScreen.equalsIgnoreCase(AppConstant.PARENT_DASHBOARD)){
                    OFFSET_X = -300;
                    OFFSET_Y = -20;
                }else if(fromScreen.equalsIgnoreCase(AppConstant.ADD_TASK)){
                    OFFSET_X = -290;
                    OFFSET_Y = -10;
                }else if(fromScreen.equalsIgnoreCase(AppConstant.CHECKED_IN_SCREEN) || fromScreen.equalsIgnoreCase(AppConstant.CHECKED_IN_TASK_APPROVAL__SCREEN)){
                    OFFSET_X = -300;
                    OFFSET_Y = -10;
                }else if(fromScreen.equalsIgnoreCase(AppConstant.GOAL_SCREEN)){
                    OFFSET_X = -290;
                    OFFSET_Y = -15;
                }
                break;
            case DisplayMetrics.DENSITY_XXHIGH :
                Utils.logDebug(TAG, "dpi : xxhdpi");
                popupWidth = 600;
                popupHeight = 670;
                if(fromScreen.equalsIgnoreCase(AppConstant.PARENT_DASHBOARD)){
                    OFFSET_X = -370;
                    OFFSET_Y = -20;
                }else if(fromScreen.equalsIgnoreCase(AppConstant.ADD_TASK)){
                    OFFSET_X = -370;
                    OFFSET_Y = -15;
                }else if(fromScreen.equalsIgnoreCase(AppConstant.CHECKED_IN_SCREEN) || fromScreen.equalsIgnoreCase(AppConstant.CHECKED_IN_TASK_APPROVAL__SCREEN)){
                    OFFSET_X = -360;
                    OFFSET_Y = -10;
                }else if(fromScreen.equalsIgnoreCase(AppConstant.GOAL_SCREEN)){
                    OFFSET_X = -370;
                    OFFSET_Y = -15;
                }
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                Utils.logDebug(TAG, "dpi : xxxhdpi");
                popupWidth = 800;
                popupHeight = 970;
                if(fromScreen.equalsIgnoreCase(AppConstant.PARENT_DASHBOARD)){
                    OFFSET_X = -500;
                    OFFSET_Y = -20;
                }else if(fromScreen.equalsIgnoreCase(AppConstant.ADD_TASK)){
                    OFFSET_X = -480;
                    OFFSET_Y = -15;
                }else if(fromScreen.equalsIgnoreCase(AppConstant.CHECKED_IN_SCREEN) || fromScreen.equalsIgnoreCase(AppConstant.CHECKED_IN_TASK_APPROVAL__SCREEN)){
                    OFFSET_X = -500;
                    OFFSET_Y = -10;
                }else if(fromScreen.equalsIgnoreCase(AppConstant.GOAL_SCREEN)){
                    OFFSET_X = -490;
                    OFFSET_Y = -15;
                }
                break;
            case DisplayMetrics.DENSITY_420:
                Utils.logDebug(TAG, "dpi : 420dpi");
                popupWidth = 550;
                popupHeight = 620;
                if(fromScreen.equalsIgnoreCase(AppConstant.PARENT_DASHBOARD)){
                    OFFSET_X = -330;
                    OFFSET_Y = -10;
                }else if(fromScreen.equalsIgnoreCase(AppConstant.ADD_TASK)){
                    OFFSET_X = -340;
                    OFFSET_Y = -10;
                }else if(fromScreen.equalsIgnoreCase(AppConstant.CHECKED_IN_SCREEN) || fromScreen.equalsIgnoreCase(AppConstant.CHECKED_IN_TASK_APPROVAL__SCREEN)){
                    OFFSET_X = -325;
                    OFFSET_Y = -3;
                }else if(fromScreen.equalsIgnoreCase(AppConstant.GOAL_SCREEN)){
                    OFFSET_X = -340;
                    OFFSET_Y = -10;
                }
                break;
            case DisplayMetrics.DENSITY_560:
                Utils.logDebug(TAG, "dpi : 560dpi");
                popupWidth = 700;
                popupHeight = 820;
                if(fromScreen.equalsIgnoreCase(AppConstant.PARENT_DASHBOARD)){
                    OFFSET_X = -400;
                    OFFSET_Y = -10;
                }else if(fromScreen.equalsIgnoreCase(AppConstant.ADD_TASK)){
                    OFFSET_X = -420;
                    OFFSET_Y = -5;
                }else if(fromScreen.equalsIgnoreCase(AppConstant.CHECKED_IN_SCREEN) || fromScreen.equalsIgnoreCase(AppConstant.CHECKED_IN_TASK_APPROVAL__SCREEN)){
                    OFFSET_X = -400;
                    OFFSET_Y = -3;
                }else if(fromScreen.equalsIgnoreCase(AppConstant.GOAL_SCREEN)){
                    OFFSET_X = -420;
                    OFFSET_Y = -5;
                }
                break;
        }
    }

    public void checkDeviceResolutionChild(){
        switch (activity.getResources().getDisplayMetrics().densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                Utils.logDebug(TAG, "dpi : ldpi");
                popupWidth = 450;
                popupHeight = 280;
                OFFSET_X = -295;
                OFFSET_Y = -10;
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                Utils.logDebug(TAG, "dpi : mdpi");
                popupWidth = 450;
                popupHeight = 280;
                OFFSET_X = -295;
                OFFSET_Y = -10;
                break;
            case DisplayMetrics.DENSITY_HIGH:
                Utils.logDebug(TAG, "dpi : hdpi");
                popupWidth = 360;
                popupHeight = 240;
                OFFSET_X = -255;
                OFFSET_Y = -15;
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                Log.d(TAG, "dpi : xhdpi");
                popupWidth = 450;
                popupHeight = 280;
                OFFSET_X = -295;
                OFFSET_Y = -10;
                break;
            case DisplayMetrics.DENSITY_XXHIGH :
                Utils.logDebug(TAG, "dpi : xxhdpi");
                popupWidth = 600;
                popupHeight = 360;
                OFFSET_X = -370;
                OFFSET_Y = -15;
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                Utils.logDebug(TAG, "dpi : xxxhdpi");
                popupWidth = 800;
                popupHeight = 510;
                OFFSET_X = -490;
                OFFSET_Y = -15;
                break;
            case DisplayMetrics.DENSITY_420:
                Utils.logDebug(TAG, "dpi : 420dpi");
                popupWidth = 550;
                popupHeight = 360;
                OFFSET_X = -325;
                OFFSET_Y = -10;
                break;
            case DisplayMetrics.DENSITY_560:
                Utils.logDebug(TAG, "dpi : 560dpi");
                popupWidth = 720;
                popupHeight = 440;
                OFFSET_X = -420;
                OFFSET_Y = -10;
                break;
        }
    }

    public void updateDeviceFCM(){
        new AsyncTask<Void,Void,Void>()
        {
            @Override
            protected Void doInBackground(Void... params)
            {
                {
                    try
                    {
                        FirebaseInstanceId.getInstance().deleteInstanceId();
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                return null;
            }
            @Override
            protected void onPostExecute(Void result)
            {
            }
        }.execute();
    }
}
