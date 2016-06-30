package com.zhan.vip_teacher.ui.fragment.course;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhan.framework.component.container.FragmentArgs;
import com.zhan.framework.component.container.FragmentContainerActivity;
import com.zhan.framework.network.HttpRequestHandler;
import com.zhan.framework.support.inject.ViewInject;
import com.zhan.framework.ui.fragment.ABaseFragment;
import com.zhan.framework.utils.ToastUtils;
import com.zhan.framework.view.pickerview.LoopView;
import com.zhan.vip_teacher.R;
import com.zhan.vip_teacher.base.Constants;
import com.zhan.vip_teacher.bean.CommDictionary;
import com.zhan.vip_teacher.bean.GetFeedbackRequest;
import com.zhan.vip_teacher.bean.GetFeedbackResponse;
import com.zhan.vip_teacher.bean.TeacherClassFeedbackRequestBean;
import com.zhan.vip_teacher.bean.TeacherClassFeedbackResponseBean;
import com.zhan.vip_teacher.bean.UserInfo;
import com.zhan.vip_teacher.network.ApiUrls;
import com.zhan.vip_teacher.utils.SendEventUtil;
import com.zhan.vip_teacher.utils.Tools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by WuYue on 2016/3/8.
 */
public class ClassFeedbackFragment extends ABaseFragment {
    private final static String ARG_KEY = "lessonId";
    private long mLessonId;

    //学生信息
    @ViewInject(id = R.id.main_time)
    private TextView mViewTime;
    @ViewInject(id = R.id.sub_time)
    private TextView mViewSubTime;
    //课程
    @ViewInject(id = R.id.course_type)
    private TextView mViewCourseType;
    //学生
    @ViewInject(id = R.id.student)
    private TextView mViewStudent;
    //学生QQ
    @ViewInject(id = R.id.student_qq)
    private TextView mViewStudentQQ;
    //班主任
    @ViewInject(id = R.id.class_teacher)
    private TextView mViewClassTeacher;
    //总课时数
    @ViewInject(id = R.id.course_number)
    private TextView mViewCourseNumber;
    //保分目标
    @ViewInject(id = R.id.target)
    private TextView mViewTarget;

    @ViewInject(id = R.id.lesson_status_content, click = "OnClick")
    private View mViewLessStatusLine;

    @ViewInject(id = R.id.task_status_content, click = "OnClick")
    private View mViewTaskStatusLine;

    //上课状态
    @ViewInject(id = R.id.lesson_status)
    private TextView mViewLessonStatus;
    //作业打分
    @ViewInject(id = R.id.task_status)
    private TextView mViewHomeworkScore;
    //作业评语
    @ViewInject(id = R.id.HomeworkComments)
    private TextView mViewHomeworkComments;
    //上堂课后作业
    @ViewInject(id = R.id.LastHomework)
    private TextView mViewLastHomework;
    //课程内容
    @ViewInject(id = R.id.CourseContent)
    private TextView mViewCourseContent;
    //本堂课后作业
    @ViewInject(id = R.id.Homework)
    private TextView mViewHomework;
    //学习反馈信息及进度小结
    @ViewInject(id = R.id.StudentFeedback)
    private TextView mViewStudentFeedback;

    @ViewInject(id = R.id.submit, click = "OnClick")
    private View mViewSubmit;

    //上课状态
    private String mLessonStatusCode;
    //作业打分
    private String mHomeworkScoreCode;

    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat mLessonTimeFormat = new SimpleDateFormat("HH:mm");

    //底部选择框内容
    private enum WheelMenuStatus{CourseStatus,HomeworkScore}//上课状态，作业打分

    private WheelMenuStatus mBottomPopMenuStatus=WheelMenuStatus.CourseStatus;

    private CommDictionary mCommDictionary;

    private LoopView mPickView;
    private View mPopMenuContent;
    private PopupWindow mPopupWindow;
    private List<String> mWheelValues;

