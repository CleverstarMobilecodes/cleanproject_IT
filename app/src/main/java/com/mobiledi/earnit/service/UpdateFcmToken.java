package com.mobiledi.earnit.service;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.mobiledi.earnit.model.Child;
import com.mobiledi.earnit.model.Parent;
import com.mobiledi.earnit.utils.AppConstant;
import com.mobiledi.earnit.utils.Utils;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

/**
 * Created by mobile-di on 18/8/17.
 */

public class UpdateFcmToken extends Service {
    Child child;
    Parent parent;
    String token;
    String mode;
    String TAG="UpdateFcmToken";
    UpdateFcmToken updateFcmToken;
    boolean isLogout = false;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateFcmToken = this;
        child = (Child) intent.getSerializableExtra(AppConstant.CHILD_OBJECT);
        parent = (Parent) intent.getSerializableExtra(AppConstant.PARENT_OBJECT);
        token = intent.getStringExtra(AppConstant.FCM_TOKEN);
        mode = intent.getStringExtra(AppConstant.MODE);
        isLogout = intent.getBooleanExtra(AppConstant.IS_LOGOUT, false);
        if(mode.equalsIgnoreCase(AppConstant.CHILD))
             updateChildTokenId(child, token);
        else
            updateParentTokenId(parent, token);

        return Service.START_NOT_STICKY;
    }

    private void updateParentTokenId(Parent parent, String token) {
        JSONObject signInJson = new JSONObject();
        try {

            signInJson.put(AppConstant.ACCOUNT, new JSONObject().put(AppConstant.ID, parent.getAccount().getId()));
            signInJson.put(AppConstant.ID, parent.getId());
            signInJson.put(AppConstant.AVATAR, parent.getAvatar());
            signInJson.put(AppConstant.EMAIL, parent.getEmail());
            signInJson.put(AppConstant.PHONE, parent.getPhone());
            signInJson.put(AppConstant.FIRST_NAME, parent.getFirstName());
            signInJson.put(AppConstant.LAST_NAME, parent.getLastName());
            signInJson.put(AppConstant.CREATE_DATE, new DateTime().getMillis());
            signInJson.put(AppConstant.UPDATE_DATE, new DateTime().getMillis());
            signInJson.put(AppConstant.PASSWORD, parent.getPassword());
            signInJson.put(AppConstant.TYPE, parent.getUserType());

            if(!isLogout)
            signInJson.put(AppConstant.FCM_TOKEN, token);

            Utils.logDebug(TAG, " profile-Json : "+signInJson.toString());
            StringEntity entity = new StringEntity(signInJson.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, AppConstant.APPLICATION_JSON));
            AsyncHttpClient httpClient = new AsyncHttpClient();
            httpClient.setBasicAuth(parent.getEmail(), parent.getPassword());
            PersistentCookieStore myCookieStore = new PersistentCookieStore(updateFcmToken);
            httpClient.setCookieStore(myCookieStore);
            httpClient.put(updateFcmToken, AppConstant.BASE_URL + AppConstant.UPDATE_PARENT, entity, AppConstant.APPLICATION_JSON, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void updateChildTokenId(Child child, String token) {

        JSONObject signInJson = new JSONObject();
        try {
            signInJson.put(AppConstant.ACCOUNT, new JSONObject().put(AppConstant.ID, child.getAccount().getId()));
            signInJson.put(AppConstant.EMAIL, child.getEmail());
            signInJson.put(AppConstant.FIRST_NAME, child.getFirstName());
            signInJson.put(AppConstant.LAST_NAME, child.getLastName());
            signInJson.put(AppConstant.PASSWORD, child.getPassword());
            signInJson.put(AppConstant.PHONE, child.getPhone());
            signInJson.put(AppConstant.CREATE_DATE, new DateTime().minus(child.getCreateDate()));
            signInJson.put(AppConstant.UPDATE_DATE, new DateTime().getMillis());
            signInJson.put(AppConstant.ID,child.getId());

            if (!isLogout)
            signInJson.put(AppConstant.FCM_TOKEN, token);

            signInJson.put(AppConstant.AVATAR, child.getAvatar());
            Utils.logDebug(TAG, "json :"+ signInJson.toString());
            StringEntity entity = new StringEntity(signInJson.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, AppConstant.APPLICATION_JSON));
            AsyncHttpClient httpClient = new AsyncHttpClient();
            httpClient.setBasicAuth(child.getEmail(), child.getPassword());
            PersistentCookieStore myCookieStore = new PersistentCookieStore(updateFcmToken);
            httpClient.setCookieStore(myCookieStore);
            httpClient.put(updateFcmToken, AppConstant.BASE_URL + AppConstant.UPDATE_CHILD, entity, AppConstant.APPLICATION_JSON, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Utils.logDebug(TAG, "success :"+ response.toString());
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        Utils.logDebug(TAG, "onSuccess :"+ response.toString());

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Utils.logDebug(TAG, "onFailure :"+ errorResponse.toString());


                    }
                });

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
