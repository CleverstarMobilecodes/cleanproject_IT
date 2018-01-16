package com.mobiledi.earnit.activity;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
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
import com.mobiledi.earnit.interfaces.ImageSelection;
import com.mobiledi.earnit.model.Child;
import com.mobiledi.earnit.model.Country;
import com.mobiledi.earnit.model.Parent;
import com.mobiledi.earnit.utils.AppConstant;
import com.mobiledi.earnit.utils.ScalingUtilities;
import com.mobiledi.earnit.utils.ScreenSwitch;
import com.mobiledi.earnit.utils.Utils;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
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

public class AddChild extends UploadRuntimePermission implements View.OnClickListener, Validator.ValidationListener{

    final int PERMISSIONS_REQUEST = 13;
    public  Intent in = null;

    @Pattern(regex = "[a-zA-Z ]+(\\\\s+[a-zA-Z ]+)*",  message = "Please enter valid username")
    @Length(max = 12, min = 2,message = "")
    EditText firstName;

    @NotEmpty
    @Email
    EditText email;

    EditText phone;

    @Password(min = 6, scheme = Password.Scheme.ANY)
    EditText password;

    @ConfirmPassword
    EditText confirmPassword;

    Button save, cancel;
    AddChild addChild;
    Parent parentObject;
    Child child;
    Validator validator;
    RelativeLayout progressBar;
    CircularImageView childAvatar;
    TextView addChildHeader;
    String mode, switchFrom;
    int childId;
    public final String TAG = "AddChild";
    ScreenSwitch screenSwitch;
    ArrayList<Country> countries;
    TextView countryName, countryDial;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_child_layout);
        addChild = this;
        setViewId();
        screenSwitch = new ScreenSwitch(addChild);
        parentObject = (Parent) getIntent().getSerializableExtra(AppConstant.PARENT_OBJECT);
        child = (Child) getIntent().getSerializableExtra(AppConstant.CHILD_OBJECT);
        childId = getIntent().getIntExtra(AppConstant.CHILD_ID, 0);
        mode = getIntent().getStringExtra(AppConstant.MODE);
        switchFrom = getIntent().getStringExtra(AppConstant.SCREEN);
        countries = new ArrayList<>();
        countries = Utils.loadCountryData(TAG);
        if(mode.equalsIgnoreCase(AppConstant.UPDATE)){
            addChildHeader.setText(AppConstant.EDIT+" Child");
            save.setText(AppConstant.UPDATE);
            try {
                Picasso.with(addChild).load(child.getAvatar()).error(R.drawable.default_avatar).into(childAvatar);
            }catch (Exception e){
                e.printStackTrace();
                Picasso.with(addChild).load(R.drawable.default_avatar).into(childAvatar);
            }
            email.setText(child.getEmail());
            firstName.setText(child.getFirstName().substring(0,1)+child.getFirstName().substring(1));
            password.setText(child.getPassword());
            confirmPassword.setText(child.getPassword());

                if (!child.getPhone().isEmpty()) {
                    if(child.getPhone().length() > 10){
                        String childPhone = child.getPhone();
                        Utils.logDebug(TAG, String.valueOf(childPhone.length()));
                        String cPhone = childPhone.substring(childPhone.length()-10, childPhone.length());
                        String cCountryCode = childPhone.substring(0,childPhone.length()-10);
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
                        phone.setText(cPhone);

                    }else{
                        countryName.setText(AppConstant.D_COUNTRIES_CODE);
                        countryDial.setText(AppConstant.D_COUNTRIES_DIAL);
                        phone.setText(child.getPhone());}
                }
        }else save.setText(AppConstant.SAVE);
        requestRequiredApplicationPermission(new String[]{Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, R.string.msg, PERMISSIONS_REQUEST);

        validator = new Validator(addChild);
        validator.setValidationListener(addChild);
        setCursorPosition();
    }

    private void setViewId() {

        firstName = (EditText) findViewById(R.id.child_first_name);
        email = (EditText) findViewById(R.id.child_email);
        phone = (EditText) findViewById(R.id.child_phone);
        password = (EditText) findViewById(R.id.child_password);
        confirmPassword = (EditText) findViewById(R.id.child_confirm_password);
        save = (Button) findViewById(R.id.save_button);
        cancel = (Button) findViewById(R.id.cancel_button);
        progressBar = (RelativeLayout) findViewById(R.id.loadingPanel);
        childAvatar = (CircularImageView) findViewById(R.id.child_user_image);
        addChildHeader = (TextView) findViewById(R.id.add_child_header_id);
        countryName = (TextView) findViewById(R.id.countryCode);
        countryDial = (TextView) findViewById(R.id.country_dial_code);
        countryName.setCompoundDrawablesWithIntrinsicBounds(null, null, (new IconDrawable(addChild, FontAwesomeIcons.fa_caret_down)
                .colorRes(R.color.edit_text_hint).sizeDp(AppConstant.FEB_ICON_SIZE)), null);
        save.setOnClickListener(addChild);
        cancel.setOnClickListener(addChild);
        childAvatar.setOnClickListener(addChild);
        countryName.setOnClickListener(addChild);
    }

    private void setCursorPosition() {
        Utils.SetCursorPosition(firstName);
        Utils.SetCursorPosition(email);
        Utils.SetCursorPosition(phone);
        Utils.SetCursorPosition(password);
        Utils.SetCursorPosition(confirmPassword);
    }
    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.cancel_button:
                screenSwitch.moveToParentProfile(childId, parentObject, switchFrom);

                break;

            case R.id.save_button:
                validator.validate();
                break;

            case R.id.child_user_image:
                vRuntimePermission(childAvatar);
                selectImage();
                break;

            case R.id.countryCode:
                Utils.showCountryDialog(countries, addChild, countryDial, countryName);
                break;



        }
    }

    @Override
    public void onBackPressed() {
        screenSwitch.moveToParentProfile(childId, parentObject, switchFrom);
    }

    @Override
    public void onValidationSucceeded() {
        if(Utils.validatePhoneNumber(phone.getText().toString()) && phone.getText().toString().trim().length() ==10){
            if (gFileName != null){
                progressBar.setVisibility(View.VISIBLE);
                new ProfileAsyncTask().execute(gFileName);
            }else if(mode.equalsIgnoreCase(AppConstant.UPDATE)) {
                if (child.getAvatar().isEmpty())
                    addChild(null);
                else
                    addChild(child.getAvatar());
            }else{
                addChild(null);
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


    private void addChild(String childImage) {

        final JSONObject signInJson = new JSONObject();
        try {
            signInJson.put(AppConstant.ACCOUNT, new JSONObject().put(AppConstant.ID, parentObject.getAccount().getId()));
            signInJson.put(AppConstant.EMAIL, email.getText().toString().trim());
            signInJson.put(AppConstant.FIRST_NAME, firstName.getText().toString().trim());
            signInJson.put(AppConstant.LAST_NAME, null);
            signInJson.put(AppConstant.PASSWORD, password.getText().toString().trim());
            signInJson.put(AppConstant.PHONE, countryDial.getText()+phone.getText().toString().trim());
            signInJson.put(AppConstant.CREATE_DATE, new DateTime().getMillis());
            signInJson.put(AppConstant.UPDATE_DATE, new DateTime().getMillis());

            if(mode.equalsIgnoreCase(AppConstant.UPDATE)){
                signInJson.put(AppConstant.ID,child.getId());
                signInJson.put(AppConstant.FCM_TOKEN, child.getFcmToken());}

            signInJson.put(AppConstant.AVATAR, childImage);
            Utils.logDebug(TAG+" add-child-json", signInJson.toString());
            StringEntity entity = new StringEntity(signInJson.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, AppConstant.APPLICATION_JSON));
            AsyncHttpClient httpClient = new AsyncHttpClient();
            httpClient.setBasicAuth(parentObject.getEmail(), parentObject.getPassword());
            PersistentCookieStore myCookieStore = new PersistentCookieStore(addChild);
            httpClient.setCookieStore(myCookieStore);
            if(mode.equalsIgnoreCase(AppConstant.UPDATE))
                httpClient.put(addChild, AppConstant.BASE_URL + AppConstant.UPDATE_CHILD, entity, AppConstant.APPLICATION_JSON, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Utils.logDebug(TAG+" onSuccess", response.toString());
                        if(mode.equalsIgnoreCase(AppConstant.UPDATE))
                            showToast(firstName.getText()+" updated");
                        else
                            showToast(firstName.getText()+" added");

                        screenSwitch.moveToParentProfile(childId, parentObject, switchFrom);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        Utils.logDebug(TAG+" onSuccess", response.toString());

                        if(mode.equalsIgnoreCase(AppConstant.UPDATE))
                            showToast(firstName.getText()+" updated");
                        else
                            showToast(firstName.getText()+" added");

                        screenSwitch.moveToParentProfile(childId, parentObject, switchFrom);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Utils.logDebug(TAG+" onFailure", throwable.toString());
                        unLockScreen();
                        josnError(errorResponse);

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
            else
                httpClient.post(addChild, AppConstant.BASE_URL + AppConstant.ADD_CHILD_PARENT, entity, AppConstant.APPLICATION_JSON, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Utils.logDebug(TAG+" onSuccess", response.toString());
                        if(mode.equalsIgnoreCase(AppConstant.UPDATE))
                            showToast(firstName.getText()+" updated");
                        else
                            showToast(firstName.getText()+" added");

                        screenSwitch.moveToParentProfile(childId, parentObject, switchFrom);

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        Utils.logDebug(TAG+" onSuccess", response.toString());

                        if(mode.equalsIgnoreCase(AppConstant.UPDATE))
                            showToast(firstName.getText()+" updated");
                        else
                            showToast(firstName.getText()+" added");

                        screenSwitch.moveToParentProfile(childId, parentObject, switchFrom);

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Utils.logDebug(TAG+" onFailure", throwable.toString());
                        Toast.makeText(addChild,getResources().getString(R.string.user_already_exists), Toast.LENGTH_LONG).show();
                        unLockScreen();
                        josnError(errorResponse);
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


    public class ProfileAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                AmazonS3Client s3 = new AmazonS3Client(new BasicAWSCredentials(AppConstant.ACCESS_KEY_KEY, AppConstant.SECRET_ACCESS_KEY));
                s3.setRegion(Region.getRegion(Regions.US_WEST_2));

                File filePath = new File(params[0]);
                File compressedImageFile = new Compressor(addChild).compressToFile(filePath);
                String fileName = AppConstant.CHILD_IMAGE_FOLDER + AppConstant.SUFFIX + String.valueOf(new SimpleDateFormat(AppConstant.IMAGE_DATE_FORMAT).format(new Date()));
                s3.putObject(new PutObjectRequest(AppConstant.BUCKET_NAME, fileName, compressedImageFile).withCannedAcl(CannedAccessControlList.PublicRead));
                String profileUrl = String.valueOf(s3.getResourceUrl(AppConstant.BUCKET_NAME, fileName));
                Utils.logDebug(TAG+"return image url", profileUrl);
                return profileUrl;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String userImage) {
            gFileName = null;
            addChild(userImage);       }

        @Override
        protected void onPreExecute() {

        }

    }


}
