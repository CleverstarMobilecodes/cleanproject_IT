package com.mobiledi.earnit.utils;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by mobile-di on 27/10/17.
 */

public class EarnitRestClientUsage {

    public void getPublicTimeline() throws JSONException {
        EarnitRestClient.get(AppConstant.GOAL_API + 1, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                // Pull out the first event on the public timeline
                try {
                    JSONObject firstEvent = (JSONObject) timeline.get(0);
                    String tweetText = firstEvent.getString(AppConstant.GOAL_NAME);

                    // Do something with the response
                    System.out.println(tweetText);
                }catch (JSONException e){}
                }


        });
    }
}
