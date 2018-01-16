package com.mobiledi.earnit.activity;

import android.support.v7.app.AppCompatActivity;

import com.mobiledi.earnit.utils.Utils;

import org.json.JSONObject;

/**
 * Created by mobile-di on 27/10/17.
 */

public class BaseActivity extends AppCompatActivity {

    public void showToast(String message){
        Utils.showToast(BaseActivity.this, message);
    }


    public void lockScreen(){
        Utils.lockScreen(getWindow());
    }


    public void unLockScreen(){
        Utils.unLockScreen(getWindow());
    }
    public void josnError(JSONObject message){
        Utils.JsonError(message, BaseActivity.this );
    }
}
