package com.mobiledi.earnit.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.mobiledi.earnit.model.Child;
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
 * Created by mobile-di on 24/8/17.
 */

public class SendMessageService extends Service {

    Child child;
    String TAG="SendMessageService";
    String message;
    SendMessageService sendMessageService;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sendMessageService = this;
        child = (Child) intent.getSerializableExtra(AppConstant.CHILD_OBJECT);
        message = intent.getStringExtra(AppConstant.MESSAGE);
        Utils.logDebug(TAG, "message :"+ message);
        Utils.logDebug(TAG, "message :"+ child.getFirstName());
        updateChildMessage(message);
        return Service.START_NOT_STICKY;
    }

    private void updateChildMessage(String message) {
        JSONObject signInJson = new JSONObject();
        try {
            signInJson.put(AppConstant.EMAIL, child.getEmail());
            signInJson.put(AppConstant.FIRST_NAME, child.getFirstName());
            signInJson.put(AppConstant.LAST_NAME, child.getLastName());
            signInJson.put(AppConstant.PASSWORD, child.getPassword());
            signInJson.put(AppConstant.PHONE, child.getPhone());
            signInJson.put(AppConstant.CREATE_DATE, new DateTime().getMillis());
            signInJson.put(AppConstant.UPDATE_DATE, new DateTime().getMillis());
            signInJson.put(AppConstant.ID,child.getId());
            signInJson.put(AppConstant.FCM_TOKEN, child.getFcmToken());
            signInJson.put(AppConstant.AVATAR, child.getAvatar());
            signInJson.put(AppConstant.MESSAGE, message);
            signInJson.put(AppConstant.ACCOUNT, new JSONObject().put(AppConstant.ID, child.getAccount().getId()));
            Utils.logDebug(TAG, "update-child-json : "+signInJson.toString());
            StringEntity entity = new StringEntity(signInJson.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, AppConstant.APPLICATION_JSON));
            AsyncHttpClient httpClient = new AsyncHttpClient();
            httpClient.setBasicAuth(child.getEmail(), child.getPassword());
            PersistentCookieStore myCookieStore = new PersistentCookieStore(sendMessageService);
            httpClient.setCookieStore(myCookieStore);
            httpClient.put(sendMessageService, AppConstant.BASE_URL + AppConstant.UPDATE_CHILD, entity, AppConstant.APPLICATION_JSON, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Utils.logDebug(TAG, "update-child-json-os : "+response.toString());
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        Utils.logDebug(TAG, "update-child-json-as : "+response.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Utils.logDebug(TAG, "update-child-json-of : "+throwable.toString());
                        Utils.logDebug(TAG, "update-child-json-of : "+errorResponse.toString());
                        Utils.logDebug(TAG, "update-child-json-of : "+throwable.getMessage());

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
}
