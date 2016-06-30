package com.zhan.vip_teacher.ui.fragment.course;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;
import com.zhan.framework.component.container.FragmentContainerActivity;
import com.zhan.framework.network.HttpRequestHandler;
import com.zhan.framework.support.inject.ViewInject;
import com.zhan.framework.ui.activity.ActionBarActivity;
import com.zhan.framework.ui.fragment.ABaseFragment;
import com.zhan.framework.utils.PixelUtils;
import com.zhan.framework.utils.ToastUtils;
import com.zhan.vip_teacher.R;
import com.zhan.vip_teacher.base.App;
import com.zhan.vip_teacher.bean.NoFeedbackListRequestBean;
import com.zhan.vip_teacher.bean.NoFeedbackListResponseBean;
import com.zhan.vip_teacher.bean.TeacherLessonRequest;
import com.zhan.vip_teacher.bean.TeacherLessonResponse;
import com.zhan.vip_teacher.bean.UserInfo;
import com.zhan.vip_teacher.db.bean.DailyLesson;
import com.zhan.vip_teacher.db.bean.RestTime;
import com.zhan.vip_teacher.db.bean.WeekLesson;
import com.zhan.vip_teacher.db.bean.WeekPublicLesson;
import com.zhan.vip_teacher.network.ApiUrls;
import com.zhan.vip_teacher.ui.widget.BadgeView;
import com.zhan.vip_teacher.ui.widget.CachedViewPager;
import com.zhan.vip_teacher.ui.widget.CalendarCellItemClickListener;
import com.zhan.vip_teacher.ui.widget.CalendarCellView;
import com.zhan.vip_teacher.ui.widget.CalendarRowView;
import com.zhan.vip_teacher.ui.widget.FixHeightCachedViewPager;
import com.zhan.vip_teacher.ui.widget.WeekView;
import com.zhan.vip_teacher.utils.CacheUtility;
import com.zhan.vip_teacher.utils.LessonNotifyManager;
import com.zhan.vip_teacher.utils.OperatorGuideManager;
import com.zhan.vip_teacher.utils.SendEventUtil;
import com.zhan.vip_teacher.utils.ShareUtil;
import com.zhan.vip_teacher.utils.Tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import static java.util.Calendar.DATE;

/**
 * Created by WuYue on 2016/3/4.
 */
public class WeekCourseFragment extends ABaseFragment implements CachedViewPager.ICacheView, CalendarCellItemClickListener {

    public static void launch(Activity from) {
        FragmentContainerActivity.launch(from, WeekCourseFragment.class, null);
        SendEventUtil.sendEvent(5, "查看周日历", "");
    }

    @ViewInject(idStr = "calendar_header")
    private ViewGroup mViewCalendarHeader;

    @ViewInject(idStr = "calendar_view")
    FixHeightCachedViewPager viewCalendar;//日历

    private TextView mActionBarRightMenu;
    private BadgeView mBadgeView;

    private final int PAGE_SIZE = 200;

    private boolean mFirstRequest = true;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd");
    private SimpleDateFormat mLessonTimeFormat = new SimpleDateFormat("HH:mm");

    private LessonNotifyManager mLessonNotifyManager;

    private Locale locale;

    private Calendar weekCounter;
    private Calendar today;

    private int dividerColor;
    private int dayTextColorResId;

    private int[] mStartTimes = new int[]{6, 8, 10, 12, 13, 15, 17, 18, 20, 22};

    private class MgsInfo {
        WeekView weekView;
        Date date;
    }

    private class CellTag {
        List<WeekLesson> weekLessonList = new LinkedList<>();
        List<WeekPublicLesson> weekPublicLessonList = new LinkedList<>();
    }

