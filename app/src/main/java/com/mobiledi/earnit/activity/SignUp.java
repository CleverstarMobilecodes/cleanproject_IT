package com.mobiledi.earnit.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobiledi.earnit.R;
import com.mobiledi.earnit.utils.AppConstant;
import com.mobiledi.earnit.utils.RestCall;
import com.mobiledi.earnit.utils.ScreenSwitch;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

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
 * Created by mradul on 8/3/17.
 */

public class SignUp extends BaseActivity implements OnClickListener, Validator.ValidationListener {

    SignUp  signUp;
    Validator  validator;
    @NotEmpty
    @Email
    EditText email;
    @Password(min = 6, scheme = Password.Scheme.ANY)
    EditText password;
    @ConfirmPassword
    EditText confirmPassword;
    Button cancelButton, signUpButton;
    RelativeLayout progress;
    ScreenSwitch screenSwitch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_layout);

        signUp = this;
        screenSwitch = new ScreenSwitch(signUp);
        email = (EditText) findViewById(R.id.input_email);
        password = (EditText) findViewById(R.id.input_password);
        confirmPassword = (EditText) findViewById(R.id.input_re_password);

        cancelButton = (Button) findViewById(R.id.cancel_button);
        signUpButton = (Button) findViewById(R.id.sign_up_button);

        progress = (RelativeLayout) findViewById(R.id.loadingPanel);

        cancelButton.setOnClickListener(signUp);
        signUpButton.setOnClickListener(signUp);
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.cancel_button:
                screenSwitch.moveToLogin();
                break;

            case R.id.sign_up_button:
                validator.validate();
                break;

        }

    }




    private void signUpParent() {



        try {
            JSONObject signUpJson = new JSONObject();
            signUpJson.put(AppConstant.EMAIL, email.getText().toString().trim());
            signUpJson.put(AppConstant.PASSWORD, password.getText().toString().trim());
            StringEntity entity = new StringEntity(signUpJson.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, AppConstant.APPLICATION_JSON));
            AsyncHttpClient httpClient = new AsyncHttpClient();
            httpClient.post(this, AppConstant.BASE_URL + AppConstant.SIGNUP_PARENT, entity, AppConstant.APPLICATION_JSON, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    showToast("Signup Success");
                    new RestCall(signUp).authenticateUser(email.getText().toString().trim(), password.getText().toString().trim(), password, AppConstant.SIGN_UP_SCREEN, progress);

                }
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    progress.setVisibility(View.GONE);
                    clearForm();
                    unLockScreen();
                    josnError(errorResponse);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    progress.setVisibility(View.GONE);
                    clearForm();
                    unLockScreen();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    progress.setVisibility(View.GONE);
                    clearForm();
                    unLockScreen();
                }

                @Override
                public void onStart() {
                    progress.setVisibility(View.VISIBLE);
                    lockScreen();
                }

                @Override
                public void onFinish() {
                    progress.setVisibility(View.GONE);
                    unLockScreen();
                }
            });
             }

          catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e){e.printStackTrace();}

    }

    public void clearForm(){
        email.setText("");
        password.setText("");
        confirmPassword.setText("");
//        showToast(getResources().getString(R.string.user_already_exists));
    }
    @Override
    public void onBackPressed() {
        screenSwitch.moveToLogin();
    }

    @Override
    public void onValidationSucceeded() {
        signUpParent();
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
}
