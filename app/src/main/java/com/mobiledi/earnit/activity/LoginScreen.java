package com.mobiledi.earnit.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.mobiledi.earnit.R;
import com.mobiledi.earnit.utils.AppConstant;
import com.mobiledi.earnit.utils.RestCall;
import com.mobiledi.earnit.utils.Utils;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

/**
 * Created by mradul on 7/4/17.
 */

public class LoginScreen extends BaseActivity implements View.OnClickListener, Validator.ValidationListener {
    @NotEmpty
    @Email
    EditText username;
    @Password(min = 6, scheme = Password.Scheme.ANY)

    EditText password;
    Button loginButton, sign_up;
    LoginScreen loginScreen;
    RelativeLayout progressBar;
    SharedPreferences preferences;
    Validator validator;
    private final String TAG = "LoginScreen";
    Resources resources;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_layout);
        loginScreen = this;
        username = (EditText) findViewById(R.id.input_email);
        password = (EditText) findViewById(R.id.input_password);
        loginButton = (Button) findViewById(R.id.login);
        sign_up = (Button) findViewById(R.id.sign_up);
        progressBar = (RelativeLayout) findViewById(R.id.loadingPanel);
        username.setOnClickListener(loginScreen);
        password.setOnClickListener(loginScreen);
        loginButton.setOnClickListener(loginScreen);
        sign_up.setOnClickListener(loginScreen);
        resources = getResources();
        setCursorPosition();
        callFirebaseService();
        preferences = getSharedPreferences(AppConstant.FIREBASE_PREFERENCE, MODE_PRIVATE);
        try{
        if(preferences.getString(AppConstant.EMAIL, null) != null && !preferences.getString(AppConstant.EMAIL, null).equals(null)){
            new RestCall(loginScreen).authenticateUser(preferences.getString(AppConstant.EMAIL, null), preferences.getString(AppConstant.PASSWORD, null), password, AppConstant.LOGIN_SCREEN, progressBar);
        }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        validator = new Validator(loginScreen);
        validator.setValidationListener(loginScreen);
    }

    private void callFirebaseService() {
        new AsyncTask<Void,Void,String>()
        {
            @Override
            protected String doInBackground(Void... params)
            {
                String token = FirebaseInstanceId.getInstance().getToken();
                while(token == null)//this is used to get firebase token until its null so it will save you from null pointer exeption
                {
                    token = FirebaseInstanceId.getInstance().getToken();
                }
                return token;
            }
            @Override
            protected void onPostExecute(String result)
            {
                SharedPreferences shareToken = getSharedPreferences(AppConstant.FIREBASE_PREFERENCE, MODE_PRIVATE);
                SharedPreferences.Editor editor = shareToken.edit();
                editor.putString(AppConstant.TOKEN_ID, result);
                editor.commit();
            }
        }.execute();
    }

    private void setCursorPosition() {
        Utils.SetCursorPosition(username);
        Utils.SetCursorPosition(password);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                validator.validate();
                break;

            case R.id.sign_up:
                showDialogOnCheckBox();
                break;
        }
    }

    private void moveToSignUp() {
        Intent moveToSignUp = new Intent(LoginScreen.this, SignUp.class);
        moveToSignUp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(moveToSignUp);
    }

    public void showDialogOnCheckBox() {
        final Dialog dialog = new Dialog(loginScreen);
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
        message.setText(resources.getString(R.string.ask_user_type));
        Button declineButton = (Button) dialog.findViewById(R.id.cancel);
        declineButton.setText(AppConstant.NO);
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast(resources.getString(R.string.child_sign_up_message));
                dialog.dismiss();

            }
        });
        Button acceptButton = (Button) dialog.findViewById(R.id.ok);
        acceptButton.setText(AppConstant.YES);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToSignUp();
            }
        });
        dialog.show();
    }

    @Override
    public void onValidationSucceeded() {
        new RestCall(loginScreen).authenticateUser(username.getText().toString().trim(), password.getText().toString().trim(), password, AppConstant.LOGIN_SCREEN, progressBar);

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
                showToast(message);
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