    public static void launch(Activity from, long lessonId) {
        FragmentArgs args = new FragmentArgs();
        args.add(ARG_KEY, lessonId);
        FragmentContainerActivity.launch(from, ClassFeedbackFragment.class, args);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLessonId = savedInstanceState == null ? (long) getArguments().getSerializable(ARG_KEY)
                : (long) savedInstanceState.getSerializable(ARG_KEY);
    }

    @Override
    protected int inflateContentView() {
        return R.layout.calss_feedback_layout;
    }

    @Override
    protected void layoutInit(LayoutInflater inflater, Bundle savedInstanceSate) {
        super.layoutInit(inflater, savedInstanceSate);

        getActivity().setTitle(R.string.title_feedback);

        intiPopMenu();

        CommDictionary.getCommDictionary(new CommDictionary.GetCallback() {
            @Override
            public void onGetResult(CommDictionary data) {
                mCommDictionary = data;
                //初始化上课状态和作业打分
                if (mCommDictionary != null) {
                    if (mCommDictionary.getCourseStatus() != null && mCommDictionary.getCourseStatus().size() > 0) {
                        mViewLessonStatus.setText(mCommDictionary.getCourseStatus().get(0).getName());
                        mLessonStatusCode = mCommDictionary.getCourseStatus().get(0).getId();
                    }
                }
            }
        });

        populateView(null);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ARG_KEY, mLessonId);
    }

    @Override
    public void requestData() {
        GetFeedbackRequest requestBean=new GetFeedbackRequest();
        requestBean.setData((int) mLessonId);

        startRequest(ApiUrls.GET_FEEDBACK, requestBean, new BaseHttpRequestTask<GetFeedbackResponse>() {

            @Override
            public GetFeedbackResponse parseResponseToResult(String content) {
                return Tools.parseJson(content, GetFeedbackResponse.class);
            }

            @Override
            public String verifyResponseResult(GetFeedbackResponse result) {
                String error=Tools.verifyResponseResult(result);
                if(TextUtils.isEmpty(error)){
                    //验证是否可以反馈
                    if(result==null||result.getData()==null){
                        return null;
                    }
                    GetFeedbackResponse.DataEntity.TeacherStudentAreaInfoEntity student=result.getData().getTeacherStudentAreaInfo();
                    if(student!=null){
                        long sendTime=Tools.parseServerTime(student.getEndTime());
                        long serviceTime=Tools.parseServerTime(result.getServerTime());
                        //课程开始后才能反馈
                        if(serviceTime<sendTime){
                            error=getString(R.string.feedback_too_early);
                        }
                    }
                }
                return error;
            }

            @Override
            protected boolean resultIsEmpty(GetFeedbackResponse result) {
                boolean isEmpty = true;
                if (result != null && result.getData() != null) {
                    isEmpty = false;
                }
                return isEmpty;
            }

            @Override
            protected void onSuccess(GetFeedbackResponse teacherInfoResponseBean) {
                super.onSuccess(teacherInfoResponseBean);
                populateView(teacherInfoResponseBean);
            }
        });
    }

    private void populateView(GetFeedbackResponse responseBean){
        if(responseBean==null||responseBean.getData()==null){
            return;
        }
        GetFeedbackResponse.DataEntity.TeacherStudentAreaInfoEntity student=responseBean.getData().getTeacherStudentAreaInfo();

        if(student!=null){
            String courseDate=mDateFormat.format(Tools.parseServerTime(student.getStartTime()));
            String courseStartTime=mLessonTimeFormat.format(Tools.parseServerTime(student.getStartTime()));
            String courseEndTime=mLessonTimeFormat.format(Tools.parseServerTime(student.getEndTime()));
            mViewTime.setText(courseDate);
            mViewSubTime.setText(String.format("%s-%s", courseStartTime, courseEndTime));

            int colorContent=getResources().getColor(R.color.text_color_content);
            //课程
            mViewCourseType.setText(R.string.feedback_course_type);
            if(!TextUtils.isEmpty(student.getCourseType_toName())){
                SpannableString ss = new SpannableString(student.getCourseType_toName());
                ss.setSpan(new ForegroundColorSpan(colorContent), 0, student.getCourseType_toName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                mViewCourseType.append(ss);
            }
            if(!TextUtils.isEmpty(student.getCourseSubType_toName())){
                SpannableString ss = new SpannableString(student.getCourseSubType_toName());
                ss.setSpan(new ForegroundColorSpan(colorContent), 0, student.getCourseSubType_toName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                mViewCourseType.append(ss);
            }


            //学生
            mViewStudent.setText(R.string.feedback_student);
            if(!TextUtils.isEmpty(student.getCName())){
                SpannableString ss = new SpannableString(student.getCName());
                ss.setSpan(new ForegroundColorSpan(colorContent), 0, student.getCName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                mViewStudent.append(ss);
            }

            //学生QQ
            mViewStudentQQ.setText(R.string.feedback_qq);
            if(!TextUtils.isEmpty(student.getQQ())){
                SpannableString ss = new SpannableString(student.getQQ());
                ss.setSpan(new ForegroundColorSpan(colorContent), 0, student.getQQ().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                mViewStudentQQ.append(ss);
            }

            //班主任
            mViewClassTeacher.setText(R.string.feedback_class_teacher);
            if(!TextUtils.isEmpty(student.getTrueName())){
                SpannableString ss = new SpannableString(student.getTrueName());
                ss.setSpan(new ForegroundColorSpan(colorContent), 0, student.getTrueName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                mViewClassTeacher.append(ss);
            }

            //总课时数
            mViewCourseNumber.setText(R.string.feedback_course_number);
            String specialCount=String.valueOf(student.getSpecialCount());
            if(!TextUtils.isEmpty(specialCount)){
                SpannableString ss = new SpannableString(specialCount);
                ss.setSpan(new ForegroundColorSpan(colorContent), 0, specialCount.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                mViewCourseNumber.append(ss);
            }

            //保分目标
            mViewTarget.setText(R.string.feedback_target);
            if(!TextUtils.isEmpty(student.getGuaranteeScore())){
                SpannableString ss = new SpannableString(student.getGuaranteeScore());
                ss.setSpan(new ForegroundColorSpan(colorContent), 0, student.getGuaranteeScore().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                mViewTarget.append(ss);
            }

            mViewLastHomework.setText(student.getLastHomework());
        }


        GetFeedbackResponse.DataEntity.TeacherClassFeedbackInfoEntity feedbackInfo=responseBean.getData().getTeacherClassFeedbackInfo();
        if(feedbackInfo==null){
            mViewLessonStatus.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.arrow_down,0);
        }else{
            mLessonStatusCode=feedbackInfo.getStudentCourseState();
            mHomeworkScoreCode=feedbackInfo.getOperatingCondition();
            //上课状态 更新时 不可编辑
            mViewLessStatusLine.setClickable(false);
            mViewLessonStatus.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
            mViewHomeworkComments.setText(feedbackInfo.getHomeworkComments());
            mViewCourseContent.setText(feedbackInfo.getCourseContent());
            mViewHomework.setText(feedbackInfo.getHomework());
            mViewStudentFeedback.setText(feedbackInfo.getStudentFeedback());
        }
        //上课状态，作业打分从字典中获取
        mViewLessonStatus.setText(getLessonStatusByCode(mLessonStatusCode));
        mViewHomeworkScore.setText(getHomeworkScoreByCode(mHomeworkScoreCode));
    }

    void OnClick(View v) {
        switch (v.getId()) {
            case R.id.lesson_status_content:
                mBottomPopMenuStatus=WheelMenuStatus.CourseStatus;
                showChooseMenu();
                break;
            case R.id.task_status_content:
                mBottomPopMenuStatus=WheelMenuStatus.HomeworkScore;
                showChooseMenu();
                break;
            case R.id.submit:
                if(isRequestProcessing(ApiUrls.TEACHER_CLASS_FEEDBACK)){
                    return;
                }
                if(verifySubmitData()){
                    TeacherClassFeedbackRequestBean requestBean=new TeacherClassFeedbackRequestBean();
                    TeacherClassFeedbackRequestBean.DataEntity data=new TeacherClassFeedbackRequestBean.DataEntity();
                    requestBean.setData(data);
                    data.setLessonId((int) mLessonId);
                    data.setAttendClassStatus(mLessonStatusCode);
                    data.setTaskStatus(mHomeworkScoreCode);
                    data.setTaskComment(mViewHomeworkComments.getText().toString());
                    data.setLastTask(mViewLastHomework.getText().toString());
                    data.setClassContent(mViewCourseContent.getText().toString());
                    data.setTask(mViewHomework.getText().toString());
                    data.setSummary(mViewStudentFeedback.getText().toString());
                    data.setCreater(UserInfo.getCurrentUser().getUserID());
                    data.setCreateTime(Tools.formatToServerTimeStr(System.currentTimeMillis()));
                    data.setTeacherUpdateTime(Tools.formatToServerTimeStr(System.currentTimeMillis()));
                    showRotateProgressDialog(getString(R.string.feedback_submitting),true);
                    startRequest(ApiUrls.TEACHER_CLASS_FEEDBACK, requestBean, new HttpRequestHandler(){
                        @Override
                        public void onRequestFinished(ResultCode resultCode, String result) {
                            super.onRequestFinished(resultCode, result);
                            SendEventUtil.sendEvent(6,"提交课后反馈",result);
                            switch (resultCode){
                                case success:
                                    if(Tools.parseJsonTostError(result,TeacherClassFeedbackResponseBean.class)!=null){
                                        ToastUtils.toast(R.string.feedback_submitting_success);
                                        //mViewLessStatusLine.setEnabled(false);
                                        mViewLessStatusLine.setClickable(false);
                                        mViewLessonStatus.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                    }
                                    break;
                                default:
                                    ToastUtils.toast(result);
                                    break;
                            }
                            closeRotateProgressDialog();
                        }
                    });
                }
                break;
        }
    }

    /***
     * 根据Code返回上课状态
     * @param code
     * @return
     */
    private String getLessonStatusByCode(String code){
        String lessonStatus=null;
        if(TextUtils.isEmpty(code)){
            return null;
        }
        if(mCommDictionary!=null&&mCommDictionary.getCourseStatus()!=null&&mCommDictionary.getCourseStatus().size()>0){
            for (CommDictionary.CourseStatusEntity status:mCommDictionary.getCourseStatus()){
                if(code.equals(status.getId())){
                    lessonStatus=status.getName();
                    break;
                }
            }
        }
        return lessonStatus;
    }

    /***
     * 根据Code返回作业打分
     * @param code
     * @return
     */
    private String getHomeworkScoreByCode(String code){
        String homeworkScore=null;
        if(TextUtils.isEmpty(code)){
            return getString(R.string.please_select_homework_score);
        }
        if(mCommDictionary!=null&&mCommDictionary.getHomeworkScore()!=null&&mCommDictionary.getHomeworkScore().size()>0){
            for (CommDictionary.HomeworkScoreEntity score:mCommDictionary.getHomeworkScore()){
                if(code.equals(score.getId())){
                    homeworkScore=score.getName();
                    break;
                }
            }
        }
        return homeworkScore;
    }

    private View.OnClickListener mOnPopMenuBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.cancel:
                    closePopWin();
                    break;
                case R.id.finish:
                    int position=mPickView.getSelectedItem();
                    if(position>-1){
                      String name= mWheelValues.get(position);
                        if(mBottomPopMenuStatus==WheelMenuStatus.CourseStatus){
                            mViewLessonStatus.setText(name);
                            mLessonStatusCode=mCommDictionary.getCourseStatus().get(position).getId();
                        }
                        else if(mBottomPopMenuStatus==WheelMenuStatus.HomeworkScore){
                            if(position==0){
                                //第一项是请选择
                                mHomeworkScoreCode=null;
                                mViewHomeworkScore.setText(R.string.please_select_homework_score);
                            }else {
                                mViewHomeworkScore.setText(name);
                                mHomeworkScoreCode=mCommDictionary.getHomeworkScore().get(position-1).getId();
                            }
                        }
                    }
                    closePopWin();
                    break;
            }
        }
    };

    private boolean verifySubmitData(){
        //上课状态不能为空
        String courseStatus=mViewLessonStatus.getText().toString();
        if(TextUtils.isEmpty(courseStatus)){
            ToastUtils.toast(R.string.course_status_empty);
            return false;
        }
        //1.正常上课、学生迟到、提前下课中的一种，课程内容/本堂课后作业/学习反馈信息>10
        if(Constants.COURSE_STATUS_IN_TIME.equals(mLessonStatusCode)
                ||Constants.COURSE_STATUS_STUDENT_LATE.equals(mLessonStatusCode)
                ||Constants.COURSE_STATUS_EARLY.equals(mLessonStatusCode)){
            if(mViewCourseContent.getText().toString().length()<=10){
                ToastUtils.toast(R.string.course_content_too_short);
                return false;
            }
            if(mViewHomework.getText().toString().length()<=10){
                ToastUtils.toast(R.string.homework_too_short);
                return false;
            }
            if(mViewStudentFeedback.getText().toString().length()<=10){
                ToastUtils.toast(R.string.student_feedback_too_short);
                return false;
            }
        }

        return true;
    }

    private void intiPopMenu() {
        /* 第一个参数弹出显示view 后两个是窗口大小 */
        mPopupWindow = new PopupWindow(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        /* 设置背景显示 */
        int bgColor = getResources().getColor(com.zhan.framework.R.color.main_background);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(bgColor));
        /* 设置触摸外面时消失 */
        mPopupWindow.setOutsideTouchable(true);
        /* 设置系统动画 */
        mPopupWindow.setAnimationStyle(R.style.pop_menu_animation);
        mPopupWindow.update();
        mPopupWindow.setTouchable(true);
        /* 设置点击menu以外其他地方以及返回键退出 */
        mPopupWindow.setFocusable(true);
    }

    private void showChooseMenu() {
        mPopMenuContent = getActivity().getLayoutInflater().inflate(R.layout.pop_memu_comm_wheel_layout, null);
        mPopupWindow.setContentView(mPopMenuContent);
        View btnCancel = mPopMenuContent.findViewById(R.id.cancel);
        btnCancel.setOnClickListener(mOnPopMenuBtnClickListener);
        View btnFinish = mPopMenuContent.findViewById(R.id.finish);
        btnFinish.setOnClickListener(mOnPopMenuBtnClickListener);

        TextView textViewTitle = (TextView)mPopMenuContent.findViewById(R.id.title);
        if(mBottomPopMenuStatus==WheelMenuStatus.CourseStatus){
            textViewTitle.setText(R.string.select_course_status);
        }
        else if(mBottomPopMenuStatus==WheelMenuStatus.HomeworkScore){
            textViewTitle.setText(R.string.select_homework_score);
        }
        mPickView = (LoopView) mPopMenuContent.findViewById(R.id.picker_view);
        mPickView.setNotLoop();

        mWheelValues = new ArrayList();
        if(mCommDictionary!=null){
            if(mBottomPopMenuStatus==WheelMenuStatus.CourseStatus){
                for(CommDictionary.CourseStatusEntity courseStatus:mCommDictionary.getCourseStatus()){
                    mWheelValues.add(courseStatus.getName());
                }
            }

            if(mBottomPopMenuStatus==WheelMenuStatus.HomeworkScore){
                mWheelValues.add(getString(R.string.please_select_homework_score));
                for(CommDictionary.HomeworkScoreEntity homeworkScore:mCommDictionary.getHomeworkScore()){
                    mWheelValues.add(homeworkScore.getName());
                }
            }

        }
        mPickView.setArrayList((ArrayList) mWheelValues);

        showPopMenu();
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
            /* 最重要的一步：弹出显示 在指定的位置(parent) 最后两个参数 是相对于 x / y 轴的坐标 */
            mPopupWindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
            backgroundAlpha(0.7f);
            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    backgroundAlpha(1f);
                }
            });
        }
        Tools.hideSoftInputFromWindow(getRootView());
    }

    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }
}
