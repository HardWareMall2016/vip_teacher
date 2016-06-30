package com.zhan.vip_teacher.ui.fragment.mine;

import android.animation.ObjectAnimator;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.zhan.vip_teacher.bean.MySignatureListBean;
import com.zhan.vip_teacher.bean.MySignatureListRequestBean;
import com.zhan.vip_teacher.bean.MySignatureRequestBean;
import com.zhan.vip_teacher.bean.MySignatureResponseBean;
import com.zhan.vip_teacher.bean.SearchContent;
import com.zhan.vip_teacher.bean.SignatureListBean;
import com.zhan.vip_teacher.bean.SignatureListBeans;
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
 * Created by Administrator on 2016/3/17.
 */
public class MySignatureFragment extends ASwipeRefreshListFragment<SignatureListBean, SignatureListBeans> implements View.OnClickListener {

    private Long date;
    private PopupWindow mPopupWindow;
    private View mChangLessonPopMenuContent;
    private List<String> list_type_name = new ArrayList<>();
    private List<String> list_type = new ArrayList<>();
    private boolean mFirst = true;

    private int type = 0;
    private String SignStatus = "781";//准时 781，警告 782，迟到 783，忘记签到 784

    private TextView mMouth;
    private TextView mSignInTotal;
    private TextView mForgetSignIn;
    private TextView mBeLate;
    private TextView mCaveat;
    private RelativeLayout mysignatureSignInTotal;
    private RelativeLayout mysignatureForgetSignIn;
    private RelativeLayout mysignatureBeLate;
    private RelativeLayout mysignatureCaveat;
    String startTime = null;
    String endTime = null;

    private int mRecordingBtnTranslationY;
    private int mRecordingBtnMarginBottom;

    //Handler MSG
    private static final int MSG_SHOW_RECORDING_BTN = 100;
    private static final int MSG_REFRESH_RECORDING_TIME = 101;
    private static final int MSG_FINISH_RECORDING = 102;

    private static final int SHOW_RECORDING_DELAY = 1000;
    private final int ANIM_DURATION = 300;

    @ViewInject(id = R.id.my_changesignature, click = "OnClick")
    RelativeLayout mLesson;
    @ViewInject(id = R.id.teach_lesson_changesignature)
    TextView mChangeSignature;
    @ViewInject(id = R.id.mysignature_picker_view)
    LoopView mPickView;

    //头部
    private View mHeader;
    private View ll;
    private int mHeaderPaddingTop;
    private ImageView mLeft;
    private ImageView mRight;


    public static MySignatureFragment newInstance(long date) {
        MySignatureFragment fragment = new MySignatureFragment();
        Bundle args = new Bundle();
        args.putLong("date", date);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        date = getArguments().getLong("date");
    }


