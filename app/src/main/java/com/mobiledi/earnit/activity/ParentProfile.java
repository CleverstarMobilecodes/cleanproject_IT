package com.mobiledi.earnit.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.mobiledi.earnit.R;
import com.mobiledi.earnit.adapter.ChildListAdapter;
import com.mobiledi.earnit.interfaces.ImageSelection;
import com.mobiledi.earnit.model.Child;
import com.mobiledi.earnit.model.Country;
import com.mobiledi.earnit.model.Parent;
import com.mobiledi.earnit.model.Tasks;
import com.mobiledi.earnit.utils.AppConstant;
import com.mobiledi.earnit.utils.GetObjectFromResponse;
import com.mobiledi.earnit.utils.ScalingUtilities;
import com.mobiledi.earnit.utils.ScreenSwitch;
import com.mobiledi.earnit.utils.Utils;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Pattern;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;
import id.zelory.compressor.Compressor;

/**
 * Created by mradul on 8/4/17.
 */

public class ParentProfile extends UploadRuntimePermission implements View.OnClickListener, Validator.ValidationListener{

    final int PERMISSIONS_REQUEST = 10;
    public  Intent in = null;
    private List<Child> childList = new ArrayList<>();
    public final String TAG = "ParentProfile";

    @Pattern(regex = "[a-zA-Z]+(\\\\s+[a-zA-Z]+)*",  message = "Please enter valid First name")
    @Length(max = 12, min = 2, message = "")
    EditText firstName;

    @Pattern(regex = "[a-zA-Z]+(\\\\s+[a-zA-Z]+)*",  message = "Please enter valid Last name")
    @Length(max = 12, min = 2,message = "")
    EditText lastName;

    @NotEmpty
    @Email
    EditText email;

    EditText phone;

    EditText currentPassword ;

    EditText newPassword ;

    EditText confirmPassword ;

