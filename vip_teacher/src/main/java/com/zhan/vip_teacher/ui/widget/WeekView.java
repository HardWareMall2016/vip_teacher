// Copyright 2012 Square, Inc.
package com.zhan.vip_teacher.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import com.zhan.vip_teacher.R;

public class WeekView extends LinearLayout {

    CalendarGridView grid;

    public WeekView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static WeekView create(LayoutInflater inflater, int startTimes[],
                                  CalendarCellItemClickListener listener, int dividerColor, int dayTextColorResId) {
        final WeekView view = (WeekView) inflater.inflate(R.layout.week_layout, null, false);
        view.setDividerColor(dividerColor);
        view.setDayTextColor(dayTextColorResId);

        //Add 时间段
        CalendarGridView gridView = (CalendarGridView) view.findViewById(R.id.calendar_grid);
        for (int i = 0; i < startTimes.length; i++) {
            CalendarRowView weekRow = (CalendarRowView) inflater.inflate(R.layout.week_time, null);

            weekRow.setListener(listener);

            gridView.addView(weekRow);
        }

        return view;
    }


    public CalendarGridView getGrid() {
        return grid;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        grid = (CalendarGridView) findViewById(R.id.calendar_grid);
    }

    public void setDividerColor(int color) {
        grid.setDividerColor(color);
    }

    public void setDayBackground(int resId) {
        grid.setDayBackground(resId);
    }

    public void setDayTextColor(int resId) {
        grid.setDayTextColor(resId);
    }
}
