package com.zhan.vip_teacher.ui.fragment.mine;


import android.animation.ObjectAnimator;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhan.framework.network.HttpRequestHandler;
import com.zhan.framework.support.adapter.ABaseAdapter;
import com.zhan.framework.support.inject.ViewInject;
import com.zhan.framework.support.paging.IPaging;
import com.zhan.framework.support.paging.PageIndexPaging;
import com.zhan.framework.ui.activity.ActionBarActivity;
import com.zhan.framework.ui.fragment.ASwipeRefreshListFragment;
import com.zhan.framework.utils.PixelUtils;
import com.zhan.framework.utils.ToastUtils;
import com.zhan.framework.view.pickerview.LoopView;
import com.zhan.vip_teacher.R;
import com.zhan.vip_teacher.bean.FirstEvent;
import com.zhan.vip_teacher.bean.LessonListBean;
import com.zhan.vip_teacher.bean.LessonListBeans;
import com.zhan.vip_teacher.bean.MyLessonBean;
import com.zhan.vip_teacher.bean.MyLessonListBean;
import com.zhan.vip_teacher.bean.MyLessonListRequestBean;
import com.zhan.vip_teacher.bean.MyLessonRequestBean;
import com.zhan.vip_teacher.bean.SearchContent;
import com.zhan.vip_teacher.bean.UserInfo;
import com.zhan.vip_teacher.network.ApiUrls;
import com.zhan.vip_teacher.ui.fragment.student.StudentInfoFragment;
import com.zhan.vip_teacher.utils.OperatorGuideManager;
import com.zhan.vip_teacher.utils.ShareUtil;
import com.zhan.vip_teacher.utils.Tools;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/3/14.
 */
public class MyLessonFragment extends ASwipeRefreshListFragment<LessonListBean, LessonListBeans> implements View.OnClickListener {

    private TextView mAleadyCount;
    private TextView mConfirmCount;
    private TextView mNoCount;
    private TextView mMouth;
    private RelativeLayout mLessonConfirmCount ;
    private RelativeLayout mLessonNoCount ;
    private RelativeLayout mLessonAleadyCount;
    private PopupWindow mPopupWindow;
    private View mChangLessonPopMenuContent;
    private List<String> list_name = new ArrayList<>();
    private List<Integer> list_id = new ArrayList<>();

    String startTime = null;
    String endTime = null;
    private Long date;
    private int index = 1;
    private int type = 1;//0为未上课，1为已上课，-1已排课
    private int RequestType = 1;
    private boolean mFirst = true;

    private int mRecordingBtnTranslationY;
    private int mRecordingBtnMarginBottom;

    //Handler MSG
    private static final int MSG_SHOW_RECORDING_BTN = 100;
    private static final int MSG_REFRESH_RECORDING_TIME = 101;
    private static final int MSG_FINISH_RECORDING = 102;

    private static final int SHOW_RECORDING_DELAY = 1000;
    private final int ANIM_DURATION = 300;

    @ViewInject(id = R.id.mylesson_picker_view)
    LoopView mPickView;
    @ViewInject(id = R.id.teach_lesson, click = "OnClick")
    RelativeLayout mLesson;
    @ViewInject(id = R.id.teach_lesson_changeLesson)
    TextView mChangeLesson;


    //头部
    private View mHeader;
    private View ll;
    private int mHeaderPaddingTop;
    private ImageView mLeft;
    private ImageView mRight ;

    public static MyLessonFragment newInstance(Long date) {
        MyLessonFragment fragment = new MyLessonFragment();
        Bundle args = new Bundle();
        args.putLong("date", date);
        fragment.setArguments(args);
        return fragment;
    }

    //重载父类的方法 ，无论什么情况下 contentLayout都让它显示
    @Override
    protected void taskStateChanged(ABaseTaskState state, Serializable tag) {
        super.taskStateChanged(state, tag);
        setViewVisiable(contentLayout, View.VISIBLE);
    }

    @Override
    public boolean isContentEmpty() {
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        date = getArguments().getLong("date");
    }

    @Override
    protected int inflateContentView() {
        return R.layout.my_lesson_frg_layout;
    }

