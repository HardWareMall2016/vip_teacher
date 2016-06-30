package com.zhan.vip_teacher.ui.fragment.course;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.zhan.framework.network.HttpClientUtils;
import com.zhan.framework.network.HttpRequestHandler;
import com.zhan.framework.support.inject.ViewInject;
import com.zhan.framework.ui.fragment.ABaseFragment;
import com.zhan.framework.utils.ToastUtils;
import com.zhan.vip_teacher.R;
import com.zhan.vip_teacher.base.App;
import com.zhan.vip_teacher.base.Constants;
import com.zhan.vip_teacher.bean.DailyLessonRequestBean;
import com.zhan.vip_teacher.bean.DailyLessonResponseBean;
import com.zhan.vip_teacher.bean.EventSignInfoBean;
import com.zhan.vip_teacher.bean.SignEventRequestBean;
import com.zhan.vip_teacher.bean.SignInRequestBean;
import com.zhan.vip_teacher.bean.SignInResponseBean;
import com.zhan.vip_teacher.bean.TeacherHastenLessonRequestBean;
import com.zhan.vip_teacher.bean.TeacherHastenLessonResponseBean;
import com.zhan.vip_teacher.bean.UserInfo;
import com.zhan.vip_teacher.db.bean.DailyLesson;
import com.zhan.vip_teacher.db.bean.LocalMessage;
import com.zhan.vip_teacher.event.NewNotifyEvent;
import com.zhan.vip_teacher.network.ApiUrls;
import com.zhan.vip_teacher.ui.activity.MainActivity;
import com.zhan.vip_teacher.ui.widget.CachedViewPager;
import com.zhan.vip_teacher.utils.CacheUtility;
import com.zhan.vip_teacher.utils.LessonNotifyManager;
import com.zhan.vip_teacher.utils.MessageUtility;
import com.zhan.vip_teacher.utils.SendEventUtil;
import com.zhan.vip_teacher.utils.Tools;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;

import static java.util.Calendar.DATE;
import static java.util.Calendar.getInstance;

/**
 * Created by Administrator on 2016/3/1.
 */
public class CourseFragment extends ABaseFragment implements SwipeRefreshLayout.OnRefreshListener, CachedViewPager.ICacheView, AdapterView.OnItemClickListener {

    @ViewInject(idStr = "calendar", click = "OnClick")
    View viewCalendar;//日历

    @ViewInject(idStr = "sign_in", click = "OnClick")
    View viewSignIn;//签到

    @ViewInject(idStr = "title", click = "OnClick")
    TextView viewTitle;

    @ViewInject(id = R.id.daily_course)
    CachedViewPager mDailyCourse;//每日课程

    /*@ViewInject(id = R.id.fab)
    private FloatingActionButton mFab;*/

    private Dialog mSignDialog;

    private final int DAYS_COUNT = 14;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");

    private boolean mFirstRequest = true;
    private int mPreSelectedPos = -1;

    private Calendar mDayCounter;
    private DailyLesson mSignInLesson;
    private LessonAdapter mAdapter;

    private SimpleDateFormat mLessonTimeFormat = new SimpleDateFormat("HH:mm");

    private final String LESSON_TYPE = "-1";

    private boolean mForceRefreshToday = false;

    private LessonNotifyManager mLessonNotifyManager;

    private class PageViewHolder {
        ListView dailyCourseList;
        View layoutLoading;
        View layoutLoadFailed;
        View layoutEmpty;
        SwipeRefreshLayout swipeRefreshLayout;
    }

    class ListViewHolder {
        ImageView icon;
        DailyLesson lesson;
        TextView time;
        TextView courseType;
        TextView lessonType;
        TextView lessonStatus;
        TextView signStatus;
        TextView student;
    }

    private class MgsInfo {
        PageViewHolder viewHolder;
        Date date;
    }

    private enum ViewStatus {LOADING, LOADING_SUCCESS, LOADING_FAILED, EMPTY}

    @Override
    protected int inflateContentView() {
        return R.layout.course_frag_layout;
    }

    /*@Override
    public boolean isCacheRootView() {
        return true;
    }*/

