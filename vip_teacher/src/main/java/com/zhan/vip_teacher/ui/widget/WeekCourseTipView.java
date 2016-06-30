package com.zhan.vip_teacher.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhan.framework.utils.PixelUtils;
import com.zhan.vip_teacher.R;

/**
 * Created by WuYue on 2016/3/22.
 */
public class WeekCourseTipView extends LinearLayout {
    public WeekCourseTipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
        TypedArray tArray = context.obtainStyledAttributes(attrs, R.styleable.WeekCourseTipView_LayoutParams);
        String name = tArray.getString(R.styleable.WeekCourseTipView_LayoutParams_name);
        int color = tArray.getColor(R.styleable.WeekCourseTipView_LayoutParams_divColor, Color.BLUE);
        tArray.recycle();

        setGravity(Gravity.CENTER_VERTICAL);
        View colorDiv = new View(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(PixelUtils.dp2px(20), PixelUtils.dp2px(6));
        colorDiv.setLayoutParams(layoutParams);
        colorDiv.setBackgroundColor(color);
        TextView textView = new TextView(context);
        textView.setText(name);
        textView.setPadding(PixelUtils.dp2px(2), 0, 0, 0);
        addView(colorDiv);
        addView(textView);
    }
}