    TextView changePassword, addChild;
    Button save, cancel;
    ParentProfile profile;
    Parent parentObject;
    Child child, otherChild;
    int childID;
    Validator validator;
    RelativeLayout progressBar;
    Parent updateParent;
    CircularImageView parentAvatar;
    RecyclerView childListView;
    TextView recyclerLayout;
    boolean isParentUpdate = false;
    String switchFrom;
    ScreenSwitch screenSwitch;
    ArrayList<Country> countries;
    TextView countryName, countryDial;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_profile_layout);
        profile = this;
        setViewId();
        screenSwitch = new ScreenSwitch(profile);
        countries = new ArrayList<>();
        countries = Utils.loadCountryData(TAG);
        parentObject = (Parent) getIntent().getSerializableExtra(AppConstant.PARENT_OBJECT);
        switchFrom = getIntent().getStringExtra(AppConstant.SCREEN);
        if(switchFrom.equalsIgnoreCase(AppConstant.CHECKED_IN_SCREEN) || switchFrom.equalsIgnoreCase(AppConstant.CHECKED_IN_TASK_APPROVAL__SCREEN))
            childID = getIntent().getIntExtra(AppConstant.CHILD_ID, 0);
        try {
            Picasso.with(profile).load(parentObject.getAvatar()).error(R.drawable.default_avatar).into(parentAvatar);
        }catch (Exception e){
            Picasso.with(profile).load(R.drawable.default_avatar).into(parentAvatar);
            e.printStackTrace();
        }
        email.setText(parentObject.getEmail());
        email.setEnabled(false);
        if(!parentObject.getPhone().isEmpty()){
            if(parentObject.getPhone().length() > 10){
                String parentPhone = parentObject.getPhone();
                Utils.logDebug(TAG, String.valueOf(parentPhone.length()));
                String pPhone = parentPhone.substring(parentPhone.length()-10, parentPhone.length());
                String cCountryCode = parentPhone.substring(0,parentPhone.length()-10);
                for(Country country : countries){
                    if(country.getCountryDialCode().equalsIgnoreCase(cCountryCode)){
                        countryName.setText(country.getCountryCode());
                        countryDial.setText(country.getCountryDialCode());
                        break;}
                    else {
                        countryName.setText(AppConstant.D_COUNTRIES_CODE);
                        countryDial.setText(AppConstant.D_COUNTRIES_DIAL);
                    }
                }
                phone.setText(pPhone);

            }else{
                countryName.setText(AppConstant.D_COUNTRIES_CODE);
                countryDial.setText(AppConstant.D_COUNTRIES_DIAL);
                phone.setText(parentObject.getPhone());}
        }
        if(!parentObject.getFirstName().isEmpty())
            firstName.setText(parentObject.getFirstName());
        if(!parentObject.getLastName().isEmpty())
            lastName.setText(parentObject.getLastName());
        requestRequiredApplicationPermission(new String[]{Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, R.string.msg, PERMISSIONS_REQUEST);
        validator = new Validator(profile);
        validator.setValidationListener(profile);
        fetchChildList();
        Utils.logDebug(TAG,"Child-list-response "+String.valueOf(childList.size()));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        childListView.setLayoutManager(mLayoutManager);
        childListView.setItemAnimator(new DefaultItemAnimator());
        setCursorPosition();

    }

    private void setCursorPosition() {
        Utils.SetCursorPosition(firstName);
        Utils.SetCursorPosition(lastName);
        Utils.SetCursorPosition(email);
        Utils.SetCursorPosition(phone);
    }
    private void setViewId() {

        firstName = (EditText) findViewById(R.id.parent_first_name);
        lastName = (EditText) findViewById(R.id.parent_last_name);
        email = (EditText) findViewById(R.id.parent_email);
        phone = (EditText) findViewById(R.id.parent_phone);
        changePassword = (TextView) findViewById(R.id.parent_password);
        addChild = (TextView) findViewById(R.id.parent_add_child);
        save = (Button) findViewById(R.id.save_button);
        cancel = (Button) findViewById(R.id.cancel_button);
        progressBar = (RelativeLayout) findViewById(R.id.loadingPanel);
        parentAvatar = (CircularImageView) findViewById(R.id.user_image);
        childListView = (RecyclerView) findViewById(R.id.child_list_id);
        recyclerLayout = (TextView) findViewById(R.id.chil_list_layout);
        countryName = (TextView) findViewById(R.id.countryCode);
        countryDial = (TextView) findViewById(R.id.country_dial_code);
        countryName.setCompoundDrawablesWithIntrinsicBounds(null, null, (new IconDrawable(profile, FontAwesomeIcons.fa_caret_down)
                .colorRes(R.color.edit_text_hint).sizeDp(AppConstant.FEB_ICON_SIZE)), null);
        save.setOnClickListener(profile);
        cancel.setOnClickListener(profile);
        addChild.setOnClickListener(profile);
        changePassword.setOnClickListener(profile);
        parentAvatar.setOnClickListener(profile);
        countryName.setOnClickListener(profile);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cancel_button:
                if(isParentUpdate)
                    moveToParentDashboard(updateParent);
                else
                    moveToParentDashboard(parentObject);
                break;

            case R.id.save_button:
                    if(firstName.getText().toString().trim().length() == 0)
                        firstName.setError("Please enter valid String");
                    else  validator.validate();
                break;

            case R.id.parent_add_child:
                screenSwitch.moveToAddChild(parentObject, childID, switchFrom, AppConstant.ADD, null);
                break;

            case R.id.parent_password:
                if(progressBar.getVisibility() == View.GONE)
                 showDialogOnCheckBox();
                break;

            case R.id.user_image:
                vRuntimePermission(parentAvatar);
                selectImage();
                break;

            case R.id.countryCode:
                Utils.showCountryDialog(countries, profile, countryDial, countryName);
                break;

        }

    }

    public void showDialogOnCheckBox() {
        final Dialog dialog = new Dialog(profile);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        window.getAttributes().windowAnimations = R.style.DialogAnimation;
        window.setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.change_password_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        currentPassword = (EditText) dialog.findViewById(R.id.current_password);
        newPassword = (EditText) dialog.findViewById(R.id.new_password);
        confirmPassword = (EditText) dialog.findViewById(R.id.confirm_password);

        Button declineButton = (Button) dialog.findViewById(R.id.cancel);
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });
        Button acceptButton = (Button) dialog.findViewById(R.id.ok);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!newPassword.getText().toString().trim().isEmpty() && !confirmPassword.getText().toString().trim().isEmpty() &&  !currentPassword.getText().toString().trim().isEmpty()){

                    if(currentPassword.getText().toString().equals(parentObject.getPassword())){
                        if(newPassword.getText().toString().trim().equals(confirmPassword.getText().toString().trim())){
                            dialog.dismiss();
                            updateParentProfile(parentObject.getAccount().getId(), parentObject.getId(),parentObject.getFirstName(),parentObject.getLastName(),parentObject.getAvatar(),parentObject.getEmail(),parentObject.getPhone(),newPassword.getText().toString().trim()
                                    , parentObject.getCreateDate(), true);
                        }else {
                            showToast("new password doesn't match");
                            newPassword.setText("");
                            confirmPassword.setText("");
                        }
                    }else{
                       showToast("current password incorrect");
                        currentPassword.setText("");
                    }
                }else {
                    showToast("Please enter all required fields");
                }

            }
        });
        dialog.show();
    }

    private void moveToParentDashboard(Parent parent) {
       if(switchFrom.equalsIgnoreCase(AppConstant.CHECKED_IN_SCREEN)){
           if(otherChild.getTasksArrayList().size() > 0)
               screenSwitch.moveToAllTaskScreen( child, otherChild, switchFrom, parent, switchFrom);
           else
               showToast(getResources().getString(R.string.no_task_available));
       }else if (switchFrom.equalsIgnoreCase(AppConstant.CHECKED_IN_TASK_APPROVAL__SCREEN)) {
           if (child.getTasksArrayList().size() > 0)
               screenSwitch.moveToAllTaskScreen( child, otherChild, switchFrom, parent, switchFrom);
           else
               showToast(getResources().getString(R.string.no_task_for_approval));
         }else
             screenSwitch.moveToParentDashboard(parent);
    }

    @Override
    public void onValidationSucceeded() {

        if(Utils.validatePhoneNumber(phone.getText().toString()) && phone.getText().toString().trim().length() ==10){
            if (gFileName != null){
                progressBar.setVisibility(View.VISIBLE);
                new ProfileAsyncTask().execute(gFileName);
            }else {
                updateParentProfile(parentObject.getAccount().getId(), parentObject.getId(),firstName.getText().toString().trim(),lastName.getText().toString().trim(),parentObject.getAvatar(),email.getText().toString().trim(),countryDial.getText()+phone.getText().toString().trim(),parentObject.getPassword()
                        , parentObject.getCreateDate(), false);
            }
        }else {
            phone.setError("Please enter 10 digit number only");
        }
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

    private void updateParentProfile(int accountId, int parentId, String firstName, String lastName, String avatar, String email, String phone, final String password, String createDate, final boolean isDialogCall) {

        JSONObject signInJson = new JSONObject();
        try {
            JSONObject accountObject = new JSONObject();
            accountObject.put(AppConstant.ID, accountId);
            accountObject.put(AppConstant.ACCOUNT_CODE, parentObject.getAccount().getAccountCode());
            accountObject.put(AppConstant.CREATE_DATE, new DateTime(parentObject.getAccount().getCreateDate()).getMillis());
            signInJson.put(AppConstant.ACCOUNT, accountObject);
            signInJson.put(AppConstant.ID, parentId);
            signInJson.put(AppConstant.AVATAR, avatar);
            signInJson.put(AppConstant.EMAIL, email);
            signInJson.put(AppConstant.PHONE, phone);
            signInJson.put(AppConstant.FIRST_NAME, firstName);
            signInJson.put(AppConstant.LAST_NAME, lastName);
            signInJson.put(AppConstant.CREATE_DATE, new DateTime(createDate).getMillis());
            signInJson.put(AppConstant.UPDATE_DATE, new DateTime().getMillis());
            signInJson.put(AppConstant.PASSWORD, password);
            signInJson.put(AppConstant.TYPE, parentObject.getUserType());
            signInJson.put(AppConstant.FCM_TOKEN, getSharedPreferences(AppConstant.FIREBASE_PREFERENCE, MODE_PRIVATE).getString(AppConstant.TOKEN_ID, null));
            Utils.logDebug(TAG,"profile-Json : "+ signInJson.toString());
            StringEntity entity = new StringEntity(signInJson.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, AppConstant.APPLICATION_JSON));
            AsyncHttpClient httpClient = new AsyncHttpClient();
            httpClient.setBasicAuth(parentObject.getEmail(), parentObject.getPassword());
            PersistentCookieStore myCookieStore = new PersistentCookieStore(profile);
            httpClient.setCookieStore(myCookieStore);
            httpClient.put(profile, AppConstant.BASE_URL + AppConstant.UPDATE_PARENT, entity, AppConstant.APPLICATION_JSON, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try{
                        updateAutoLoginCredential(response.getString(AppConstant.EMAIL), response.getString(AppConstant.PASSWORD));
                    }catch (JSONException e){e.printStackTrace();}
                    unLockScreen();
                    isParentUpdate = true;
                        updateParent = new GetObjectFromResponse().getParentObject(response);
                    if(!isDialogCall)
                       moveToParentDashboard(updateParent);
                 }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    unLockScreen();
                    josnError(errorResponse);

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    unLockScreen();
                    showToast(getResources().getString(R.string.other_message));

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
        if(isParentUpdate)
            moveToParentDashboard(updateParent);
        else
            moveToParentDashboard(parentObject);
    }

    public class ProfileAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                AmazonS3Client s3 = new AmazonS3Client(new BasicAWSCredentials(AppConstant.ACCESS_KEY_KEY, AppConstant.SECRET_ACCESS_KEY));
                s3.setRegion(Region.getRegion(Regions.US_WEST_2));

                File filePath = new File(params[0]);
                File compressedImageFile = new Compressor(profile).compressToFile(filePath);
                String fileName = AppConstant.PARENT_IMAGE_FOLDER + AppConstant.SUFFIX + String.valueOf(new SimpleDateFormat(AppConstant.IMAGE_DATE_FORMAT).format(new Date()));
                s3.putObject(new PutObjectRequest(AppConstant.BUCKET_NAME, fileName, compressedImageFile).withCannedAcl(CannedAccessControlList.PublicRead));
                String profileUrl = String.valueOf(s3.getResourceUrl(AppConstant.BUCKET_NAME, fileName));
                return profileUrl;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String userImage) {
            gFileName = null;
            updateParentProfile(parentObject.getAccount().getId(), parentObject.getId(),firstName.getText().toString().trim(),lastName.getText().toString().trim(), userImage, email.getText().toString().trim(),countryDial.getText()+phone.getText().toString().trim(),parentObject.getPassword()
                    , parentObject.getCreateDate(), false);        }

        @Override
        protected void onPreExecute() {

        }

    }

    public void fetchChildList(){
        AsyncHttpClient client = new AsyncHttpClient();
        client.setBasicAuth(parentObject.getEmail(), parentObject.getPassword());
        client.get(AppConstant.BASE_URL + AppConstant.CHILDREN_API + parentObject.getAccount().getId(), null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                childList.clear();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject childObject = response.getJSONObject(i);
                            Child cList =  new GetObjectFromResponse().getChildObject(childObject);
                            if(childID == childObject.getInt(AppConstant.ID)){
                                child = new GetObjectFromResponse().getChildObject(childObject);
                                otherChild = new GetObjectFromResponse().getChildObject(childObject);

                                ArrayList<Tasks> taskList = new ArrayList<>();
                                ArrayList<Tasks> otherTaskList = new ArrayList<>();
                                JSONArray taskArray = childObject.getJSONArray(AppConstant.TASKS);
                                for (int taskIndex = 0; taskIndex < taskArray.length(); taskIndex++){
                                    JSONObject taskObject = taskArray.getJSONObject(taskIndex);
                                    if(taskObject.getString(AppConstant.STATUS).equals(AppConstant.COMPLETED)){
                                        Tasks task = new GetObjectFromResponse().getTaskObject(taskObject,child.getId());
                                        taskList.add(task);
                                    }
                                    JSONObject othertaskObject = taskArray.getJSONObject(taskIndex);
                                    if(!othertaskObject.getString(AppConstant.STATUS).equals(AppConstant.APPROVED)){
                                        Tasks task = new GetObjectFromResponse().getTaskObject(othertaskObject,otherChild.getId());
                                        otherTaskList.add(task);
                                    }
                                }
                                child.setTasksArrayList(taskList);
                                otherChild.setTasksArrayList(otherTaskList);
                            }
                        childList.add(cList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (childList.size()>0){
                    recyclerLayout.setVisibility(View.VISIBLE);
                    childListView.setVisibility(View.VISIBLE);
                     childListView.setAdapter(new ChildListAdapter(profile, childList,parentObject, switchFrom, childID));}
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {

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

    public void updateAutoLoginCredential(String email, String password){
            SharedPreferences shareToken = getSharedPreferences(AppConstant.FIREBASE_PREFERENCE, MODE_PRIVATE);
            SharedPreferences.Editor editor = shareToken.edit();
            editor.putString(AppConstant.EMAIL, email);
            editor.putString(AppConstant.PASSWORD, password);
            editor.commit();
    }
}
