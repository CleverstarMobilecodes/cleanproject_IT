package com.mobiledi.earnit.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
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
import com.mobiledi.earnit.adapter.CountryAdapter;
import com.mobiledi.earnit.interfaces.ImageSelection;
import com.mobiledi.earnit.model.Country;
import com.mobiledi.earnit.model.Parent;
import com.mobiledi.earnit.utils.AppConstant;
import com.mobiledi.earnit.utils.GetObjectFromResponse;
import com.mobiledi.earnit.utils.ScreenSwitch;
import com.mobiledi.earnit.utils.Utils;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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
 * Created by mobile-di on 18/10/17.
 */

public class InitialParentProfile  extends UploadRuntimePermission implements Validator.ValidationListener, View.OnClickListener, CountryAdapter.CountryListner{

    @Pattern(regex = "[a-zA-Z]+(\\\\s+[a-zA-Z]+)*",  message = "Please enter valid First name")
    @Length(max = 12, min = 2, message = "")
    EditText firstName;

    @Pattern(regex = "[a-zA-Z]+(\\\\s+[a-zA-Z]+)*",  message = "Please enter valid Last name")
    @Length(max = 12, min = 2,message = "")
    EditText lastName;

    EditText phone;

    Button home;
    CircularImageView avatar;
    InitialParentProfile profile;
    private final String TAG="InitialParentProfile";
    final int PERMISSIONS_REQUEST = 11;
    public Intent in = null;
    Parent parentObject, updateParent;
    RelativeLayout progressBar;
    ScreenSwitch screenSwitch;
    int bCount = 0;
    long time;
    Validator validator;
    ArrayList<Country> countries;
    TextView countryName, countryDial;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.initial_parent_profile);
        profile = this;
        setView();
        setCursorPosition();
        screenSwitch = new ScreenSwitch(profile);
        parentObject = (Parent) getIntent().getSerializableExtra(AppConstant.PARENT_OBJECT);
        setParentDetils();
        validator = new Validator(profile);
        countries = new ArrayList<>();
        validator.setValidationListener(profile);
        countries = Utils.loadCountryData(TAG);
        requestRequiredApplicationPermission(new String[]{Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, R.string.msg, PERMISSIONS_REQUEST);
    }

    private void setView() {
        firstName = (EditText) findViewById(R.id.f_name);
        lastName = (EditText) findViewById(R.id.l_name);
        phone = (EditText) findViewById(R.id.p_phone);
        home = (Button) findViewById(R.id.home);
        avatar = (CircularImageView) findViewById(R.id.p_avatar);
        progressBar = (RelativeLayout) findViewById(R.id.loadingPanel);
        countryName = (TextView) findViewById(R.id.countryCode);
        countryDial = (TextView) findViewById(R.id.country_dial_code);
        countryName.setCompoundDrawablesWithIntrinsicBounds(null, null, (new IconDrawable(profile, FontAwesomeIcons.fa_caret_down)
                .colorRes(R.color.edit_text_hint).sizeDp(AppConstant.FEB_ICON_SIZE)), null);
        avatar.setOnClickListener(profile);
        home.setOnClickListener(profile);
        countryName.setOnClickListener(profile);
    }

    private void setParentDetils(){
        if(!parentObject.getFirstName().isEmpty())
            phone.setText(parentObject.getPhone());
        if(!parentObject.getFirstName().isEmpty())
            firstName.setText(parentObject.getFirstName());
        if(!parentObject.getLastName().isEmpty())
            lastName.setText(parentObject.getLastName());
    }

    private void setCursorPosition() {
        Utils.SetCursorPosition(firstName);
        Utils.SetCursorPosition(lastName);
        Utils.SetCursorPosition(phone);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home:
                validator.validate();
                break;

            case R.id.p_avatar:
                vRuntimePermission(avatar);
                selectImage();
                break;

            case R.id.countryCode:
                Utils.showCountryDialog(countries, InitialParentProfile.this, countryDial, countryName);
                break;

        }
    }

    @Override
    public void onValidationSucceeded() {

            if(Utils.validatePhoneNumber(phone.getText().toString()) && phone.getText().toString().trim().length() ==10) {
                if (gFileName != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    new ProfileAsyncTask().execute(gFileName);
                } else {
                    updateParentProfile(parentObject.getAccount().getId(), parentObject.getId(), firstName.getText().toString().trim(), lastName.getText().toString().trim(), parentObject.getAvatar(), parentObject.getEmail(), countryDial.getText()+phone.getText().toString().trim(), parentObject.getPassword()
                            , parentObject.getCreateDate());
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
    @Override
    public void onItemClick(Country country) {

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
            updateParentProfile(parentObject.getAccount().getId(), parentObject.getId(),firstName.getText().toString().trim(),lastName.getText().toString().trim(), userImage, parentObject.getEmail(),countryDial.getText()+phone.getText().toString().trim(),parentObject.getPassword()
                    , parentObject.getCreateDate());        }

        @Override
        protected void onPreExecute() {

        }
    }

    private void updateParentProfile(int accountId, int parentId, String firstName, String lastName, String avatar, String email, String phone, final String password, String createDate) {

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
                    updateParent = new GetObjectFromResponse().getParentObject(response);
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

    public void updateAutoLoginCredential(String email, String password){
        SharedPreferences shareToken = getSharedPreferences(AppConstant.FIREBASE_PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = shareToken.edit();
        editor.putString(AppConstant.EMAIL, email);
        editor.putString(AppConstant.PASSWORD, password);
        editor.commit();
    }



    private void moveToParentDashboard(Parent parent) {
        screenSwitch.moveToParentDashboard(parent);
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


}
