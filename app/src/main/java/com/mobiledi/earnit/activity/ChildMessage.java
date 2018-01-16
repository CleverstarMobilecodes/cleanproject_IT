package com.mobiledi.earnit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.mobiledi.earnit.R;
import com.mobiledi.earnit.model.Child;
import com.mobiledi.earnit.service.UpdateFcmToken;
import com.mobiledi.earnit.utils.AppConstant;
import com.mobiledi.earnit.utils.ScreenSwitch;
import com.squareup.picasso.Picasso;

/**
 * Created by mobile-di on 24/8/17.
 */

public class ChildMessage extends BaseActivity implements View.OnClickListener {

    TextView childName, messageFrom, messageContent;
    CircularImageView childAvatar;
    Button closeMessage;
    ChildMessage childMessage;
    private final String TAG = "ChildMessage";
    private  String token;
    Child child;
    ScreenSwitch screenSwitch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_massage_layout);
        childMessage = this;
        setViewIds();
        screenSwitch = new ScreenSwitch(childMessage);
        //SERIALIZE OBJECT FROM INTENT OBJECT
        Intent intent = getIntent();
        child = (Child) intent.getSerializableExtra(AppConstant.CHILD_OBJECT);
        token = getSharedPreferences(AppConstant.FIREBASE_PREFERENCE, MODE_PRIVATE).getString(AppConstant.TOKEN_ID, null);
        childName.setText(child.getFirstName());
        try{
            Picasso.with(getApplicationContext()).load(child.getAvatar()).error(R.drawable.default_avatar).into(childAvatar);
        }catch (Exception e){
            e.printStackTrace();
            Picasso.with(getApplicationContext()).load(R.drawable.default_avatar).into(childAvatar);
        }
        messageContent.setText(child.getMessage());

    }

    private void setViewIds() {
        childName = (TextView) findViewById(R.id.child_name);
        messageFrom = (TextView) findViewById(R.id.message_from_id);
        messageContent = (TextView) findViewById(R.id.message_content);
        childAvatar = (CircularImageView) findViewById(R.id.child_avatar);
        closeMessage = (Button) findViewById(R.id.message_close);
        closeMessage.setOnClickListener(childMessage);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.message_close:
                screenSwitch.updateFCMToken(child, token);
                    //LOAD CHILD ACTIVITY
                screenSwitch.moveTOChildDashboard(child);
                break;
        }
    }
}
