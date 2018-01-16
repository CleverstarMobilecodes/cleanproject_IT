package com.mobiledi.earnit.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mobiledi.earnit.R;
import com.mobiledi.earnit.adapter.ChildViewDateAdapter;
import com.mobiledi.earnit.model.Child;
import com.mobiledi.earnit.model.ChildsTaskObject;
import com.mobiledi.earnit.service.UpdateFcmToken;
import com.mobiledi.earnit.utils.AppConstant;
import com.mobiledi.earnit.utils.FloatingMenu;
import com.mobiledi.earnit.utils.GetObjectFromResponse;
import com.mobiledi.earnit.utils.RestCall;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;


public class ChildDashboard extends BaseActivity {
    private static final int FEB_ICON_SIZE = 15;
    ChildDashboard childDashboard;
    private Child childObject;
    private RecyclerView childTaskDateList;
    private CircularImageView childImage;
    private ChildViewDateAdapter childViewDateAdapter;
    private RelativeLayout progress;
    private TextView childDashboardHeader;
    int bCount = 0;
    long time;
    Handler handler;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_dashboard);
        childDashboard = this;
        //SERIALIZE OBJECT FROM INTENT OBJECT
        Intent intent = getIntent();
        childObject = (Child) intent.getSerializableExtra(AppConstant.CHILD_OBJECT);
        //SET PROFILE IMAGE
        childImage = (CircularImageView) findViewById(R.id.child_dashboard_avatar);
        childDashboardHeader = (TextView) findViewById(R.id.child_dashboard_header);
        childDashboardHeader.setText(getResources().getString(R.string.my_task));
        try{
        Picasso.with(childDashboard.getApplicationContext()).load(childObject.getAvatar()).error(R.drawable.default_avatar).into(childImage);
        }catch (Exception e){
            e.printStackTrace();
            Picasso.with(childDashboard.getApplicationContext()).load(R.drawable.default_avatar).into(childImage);
        }
        progress = (RelativeLayout) findViewById(R.id.loadingPanel);
        childTaskDateList = (RecyclerView) findViewById(R.id.child_task_date_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        childTaskDateList.setLayoutManager(mLayoutManager);
        childTaskDateList.setItemAnimator(new DefaultItemAnimator());
        List<ChildsTaskObject>  childTaskObjects = new GetObjectFromResponse().getChildTaskListObject(childObject, AppConstant.CHILD, AppConstant.CHECKED_IN_SCREEN);
        if(childTaskObjects.size() > 0) {
            childViewDateAdapter = new ChildViewDateAdapter(progress, childDashboard, childTaskObjects, childObject, null, AppConstant.CHILD, AppConstant.CHILD_DASHBOARD_SCREEN, false, null);
            childTaskDateList.setAdapter(childViewDateAdapter);
        }else showToast(getResources().getString(R.string.please_ask_parent_to_add_task));
        childImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FloatingMenu(childDashboard).fetchAvatarDimension(childImage, childObject, AppConstant.CHILD_DASHBOARD_SCREEN, progress);


            }
        });
        callApi();

    }

    private void showDateTimePicker() {
        Calendar now = Calendar.getInstance();

        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog datePickerDialog, int i, int i1, int i2) {

                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), AppConstant.DATE_PICKER_DIALOG);


    }


    @Override
    public void onBackPressed() {
        bCount++;


        if (bCount == 1) {
            time = System.currentTimeMillis();
            showToast(getResources().getString(R.string.back_to_exit));
        } else {
            if (System.currentTimeMillis() - time > 4000) {

                bCount = 0;
                showToast(getResources().getString(R.string.back_to_exit));
            } else {
                finish();
            }

        }
    }


    public void callApi(){
         handler = new Handler();
         runnable = new Runnable()
        {
            public void run()
            {

                try {
                    new RestCall(childDashboard).authenticateUser(childObject.getEmail(), childObject.getPassword()
                            ,null,AppConstant.CHILD_DASHBOARD_SCREEN,progress);
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
    public void logoutChild(final Child child){
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
                Intent updateToken = new Intent(childDashboard, UpdateFcmToken.class);
                updateToken.putExtra(AppConstant.IS_LOGOUT, true);
                updateToken.putExtra(AppConstant.CHILD_OBJECT, child);
                updateToken.putExtra(AppConstant.MODE, AppConstant.CHILD);
                startService(updateToken);          }
        }.execute();
    }
}
