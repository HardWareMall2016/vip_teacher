package com.zhan.vip_teacher.utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.loopj.android.http.RequestHandle;
import com.zhan.framework.network.HttpRequestHandler;
import com.zhan.framework.network.HttpRequestUtils;
import com.zhan.framework.utils.ToastUtils;
import com.zhan.vip_teacher.R;
import com.zhan.vip_teacher.base.App;
import com.zhan.vip_teacher.bean.DailyLessonRequestBean;
import com.zhan.vip_teacher.bean.DailyLessonResponseBean;
import com.zhan.vip_teacher.bean.TeacherHastenLessonRequestBean;
import com.zhan.vip_teacher.bean.TeacherHastenLessonResponseBean;
import com.zhan.vip_teacher.bean.UserInfo;
import com.zhan.vip_teacher.db.bean.DailyLesson;
import com.zhan.vip_teacher.network.ApiUrls;
import com.zhan.vip_teacher.receiver.VipReceiver;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class LessonNotifyManager {
    private static final String TAG = "LessonNotifyManager";

    private RequestHandle mRequestHandle;

    private RequestHandle mRemindRequestHandle;

    private Activity mActivity;

    private final String LESSON_TYPE = "-1";

    public LessonNotifyManager(Activity activity){
        mActivity=activity;
    }

    public DailyLessonRequestBean getRequestBean(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startTime = calendar.getTime();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date endTime = calendar.getTime();

        DailyLessonRequestBean requestBean = new DailyLessonRequestBean();
        DailyLessonRequestBean.DataEntity dataEntity = new DailyLessonRequestBean.DataEntity();
        requestBean.setData(dataEntity);
        dataEntity.setTeacherId(UserInfo.getCurrentUser().getUserID());
        dataEntity.setStartTime(Tools.formatToServerTimeStr(startTime.getTime()));
        dataEntity.setEndTime(Tools.formatToServerTimeStr(endTime.getTime()));

        return requestBean;
    }


    public DailyLessonRequestBean getTodayRequestBean(){
        Calendar calendar = Calendar.getInstance();
        //calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startTime = calendar.getTime();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date endTime = calendar.getTime();

        DailyLessonRequestBean requestBean = new DailyLessonRequestBean();
        DailyLessonRequestBean.DataEntity dataEntity = new DailyLessonRequestBean.DataEntity();
        requestBean.setData(dataEntity);
        dataEntity.setTeacherId(UserInfo.getCurrentUser().getUserID());
        dataEntity.setStartTime(Tools.formatToServerTimeStr(startTime.getTime()));
        dataEntity.setEndTime(Tools.formatToServerTimeStr(endTime.getTime()));

        return requestBean;
    }

    private void parseTodayLessons(){
        DailyLessonRequestBean requestBean = getTodayRequestBean();
        List<DailyLesson> lessons= CacheUtility.findCacheData(ApiUrls.TEACHER_DAILY_LESSON, requestBean, DailyLesson.class);
        if(lessons!=null&&lessons.size()>0){
            //设置通知闹钟
            setNotifyAlarm(lessons);
        }else{
            syncTodayLesson(requestBean);
        }
    }

    private void syncTodayLesson(final DailyLessonRequestBean requestBean){
        releaseRequest(mRequestHandle);
        mRequestHandle = HttpRequestUtils.startRequest(ApiUrls.TEACHER_DAILY_LESSON, requestBean, new HttpRequestHandler(){
            @Override
            public void onRequestSucceeded(String content) {
                super.onRequestSucceeded(content);

                List<DailyLesson> lessons=new LinkedList<>();
                DailyLessonResponseBean responseBean = Tools.parseJsonTostError(content, DailyLessonResponseBean.class);
                if (responseBean != null && responseBean.getData() != null && responseBean.getData().getListLesson() != null && responseBean.getData().getListLesson().size() > 0) {

                    for(DailyLessonResponseBean.DataEntity.ListLessonEntity lessonData:responseBean.getData().getListLesson()){
                        DailyLesson lesson=new DailyLesson();
                        lesson.LessonId=lessonData.getLessonId();
                        lesson.StartTime=Tools.parseServerTime(lessonData.getStartTime());
                        lesson.EndTime=Tools.parseServerTime(lessonData.getEndTime());
                        lesson.CourseSubType=lessonData.getCourseSubType();
                        lesson.CourseSubTypeName=lessonData.getCourseSubTypeName();
                        lesson.CourseType=lessonData.getCourseType();
                        lesson.CourseTypeName=lessonData.getCourseTypeName();
                        lesson.LessonStatus=lessonData.getLessonStatus();
                        lesson.LessonStatusName=lessonData.getLessonStatusName();
                        lesson.signStatus=lessonData.getSignStatusName();
                        //对于为空的签到
                        if(TextUtils.isEmpty(lesson.signStatus)&&!LESSON_TYPE.equals(lesson.getLessonType())){
                            setExtSignStatus(lesson);
                        }

                        lesson.LessonType=lessonData.getLessonType();
                        lesson.LessonTypeName=lessonData.getLessonTypeName();
                        lesson.StudentName=lessonData.getStudentName();
                        lesson.PublicClassTitle=lessonData.getPublicClassTitle();
                        lessons.add(lesson);
                    }

                }
                //缓存起来
                CacheUtility.addCacheDataList(ApiUrls.TEACHER_DAILY_LESSON, requestBean, lessons, DailyLesson.class);

                //设置通知闹钟
                setNotifyAlarm(lessons);
            }
        });
    }

    public void releaseRequest(RequestHandle request) {
        HttpRequestUtils.releaseRequest(request);
    }


    public void cancelTodayLessonsNotifyAlarm(){
        DailyLessonRequestBean requestBean = getTodayRequestBean();
        List<DailyLesson> lessons= CacheUtility.findCacheData(ApiUrls.TEACHER_DAILY_LESSON, requestBean, DailyLesson.class);
        if(lessons!=null&&lessons.size()>0){
            cancelNotifyAlarm(lessons);
        }
    }

    /***
     * 课程结束前都算待签到，其他都算忘记签到
     * @param lesson
     */
    private void setExtSignStatus(DailyLesson lesson){
        long currentTimeMillis = System.currentTimeMillis();
        if(currentTimeMillis>lesson.EndTime){
            lesson.signStatus=mActivity.getString(R.string.sign_status_forget);
        }else{
            lesson.signStatus=mActivity.getString(R.string.sign_status_unsign);
        }
    }

    public void setNotifyAlarm(List<DailyLesson> lessons){
        AlarmManager am =(AlarmManager)mActivity.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();

        for(DailyLesson lesson:lessons){

            if(!checkCanSetNotify(lesson)){
                continue;
            }

            Log.i(TAG, "setNotifyAlarm : " + lesson.getLessonId());

            calendar.setTimeInMillis(lesson.StartTime);
            calendar.add(Calendar.MINUTE, -24);

            Intent intent = new Intent(App.getInstance(), VipReceiver.class);
            intent.setAction(VipReceiver.ACTION_SIGN_REMIND);
            intent.putExtra(VipReceiver.INTENT_KEY_LESSON_ID, lesson.getLessonId());

            PendingIntent pendingIntent=PendingIntent.getBroadcast(App.getInstance(), (int) lesson.getLessonId(), intent, 0);

            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    private void cancelNotifyAlarm(List<DailyLesson> lessons){
        AlarmManager am =(AlarmManager)mActivity.getSystemService(Context.ALARM_SERVICE);
        for(DailyLesson lesson:lessons){

            if(!checkCanSetNotify(lesson)){
                continue;
            }

            Log.i(TAG, "cancelNotifyAlarm : " + lesson.getLessonId());

            Intent intent = new Intent(App.getInstance(), VipReceiver.class);
            intent.setAction(VipReceiver.ACTION_SIGN_REMIND);
            intent.putExtra(VipReceiver.INTENT_KEY_LESSON_ID, lesson.getLessonId());

            PendingIntent pendingIntent=PendingIntent.getBroadcast(App.getInstance(), (int) lesson.getLessonId(), intent, 0);
            am.cancel(pendingIntent);
        }
    }

    /**
     * 当前时间在课前25分钟前，就设置推送提醒
     * @param lesson
     * @return
     */
    private boolean checkCanSetNotify(DailyLesson lesson){
        //跳过公开课
        if (LESSON_TYPE.equals(lesson.getLessonType())) {
            return false;
        }

        long currentTimeMillis = System.currentTimeMillis();

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(lesson.StartTime);
        cal.add(Calendar.MINUTE, -24);

        long minTime = cal.getTimeInMillis();

        if(currentTimeMillis<minTime){
            return true;
        }else{
            return false;
        }
    }

    /***
     * 上课提醒
     */
    public void remindForLesson(int lessonId){

        if (mRemindRequestHandle != null && !mRemindRequestHandle.isFinished()) {
            return;
        }

        TeacherHastenLessonRequestBean requestBean = new TeacherHastenLessonRequestBean();
        requestBean.setData(lessonId);
        mRemindRequestHandle = HttpRequestUtils.startRequest(ApiUrls.TEACHER_HASTEN_LESSON, requestBean, new HttpRequestHandler() {
            @Override
            public void onRequestSucceeded(String content) {
                super.onRequestSucceeded(content);
                TeacherHastenLessonResponseBean responseBean = Tools.parseJsonTostError(content, TeacherHastenLessonResponseBean.class);
                if (responseBean != null) {
                    ToastUtils.toast(R.string.remind_for_class_success);
                }
            }

            @Override
            public void onRequestFinished(ResultCode resultCode, String result) {
                super.onRequestFinished(resultCode, result);
                SendEventUtil.sendEvent(4, "催课", result);
                switch (resultCode) {
                    case failed:
                    case timeout:
                    case noNetwork:
                        ToastUtils.toast(result);
                        break;
                }
            }
        });
    }

    public DailyLesson getRemindLesson(){

        DailyLesson remindLesson=null;

        DailyLessonRequestBean requestBean = getTodayRequestBean();
        List<DailyLesson> lessons= CacheUtility.findCacheData(ApiUrls.TEACHER_DAILY_LESSON, requestBean, DailyLesson.class);
        if(lessons!=null&&lessons.size()>0){
            for(DailyLesson lesson :lessons){
                if(canRemindForCalss(lesson)){
                    remindLesson=lesson;
                    break;
                }
            }
        }
        return remindLesson;
    }

    /***
     * 课后6-15分钟，才能催课
     */
    private boolean canRemindForCalss(DailyLesson lesson) {
        //跳过公开课
        if (LESSON_TYPE.equals(lesson.getLessonType())) {
            return false;
        }

        long currentTimeMillis = System.currentTimeMillis();

        Calendar cal = Calendar.getInstance();
        //课后6分钟
        cal.setTimeInMillis(lesson.StartTime);
        cal.add(Calendar.MINUTE, 6);
        long minTime = cal.getTimeInMillis();

        //课后15分钟
        cal.setTimeInMillis(lesson.StartTime);
        cal.add(Calendar.MINUTE, 15);
        long maxTime = cal.getTimeInMillis();

        if (currentTimeMillis > minTime && currentTimeMillis < maxTime) {
            return true;
        } else {
            return false;
        }
    }

    public void release(){
        HttpRequestUtils.releaseRequest(mRemindRequestHandle);
    }
}