    @Override
    protected void layoutInit(LayoutInflater inflater, Bundle savedInstanceSate) {
        mLessonNotifyManager = new LessonNotifyManager(getActivity());
        //mFab.setOnClickListener(mRemindClicked);

        //refreshBtnStatus();
        if (mPreSelectedPos == -1) {
            mPreSelectedPos = DAYS_COUNT / 2;
        }
        mDailyCourse.initViewPager(DAYS_COUNT, CachedViewPager.DEF_CACHE_SIZE, this, mPreSelectedPos);
        mFirstRequest = true;
        mDayCounter = Calendar.getInstance();
        Date date = parsePositionToDate(mPreSelectedPos);
        viewTitle.setText(dateFormat.format(date));

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mForceRefreshToday) {
            mForceRefreshToday = false;
            forceRefreshToday();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mHandler.removeMessages(MSG_QUERY);
    }

    public void setForceRefreshToday(boolean mForceRefreshToday) {
        this.mForceRefreshToday = mForceRefreshToday;
    }

    private void forceRefreshToday() {
        mFirstRequest = true;
        if (UserInfo.getCurrentUser() == null || !UserInfo.getCurrentUser().isLogined()) {
            return;
        }
        if (mDailyCourse.getCurrentItem() == DAYS_COUNT / 2) {
            onRefresh();
        } else {
            mDailyCourse.setCurrentItem(DAYS_COUNT / 2);
        }
    }

    void OnClick(View v) {
        switch (v.getId()) {
            case R.id.calendar:
                WeekCourseFragment.launch(CourseFragment.this.getActivity());
                break;
            case R.id.sign_in:
                mSignInLesson=null;
                DailyLessonRequestBean requestBean = mLessonNotifyManager.getTodayRequestBean();
                List<DailyLesson> lessons= CacheUtility.findCacheData(ApiUrls.TEACHER_DAILY_LESSON, requestBean, DailyLesson.class);
                if(lessons!=null&&lessons.size()>0){
                    mSignInLesson = findCanSignLesson(lessons);
                }
                if (mSignInLesson != null) {
                    signIn((int) mSignInLesson.LessonId);
                }else{
                    showSignResultDialog(null,false,getString(R.string.sign_in_no_lesson));
                }
                break;
        }
    }

    @Override
    public View inflaterPageView(LayoutInflater inflater, int position) {
        View page = inflater.inflate(R.layout.daily_course_layout, null);
        PageViewHolder viewHolder = new PageViewHolder();
        page.setTag(viewHolder);
        viewHolder.dailyCourseList = (ListView) page.findViewById(R.id.daily_course_list);
        viewHolder.layoutLoading = page.findViewById(R.id.layoutLoading);
        viewHolder.layoutLoadFailed = page.findViewById(R.id.layoutLoadFailed);
        viewHolder.layoutEmpty = page.findViewById(R.id.layoutEmpty);
        viewHolder.swipeRefreshLayout = (SwipeRefreshLayout) page.findViewById(R.id.swipeRefreshLayout);
        viewHolder.swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        //设置下拉刷新功能
        viewHolder.swipeRefreshLayout.setOnRefreshListener(this);
        return page;
    }

    @Override
    public void initPageView(View page, int position) {
        PageViewHolder viewHolder = (PageViewHolder) page.getTag();
        viewHolder.swipeRefreshLayout.setRefreshing(false);

        Date date = parsePositionToDate(position);
        DailyLessonRequestBean requestBean = mLessonNotifyManager.getRequestBean(date);

        List<DailyLesson> lessons = CacheUtility.findCacheData(ApiUrls.TEACHER_DAILY_LESSON, requestBean, DailyLesson.class);
        if (lessons != null && lessons.size() > 0) {
            refreshView(viewHolder, ViewStatus.LOADING_SUCCESS);
            //Log.i("wuyue","Has cached position : "+position+", size : "+lessons.size());
            mAdapter = new LessonAdapter(lessons);
            viewHolder.dailyCourseList.setAdapter(mAdapter);
            viewHolder.dailyCourseList.setOnItemClickListener(CourseFragment.this);
        } else {
            lessons = new LinkedList<>();
            mAdapter = new LessonAdapter(lessons);
            viewHolder.dailyCourseList.setAdapter(mAdapter);
            refreshView(viewHolder, ViewStatus.LOADING);
            //Log.i("wuyue","NOT cached : "+position);
        }
    }

