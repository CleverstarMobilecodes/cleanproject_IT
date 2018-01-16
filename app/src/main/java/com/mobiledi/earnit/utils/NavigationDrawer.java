package com.mobiledi.earnit.utils;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.mobiledi.earnit.R;
import com.mobiledi.earnit.activity.LoginScreen;
import com.mobiledi.earnit.activity.ParentDashboard;
import com.mobiledi.earnit.activity.ParentProfile;
import com.mobiledi.earnit.model.Parent;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by mradul on 7/29/17.
 */

public class NavigationDrawer {
    private final String TAG = "NavigationDrawer";
    private Drawer navigationDrawer;
    String firstName = " ", lastName = " ";
    private OnDrawerToggeled onDrawerToggeled;
    Resources resources;
    AppCompatActivity context;

    public void setOnDrawerToggeled(OnDrawerToggeled onDrawerToggeled) {
        this.onDrawerToggeled = onDrawerToggeled;
    }

    public NavigationDrawer(final AppCompatActivity context, final Parent parentObject,
                            Toolbar toolbar, ImageButton drawerToggle, final String screen,
                            final int childId) {
        this.context = context;
        resources = context.getResources();
        //initialize and create the image loader logic
        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                try {
                    Picasso.with(imageView.getContext()).load(uri).error(R.drawable.default_avatar)
                            .into(imageView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void cancel(ImageView imageView) {
                Picasso.with(imageView.getContext()).cancelRequest(imageView);
            }

        });

        if (parentObject.getFirstName().equalsIgnoreCase("null"))
            Utils.logDebug(TAG, resources.getString(R.string.lastname_empty));
        else
            firstName = parentObject.getFirstName();
        if (parentObject.getLastName().equalsIgnoreCase("null"))
            Utils.logDebug(TAG, resources.getString(R.string.firstname_empty));
        else
            lastName = parentObject.getLastName();
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(context)
                .withHeaderBackground(R.drawable.drawer_background)
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();
        if (parentObject.getAvatar().equalsIgnoreCase("null")) {
            headerResult.addProfiles(
                    new ProfileDrawerItem().withName(firstName + " " + lastName)
                            .withEmail(parentObject.getEmail())
                            .withIcon(R.drawable.default_avatar)
            );
        } else {
            headerResult.addProfiles(
                    new ProfileDrawerItem().withName(firstName + " " + lastName)
                            .withEmail(parentObject.getEmail())
                            .withIcon(parentObject.getAvatar())
            );

        }

        //if you want to update the items at a later time it is recommended to keep it in a variable
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1)
                .withName(AppConstant.HOME)
                .withIcon(new IconDrawable(context, FontAwesomeIcons.fa_home)
                        .colorRes(R.color.md_white_1000)
                        .actionBarSize())
                .withTextColor(context.getResources().getColor(R.color.md_white_1000));
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2)
                .withName(AppConstant.PROFILE)
                .withIcon(new IconDrawable(context, FontAwesomeIcons.fa_user)
                        .colorRes(R.color.md_white_1000)
                        .actionBarSize())
                .withTextColor(context.getResources().getColor(R.color.md_white_1000));
        /*SecondaryDrawerItem item3 = new SecondaryDrawerItem().withIdentifier(3)
                .withName(AppConstant.SETTING)
                .withIcon(new IconDrawable(context, FontAwesomeIcons.fa_cog)
                        .colorRes(R.color.md_white_1000)
                        .actionBarSize())
                .withTextColor(context.getResources().getColor(R.color.md_white_1000));*/
        SecondaryDrawerItem item3 = new SecondaryDrawerItem().withIdentifier(3)
                .withName(AppConstant.LOGOUT)
                .withIcon(new IconDrawable(context, FontAwesomeIcons.fa_lock)
                        .colorRes(R.color.md_white_1000)
                        .actionBarSize())
                .withTextColor(context.getResources().getColor(R.color.md_white_1000));
        TextView view = new TextView(context);
        view.setText(context.getResources().getString(R.string.app_version));
        view.setTextColor(context.getResources().getColor(R.color.main_font));
        view.setGravity(Gravity.CENTER);

        navigationDrawer = new DrawerBuilder()
                .withActivity(context)
                .withAccountHeader(headerResult)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(false)
                .withDrawerGravity(Gravity.LEFT)
                .withSliderBackgroundColorRes(R.color.background)
                .addDrawerItems(item1, item2, item3)
                .withFooter(view)
                .withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(View drawerView) {
                        if (onDrawerToggeled != null)
                            onDrawerToggeled.onDrawerToggeled();
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {

                    }

                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        if (onDrawerToggeled != null)
                            onDrawerToggeled.onDrawerToggeled();
                    }
                })
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        if (drawerItem instanceof PrimaryDrawerItem) {
                            if (position == 1) {
                                Intent intent = new Intent(context, ParentDashboard.class);
                                intent.putExtra(AppConstant.PARENT_OBJECT, parentObject);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                                Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                context.startActivity(intent);
                            }
                        } else if (drawerItem instanceof SecondaryDrawerItem)
                            if (position == 2) {
                                Intent intent = new Intent(context, ParentProfile.class);
                                intent.putExtra(AppConstant.PARENT_OBJECT, parentObject);
                                intent.putExtra(AppConstant.SCREEN, screen);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                                Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                if (screen.equalsIgnoreCase(AppConstant.CHECKED_IN_SCREEN) ||
                                        screen.equalsIgnoreCase(
                                                AppConstant.CHECKED_IN_TASK_APPROVAL__SCREEN))
                                    intent.putExtra(AppConstant.CHILD_ID, childId);
                                context.startActivity(intent);
                            } else if (position == 3) {
                                SharedPreferences sharedPreferences = context.getSharedPreferences(
                                        AppConstant.FIREBASE_PREFERENCE, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear();
                                editor.commit();
                                logoutParent(parentObject);
                                Intent intentLogout = new Intent(context, LoginScreen.class);
                                intentLogout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                                    Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                Utils.showToast(context, resources.getString(R.string.logout));
                                context.startActivity(intentLogout);

                            } else
                                Utils.showToast(context, resources.getString(R.string.todo) + ((SecondaryDrawerItem) drawerItem).getName());

                        return false;
                    }
                })
                .build();
        drawerToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!navigationDrawer.isDrawerOpen())
                    navigationDrawer.openDrawer();
            }
        });
    }

    public interface OnDrawerToggeled {
        void onDrawerToggeled();
    }

    public void logoutParent(final Parent parent){
        new AsyncTask<Void,Void,Void>()
        {
            @Override
            protected Void doInBackground(Void... params)
            {
                {
                    try
                    {
                        FirebaseInstanceId.getInstance().deleteInstanceId();
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                return null;
            }
            @Override
            protected void onPostExecute(Void result){}
        }.execute();
    }
}