    private final static int MSG_QUERY = 101;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_QUERY:
                    showRotateProgressDialog("努力加载中...", true);
                    removeMessages(MSG_QUERY);
                    MgsInfo msginfo = (MgsInfo) msg.obj;
                    startQueryLesson(msginfo.date, msginfo.weekView);
                    break;
            }
        }
    };

    @Override
    public void onPrepareActionbarMenu(TextView menu) {
        //super.onPrepareActionbarMenu(menu);
        mActionBarRightMenu = menu;
        menu.setBackgroundResource(R.drawable.unfeedback_selector);
        menu.setPadding(0, PixelUtils.dp2px(4), PixelUtils.dp2px(8), PixelUtils.dp2px(4));
    }

    @Override
    public void onActionBarMenuClick() {
        //super.onActionBarMenuClick();
        NoFeedbackListFragment.launch(getActivity());
    }

    @Override
    protected int inflateContentView() {
        return R.layout.week_course_layout;
    }

    @Override
    protected void layoutInit(LayoutInflater inflater, Bundle savedInstanceSate) {
        viewCalendar.initViewPager(PAGE_SIZE, CachedViewPager.DEF_CACHE_SIZE, this, PAGE_SIZE / 2);
        mFirstRequest = true;

        getActivity().setTitle(R.string.week_schedule);

        Resources res = getResources();
        final int bg = res.getColor(R.color.calendar_bg);
        viewCalendar.setBackgroundColor(bg);

        dividerColor = res.getColor(R.color.calendar_divider);
        dayTextColorResId = R.color.calendar_text_selector;

        locale = Locale.getDefault();
        today = Calendar.getInstance(locale);
        weekCounter = Calendar.getInstance(locale);


        mBadgeView = new BadgeView(getActivity(), mActionBarRightMenu);
        mBadgeView.setTextColor(Color.WHITE);
        mBadgeView.setBadgeBackgroundColor(Color.RED);
        mBadgeView.setTextSize(10);
        mBadgeView.setBadgePosition(BadgeView.POSITION_BOTTOM_RIGHT);
        mBadgeView.setBadgeMargin(0, 0);


        mLessonNotifyManager=new LessonNotifyManager(getActivity());
        showRemindBtn();

        if (ShareUtil.VALUE_TURN_ON.equals(ShareUtil.getStringValue(ShareUtil.WEEKCOURSE_PAGE, ShareUtil.VALUE_TURN_ON))) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setImageResource(R.drawable.weekcourse_guide);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            final OperatorGuideManager operatorGuideManager = new OperatorGuideManager((ActionBarActivity) getActivity());
            operatorGuideManager.showGuideWindow(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    operatorGuideManager.hideGuideWindow();
                    ShareUtil.setValue(ShareUtil.WEEKCOURSE_PAGE, ShareUtil.VALUE_TURN_OFF);
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getUnFeedbackCount();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mHandler.removeMessages(MSG_QUERY);
        mHandler.removeCallbacks(mGetRemindLessonRunnable);
        mLessonNotifyManager.release();
        closeLessonInfoDialog();
    }

    private void getUnFeedbackCount() {
        releaseRequest(ApiUrls.GET_NO_FEEDBACK_LIST);

        NoFeedbackListRequestBean requestBean = new NoFeedbackListRequestBean();
        requestBean.setData(UserInfo.getCurrentUser().getUserID());
        startRequest(ApiUrls.GET_NO_FEEDBACK_LIST, requestBean, new HttpRequestHandler() {
            @Override
            public void onRequestFinished(ResultCode resultCode, String result) {
                super.onRequestFinished(resultCode, result);
                mBadgeView.hide();
                switch (resultCode) {
                    case success:
                        NoFeedbackListResponseBean bean = Tools.parseJson(result, NoFeedbackListResponseBean.class);
                        if (bean != null && bean.getCode() == 0 && bean.getData() != null) {
                            int count = bean.getData().size();
                            if (count > 0) {
                                mBadgeView.setText(String.valueOf(count));
                                mBadgeView.show();
                            }
                        }
                        break;
                    case canceled:
                        break;
                    default:
                        ToastUtils.toast(result);
                        break;
                }
            }
        });
    }


    @Override
    public View inflaterPageView(LayoutInflater inflater, int position) {
        WeekView weekView = WeekView.create(inflater, mStartTimes, this, dividerColor, dayTextColorResId);
        return weekView;
    }

    @Override
    public void initPageView(View page, int position) {
        WeekView weekView = (WeekView) page;
        initWeekView(weekView, position);
    }

    @Override
    public void onPageSelected(View selectedPage, int position) {
        Date date = getTimeByPosition(position);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int firstDayOfWeek = calendar.getFirstDayOfWeek();
        calendar.set(Calendar.DAY_OF_WEEK, firstDayOfWeek);

        for(int i=0;i<7;i++){
            TextView viewHeader = (TextView)mViewCalendarHeader.getChildAt(i+1);

            calendar.set(Calendar.DAY_OF_WEEK, firstDayOfWeek + i);

            String startTime = dateFormat.format(calendar.getTime());

            SpannableString spDate=new SpannableString(startTime);
            spDate.setSpan(new AbsoluteSizeSpan(10, true), 0, startTime.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            viewHeader.setText(spDate);

            String weekStr=getWeekStr(i);
            viewHeader.append("\n"+weekStr);
        }


        Message msg = Message.obtain();
        msg.what = MSG_QUERY;
        MgsInfo msgInfo = new MgsInfo();
        msgInfo.weekView = (WeekView) selectedPage;
        msgInfo.date = date;
        msg.obj = msgInfo;

        if (mFirstRequest) {
            mFirstRequest = false;
            mHandler.sendMessage(msg);
        } else {
            mHandler.removeMessages(MSG_QUERY);
            mHandler.sendMessageDelayed(msg, 1000);
        }
    }

    @Override
    public void handleClick(View cell) {
        CalendarCellView cellView=(CalendarCellView)cell;
        CellTag cellTag = (CellTag) cell.getTag();
        showLessonInfoDialog(cellTag, cellView.getRestTime());

    }

    private void initWeekView(WeekView weekView, int position) {
        Date date = getTimeByPosition(position);
        TeacherLessonRequest requestBean = getTeacherLessonRequest(date);
        //从缓存中获取数据
        List<WeekLesson> cachedWeekLessonList = CacheUtility.findCacheData(ApiUrls.TEACHER_LESSON, requestBean, WeekLesson.class);
        List<WeekPublicLesson> cachedWeekPublicList = CacheUtility.findCacheData(ApiUrls.TEACHER_LESSON, requestBean, WeekPublicLesson.class);
        List<RestTime> cachedRestTimeList = CacheUtility.findCacheData(ApiUrls.TEACHER_LESSON, requestBean, RestTime.class);
        //显示缓存数据
        populateWeekView(weekView, date, cachedWeekLessonList, cachedWeekPublicList, cachedRestTimeList);
    }


    private void startQueryLesson(Date date, WeekView weekView) {
        releaseAllRequest();

        TeacherLessonRequest requestBean = getTeacherLessonRequest(date);

        startRequest(ApiUrls.TEACHER_LESSON, requestBean, new WeekLessonRequestHandler(weekView, date, requestBean));
    }

    private class WeekLessonRequestHandler extends HttpRequestHandler {
        private WeekView mWeekView;
        private Date mDate;
        private TeacherLessonRequest mRequestBean;

        public WeekLessonRequestHandler(WeekView weekView, Date date, TeacherLessonRequest requestBean) {
            mWeekView = weekView;
            mDate = date;
            mRequestBean = requestBean;
        }

        @Override
        public void onRequestSucceeded(String content) {
            super.onRequestSucceeded(content);

            TeacherLessonResponse bean = Tools.parseJsonTostError(content, TeacherLessonResponse.class);
            if (bean != null) {
                //提取上课时间
                List<WeekLesson> weekLessonList = getWeekLessons(bean);
                //放入缓存
                CacheUtility.addCacheDataList(ApiUrls.TEACHER_LESSON, mRequestBean, weekLessonList, WeekLesson.class);

                //提取公开课
                List<WeekPublicLesson> weekPublicLessonList = getWeekPublicLesson(bean);
                //放入缓存
                CacheUtility.addCacheDataList(ApiUrls.TEACHER_LESSON, mRequestBean, weekPublicLessonList, WeekPublicLesson.class);

                //提取休息时间
                List<RestTime> restTimeList=getRestTimeList(bean);
                //放入缓存
                CacheUtility.addCacheDataList(ApiUrls.TEACHER_LESSON, mRequestBean, restTimeList, RestTime.class);

                //显示数据
                populateWeekView(mWeekView, mDate, weekLessonList, weekPublicLessonList,restTimeList);
            }
        }

        @Override
        public void onRequestFinished(ResultCode resultCode, String result) {
            super.onRequestFinished(resultCode, result);
            closeRotateProgressDialog();
        }
    }

    @NonNull
    private List<WeekPublicLesson> getWeekPublicLesson(TeacherLessonResponse bean) {
        if (bean.getData() == null || bean.getData().getListPublicClass() == null) {
            return null;
        }
        List<WeekPublicLesson> weekPublicList = new LinkedList<>();
        List<TeacherLessonResponse.DataEntity.ListPublicClassEntity> publicList = bean.getData().getListPublicClass();
        for (TeacherLessonResponse.DataEntity.ListPublicClassEntity item : publicList) {
            long startTime = Tools.parseServerTime(item.getStartTime());
            long endTime = Tools.parseServerTime(item.getEndTime());
            WeekPublicLesson publicLesson = new WeekPublicLesson();
            publicLesson.setStartTime(startTime);
            publicLesson.setEndTime(endTime);
            publicLesson.setSignStatus(item.getSignStatus());
            publicLesson.setClassTitle(item.getClassTitle());
            publicLesson.setCourseId(item.getCourseId());

            weekPublicList.add(publicLesson);
        }
        return weekPublicList;
    }

    @NonNull
    private List<WeekLesson> getWeekLessons(TeacherLessonResponse bean) {
        if (bean.getData() == null || bean.getData().getListLessonInfo() == null) {
            return null;
        }

        List<WeekLesson> weekLessonList = new LinkedList<>();
        List<TeacherLessonResponse.DataEntity.ListLessonInfoEntity> lessonList = bean.getData().getListLessonInfo();
        for (TeacherLessonResponse.DataEntity.ListLessonInfoEntity item : lessonList) {

            long startTime = Tools.parseServerTime(item.getStartTime());
            long endTime = Tools.parseServerTime(item.getEndTime());

            WeekLesson weekLesson = new WeekLesson();
            weekLesson.setLessonId(item.getLessonId());
            weekLesson.setStartTime(startTime);
            weekLesson.setEndTime(endTime);
            weekLesson.setCourseSubType(item.getCourseSubType());
            weekLesson.setCourseSubTypeName(item.getCourseSubTypeName());
            weekLesson.setCourseType(item.getCourseType());
            weekLesson.setCourseTypeName(item.getCourseTypeName());
            weekLesson.setIsFeedBack(item.isIsFeedBack());
            weekLesson.setIsFirstLesson(item.isIsFirstLesson());
            weekLesson.setIsOnLine(item.isIsOnLine());
            weekLesson.setLessonStatus(item.getLessonStatus());
            weekLesson.setLessonStatusName(item.getLessonStatusName());
            weekLesson.setStudentName(item.getStudentName());
            weekLesson.setSignStatus(item.getSignStatus());
            weekLesson.setSignStatusName(item.getSignStatusName());
            weekLessonList.add(weekLesson);
        }
        return weekLessonList;
    }

    @NonNull
    private List<RestTime> getRestTimeList(TeacherLessonResponse bean) {
        List<RestTime> restList = new LinkedList<>();
        //ListRestTime列表
        if (bean.getData() != null && bean.getData().getListRestTime() != null) {
            List<TeacherLessonResponse.DataEntity.ListRestTimeEntity> restTimeEntityList = bean.getData().getListRestTime();
            for (TeacherLessonResponse.DataEntity.ListRestTimeEntity item : restTimeEntityList) {

                long startTime = Tools.parseServerTime(item.getStartTime());
                long endTime = Tools.parseServerTime(item.getEndTime());

                RestTime rest = new RestTime();
                rest.setStartTime(startTime);
                rest.setEndTime(endTime);
                rest.setRestType(item.getRestType());
                rest.setRestTypeName(item.getRestTypeName());

                restList.add(rest);
            }
        }


        //ListConferenceRest列表
        if (bean.getData() != null && bean.getData().getListRestTime() != null) {
            List<TeacherLessonResponse.DataEntity.ListConferenceRestEntity> conferenceRestList = bean.getData().getListConferenceRest();
            for (TeacherLessonResponse.DataEntity.ListConferenceRestEntity item : conferenceRestList) {

                long startTime = Tools.parseServerTime(item.getStartTime());
                long endTime = Tools.parseServerTime(item.getEndTime());

                RestTime rest = new RestTime();
                rest.setStartTime(startTime);
                rest.setEndTime(endTime);
                rest.setRestType(item.getRestType());
                rest.setRestTypeName(item.getRestTypeName());

                restList.add(rest);
            }
        }

        //ListHolidayInfo列表
        if (bean.getData() != null && bean.getData().getListHolidayInfo() != null) {
            List<TeacherLessonResponse.DataEntity.ListHolidayInfoEntity> holidayList = bean.getData().getListHolidayInfo();
            for (TeacherLessonResponse.DataEntity.ListHolidayInfoEntity item : holidayList) {

                long startTime = Tools.parseServerTime(item.getStartTime());
                long endTime = Tools.parseServerTime(item.getEndTime());

                RestTime rest = new RestTime();
                rest.setStartTime(startTime);
                rest.setEndTime(endTime);
                rest.setRestType("1");
                rest.setRestTypeName(item.getHolidayName());

                restList.add(rest);
            }
        }

        return restList;
    }

    @NonNull
    private TeacherLessonRequest getTeacherLessonRequest(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int firstDayOfWeek = calendar.getFirstDayOfWeek();
        calendar.set(Calendar.DAY_OF_WEEK, firstDayOfWeek);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startTime = calendar.getTime();

        calendar.set(Calendar.DAY_OF_WEEK, firstDayOfWeek + 6);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date endTime = calendar.getTime();

        TeacherLessonRequest requestBean = new TeacherLessonRequest();
        TeacherLessonRequest.DataEntity dataEntity = new TeacherLessonRequest.DataEntity();
        requestBean.setData(dataEntity);
        dataEntity.setTeacherId(UserInfo.getCurrentUser().getUserID());
        dataEntity.setStartTime(Tools.formatToServerTimeStr(startTime.getTime()));
        dataEntity.setEndTime(Tools.formatToServerTimeStr(endTime.getTime()));
        try {
            int groupOf = Integer.parseInt(UserInfo.getCurrentUser().getGroupOf());
            dataEntity.setGroupof(groupOf);
        } catch (NumberFormatException ex) {
            Log.e("WeekCourseFragment", "groupOf tans to int error: " + UserInfo.getCurrentUser().getGroupOf());
        }
        return requestBean;
    }

    private void clearWeekView(WeekView weekView) {
        for (int i = 0; i < mStartTimes.length; i++) {
            CalendarRowView weekRow = (CalendarRowView) weekView.getGrid().getChildAt(i);
            for (int offset = 0; offset < weekRow.getChildCount(); offset++) {
                //清空内容
                CalendarCellView cellView = (CalendarCellView) weekRow.getChildAt(offset);
                if (offset == 0) {
                    cellView.setCell(CalendarCellView.CellType.X_HEAD, String.format("%d:00", mStartTimes[i]));
                } else {
                    cellView.setCell(CalendarCellView.CellType.NONE, "");
                    cellView.setTag(null);
                }
            }
        }
    }

    private void populateWeekView(WeekView weekView, Date date, List<WeekLesson> weekLessonList, List<WeekPublicLesson> weekPublicLessonList,List<RestTime> restTimeList) {
        //先清空内容
        clearWeekView(weekView);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        //上课时间
        if (weekLessonList != null && weekLessonList.size() > 0) {
            for (WeekLesson lesson : weekLessonList) {
                Calendar startCal = Calendar.getInstance();
                startCal.setTimeInMillis(lesson.getStartTime());

                Calendar endCal = Calendar.getInstance();
                endCal.setTimeInMillis(lesson.getEndTime());

                int xOffset = getXoffest(startCal);
                for (int yOffSet = 0; yOffSet < mStartTimes.length; yOffSet++) {
                    if (checkCellCanPut(xOffset, yOffSet, calendar, startCal)) {
                        CalendarCellView cellView = getCell(weekView, xOffset, yOffSet);
                        CellTag tag = (CellTag) cellView.getTag();
                        if (tag == null) {
                            tag = new CellTag();
                            cellView.setTag(tag);
                        }
                        tag.weekLessonList.add(lesson);
                        int totalLesson = tag.weekLessonList.size() + tag.weekPublicLessonList.size();
                        if (totalLesson > 1) {
                            cellView.setCell(CalendarCellView.CellType.MORE, totalLesson);
                        } else {
                            cellView.setCell(CalendarCellView.CellType.LESSON, lesson);
                        }
                    }
                }
            }
        }

        //公开课
        if (weekPublicLessonList != null && weekPublicLessonList.size() > 0) {
            for (WeekPublicLesson lesson : weekPublicLessonList) {
                Calendar startCal = Calendar.getInstance();
                startCal.setTimeInMillis(lesson.getStartTime());

                Calendar endCal = Calendar.getInstance();
                endCal.setTimeInMillis(lesson.getEndTime());

                int xOffset = getXoffest(startCal);
                for (int yOffSet = 0; yOffSet < mStartTimes.length; yOffSet++) {
                    if (checkCellCanPut(xOffset, yOffSet, calendar, startCal)) {
                        CalendarCellView cellView = getCell(weekView, xOffset, yOffSet);

                        CellTag tag = (CellTag) cellView.getTag();
                        if (tag == null) {
                            tag = new CellTag();
                            cellView.setTag(tag);
                        }
                        tag.weekPublicLessonList.add(lesson);

                        int totalLesson = tag.weekLessonList.size() + tag.weekPublicLessonList.size();
                        if (totalLesson > 1) {
                            cellView.setCell(CalendarCellView.CellType.MORE, totalLesson);
                        } else {
                            cellView.setCell(CalendarCellView.CellType.PUBLIC_LESSON, lesson);
                        }
                    }
                }
            }
        }

        //休息时段
        if (restTimeList != null && restTimeList.size() > 0) {
            for (RestTime restTime : restTimeList) {
                Calendar startCal = Calendar.getInstance();
                startCal.setTimeInMillis(restTime.getStartTime());

                Calendar endCal = Calendar.getInstance();
                endCal.setTimeInMillis(restTime.getEndTime());

                int xOffset = getXoffest(startCal);
                //休息时段
                for (int yOffSet = 0; yOffSet < mStartTimes.length; yOffSet++) {
                    if (inRestTimePeriod(xOffset, yOffSet, calendar, restTime.getStartTime(),restTime.getEndTime())) {
                        CalendarCellView cellView = getCell(weekView, xOffset, yOffSet);
                        cellView.setCell(CalendarCellView.CellType.REST,restTime);
                    }
                }

                //整天休息
                /*if("2".equals(restTime.getRestType())){
                    for (int yOffSet = 0; yOffSet < mStartTimes.length; yOffSet++) {
                        CalendarCellView cellView = getCell(weekView, xOffset, yOffSet);
                        cellView.setCell(CalendarCellView.CellType.REST,null);
                    }
                }else{

                }*/
            }
        }
    }

    private CalendarCellView getCell(WeekView weekView, int xOffest, int yOffest) {
        CalendarRowView weekRow = (CalendarRowView) weekView.getGrid().getChildAt(yOffest);
        CalendarCellView cellView = (CalendarCellView) weekRow.getChildAt(xOffest + 1);//第一个是头部
        return cellView;
    }

    /***
     * @param xOffset  : 0-6
     * @param yOffset
     * @param calendar
     * @return
     */
    private boolean checkCellCanPut(int xOffset, int yOffset, Calendar calendar, Calendar lessonStartTime) {

        Date lessonStartDate = lessonStartTime.getTime();

        int startTime = mStartTimes[yOffset];

        int firstDayOfWeek = calendar.getFirstDayOfWeek();
        calendar.set(Calendar.DAY_OF_WEEK, firstDayOfWeek + xOffset);
        calendar.set(Calendar.HOUR_OF_DAY, startTime);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, -1);
        Date cellStartTime = calendar.getTime();

        if (yOffset == mStartTimes.length - 1) {
            return (lessonStartDate.equals(cellStartTime) || lessonStartDate.after(cellStartTime));
        } else {
            int endTime = mStartTimes[yOffset + 1];
            calendar.set(Calendar.DAY_OF_WEEK, firstDayOfWeek + xOffset);
            calendar.set(Calendar.HOUR_OF_DAY, endTime);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, -1);
            Date cellEndTime = calendar.getTime();

            return (lessonStartDate.equals(cellStartTime) || lessonStartDate.after(cellStartTime)) // >= minCal
                    && lessonStartDate.before(cellEndTime);
        }
    }

    /**
     * 单元格是个在休息时段
     * @return
     */
    private boolean inRestTimePeriod (int xOffset, int yOffset, Calendar calendar, long restStartTime,long restEndTime) {

        int startHour = mStartTimes[yOffset];

        int firstDayOfWeek = calendar.getFirstDayOfWeek();
        calendar.set(Calendar.DAY_OF_WEEK, firstDayOfWeek + xOffset);
        calendar.set(Calendar.HOUR_OF_DAY, startHour);
        calendar.set(Calendar.MINUTE, 1);
        calendar.set(Calendar.SECOND, 1);
        long cellStartTime=calendar.getTimeInMillis();
        //看单元格的开始时间是否在休息时段
        if(restStartTime<cellStartTime&&restEndTime>cellStartTime){
            return true;
        }else if (yOffset < mStartTimes.length - 1){//结束时间是否在休息时段内
            int endTime = mStartTimes[yOffset + 1];
            calendar.set(Calendar.DAY_OF_WEEK, firstDayOfWeek + xOffset);
            calendar.set(Calendar.HOUR_OF_DAY, endTime);
            calendar.set(Calendar.MINUTE, -1);
            calendar.set(Calendar.SECOND, -1);
            long cellEndTime=calendar.getTimeInMillis();
            if(restStartTime<cellEndTime&&restEndTime>cellEndTime){
                return true;
            }else{
                return false;
            }
        }else if (yOffset ==mStartTimes.length - 1){//最后一个当独处理,只要结束时间在最后一个的开始时间后
            if(restEndTime>cellStartTime){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }


    private int getXoffest(Calendar startCal) {
        int firstDayOfWeek = startCal.getFirstDayOfWeek();
        int dayOfWeek = startCal.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek - firstDayOfWeek;
    }

    /***
     * 通过position获取对应的时间，
     * 以PAGE_SIZE/2的位置为今天
     *
     * @return
     */
    private Date getTimeByPosition(int position) {
        int todayPosition = PAGE_SIZE / 2;
        int offset = position - todayPosition;
        weekCounter.setTime(today.getTime());
        weekCounter.add(DATE, 7 * offset);
        return weekCounter.getTime();
    }

    public int getDayOfWeek(int firstDayOfWeek, int offset) {
        int dayOfWeek = firstDayOfWeek + offset;
        return dayOfWeek;
    }

    boolean betweenDates(Date date, Calendar minCal, Calendar maxCal) {
        final Date min = minCal.getTime();
        return (date.equals(min) || date.after(min)) // >= minCal
                && date.before(maxCal.getTime()); // && < maxCal
    }


    private Dialog mDialog;

    private void closeLessonInfoDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    private void showLessonInfoDialog(CellTag cellTag,RestTime restTime) {
        //只有休息
        if(restTime!=null&&cellTag==null){
            mDialog = new Dialog(getActivity(), com.zhan.framework.R.style.Dialog);
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            mDialog.setContentView(populateRestInfo(inflater, restTime));
            mDialog.show();
            return;
        }
        if (cellTag == null) {
            return;
        }
        mDialog = new Dialog(getActivity(), com.zhan.framework.R.style.Dialog);
        int totalSize = cellTag.weekPublicLessonList.size() + cellTag.weekLessonList.size();
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        if (totalSize == 1&&restTime==null) {
            if (cellTag.weekLessonList.size() == 1) {
                mDialog.setContentView(populateLessonInfo(inflater, cellTag.weekLessonList.get(0)));
            } else {
                mDialog.setContentView(populatePublicLessonInfo(inflater, cellTag.weekPublicLessonList.get(0)));
            }
        } else{
            LinearLayout content = new LinearLayout(getActivity());
            content.setOrientation(LinearLayout.VERTICAL);
            for (WeekLesson lesson : cellTag.weekLessonList) {
                content.addView(populateLessonInfo(inflater, lesson));
            }
            for (WeekPublicLesson lesson : cellTag.weekPublicLessonList) {
                content.addView(populatePublicLessonInfo(inflater, lesson));
            }
            if(restTime!=null){
                content.addView(populateRestInfo(inflater, restTime));
            }
            mDialog.setContentView(content);
        }
        mDialog.show();
    }

    private View populateLessonInfo(LayoutInflater inflater, WeekLesson lesson) {

        View dialogView = inflater.inflate(R.layout.lesson_info_dialog_layout, null);

        ImageView icon = (ImageView) dialogView.findViewById(R.id.icon);
        icon.setImageResource(Tools.getBlueCourseTypeIconBySubTypeName(lesson.getCourseSubTypeName()));

        TextView timeTxt = (TextView) dialogView.findViewById(R.id.time);
        String startTime = mLessonTimeFormat.format(lesson.getStartTime());
        String sendTime = mLessonTimeFormat.format(lesson.getEndTime());
        timeTxt.setText(startTime + "-" + sendTime);


        TextView summaryTxt = (TextView) dialogView.findViewById(R.id.summary);
        summaryTxt.setText(String.format("%s%s  %s%s", lesson.getCourseTypeName(), lesson.getCourseSubTypeName(), getString(R.string.title_student), lesson.getStudentName()));

        Button submitBtn = (Button) dialogView.findViewById(R.id.submit);
        submitBtn.setVisibility(View.VISIBLE);
        submitBtn.setTag(lesson);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WeekLesson lesson = (WeekLesson) view.getTag();
                ClassFeedbackFragment.launch(getActivity(), lesson.getLessonId());
                closeLessonInfoDialog();
            }
        });

        return dialogView;
    }

    private View populatePublicLessonInfo(LayoutInflater inflater, final WeekPublicLesson lesson) {
        View dialogView = inflater.inflate(R.layout.lesson_info_dialog_layout, null);
        TextView timeTxt = (TextView) dialogView.findViewById(R.id.time);
        String startTime = mLessonTimeFormat.format(lesson.getStartTime());
        String sendTime = mLessonTimeFormat.format(lesson.getEndTime());
        timeTxt.setText(startTime + "-" + sendTime);

        TextView summaryTxt = (TextView) dialogView.findViewById(R.id.summary);
        summaryTxt.setText(String.format("%s (%s)", lesson.getClassTitle(), getString(R.string.open_course)));

        Button submitBtn = (Button) dialogView.findViewById(R.id.submit);
        submitBtn.setVisibility(View.GONE);

        return dialogView;
    }

    private View populateRestInfo(LayoutInflater inflater, RestTime restInfo) {
        View dialogView = inflater.inflate(R.layout.rest_info_dialog_layout, null);
        TextView timeTxt = (TextView) dialogView.findViewById(R.id.time);
        String startTime = mLessonTimeFormat.format(restInfo.getStartTime());
        String sendTime = mLessonTimeFormat.format(restInfo.getEndTime());
        timeTxt.setText(startTime + "-" + sendTime);

        TextView titleTxt = (TextView) dialogView.findViewById(R.id.title);
        titleTxt.setText(restInfo.getRestTypeName());

        return dialogView;
    }

    private String getWeekStr(int index){
        String weekStr="";
        switch (index){
            case 0:
                weekStr="Sun";
                break;
            case 1:
                weekStr="Mon";
                break;
            case 2:
                weekStr="Tue";
                break;
            case 3:
                weekStr="Wed";
                break;
            case 4:
                weekStr="Thu";
                break;
            case 5:
                weekStr="Fri";
                break;
            case 6:
                weekStr="Sat";
                break;
        }
        return weekStr;
    }


    private void showRemindBtn(){
        mRemindBtn =new FloatingActionButton(getActivity());
        mRemindBtn.setImageResource(R.drawable.remind_for_class);
        mRemindBtn.setScaleType(ImageView.ScaleType.CENTER);
        mRemindBtn.setColorNormal(0xffc0c0c0);
        mRemindBtn.setColorPressed(0xffc0c0c0);
        mRemindBtn.setColorRipple(0xffF06292);
        mRemindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRemindLesson != null) {
                    mLessonNotifyManager.remindForLesson((int) mRemindLesson.getLessonId());
                } else {
                    ToastUtils.toast(R.string.remind_for_class_no_lesson);
                }
            }
        });

        FrameLayout.LayoutParams params =getLayoutParams();
        mRemindBtn.setLayoutParams(params);

        ((ActionBarActivity) getActivity()).getRootView().addView(mRemindBtn);

        mRemindBtn.setOnTouchListener(new View.OnTouchListener() {
            private float startRawX,startRawY;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float dx;
                float dy;
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
                        params.gravity = Gravity.LEFT | Gravity.TOP;
                        startRawX=motionEvent.getRawX();
                        startRawY=motionEvent.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        dx=Math.abs(motionEvent.getRawX()-startRawX);
                        dy=Math.abs(motionEvent.getRawY()-startRawY);
                        if(dx>PixelUtils.dp2px(5)||dy>PixelUtils.dp2px(5)){
                            moveViewWithFinger(view, motionEvent.getRawX(), motionEvent.getRawY());
                        }else{

                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        dx=Math.abs(motionEvent.getRawX()-startRawX);
                        dy=Math.abs(motionEvent.getRawY()-startRawY);
                        if(dx>PixelUtils.dp2px(5)||dy>PixelUtils.dp2px(5)){

                        }else{
                            view.performClick();
                        }
                        break;
                }
                return true;
            }
        });

        mHandler.post(mGetRemindLessonRunnable);
    }

    private void moveViewWithFinger(View view, float rawX, float rawY) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.leftMargin = (int) rawX - view.getWidth() / 2;
        params.topMargin = (int) rawY - getSystemStatusBarHeight() - view.getHeight() / 2;
        view.setLayoutParams(params);

        App.ctx.setFabTopMargin(params.topMargin);
        App.ctx.setFabLeftMargin(params.leftMargin);
    }

    private FrameLayout.LayoutParams getLayoutParams(){
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        if(App.ctx.getFabLeftMargin()==0&&App.ctx.getFabTopMargin()==0){
            params.gravity= Gravity.RIGHT|Gravity.BOTTOM;
            params.bottomMargin=PixelUtils.dp2px(60);
        }else{
            params.gravity = Gravity.LEFT | Gravity.TOP;
            params.leftMargin=App.ctx.getFabLeftMargin();
            params.topMargin=App.ctx.getFabTopMargin();
        }

        return params;
    }

    public int getSystemStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private Runnable mGetRemindLessonRunnable=new Runnable(){
        @Override
        public void run() {
            mRemindLesson=mLessonNotifyManager.getRemindLesson();
            if(mRemindLesson==null){
                mRemindBtn.setColorNormal(0xffc0c0c0);
                mRemindBtn.setColorPressed(0xffc0c0c0);
            }else{
                mRemindBtn.setColorNormal(0xff20a6fb);
                mRemindBtn.setColorPressed(0xff1b8dd6);
            }
            //每十秒轮询
            mHandler.removeCallbacks(mGetRemindLessonRunnable);
            mHandler.postDelayed(mGetRemindLessonRunnable,10000);
        }
    };

    private DailyLesson mRemindLesson;
    private FloatingActionButton mRemindBtn;
}
