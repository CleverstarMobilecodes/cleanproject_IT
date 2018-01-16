package com.mobiledi.earnit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobiledi.earnit.R;
import com.mobiledi.earnit.adapter.ChildrenAdapter;
import com.mobiledi.earnit.model.Child;
import com.mobiledi.earnit.model.Parent;
import com.mobiledi.earnit.model.Tasks;
import com.mobiledi.earnit.utils.AppConstant;
import com.mobiledi.earnit.utils.GetObjectFromResponse;
import com.mobiledi.earnit.utils.NavigationDrawer;
import com.mobiledi.earnit.utils.RestCall;
import com.mobiledi.earnit.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by mradul on 7/5/17.
 */

public class ParentDashboard extends BaseActivity implements NavigationDrawer.OnDrawerToggeled {


    public final String TAG = "ParentDashboard";
    public static Parent parentObject;
    private ParentDashboard parentDashboard;
    private TextView headerText;
    private Toolbar toolbar;
//    private Button tipsBtn;
    private ImageButton drawerToggle;
    private AlphaAnimation buttonClickAnimation;
    private RelativeLayout progressBar;
    private List<Child> childList = new ArrayList<>();
    private List<Child> childApprovalList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ChildrenAdapter mAdapter;
//    public static Map<Integer, String> childs;
    int bCount = 0;
    long time;
    Handler handler;
    Runnable runnable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get the Parent Object

        Intent intent = getIntent();
        parentObject = (Parent) intent.getSerializableExtra(AppConstant.PARENT_OBJECT);

        //Initialize FontAwesome
        Iconify.with(new FontAwesomeModule());
        //Animation for buttons
        buttonClickAnimation = new AlphaAnimation(1F, 0.1F);

        setContentView(R.layout.parent_landing_layout);
        parentDashboard = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerToggle = (ImageButton) findViewById(R.id.drawerBtn);
        progressBar = (RelativeLayout) findViewById(R.id.loadingPanel);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        recyclerView = (RecyclerView) findViewById(R.id.children_list);
        mAdapter = new ChildrenAdapter(this, childList, childApprovalList, parentObject, progressBar);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        headerText = (TextView) toolbar.findViewById(R.id.textView);
        if(parentObject.getFirstName().isEmpty())
            headerText.setText("Hi");
        else
            headerText.setText("Hi "+parentObject.getFirstName());

        NavigationDrawer navigationDrawer = new NavigationDrawer(parentDashboard, parentObject,toolbar,drawerToggle, AppConstant.PARENT_DASHBOARD, 0);
         getDashBoardData();
            callApi();

        navigationDrawer.setOnDrawerToggeled(this);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void getDashBoardData() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setBasicAuth(parentObject.getEmail(), parentObject.getPassword());
        Utils.logDebug(TAG, "Child response request: "+AppConstant.BASE_URL + AppConstant.CHILDREN_API + parentObject.getId());
        client.get(AppConstant.BASE_URL + AppConstant.CHILDREN_API + parentObject.getAccount().getId(), null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Utils.logDebug(TAG, "Child Success response: "+response.toString());

                childList.clear();
                childApprovalList.clear();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject childObject = response.getJSONObject(i);

                        //child with non-approval task
                        Child child = new GetObjectFromResponse().getChildObject(childObject);

                        //child with approval task
                        Child childApprovalTask = new GetObjectFromResponse().getChildObject(childObject);
                        ArrayList<Tasks> taskList = new ArrayList<>();
                        ArrayList<Tasks> taskApprovalList = new ArrayList<>();

                        JSONArray taskArray = response.getJSONObject(i).getJSONArray(AppConstant.TASKS);
                        for (int taskIndex = 0; taskIndex < taskArray.length(); taskIndex++) {
                            JSONObject taskObject = taskArray.getJSONObject(taskIndex);
                            if(!taskObject.getString(AppConstant.STATUS).equals(AppConstant.APPROVED)){
                                Tasks task = new GetObjectFromResponse().getTaskObject(taskObject,childObject.getInt(AppConstant.ID));

                                taskList.add(task);
                            }

                            if(taskObject.getString(AppConstant.STATUS).equals(AppConstant.COMPLETED)){
                                Tasks task = new GetObjectFromResponse().getTaskObject(taskObject,childObject.getInt(AppConstant.ID));

                                taskApprovalList.add(task);
                            }
                        }
                        child.setTasksArrayList(taskList);
                        childApprovalTask.setTasksArrayList(taskApprovalList);
                        childList.add(child);
                        childApprovalList.add(childApprovalTask);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Utils.logDebug(TAG, "Child error response: "+ errorResponse.toString());

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Utils.logDebug(TAG, "Child JSONObject response" + response.toString());

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
                    new RestCall(parentDashboard).authenticateUser(parentObject.getEmail(), parentObject.getPassword()
                            ,null,AppConstant.PARENT_SCREEN,progressBar);
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
    public void onDrawerToggeled() {
        if(mAdapter != null)
            mAdapter.closeFebMenu();
    }
}