    @Override
    public void onPageSelected(View selectedPage, int position) {
        //首先释放到所以请求
        releaseAllRequest();

        mPreSelectedPos = position;

        //设置FAB效果
        PageViewHolder viewHolder = (PageViewHolder) selectedPage.getTag();
        //mFab.attachToListView(viewHolder.dailyCourseList);

        //计算页面显示数据
        Date date = parsePositionToDate(position);
        viewTitle.setText(dateFormat.format(date));

        Message msg = Message.obtain();
        msg.what = MSG_QUERY;
        MgsInfo msgInfo = new MgsInfo();
        msgInfo.viewHolder = viewHolder;
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
    public void onRefresh() {
        //Log.i("wuyue","======onRefresh ===== ");
        View page = mDailyCourse.getPage(mDailyCourse.getCurrentItem());
        PageViewHolder viewHolder = (PageViewHolder) page.getTag();
        Date date = parsePositionToDate(mDailyCourse.getCurrentItem());

        Message msg = Message.obtain();
        msg.what = MSG_QUERY;
        MgsInfo msgInfo = new MgsInfo();
        msgInfo.viewHolder = viewHolder;
        msgInfo.date = date;
        msg.obj = msgInfo;
        mHandler.removeMessages(MSG_QUERY);
        mHandler.sendMessage(msg);
    }

    @NonNull
    private Date parsePositionToDate(int position) {
        //计算每页对应的日期
        int todayPosition = DAYS_COUNT / 2;
        int offset = position - todayPosition;
        mDayCounter.setTime(Calendar.getInstance().getTime());
        mDayCounter.add(DATE, offset);
        return mDayCounter.getTime();
    }

    private final static int MSG_QUERY = 101;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_QUERY:
                    removeMessages(MSG_QUERY);
                    MgsInfo msginfo = (MgsInfo) msg.obj;
                    startQueryLesson(msginfo.date, msginfo.viewHolder);
                    break;
            }
        }
    };

    private void refreshView(PageViewHolder viewHolder, ViewStatus status) {
        switch (status) {
            case LOADING:
                viewHolder.swipeRefreshLayout.setVisibility(View.GONE);
                viewHolder.layoutLoading.setVisibility(View.VISIBLE);
                viewHolder.layoutLoadFailed.setVisibility(View.GONE);
                viewHolder.layoutEmpty.setVisibility(View.GONE);
                break;
            case LOADING_SUCCESS:
                viewHolder.swipeRefreshLayout.setVisibility(View.VISIBLE);
                viewHolder.layoutLoading.setVisibility(View.GONE);
                viewHolder.layoutLoadFailed.setVisibility(View.GONE);
                viewHolder.layoutEmpty.setVisibility(View.GONE);
                break;
            case LOADING_FAILED:
                ListAdapter adapter = viewHolder.dailyCourseList.getAdapter();
                //显示缓存数据
                if (adapter != null && adapter.getCount() > 0) {
                    viewHolder.swipeRefreshLayout.setVisibility(View.VISIBLE);
                    viewHolder.layoutLoading.setVisibility(View.GONE);
                    viewHolder.layoutLoadFailed.setVisibility(View.GONE);
                    viewHolder.layoutEmpty.setVisibility(View.GONE);
                } else {
                    viewHolder.swipeRefreshLayout.setVisibility(View.GONE);
                    viewHolder.layoutLoading.setVisibility(View.GONE);
                    viewHolder.layoutLoadFailed.setVisibility(View.GONE);
                    viewHolder.layoutEmpty.setVisibility(View.VISIBLE);
                }
                break;
            case EMPTY:
                viewHolder.swipeRefreshLayout.setVisibility(View.GONE);
                viewHolder.layoutLoading.setVisibility(View.GONE);
                viewHolder.layoutLoadFailed.setVisibility(View.GONE);
                viewHolder.layoutEmpty.setVisibility(View.VISIBLE);
                break;
        }
    }

    /***
     * 获取教师一天的课程
     *
     * @param date
     */
    private void startQueryLesson(Date date, PageViewHolder viewHolder) {
        DailyLessonRequestBean requestBean = mLessonNotifyManager.getRequestBean(date);
        startRequest(ApiUrls.TEACHER_DAILY_LESSON, requestBean, new DailyLessonRequestHandler(viewHolder, date, requestBean));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ListViewHolder holder = (ListViewHolder) view.getTag();//取出ViewHolder对象
        if (holder != null) {
            //公开课不能反馈
            if (LESSON_TYPE.equals(holder.lesson.getLessonType())) {
                ToastUtils.toast(R.string.feedback_no_open_course);
            }
            //课后才能反馈
            else {
                ClassFeedbackFragment.launch(getActivity(), holder.lesson.LessonId);
            }
        }
    }

    private class DailyLessonRequestHandler extends HttpRequestHandler {
        private PageViewHolder viewHolder;
        private Date date;
        private DailyLessonRequestBean requestBean;

        public DailyLessonRequestHandler(PageViewHolder viewHolder, Date date, DailyLessonRequestBean requestBean) {
            this.viewHolder = viewHolder;
            this.date = date;
            this.requestBean = requestBean;
        }

        @Override
        public void onRequestFinished(ResultCode resultCode, String result) {
            switch (resultCode) {
                case success:
                    break;
                case canceled:
                    refreshView(viewHolder, ViewStatus.LOADING_FAILED);
                    break;
                default:
                    refreshView(viewHolder, ViewStatus.LOADING_FAILED);
                    ToastUtils.toast(result);
            }
            //Log.i("wuyue","isRefreshing = "+viewHolder.swipeRefreshLayout.isRefreshing());
            if (viewHolder.swipeRefreshLayout.isRefreshing())
                viewHolder.swipeRefreshLayout.setRefreshing(false);
            //Log.i("wuyue","resultCode = "+resultCode);
        }

        @Override
        public void onRequestSucceeded(String content) {
            super.onRequestSucceeded(content);
            if (getActivity() == null) {
                return;
            }
            List<DailyLesson> lessons = new LinkedList<>();
            DailyLessonResponseBean responseBean = Tools.parseJsonTostError(content, DailyLessonResponseBean.class);
            if (responseBean != null && responseBean.getData() != null && responseBean.getData().getListLesson() != null && responseBean.getData().getListLesson().size() > 0) {
                refreshView(viewHolder, ViewStatus.LOADING_SUCCESS);

                for (DailyLessonResponseBean.DataEntity.ListLessonEntity lessonData : responseBean.getData().getListLesson()) {
                    DailyLesson lesson = new DailyLesson();
                    lesson.LessonId = lessonData.getLessonId();
                    lesson.StartTime = Tools.parseServerTime(lessonData.getStartTime());
                    lesson.EndTime = Tools.parseServerTime(lessonData.getEndTime());
                    lesson.CourseSubType = lessonData.getCourseSubType();
                    lesson.CourseSubTypeName = lessonData.getCourseSubTypeName();
                    lesson.CourseType = lessonData.getCourseType();
                    lesson.CourseTypeName = lessonData.getCourseTypeName();
                    lesson.LessonStatus = lessonData.getLessonStatus();
                    lesson.LessonStatusName = lessonData.getLessonStatusName();
                    lesson.signStatus = lessonData.getSignStatusName();
                    //对于为空的签到
                    if (TextUtils.isEmpty(lesson.signStatus) && !LESSON_TYPE.equals(lesson.getLessonType())) {
                        setExtSignStatus(lesson);
                    }

                    lesson.LessonType = lessonData.getLessonType();
                    lesson.LessonTypeName = lessonData.getLessonTypeName();
                    lesson.StudentName = lessonData.getStudentName();
                    lesson.PublicClassTitle = lessonData.getPublicClassTitle();
                    lessons.add(lesson);
                }

                mAdapter = new LessonAdapter(lessons);
                viewHolder.dailyCourseList.setAdapter(mAdapter);
                viewHolder.dailyCourseList.setOnItemClickListener(CourseFragment.this);

                //如果是当天就检查签到
                Calendar calendar = getInstance();
                calendar.setTime(date);
                if (lessons.size() > 0 && Tools.sameDate(Calendar.getInstance(), calendar)) {
                    mSignInLesson = findCanSignLesson(lessons);
                    if (mSignInLesson != null && needShowSignDialog(mSignInLesson)) {
                        showSignDialog(mSignInLesson);
                    }
                }
            } else {
                refreshView(viewHolder, ViewStatus.EMPTY);
            }

            //当天的课程，设置通知
            Calendar calendar = getInstance();
            calendar.setTime(date);
            if(Tools.sameDate(Calendar.getInstance(), calendar)){
                //先取消掉以前设置过的通知
                mLessonNotifyManager.cancelTodayLessonsNotifyAlarm();
                //开始新通知
                mLessonNotifyManager.setNotifyAlarm(lessons);
            }

            //缓存起来
            CacheUtility.addCacheDataList(ApiUrls.TEACHER_DAILY_LESSON, requestBean, lessons, DailyLesson.class);
        }
    }

    private class LessonAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        private List<DailyLesson> mLessons;

        public LessonAdapter(List<DailyLesson> lessons) {
            mInflater = LayoutInflater.from(getActivity());
            this.mLessons = lessons;
        }

        @Override
        public int getCount() {
            return mLessons.size();
        }

        @Override
        public Object getItem(int i) {
            return mLessons.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ListViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.daily_course_item_layout, null);
                holder = new ListViewHolder();
                /**得到各个控件的对象*/
                holder.icon = (ImageView) convertView.findViewById(R.id.icon);
                holder.time = (TextView) convertView.findViewById(R.id.time);
                holder.courseType = (TextView) convertView.findViewById(R.id.course_type);
                holder.lessonType = (TextView) convertView.findViewById(R.id.lesson_type);
                holder.lessonStatus = (TextView) convertView.findViewById(R.id.lesson_status);
                holder.student = (TextView) convertView.findViewById(R.id.student);
                holder.signStatus = (TextView) convertView.findViewById(R.id.sign_status);

                convertView.setTag(holder);//绑定ViewHolder对象
            } else {
                holder = (ListViewHolder) convertView.getTag();//取出ViewHolder对象
            }

            DailyLesson lesson = mLessons.get(position);

            String startTime = mLessonTimeFormat.format(lesson.StartTime);
            String sendTime = mLessonTimeFormat.format(lesson.EndTime);

            holder.lesson = lesson;
            holder.time.setText(startTime + "-" + sendTime);
            //公开课，单独处理
            if (LESSON_TYPE.equals(lesson.getLessonType())) {
                String title = lesson.getPublicClassTitle();
                if (!TextUtils.isEmpty(title) && title.length() > 6) {
                    title = title.substring(0, 5) + "...";
                }
                holder.courseType.setText(String.format("%s", title));
                holder.lessonType.setText(String.format("(%s)", lesson.LessonTypeName));

                holder.lessonStatus.setVisibility(View.GONE);
                holder.student.setVisibility(View.GONE);
                holder.signStatus.setVisibility(View.GONE);
            } else {
                holder.lessonType.setText(String.format("(%s)", lesson.LessonTypeName));
                holder.courseType.setText(String.format("%s %s", lesson.CourseTypeName, lesson.CourseSubTypeName));
                if (TextUtils.isEmpty(lesson.LessonStatusName)) {
                    holder.lessonStatus.setText(getString(R.string.lesson_status_name) + getString(R.string.lesson_status_no_lesson));
                } else {
                    holder.lessonStatus.setText(getString(R.string.lesson_status_name) + lesson.LessonStatusName);
                }
                holder.student.setText(getString(R.string.title_student) + lesson.StudentName);

                holder.signStatus.setText(getString(R.string.title_sign_status));
                SpannableString ss = new SpannableString(lesson.signStatus);
                if (getString(R.string.sign_status_on_time).equals(lesson.signStatus)) {
                    ss.setSpan(new ForegroundColorSpan(0xff20a6fb), 0, lesson.signStatus.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    ss.setSpan(new ForegroundColorSpan(Color.RED), 0, lesson.signStatus.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                holder.signStatus.append(ss);

                holder.lessonStatus.setVisibility(View.VISIBLE);
                holder.student.setVisibility(View.VISIBLE);
                holder.signStatus.setVisibility(View.VISIBLE);
            }

            holder.icon.setImageResource(Tools.getCourseTypeIconBySubTypeName(lesson.getCourseSubTypeName()));

            return convertView;
        }
    }

    /***
     * 课前25分钟-上课结束
     */
    private DailyLesson findCanSignLesson(List<DailyLesson> lessons) {
        long currentTimeMillis = System.currentTimeMillis();

        Calendar cal = Calendar.getInstance();
        DailyLesson lesson = null;
        for (DailyLesson item : lessons) {
            //跳过公开课
            if (LESSON_TYPE.equals(item.getLessonType())) {
                continue;
            }
            //课前25分钟
            cal.setTimeInMillis(item.StartTime);
            cal.add(Calendar.MINUTE, -25);
            long minTime = cal.getTimeInMillis();

            //上课结束
            cal.setTimeInMillis(item.EndTime);
            //cal.add(Calendar.MINUTE, 15);
            long maxTime = cal.getTimeInMillis();
            //最新的满足条件的课程
            if (currentTimeMillis >= minTime && currentTimeMillis <= maxTime) {
                lesson = item;
            }
        }

        return lesson;
    }

    /***
     * 课前25-2分钟，并且是待签到才弹出签到
     */
    private boolean needShowSignDialog(DailyLesson lesson) {
        String unSigne = getString(R.string.sign_status_unsign);
        if (!unSigne.equals(lesson.getSignStatus())) {
            return false;
        }

        long currentTimeMillis = System.currentTimeMillis();

        Calendar cal = Calendar.getInstance();
        //课前25分钟
        cal.setTimeInMillis(lesson.StartTime);
        cal.add(Calendar.MINUTE, -25);
        long minTime = cal.getTimeInMillis();

        //课前15分钟
        cal.setTimeInMillis(lesson.StartTime);
        cal.add(Calendar.MINUTE, -2);
        long maxTime = cal.getTimeInMillis();

        if (currentTimeMillis >= minTime && currentTimeMillis <= maxTime) {
            return true;
        } else {
            return false;
        }
    }

    /***
     * 课程结束前都算待签到，其他都算忘记签到
     *
     * @param lesson
     */
    private void setExtSignStatus(DailyLesson lesson) {
        long currentTimeMillis = System.currentTimeMillis();

        /*Calendar cal = Calendar.getInstance();
        //上课后15分钟
        cal.setTimeInMillis(lesson.StartTime);
        cal.add(Calendar.MINUTE, 15);
        long afterClassTime = cal.getTimeInMillis();*/
        if (currentTimeMillis > lesson.EndTime) {
            lesson.signStatus = getString(R.string.sign_status_forget);
        } else {
            lesson.signStatus = getString(R.string.sign_status_unsign);
        }
    }

    /***
     * 课前25-2mins，准时，正常签到；课前2-0mins，警告；课后0-15mins，迟到；课后15mins后，忘记签到；
     *
     * @param lesson
     * @return
     */
    private String calculateSignStatus(DailyLesson lesson) {
        String signStatus = "";

        long currentTimeMillis = System.currentTimeMillis();

        Calendar cal = Calendar.getInstance();
        //课前25分钟
        cal.setTimeInMillis(lesson.StartTime);
        cal.add(Calendar.MINUTE, -25);
        long bef25min = cal.getTimeInMillis();

        //课前15分钟
        cal.setTimeInMillis(lesson.StartTime);
        cal.add(Calendar.MINUTE, -2);
        long bef2min = cal.getTimeInMillis();

        //课程开始
        long startMin = lesson.StartTime;

        //上课后15分钟
        cal.setTimeInMillis(lesson.StartTime);
        cal.add(Calendar.MINUTE, 15);
        long after15min = cal.getTimeInMillis();

        //课程结束
        long endTime = lesson.EndTime;

        //课前25-2mins，准时，正常签到
        if (currentTimeMillis >= bef25min && currentTimeMillis <= bef2min) {
            signStatus = getString(R.string.sign_status_on_time);
        } else if (currentTimeMillis > bef2min && currentTimeMillis <= startMin) {//课前2-0mins，警告
            signStatus = getString(R.string.sign_status_warning);
        } else if (currentTimeMillis > startMin && currentTimeMillis <= after15min) {//课后0-15mins，迟到
            signStatus = getString(R.string.sign_status_be_late);
        } else if (currentTimeMillis > after15min) {//课后15mins后，忘记签到
            signStatus = getString(R.string.sign_status_forget);
        }

        return signStatus;
    }

    /***
     * 课后5-15分钟，才能催课
     */
    private boolean canRemindForCalss(DailyLesson lesson) {

        long currentTimeMillis = System.currentTimeMillis();

        Calendar cal = Calendar.getInstance();
        //课后5分钟
        cal.setTimeInMillis(lesson.StartTime);
        cal.add(Calendar.MINUTE, 5);
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

    private void showSignDialog(DailyLesson lesson) {
        createSignDialog();

        TextView timeTxt = (TextView) mSignDialog.findViewById(R.id.time);
        TextView lessonTypeTxt = (TextView) mSignDialog.findViewById(R.id.lesson_type);
        TextView courseTypeTxt = (TextView) mSignDialog.findViewById(R.id.course_type);
        Button submitBtn = (Button) mSignDialog.findViewById(R.id.submit);
        submitBtn.setTag(lesson);
        submitBtn.setOnClickListener(mSignBtnClicked);

        String startTime = mLessonTimeFormat.format(lesson.StartTime);
        String sendTime = mLessonTimeFormat.format(lesson.EndTime);
        timeTxt.setText(startTime + "-" + sendTime);

        lessonTypeTxt.setText(String.format("(%s)", lesson.LessonTypeName));
        courseTypeTxt.setText(String.format("%s %s", lesson.CourseTypeName, lesson.CourseSubTypeName));

        mSignDialog.setCancelable(false);
        mSignDialog.show();
    }

    private void showSignResultDialog(DailyLesson lesson,boolean success,String result) {
        createSignDialog();

        TextView timeTxt = (TextView) mSignDialog.findViewById(R.id.time);
        TextView lessonTypeTxt = (TextView) mSignDialog.findViewById(R.id.lesson_type);
        TextView courseTypeTxt = (TextView) mSignDialog.findViewById(R.id.course_type);

        ImageView imgSignTip = (ImageView) mSignDialog.findViewById(R.id.img_sign_tip);
        TextView signResult = (TextView) mSignDialog.findViewById(R.id.sign_tip);

        Button submitBtn = (Button) mSignDialog.findViewById(R.id.submit);
        submitBtn.setText(R.string.sure);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeDialog();
            }
        });

        if(lesson!=null){
            String startTime = mLessonTimeFormat.format(lesson.StartTime);
            String sendTime = mLessonTimeFormat.format(lesson.EndTime);
            timeTxt.setText(startTime + "-" + sendTime);

            lessonTypeTxt.setText(String.format("(%s)", lesson.LessonTypeName));
            courseTypeTxt.setText(String.format("%s %s", lesson.CourseTypeName, lesson.CourseSubTypeName));
        }

        if(success){
            imgSignTip.setImageResource(R.drawable.sign_success);
        }else{
            imgSignTip.setImageResource(R.drawable.sign_failed);
            signResult.setTextColor(Color.RED);
        }

        signResult.setText(result);

        mSignDialog.setCancelable(false);
        mSignDialog.show();
    }

    private void createSignDialog() {
        closeDialog();

        mSignDialog = new Dialog(getActivity(), com.zhan.framework.R.style.Dialog);
        mSignDialog.setContentView(R.layout.sign_dialog_layout);

        Window window = mSignDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        WindowManager wm = (WindowManager) App.getInstance().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        lp.width = (int) (0.8 * screenW);
    }

    private void closeDialog(){
        if(mSignDialog!=null&&mSignDialog.isShowing()){
            mSignDialog.dismiss();
            mSignDialog=null;
        }
    }

    private View.OnClickListener mSignBtnClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DailyLesson lesson = (DailyLesson) view.getTag();
            signIn((int) lesson.LessonId);
        }
    };

    private void signIn(final int lessId) {
        if (isRequestProcessing(ApiUrls.TEACHER_SIGN_IN)) {
            return;
        }
        SignInRequestBean requestBean = new SignInRequestBean();
        SignInRequestBean.DataEntity dataEntity = new SignInRequestBean.DataEntity();
        dataEntity.setLessonId(lessId);
        dataEntity.setTeacherId(UserInfo.getCurrentUser().getUserID());
        requestBean.setData(dataEntity);
        startRequest(ApiUrls.TEACHER_SIGN_IN, requestBean, new HttpRequestHandler() {
            @Override
            public void onRequestSucceeded(String content) {
                super.onRequestSucceeded(content);
                if (getActivity() == null) {
                    return;
                }

                //下面是添加事件
                String message;
                boolean success = false;
                SignInResponseBean bean = Tools.parseJson(content, SignInResponseBean.class);
                if (bean == null) {
                    message = getString(R.string.json_syntax_error);
                } else if (bean.getCode() == 0) {
                    message = bean.getMessage();
                    success = true;
                } else {
                    message = bean.getMessage();
                }
                sendSignEvent(success, lessId, UserInfo.getCurrentUser().getUserID(), message);

                //下面是解析
                SignInResponseBean responseBean = Tools.parseJson(content, SignInResponseBean.class);
                String result=Tools.verifyResponseResult(responseBean);
                if(TextUtils.isEmpty(result)){
                    if (mSignInLesson != null) {
                        mSignInLesson.signStatus = calculateSignStatus(mSignInLesson);//"已签到";
                        mAdapter.notifyDataSetChanged();
                        recordAndNotifyMsg(mSignInLesson);
                    }
                    showSignResultDialog(mSignInLesson,true,getString(R.string.sign_in_success));
                }else{
                    if(responseBean!=null&&responseBean.getCode()==3){
                        //过期就不用提示了
                        closeDialog();
                        ToastUtils.toast(result);
                    }else{
                        showSignResultDialog(mSignInLesson,false,result);
                    }
                }
            }

            @Override
            public void onRequestFinished(ResultCode resultCode, String result) {
                super.onRequestFinished(resultCode, result);
                closeDialog();
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

    private View.OnClickListener mRemindClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mSignInLesson == null) {
                return;
            }
            if (isRequestProcessing(ApiUrls.TEACHER_HASTEN_LESSON)) {
                return;
            }
            TeacherHastenLessonRequestBean requestBean = new TeacherHastenLessonRequestBean();
            requestBean.setData((int) mSignInLesson.getLessonId());
            startRequest(ApiUrls.TEACHER_HASTEN_LESSON, requestBean, new HttpRequestHandler() {
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
    };

    private void sendSignEvent(boolean success, int lessId, int teacherId, String message) {
        EventSignInfoBean contentBean = new EventSignInfoBean();
        contentBean.setLessId(lessId);
        contentBean.setMessage(message);
        contentBean.setTeacherId(teacherId);
        contentBean.setSuccess(success ? 1 : 0);

        Calendar calendar = Calendar.getInstance();

        SignEventRequestBean requestBean = new SignEventRequestBean();
        requestBean.setAppid("fac0f83a77f74275ad624bec8f7b422b");
        requestBean.setModelId("8");
        requestBean.setYear(String.valueOf(calendar.get(Calendar.YEAR)));
        requestBean.setEventTypeId("1");
        requestBean.setEvent("教师签到");
        requestBean.setContent(JSON.toJSONString(contentBean));
        requestBean.setPlatform("Android");

        String jsonStr = JSON.toJSONString(requestBean);
        Log.i("SendEvent", jsonStr);
        HttpEntity entity = new StringEntity(jsonStr, "utf-8");

        Header[] headers = new Header[1];
        headers[0] = new BasicHeader("platform", "Android");
        HttpClientUtils.post(ApiUrls.ADD_EVENT, headers, entity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String content = new String(bytes);
                Log.i("SendEvent", "onSuccess statusCode = " + i);
                Log.i("SendEvent", "onSuccess responseBody = " + content);
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                if (bytes != null) {
                    String content = new String(bytes);
                    Log.i("SendEvent", "onFailure responseBody = " + content);
                }
                Log.i("SendEvent", "onFailure statusCode = " + i);
            }
        });
    }


    private void recordAndNotifyMsg(DailyLesson lesson) {
        int messageId;
        LocalMessage latestMsg = MessageUtility.findLatestMessage(LocalMessage.class);
        if (latestMsg == null) {
            messageId = 1;
        } else {
            messageId = Integer.valueOf(latestMsg.getMessageID()) + 1;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("今天(MM月dd日) , HH:mm");

        SimpleDateFormat signTimeDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");

        String msgFormat = "%s %s-%s 已签到，签到时间为 : "+signTimeDateFormat.format(System.currentTimeMillis());//"今天（05月13日），18:00 托福-阅读 已签到，签到时间为：2016.05.13 17:50"

        String msgContent = String.format(msgFormat, dateFormat.format(lesson.getStartTime()), lesson.CourseTypeName, lesson.CourseSubTypeName);

        LocalMessage remindForClassMsg = new LocalMessage();
        remindForClassMsg.setContent(msgContent);
        remindForClassMsg.setHasRead(false);
        remindForClassMsg.setMessageID(String.valueOf(messageId));
        remindForClassMsg.setMessageTypeCode(Constants.MSG_TYPE_REMIND_SUCCESS_SIGN);
        remindForClassMsg.setMessageTypeName("签到成功");
        remindForClassMsg.setTitle("签到成功");
        remindForClassMsg.setSendTime(System.currentTimeMillis());
        remindForClassMsg.setReceiveTime(System.currentTimeMillis());
        MessageUtility.insertMessage(remindForClassMsg);

        showNotification("签到成功", msgContent, messageId);

        EventBus.getDefault().post(new NewNotifyEvent());
    }

    public void showNotification(String contentTitle, String contentText, int notifyId) {

        NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(getActivity(), MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        notificationIntent.putExtra(MainActivity.EXT_KEY_SHOW_PAGE,MainActivity.TAG_MESSAGEE);
        PendingIntent contentIntent = PendingIntent.getActivity(getActivity(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder= new Notification.Builder(getActivity())
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setSmallIcon(R.mipmap.logo)
                .setContentIntent(contentIntent);

        Notification notification;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            notification = builder.build();
        }else{
            notification=builder.getNotification();
        }

        notification.defaults=Notification.DEFAULT_VIBRATE|Notification.DEFAULT_SOUND;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(notifyId, notification);
    }
}
