package com.zhan.vip_teacher.ui.widget;

import android.view.View;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by WuYue on 2016/3/4.
 */
public interface CalendarCallback {
    void onPrepareCell(int startTime, int cellX, Date date, CalendarCellView cellView);
    void onWeekChanged(Date date);
    void onCellViewClicked(View cellView);
}