    @Override
    protected int inflateContentView() {
        return R.layout.my_signature_frg_layout;
    }

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
    protected void layoutInit(LayoutInflater inflater, Bundle savedInstanceSate) {
        super.layoutInit(inflater, savedInstanceSate);
        intiPopMenu();
        mHeader = View.inflate(getActivity(), R.layout.mysignature_stick_header, null);//头部内容
        ll = mHeader.findViewById(R.id.my_signature_ll);

        ActionBarActivity activity = (ActionBarActivity) getActivity();
        mHeader.setPadding(0, activity.getActionbarHeight() + activity.getStatusBarHeight(), 0, 0);
        ((ListView) getRefreshView()).addHeaderView(mHeader);//添加头部
        mMouth = (TextView) mHeader.findViewById(R.id.mysignature_mouth);
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        Date datea = new Date(date);
        mMouth.setText(sdf.format(datea) + "月");

        mSignInTotal = (TextView) mHeader.findViewById(R.id.mysignature_SignInTotal);
        mForgetSignIn = (TextView) mHeader.findViewById(R.id.mysignature_ForgetSignIn);
        mBeLate = (TextView) mHeader.findViewById(R.id.mysignature_BeLate);
        mCaveat = (TextView) mHeader.findViewById(R.id.mysignature_Caveat);

        mLeft = (ImageView) mHeader.findViewById(R.id.mylesson_left);
        mRight = (ImageView) mHeader.findViewById(R.id.mylesson_right);

        mLeft.setOnClickListener(this);
        mRight.setOnClickListener(this);

        mysignatureSignInTotal = (RelativeLayout) mHeader.findViewById(R.id.rl_mysignature_SignInTotal);
        mysignatureForgetSignIn = (RelativeLayout) mHeader.findViewById(R.id.rl_mysignature_ForgetSignIn);
        mysignatureBeLate = (RelativeLayout) mHeader.findViewById(R.id.rl_mysignature_BeLate);
        mysignatureCaveat = (RelativeLayout) mHeader.findViewById(R.id.rl_mysignature_Caveat);

        mysignatureSignInTotal.setOnClickListener(this);
        mysignatureForgetSignIn.setOnClickListener(this);
        mysignatureBeLate.setOnClickListener(this);
        mysignatureCaveat.setOnClickListener(this);

        if (list_type_name != null && list_type_name.size() != 0) {
            if (type >= 0 && type < list_type_name.size()) {
                mChangeSignature.setText(list_type_name.get(type));
            }
        }

        hideView();

        if (ShareUtil.VALUE_TURN_ON.equals(ShareUtil.getStringValue(ShareUtil.MYSIGNATURE_PAGE, ShareUtil.VALUE_TURN_ON))) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setImageResource(R.drawable.mysignature_guide);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            final OperatorGuideManager operatorGuideManager = new OperatorGuideManager((ActionBarActivity) getActivity());
            operatorGuideManager.showGuideWindow(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    operatorGuideManager.hideGuideWindow();
                    ShareUtil.setValue(ShareUtil.MYSIGNATURE_PAGE, ShareUtil.VALUE_TURN_OFF);
                }
            });
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        if (firstVisibleItem >= 1) {
            //直接修改状态栏和actionbar颜色
            ActionBarActivity activity = (ActionBarActivity) getActivity();
            activity.setStatusbarBackgroundColor(getResources().getColor(R.color.action_bar_bg_color));
            activity.setActionbarBackgroundColor(getResources().getColor(R.color.action_bar_bg_color));
        } else {
            if (getRefreshView().getChildAt(0) != null && mHeader.getHeight() != 0) {
                int threshold = mHeader.getHeight() - mHeaderPaddingTop - ll.getHeight() - PixelUtils.dp2px(10);//间距
                int distance = getRefreshView().getChildAt(0).getTop();
                if (threshold + distance <= 0) {
                    ActionBarActivity activity = (ActionBarActivity) getActivity();
                    activity.setActionbarBackgroundColor(getResources().getColor(R.color.action_bar_bg_color));
                    activity.setStatusbarBackgroundColor(getResources().getColor(R.color.action_bar_bg_color));
                } else {
                    ActionBarActivity activity = (ActionBarActivity) getActivity();
                    activity.setActionbarBackgroundColor(getResources().getColor(R.color.transparent));
                    activity.setStatusbarBackgroundColor(getResources().getColor(R.color.transparent));
                }
            }
        }
    }


    //listview滑动时隐藏mLesson，滑动结束时显示mLesson
    private void hideView() {
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
        lp.alpha = bgAlpha; // 0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }

    void OnClick(View view) {
        switch (view.getId()) {
            case R.id.my_changesignature:
                showChooseMenu();
                break;
        }
    }

    private void showChooseMenu() {
        mChangLessonPopMenuContent = getActivity().getLayoutInflater().inflate(R.layout.pop_memu_mysignature, null);
        mPopupWindow.setContentView(mChangLessonPopMenuContent);
        View btnCancel = mChangLessonPopMenuContent.findViewById(R.id.mysignature_exam_time_cancel_time);
        btnCancel.setOnClickListener(mOnExamTimeClickListener);
        View btnFinish = mChangLessonPopMenuContent.findViewById(R.id.mysignature_exam_time_finish_time);
        btnFinish.setOnClickListener(mOnExamTimeClickListener);
        mPickView = (LoopView) mChangLessonPopMenuContent.findViewById(R.id.mysignature_picker_view);
        mPickView.setNotLoop();
        mPickView.setArrayList((ArrayList) list_type_name);
        mPickView.setInitPosition(0);

        showPopMenu();
    }

    private View.OnClickListener mOnExamTimeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.mysignature_exam_time_cancel_time:
                    closePopWin();
                    break;
                case R.id.mysignature_exam_time_finish_time:
                    type = mPickView.getSelectedItem();
                    if (type >= 0 && type < list_type_name.size()) {
                        mChangeSignature.setText(list_type_name.get(type));
                        closePopWin();
                        SignStatus = list_type.get(type);
                        queryList(RefreshMode.reset);
                    }
                    break;
            }
        }
    };

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
    protected ABaseAdapter.AbstractItemView<SignatureListBean> newItemView() {
        return new SampleItemView();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position > 0 && ((int) id > -1)) {
            final int CourseId = getAdapterItems().get((int) id).getCourseId();
            final String StudentGuid = getAdapterItems().get((int) id).getStudentGuid();
            SearchContent content = new SearchContent();
            content.setCourseId(CourseId);
            content.setGuid(StudentGuid);
            StudentInfoFragment.launch(getActivity(), content);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_mysignature_SignInTotal:
                mChangeSignature.setText("应签到");
                SignStatus = "";
                queryList(RefreshMode.reset);
                break;
            case R.id.rl_mysignature_ForgetSignIn:
                type = 3;
                if (type >= 0 && type < list_type_name.size()) {
                    mChangeSignature.setText(list_type_name.get(type));
                    SignStatus = list_type.get(type);
                    queryList(RefreshMode.reset);
                }
                break;
            case R.id.rl_mysignature_BeLate:
                type = 2;
                if (type >= 0 && type < list_type_name.size()) {
                    mChangeSignature.setText(list_type_name.get(type));
                    SignStatus = list_type.get(type);
                    queryList(RefreshMode.reset);
                }
                break;
            case R.id.rl_mysignature_Caveat:
                type = 1;
                if (type >= 0 && type < list_type_name.size()) {
                    mChangeSignature.setText(list_type_name.get(type));
                    SignStatus = list_type.get(type);
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

    class SampleItemView extends ABaseAdapter.AbstractItemView<SignatureListBean> {
        @ViewInject(id = R.id.my_signature_time)
        TextView mSignatureTime;
        @ViewInject(id = R.id.my_signature_course_time)
        TextView mTime;
        @ViewInject(id = R.id.my_signature_course_type)
        TextView courseType;
        @ViewInject(id = R.id.my_signature_status)
        TextView lessonStatus;
        @ViewInject(id = R.id.my_signature_student)
        TextView student;
        @ViewInject(id = R.id.my_signature_icon)
        ImageView icon;

        @Override
        public int inflateViewId() {
            return R.layout.my_signature_item_layout;
        }

        @Override
        public void bindingData(View convertView, SignatureListBean data) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String time = simpleDateFormat.format(Tools.parseServerTime(data.getLessonStartTime()));
            mSignatureTime.setText(time);
            SimpleDateFormat mLessonTimeFormat = new SimpleDateFormat("HH:mm");
            String startTime = mLessonTimeFormat.format(Tools.parseServerTime(data.getLessonStartTime()));
            String endTime = mLessonTimeFormat.format(Tools.parseServerTime(data.getLessonEndTime()));
            mTime.setText(startTime + "-" + endTime);

            courseType.setText(data.getCourseType() + data.getCourseSubType());
            lessonStatus.setText(getString(R.string.title_sign_status) + data.getSignInStatus());
            student.setText(getString(R.string.title_student) + data.getStudentName());
            icon.setImageResource(Tools.getCourseTypeIconBySubTypeName(data.getCourseSubType()));
        }
    }

    //配置刷新相关
    @Override
    protected void configRefresh(RefreshConfig config) {
        config.minResultSize = 10;//配置每页大小
    }

    @Override
    protected IPaging<SignatureListBean, SignatureListBeans> configPaging() {
        return new PageIndexPaging<SignatureListBean, SignatureListBeans>();
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


        if (mode == RefreshMode.reset || mode == RefreshMode.refresh) {
            firstQuest();
        } else {
            queryList(mode);
        }
    }

    private void firstQuest() {
        MySignatureRequestBean request = new MySignatureRequestBean();
        MySignatureRequestBean.DataEntity dataEntity = new MySignatureRequestBean.DataEntity();
        dataEntity.setStartTime(startTime);
        dataEntity.setEndTime(endTime);
        dataEntity.setTeacherId(UserInfo.getCurrentUser().getUserID());
        request.setData(dataEntity);
        startRequest(ApiUrls.GET_TEACHER_SIGNIN_TOTAL, request, new HttpRequestHandler() {

            @Override
            public void onRequestSucceeded(String content) {
                super.onRequestSucceeded(content);
                MySignatureResponseBean responBean = Tools.parseJsonTostError(content, MySignatureResponseBean.class);
                if (responBean != null) {
                    mSignInTotal.setText(responBean.getData().getSignInTotal() + "");
                    mForgetSignIn.setText(responBean.getData().getForgetSignIn() + "");
                    mBeLate.setText(responBean.getData().getBeLate() + "");
                    mCaveat.setText(responBean.getData().getCaveat() + "");
                    List<MySignatureResponseBean.DataEntity.SignInTypeModelListEntity> typeList = responBean.getData().getSignInTypeModelList();
                    if (typeList != null) {
                        list_type.clear();
                        list_type_name.clear();

                        for (MySignatureResponseBean.DataEntity.SignInTypeModelListEntity item : typeList) {
                            list_type_name.add(item.getSignInTypeName());
                        }
                        for (MySignatureResponseBean.DataEntity.SignInTypeModelListEntity item : typeList) {
                            list_type.add(item.getSignInType() + "");
                        }
                        queryList(RefreshMode.reset);
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

    //分页从0开始
    private void queryList(RefreshMode mode) {
        int itemCount = getAdapterItems() == null ? 0 : getAdapterItems().size();
        int pageIndex = itemCount / getRefreshConfig().minResultSize;

        if (mode == RefreshMode.refresh || mode == RefreshMode.reset) {
            pageIndex = 0;
        }

        MySignatureListRequestBean listRequest = new MySignatureListRequestBean();
        MySignatureListRequestBean.DataEntity data = new MySignatureListRequestBean.DataEntity();
        data.setStartTime(startTime);
        data.setEndTime(endTime);
        data.setTeacherId(UserInfo.getCurrentUser().getUserID());
        data.setPagedIndex(pageIndex);
        data.setPagedSize(getRefreshConfig().minResultSize);
        data.setSignStatus(SignStatus);
        listRequest.setData(data);
        startRequest(ApiUrls.GET_TEACHER_SIGNIN_LIST, listRequest, new PagingTask<MySignatureListBean>(mode) {

            @Override
            public MySignatureListBean parseResponseToResult(String content) {
                return Tools.parseJson(content, MySignatureListBean.class);
            }

            public String verifyResponseResult(MySignatureListBean result) {
                return Tools.verifyResponseResult(result);
            }

            @Override
            protected List<SignatureListBean> parseResult(MySignatureListBean mySignatureListBean) {
                List<SignatureListBean> signatureRstList = new ArrayList<>();
                for (int i = 0; i < mySignatureListBean.getData().size(); i++) {
                    MySignatureListBean.DataEntity item = mySignatureListBean.getData().get(i);
                    SignatureListBean signatureListBean = new SignatureListBean();
                    signatureListBean.setTeacherId(item.getTeacherId());
                    signatureListBean.setLessonId(item.getLessonId());
                    signatureListBean.setSignInStatus(item.getSignInStatus());
                    signatureListBean.setTeacherNo(item.getTeacherNo());
                    signatureListBean.setTeacherName(item.getTeacherName());
                    signatureListBean.setTeacherEName(item.getTeacherEName());
                    signatureListBean.setCourseType(item.getCourseType());
                    signatureListBean.setCourseSubType(item.getCourseSubType());
                    signatureListBean.setStudentName(item.getStudentName());
                    signatureListBean.setStudentGuid(item.getStudentGuid());
                    signatureListBean.setConsultantName(item.getConsultantName());
                    signatureListBean.setConsultantId(item.getConsultantId());
                    signatureListBean.setSignTime(item.getSignTime());
                    signatureListBean.setLessonStartTime(item.getLessonStartTime());
                    signatureListBean.setLessonEndTime(item.getLessonEndTime());
                    signatureListBean.setCourseId(item.getCourseId());
                    signatureRstList.add(signatureListBean);
                }
                return signatureRstList;
            }
        });

    }

}
