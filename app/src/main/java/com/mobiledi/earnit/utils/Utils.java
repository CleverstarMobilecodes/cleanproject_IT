package com.mobiledi.earnit.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobiledi.earnit.BuildConfig;
import com.mobiledi.earnit.R;
import com.mobiledi.earnit.activity.InitialParentProfile;
import com.mobiledi.earnit.adapter.CountryAdapter;
import com.mobiledi.earnit.model.Country;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 * Created by mobile-di on 27/10/17.
 */

public class Utils {

    // Screen lock and unlock
    public static void lockScreen(Window window) {
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public static void unLockScreen(Window window) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }


    // Check null value


    public static String checkIsNUll( String input){
        String toReturn;
        try {
            input = input.trim();
            if(input.equals("") || input.equals("null") || input.isEmpty() || input.equals(" ") ){
                toReturn = "";
            }else{
                toReturn = input;
            }

        }catch (NullPointerException e){
            toReturn = "";
        }

        return toReturn;
    }

    public static double roundOff(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();


        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    // show Logs
    public static void logDebug(String tag, String message){
        if(BuildConfig.DEBUG)
            Log.d(tag,message,null);
    }

    // show Toast
    public static void showToast(Context context, String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }

    public static void showToast2(Activity context, String message){
        LayoutInflater inflater = context.getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_view,
                (ViewGroup) context.findViewById(R.id.toast_layout_root));
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(message);

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    // set Cursor position
    public static void  SetCursorPosition(final EditText editText){
        editText.post(new Runnable() {
            @Override
            public void run() {
                editText.setSelection(editText.getText().length());
            }
        });
    }

    public static void JsonError(JSONObject errorResponse, Context context){

        try {
            if (errorResponse.has(AppConstant.MESSAGE))
                showToast(context, errorResponse.getJSONArray(AppConstant.MESSAGE).getString(0));
            else
                showToast(context, context.getResources().getString(R.string.other_message));
        }catch (JSONException e){e.printStackTrace();
            showToast(context, context.getResources().getString(R.string.other_message));}
    }

    public static boolean validatePhoneNumber(String phoneNo) {
        //validate phone numbers of format "1234567890"
        if (phoneNo.matches("\\d{10}")) return true;
            //validating phone number with -, . or spaces
//        else if(phoneNo.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")) return true;
            //validating phone number with extension length from 3 to 5
//        else if(phoneNo.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}")) return true;
            //validating phone number where area code is in braces ()
//        else if(phoneNo.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")) return true;
            //return false if nothing matches the input
        else return false;

    }

    public static ArrayList<Country> loadCountryData(String TAG){
        try {

            ArrayList<Country> toReturn = new ArrayList<>();
            JSONObject obj = new JSONObject(AppConstant.COUNTRY_JSON);
            JSONArray array = obj.getJSONArray(AppConstant.COUNTRIES);
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                String name =  object.getString(AppConstant.NAME);
                String dial =  object.getString(AppConstant.DIAL_CODE);
                String code =  object.getString(AppConstant.CODE);
                Country country = new Country();
                country.setCountryName(name);
                country.setCountryDialCode(dial);
                country.setCountryCode(code);
                toReturn.add(country);
            }
            return toReturn;
        } catch (JSONException e1) {
            e1.printStackTrace();
            return null;
        }
    }

    public static void showCountryDialog(ArrayList<Country> countries, final Context context, final TextView dial, final TextView code){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha=0.9f;
        window.setAttributes(lp);
        window.setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.country_view);
        dialog.setCanceledOnTouchOutside(true);
        RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new CountryAdapter(countries, new CountryAdapter.CountryListner(){
            @Override
            public void onItemClick(Country country) {
                showToast(context, country.getCountryName()+" "+country.getCountryDialCode());
                dial.setText(country.getCountryDialCode());
                code.setText(country.getCountryCode());
                dialog.dismiss();
            }
        }));
        TextView close = (TextView) dialog.findViewById(R.id.dialog_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
