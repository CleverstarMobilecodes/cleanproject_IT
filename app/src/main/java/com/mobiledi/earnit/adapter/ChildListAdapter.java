package com.mobiledi.earnit.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.mobiledi.earnit.R;
import com.mobiledi.earnit.model.Child;
import com.mobiledi.earnit.model.Parent;
import com.mobiledi.earnit.utils.AppConstant;
import com.mobiledi.earnit.utils.ScreenSwitch;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by mobile-di on 13/8/17.
 */

public class ChildListAdapter extends RecyclerView.Adapter<ChildListAdapter.ChildListHolder> {

    Activity activity;
    List<Child> childList;
    Parent parent;
    int childId;
    String switchFrom;

    public ChildListAdapter(Activity activity, List<Child> childList, Parent parent, String switchFrom, int childId) {
        this.childList = childList;
        this.activity = activity;
        this.parent = parent;
        this.switchFrom = switchFrom;
        this.childId = childId;

    }

    @Override
    public ChildListHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.child_list_single_view, parent, false);

        return new ChildListAdapter.ChildListHolder(itemView);    }

    @Override
    public void onBindViewHolder(ChildListHolder holder, int position) {
        final Child child = childList.get(position);
        try {
            Picasso.with(activity).load(child.getAvatar().toString()).error(R.drawable.default_avatar).into(holder.childImage);
        }catch (Exception e) {
            Picasso.with(activity).load(R.drawable.default_avatar).into(holder.childImage);
        }
        holder.childFullName.setText(child.getFirstName().substring(0,1)+child.getFirstName().substring(1).toLowerCase());
        holder.childFullName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ScreenSwitch(activity).moveToAddChild(parent, childId, switchFrom, AppConstant.UPDATE, child );
            }
        });
    }

    @Override
    public int getItemCount() {
        return childList.size();
    }

    public class ChildListHolder extends RecyclerView.ViewHolder {
        public TextView childFullName;
        public CircularImageView childImage;


        public ChildListHolder(View view) {
            super(view);
            childFullName = (TextView) view.findViewById(R.id.child_fullname_id);
            childImage = (CircularImageView) view.findViewById(R.id.chid_avater_id);
        }
    }
}
