package com.mobiledi.earnit.customcontrol;

/**
 * Created by praks on 08/07/17.
 */


import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mobiledi.earnit.utils.Utils;

public class ExpandableHeightListView extends ListView {

    boolean expanded = false;

    private android.view.ViewGroup.LayoutParams params;
    private int oldCount = 0;
    private final String TAG = "ExpandableHeightLV";

    public ExpandableHeightListView(Context context) {
        super(context);
    }

    public ExpandableHeightListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpandableHeightListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean isExpanded() {
        return expanded;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (isExpanded()) {
            // Calculate entire height by providing a very large height hint.
            // But do not use the highest 2 bits of this integer; those are
            // reserved for the MeasureSpec mode.
            int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);

            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = getMeasuredHeight();
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getCount() != oldCount) {
            try {
                int height = getChildAt(0).getHeight() + 1;
                Utils.logDebug(TAG, "on draw height :"+height);
                oldCount = getCount();
                params = getLayoutParams();
                params.height = getCount() * height;
                setLayoutParams(params);
            }catch (NullPointerException e){e.printStackTrace();
                Utils.logDebug(TAG, "on draw height null pointer");
            }
        }
        super.onDraw(canvas);
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}