    @Override
    protected void layoutInit(LayoutInflater inflater, Bundle savedInstanceSate) {
        super.layoutInit(inflater, savedInstanceSate);
        intiPopMenu();
        mHeader = View.inflate(getActivity(), R.layout.mylesson_stick_header, null);//头部内容
        ll= mHeader.findViewById(R.id.ll);
        ActionBarActivity activity= (ActionBarActivity)getActivity();
        mHeaderPaddingTop =activity.getActionbarHeight()+activity.getStatusBarHeight();
        mHeader.setPadding(0, mHeaderPaddingTop, 0, 0);
        ((ListView) getRefreshView()).addHeaderView(mHeader);//添加头部
        mMouth = (TextView) mHeader.findViewById(R.id.mylesson_mouth);
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        Date datea = new Date(date);
        mMouth.setText(sdf.format(datea) + "月");

        mAleadyCount = (TextView) mHeader.findViewById(R.id.mylesson_AleadyCount);
        mConfirmCount = (TextView) mHeader.findViewById(R.id.mylesson_ConfirmCount);
        mNoCount = (TextView) mHeader.findViewById(R.id.mylesson_NoCount);

        mLeft =(ImageView) mHeader.findViewById(R.id.mylesson_left);
        mRight =(ImageView) mHeader.findViewById(R.id.mylesson_right);

        mLeft.setOnClickListener(this);
        mRight.setOnClickListener(this);


        mLessonConfirmCount = (RelativeLayout) mHeader.findViewById(R.id.rl_mylesson_ConfirmCount);
        mLessonNoCount = (RelativeLayout) mHeader.findViewById(R.id.rl_mylesson_NoCount);
        mLessonAleadyCount = (RelativeLayout) mHeader.findViewById(R.id.rl_mylesson_AleadyCount);

        mLessonConfirmCount.setOnClickListener(this);
        mLessonNoCount.setOnClickListener(this);
        mLessonAleadyCount.setOnClickListener(this);

        if (list_name != null && list_name.size() != 0) {
            if (type >= 0 && type < list_name.size()) {
                mChangeLesson.setText(list_name.get(type));
            }
        }

        hideView();

        if(ShareUtil.VALUE_TURN_ON.equals(ShareUtil.getStringValue(ShareUtil.MYLESSON_PAGE, ShareUtil.VALUE_TURN_ON))){
            ImageView imageView = new ImageView(getActivity());
            imageView.setImageResource(R.drawable.mylesson_guide);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            final OperatorGuideManager operatorGuideManager=new OperatorGuideManager((ActionBarActivity) getActivity());
            operatorGuideManager.showGuideWindow(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    operatorGuideManager.hideGuideWindow();
                    ShareUtil.setValue(ShareUtil.MYLESSON_PAGE,ShareUtil.VALUE_TURN_OFF);
                }
            });
        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        if(firstVisibleItem>=1){
            //直接修改状态栏和actionbar颜色
            ActionBarActivity activity= (ActionBarActivity)getActivity();
            activity.setStatusbarBackgroundColor(getResources().getColor(R.color.action_bar_bg_color));
            activity.setActionbarBackgroundColor(getResources().getColor(R.color.action_bar_bg_color));
        }else{
            if(getRefreshView().getChildAt(0)!=null&& mHeader.getHeight()!=0){
                int threshold= mHeader.getHeight()- mHeaderPaddingTop -ll.getHeight()- PixelUtils.dp2px(10);//间距
                int distance= getRefreshView().getChildAt(0).getTop();
                if(threshold+distance<=0){
                    ActionBarActivity activity= (ActionBarActivity)getActivity();
                    activity.setActionbarBackgroundColor(getResources().getColor(R.color.action_bar_bg_color));
                    activity.setStatusbarBackgroundColor(getResources().getColor(R.color.action_bar_bg_color));
                }else{
                    ActionBarActivity activity= (ActionBarActivity)getActivity();
                    activity.setActionbarBackgroundColor(getResources().getColor(R.color.transparent));
                    activity.setStatusbarBackgroundColor(getResources().getColor(R.color.transparent));
                }
            }
        }
    }

    private void hideView(){
        ViewTreeObserver vto = mLesson.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mLesson.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mLesson.getLayoutParams();
                mRecordingBtnMarginBottom = lp.bottomMargin;
                mRecordingBtnTranslationY = mRecordingBtnMarginBottom + mLesson.getHeight();
            }
        });

        //listview滑动时隐藏mLesson，滑动结束时显示mLesson
        ((ListView) getRefreshView()).setOnTouchListener(mListViewOnTouchListener);
    }

    private View.OnTouchListener mListViewOnTouchListener = new View.OnTouchListener() {
        private float mDownY;
        private float distance;
        boolean mHideRecordingBtn = false;

        @Override
        public boolean onTouch(View view, MotionEvent event) {

            if (mLesson.getTranslationY() == mRecordingBtnTranslationY) {
                mRecordingHandler.removeMessages(MSG_SHOW_RECORDING_BTN);
                mRecordingHandler.sendEmptyMessageDelayed(MSG_SHOW_RECORDING_BTN, SHOW_RECORDING_DELAY + ANIM_DURATION);
                return false;
            }

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mHideRecordingBtn = false;
                    mDownY = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    distance = mDownY - event.getY();
                    //上滑隐藏
                    if (distance > 0) {
                        mLesson.setTranslationY(distance);
                        if (distance > mRecordingBtnMarginBottom / 2) {
                            mHideRecordingBtn = true;
                        } else {
                            mHideRecordingBtn = false;
                        }
                    }
                    break;
                default:
                    //上滑隐藏
                    if (mHideRecordingBtn) {
                        ObjectAnimator.ofFloat(mLesson, "TranslationY", distance, mRecordingBtnTranslationY).setDuration(ANIM_DURATION).start();
                        mRecordingHandler.removeMessages(MSG_SHOW_RECORDING_BTN);
                        mRecordingHandler.sendEmptyMessageDelayed(MSG_SHOW_RECORDING_BTN, SHOW_RECORDING_DELAY + ANIM_DURATION);
                    } else {
                        if (distance > 0) {
                            //滑动距离不够，返回
                            ObjectAnimator.ofFloat(mLesson, "TranslationY", distance, 0).setDuration(ANIM_DURATION).start();
                        } else {
                            mLesson.setTranslationY(0);
                        }
                    }
                    break;
            }
            return false;
        }
    };

    private Handler mRecordingHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SHOW_RECORDING_BTN:
                    ObjectAnimator.ofFloat(mLesson, "TranslationY", mRecordingBtnTranslationY, 0).setDuration(ANIM_DURATION).start();
                    break;
                case MSG_REFRESH_RECORDING_TIME:
                    break;
                case MSG_FINISH_RECORDING:
                    break;
            }
        }
    };

    void OnClick(View view) {
        switch (view.getId()) {
            case R.id.teach_lesson:
                showChooseMenu();
                break;
        }
    }

    private void showChooseMenu() {
        mChangLessonPopMenuContent = getActivity().getLayoutInflater().inflate(R.layout.pop_memu_mylesson, null);
        mPopupWindow.setContentView(mChangLessonPopMenuContent);
        View btnCancel = mChangLessonPopMenuContent.findViewById(R.id.mylesson_exam_time_cancel_time);
        btnCancel.setOnClickListener(mOnExamTimeClickListener);
        View btnFinish = mChangLessonPopMenuContent.findViewById(R.id.mylesson_exam_time_finish_time);
        btnFinish.setOnClickListener(mOnExamTimeClickListener);
        mPickView = (LoopView) mChangLessonPopMenuContent.findViewById(R.id.mylesson_picker_view);
        mPickView.setNotLoop();
        mPickView.setArrayList((ArrayList) list_name);
        mPickView.setInitPosition(0);

        showPopMenu();
    }

    private View.OnClickListener mOnExamTimeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.mylesson_exam_time_cancel_time:
                    closePopWin();
                    break;
                case R.id.mylesson_exam_time_finish_time:
                    type = mPickView.getSelectedItem();
                    if (type >= 0 && type < list_name.size()) {
                        RequestType = list_id.get(type);
                        mChangeLesson.setText(list_name.get(type));
                        closePopWin();
                        queryList(RefreshMode.reset);
                    }
                    break;
            }
        }
    };

    private void intiPopMenu() {
        mPopupWindow = new PopupWindow(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        int bgColor = getResources().getColor(com.zhan.framework.R.color.main_background);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(bgColor));
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setAnimationStyle(R.style.pop_menu_animation);
        mPopupWindow.update();
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
    }

    public boolean closePopWin() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
            return true;
        }
        return false;
    }

    private void showPopMenu() {
        if (mPopupWindow != null && !mPopupWindow.isShowing()) {
            mPopupWindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
            backgroundAlpha(0.7f);
            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    backgroundAlpha(1f);
                }
            });
        }
    }

    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getActivity().getWindow().setAttributes(lp);
    }


    @Override
    protected String loadDisabledLabel() {
        return getString(R.string.no_more_data);
    }

    @Override
    protected String loadingLabel() {
        return getString(R.string.download_ten_data);
    }

    @Override
    protected void setInitSwipeRefresh(ListView listView, SwipeRefreshLayout swipeRefreshLayout, Bundle savedInstanceState) {
        super.setInitSwipeRefresh(listView, swipeRefreshLayout, savedInstanceState);
    }

    @Override
    protected ABaseAdapter.AbstractItemView<LessonListBean> newItemView() {
        return new SampleItemView();
    }

    //配置刷新相关
    @Override
    protected void configRefresh(RefreshConfig config) {
        config.minResultSize = 10;//配置每页大小
    }

    @Override
    protected IPaging<LessonListBean, LessonListBeans> configPaging() {
        return new PageIndexPaging<LessonListBean, LessonListBeans>();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position > 0 && ((int)id > -1)){
            final int CourseId = getAdapterItems().get((int)id).getCourseId();
            final String StudentGuid = getAdapterItems().get((int)id).getStudentGuid();
            SearchContent content = new SearchContent();
            content.setCourseId(CourseId);
            content.setGuid(StudentGuid);
            StudentInfoFragment.launch(getActivity(), content);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_mylesson_ConfirmCount:
                type = 1;
                if (type >= 0 && type < list_name.size()) {
                    RequestType = list_id.get(type);
                    mChangeLesson.setText(list_name.get(type));
                    queryList(RefreshMode.reset);
                }
                break;
            case R.id.rl_mylesson_NoCount:
                type = 0;
                if (type >= 0 && type < list_name.size()) {
                    RequestType = list_id.get(type);
                    mChangeLesson.setText(list_name.get(type));
                    queryList(RefreshMode.reset);
                }
                break;
            case R.id.rl_mylesson_AleadyCount:
                type = 2;
                if (type >= 0 && type < list_name.size()) {
                    RequestType = list_id.get(type);
                    mChangeLesson.setText(list_name.get(type));
                    queryList(RefreshMode.reset);
                }
                break;
            case R.id.mylesson_left:
                EventBus.getDefault().post(new FirstEvent("left"));
                break;
            case R.id.mylesson_right:
                EventBus.getDefault().post(new FirstEvent("right"));
                break;
        }
    }

    class SampleItemView extends ABaseAdapter.AbstractItemView<LessonListBean> {
        @ViewInject(id = R.id.my_lesson_time)
        TextView mTime;
        @ViewInject(id = R.id.my_lesson_course_time)
        TextView coursetime;
        @ViewInject(id = R.id.my_lesson_course_type)
        TextView courseType;
        @ViewInject(id = R.id.my_lesson_status)
        TextView lessonStatus;
        @ViewInject(id = R.id.my_lesson_student)
        TextView student;
        @ViewInject(id = R.id.my_lesson_icon)
        ImageView icon;

        @Override
        public int inflateViewId() {
            return R.layout.my_lesson_item_layout;
        }

        @Override
        public void bindingData(View convertView, final LessonListBean data) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String time = simpleDateFormat.format(Tools.parseServerTime(data.getStartTime()));
            mTime.setText(time);
            SimpleDateFormat mLessonTimeFormat = new SimpleDateFormat("HH:mm");
            String startTime = mLessonTimeFormat.format(Tools.parseServerTime(data.getStartTime()));
            String endTime = mLessonTimeFormat.format(Tools.parseServerTime(data.getEndTime()));
            coursetime.setText(startTime + "-" + endTime);
            courseType.setText(data.getCourseTypeName() + data.getCourseSubTypeName());
            if(TextUtils.isEmpty(data.getLessonStatusName())){
                lessonStatus.setText(getString(R.string.lesson_status_name) + getString(R.string.lesson_status_no_lesson));
            }else{
                lessonStatus.setText(getString(R.string.lesson_status_name) + data.getLessonStatusName());
            }
            student.setText(getString(R.string.title_student) + data.getStudentName());
            icon.setImageResource(Tools.getCourseTypeIconBySubTypeName(data.getCourseSubTypeName()));

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void requestData(RefreshMode mode) {
        if (mFirst) {
            mFirst = false;
            return;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        startTime = dateFormat.format(calendar.getTime());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        endTime = dateFormat.format(calendar.getTime());

        if (mode == RefreshMode.reset) {
            firstQuest();
        } else {
            queryList(mode);
        }
    }

    private void firstQuest() {
        taskStateChanged(ABaseTaskState.prepare, null);
        MyLessonRequestBean request = new MyLessonRequestBean();
        MyLessonRequestBean.DataEntity data = new MyLessonRequestBean.DataEntity();
        data.setStartTime(startTime);
        data.setEndTime(endTime);
        data.setTeacherId(UserInfo.getCurrentUser().getUserID());
        request.setData(data);
        startRequest(ApiUrls.GET_LESSONSTATISTIC_COUNTBYDATA, request, new HttpRequestHandler() {

            @Override
            public void onRequestSucceeded(String content) {
                super.onRequestSucceeded(content);
                MyLessonBean responseBean = Tools.parseJsonTostError(content, MyLessonBean.class);
                if (responseBean != null) {
                    mAleadyCount.setText(responseBean.getData().getConfirmCount() + "");
                    mConfirmCount.setText(responseBean.getData().getAleadyCount() + "");
                    mNoCount.setText(responseBean.getData().getNoCount() + "");
                    List<MyLessonBean.DataEntity.ListCourseStateTypeEntity> typeList = responseBean.getData().getListCourseStateType();
                    if (typeList != null) {
                        list_name.clear();
                        list_id.clear();
                        for (MyLessonBean.DataEntity.ListCourseStateTypeEntity item : typeList) {
                            list_name.add(item.getName());
                        }
                        for (MyLessonBean.DataEntity.ListCourseStateTypeEntity item : typeList) {
                            list_id.add(item.getId());
                        }
                        queryList(RefreshMode.refresh);
                    }
                }
            }

            @Override
            public void onRequestFinished(ResultCode resultCode, String result) {
                super.onRequestFinished(resultCode, result);
                switch (resultCode) {
                    case success:
                        break;
                    case canceled:
                        onRefreshViewComplete();
                        break;
                    default:
                        taskStateChanged(ABaseTaskState.falid, result);
                        taskStateChanged(ABaseTaskState.finished, null);
                        ToastUtils.toast(result);
                        break;
                }
            }
        });
    }

    //分页从1开始
    private void queryList(RefreshMode mode) {

        int itemCount = getAdapterItems() == null ? 1 : getAdapterItems().size();
        int index = itemCount / getRefreshConfig().minResultSize + 1;

        if (mode == RefreshMode.refresh || mode == RefreshMode.reset) {
            index = 1;
        }

        MyLessonListRequestBean listRequest = new MyLessonListRequestBean();
        MyLessonListRequestBean.DataEntity listData = new MyLessonListRequestBean.DataEntity();
        listData.setTeacherId(UserInfo.getCurrentUser().getUserID());
        listData.setStartTime(startTime);
        listData.setEndTime(endTime);
        listData.setRequestType(RequestType);
        listData.setIndex(index);
        listRequest.setData(listData);
        startRequest(ApiUrls.GET_LESSONSTATISTICSBYDATA, listRequest, new PagingTask<MyLessonListBean>(mode) {

            @Override
            public MyLessonListBean parseResponseToResult(String content) {
                return Tools.parseJson(content, MyLessonListBean.class);
            }

            public String verifyResponseResult(MyLessonListBean result) {
                return Tools.verifyResponseResult(result);
            }

            @Override
            protected List<LessonListBean> parseResult(MyLessonListBean myLessonListBean) {
                List<LessonListBean> lessonRstList = new ArrayList<>();
                for (int i = 0; i < myLessonListBean.getData().getListLessonInfo().size(); i++) {
                    MyLessonListBean.DataEntity.ListLessonInfoEntity item = myLessonListBean.getData().getListLessonInfo().get(i);
                    LessonListBean lessonListBean = new LessonListBean();
                    lessonListBean.setStartTime(item.getStartTime());
                    lessonListBean.setEndTime(item.getEndTime());
                    lessonListBean.setLessonStatus(item.getLessonStatus());
                    lessonListBean.setLessonStatusName(item.getLessonStatusName());
                    lessonListBean.setStudentName(item.getStudentName());
                    lessonListBean.setCourseType(item.getCourseType());
                    lessonListBean.setCourseTypeName(item.getCourseTypeName());
                    lessonListBean.setCourseSubType(item.getCourseSubType());
                    lessonListBean.setCourseSubTypeName(item.getCourseSubTypeName());
                    lessonListBean.setStudentGuid(item.getStudentGuid());
                    lessonListBean.setCourseId(item.getCourseId());
                    lessonRstList.add(lessonListBean);
                }
                return lessonRstList;
            }
        });
    }

}
