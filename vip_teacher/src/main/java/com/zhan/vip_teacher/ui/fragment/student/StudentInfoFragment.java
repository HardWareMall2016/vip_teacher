package com.zhan.vip_teacher.ui.fragment.student;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Layout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.zhan.framework.component.container.FragmentArgs;
import com.zhan.framework.component.container.FragmentContainerActivity;
import com.zhan.framework.support.inject.ViewInject;
import com.zhan.framework.ui.activity.BaseActivity;
import com.zhan.framework.ui.fragment.ABaseFragment;
import com.zhan.framework.ui.widget.CircleImageView;
import com.zhan.framework.view.pickerview.LoopView;
import com.zhan.vip_teacher.R;
import com.zhan.vip_teacher.bean.SearchContent;
import com.zhan.vip_teacher.bean.StudentCourseContent;
import com.zhan.vip_teacher.bean.StudentInfoBean;
import com.zhan.vip_teacher.bean.StudentInfoRequestBean;
import com.zhan.vip_teacher.network.ApiUrls;
import com.zhan.vip_teacher.utils.Tools;
import com.zhan.vip_teacher.view.MyGridView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/3/10.
 */
public class StudentInfoFragment extends ABaseFragment {
    private final static String ARG_KEY = "courseId";

    private View mStudentInfoPopMenuContent;
    private PopupWindow mPopupWindow;
    private List<StudentInfoBean.DataEntity.HistoryExamDtoListEntity> historyExamDtoList = new ArrayList<>();
    private List<String> changeList = new ArrayList<>();
    private List<StudentInfoBean.DataEntity.HistoryExamDtoListEntity.DetailsCollectionEntity> detailsCollectionEntityList = new ArrayList<>();
    private int i = 0;
    @ViewInject(id = R.id.student_first, click = "OnClick")
    TextView mFirst;
    @ViewInject(id = R.id.picker_view)
    LoopView mPickView;
    @ViewInject(id = R.id.student_course, click = "OnClick")
    LinearLayout mCourse;
    @ViewInject(id = R.id.student_info_back, click = "OnClick")
    ImageView mBack ;
    @ViewInject(id = R.id.student_head)
    ImageView mStudentHead;
    @ViewInject(id = R.id.student_name)
    TextView mStudentName;
    @ViewInject(id = R.id.student_sex)
    ImageView mStudentSex ;
    @ViewInject(id = R.id.student_age)
    TextView mStudentAge;
    @ViewInject(id = R.id.student_QQ)
    TextView mStudentQQ;
    @ViewInject(id = R.id.student_educational)
    TextView mDegree;
    @ViewInject(id = R.id.student_school)
    TextView mSchool;
    @ViewInject(id = R.id.student_province)
    TextView mProvince;
    @ViewInject(id = R.id.student_vip)
    TextView mStudentVip;
    @ViewInject(id = R.id.student_lesson_name)
    TextView mLessonName;
    @ViewInject(id = R.id.student_exam_data)
    TextView mExamData;
    @ViewInject(id = R.id.student_lesson_type)
    TextView mLessonType;
    @ViewInject(id = R.id.student_goal)
    TextView mStudentGoal;
    @ViewInject(id = R.id.student_count_lesson)
    TextView mCountLesson;
    @ViewInject(id = R.id.student_reread)
    TextView mStdentReread;
    @ViewInject(id = R.id.student_reread_data)
    TextView mStudentRereadData;
    @ViewInject(id = R.id.student_next_mark)
    TextView mNextMark;
    @ViewInject(id = R.id.student_next_data)
    TextView mNextData;
    @ViewInject(id = R.id.student_first_remarks)
    TextView mRemarks;
    @ViewInject(id = R.id.student_lesson_down, click = "OnClick")
    ImageView mDown ;
    @ViewInject(id = R.id.student_history_down, click = "OnClick")
    ImageView mHistoryDown ;
    @ViewInject(id = R.id.student_gridview)
    MyGridView myGridView;
    @ViewInject(id = R.id.student_test_type)
    TextView mHistoryExamType;
    @ViewInject(id = R.id.student_test_lesson)
    TextView mHistoryExamCourseTypeName;
    @ViewInject(id = R.id.student_test_data)
    TextView mHistoryExamDate;
    @ViewInject(id = R.id.student_all_mark)
    TextView mHistoryTotalScore;
    @ViewInject(id = R.id.student_classify_mark)
    TextView mScore;
    @ViewInject(id = R.id.student_remarks)
    TextView mHistoryRemarks;
    @ViewInject(id = R.id.student_lesson_history_list)
    LinearLayout mHistoryList;

    private String studentGuid = "";
    private SearchContent searchContent ;
    private int courseId;
    private DisplayImageOptions options;
    private String studentName ;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected int inflateContentView() {
        return R.layout.student_info_frag_layout;
    }

    public static void launch(FragmentActivity activity, SearchContent searchContent) {
        FragmentArgs args = new FragmentArgs();
        args.add(ARG_KEY, searchContent);
        FragmentContainerActivity.launch(activity, StudentInfoFragment.class, args,false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchContent = savedInstanceState == null ? (SearchContent) getArguments().getSerializable(ARG_KEY)
                : (SearchContent) savedInstanceState.getSerializable(ARG_KEY);
        options= buldDisplayImageOptions();
    }

    public DisplayImageOptions buldDisplayImageOptions(){
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.student_info_avatar)
                .showImageForEmptyUri(R.drawable.student_info_avatar)
                .showImageOnFail(R.drawable.student_info_avatar)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ARG_KEY, searchContent);
    }


    @Override
    protected void layoutInit(LayoutInflater inflater, Bundle savedInstanceSate) {
        super.layoutInit(inflater, savedInstanceSate);
        ((BaseActivity)getActivity()).showActionbar(false);
        intiPopMenu();
        courseId = searchContent.getCourseId();
        studentGuid = searchContent.getGuid() ;
    }

    @Override
    public void requestData() {
        StudentInfoRequestBean request = new StudentInfoRequestBean();
        StudentInfoRequestBean.DataEntity dataEntity = new StudentInfoRequestBean.DataEntity();
        dataEntity.setCourseId(courseId);
        dataEntity.setGuid(studentGuid);
        request.setData(dataEntity);
        showRotateProgressDialog(getString(R.string.loading), true);
        startRequest(ApiUrls.GETTEACHERSTUDENTINFOBYDATA, request, new BaseHttpRequestTask<StudentInfoBean>() {
            @Override
            public StudentInfoBean parseResponseToResult(String content) {
                return Tools.parseJsonTostError(content, StudentInfoBean.class);
            }

            @Override
            protected void onSuccess(StudentInfoBean responseBean) {
                super.onSuccess(responseBean);
                closeRotateProgressDialog();
                if (responseBean != null) {
                    studentGuid = responseBean.getData().getGuid();
                    mStudentName.setText(responseBean.getData().getCName());
                    studentName = responseBean.getData().getCName() ;
                    mStudentAge.setText(responseBean.getData().getAge() + "岁");
                    if(("男").equals(responseBean.getData().getGenderName())){
                        mStudentSex.setImageResource(R.drawable.sex_woman);
                    }else{
                        mStudentSex.setImageResource(R.drawable.sex_man);
                    }

                    mStudentQQ.setText(String.format("QQ：%s", responseBean.getData().getQQ()));

                    mDegree.setText(responseBean.getData().getDegreeName());
                    mSchool.setText(responseBean.getData().getSchool());
                    mProvince.setText(responseBean.getData().getStudentAddressInfo().getProvinceName()+" "+responseBean.getData().getStudentAddressInfo().getCityName());

                    if(responseBean.getData().getVipGrade() != 0){
                        mStudentVip.setText(responseBean.getData().getVipGrade() + "级");
                    }else{
                        mStudentVip.setText("无");
                    }
                    mLessonName.setText(responseBean.getData().getCourseName());
                    mLessonType.setText(responseBean.getData().getCourseTypeName());
                    mStudentGoal.setText(responseBean.getData().getGuaranteeScore());
                    mCountLesson.setText(responseBean.getData().getLessonCount() + "");
                    mStdentReread.setText(responseBean.getData().getRereadingTimes() + "次");
                    mNextMark.setText(responseBean.getData().getNextTargetScore() + "");

                    //判断时间是否为空
                    String examData = formatTime(responseBean.getData().getExamDate());
                    mExamData.setText(examData);
                    String rereadData = formatTime(responseBean.getData().getRereadingApplyDate());
                    mStudentRereadData.setText(rereadData);
                    String nextTestTime = formatTime(responseBean.getData().getNextExamDate());
                    mNextData.setText(nextTestTime);

                    mRemarks.setText(responseBean.getData().getConsultRemark());
                    mRemarks.post(new Runnable() {
                        @Override
                        public void run() {
                            Layout layout = mRemarks.getLayout();
                            int lines = layout.getLineCount();
                            //判断是否是否超过一行。超过一行可以展开
                            if (lines > 1) {
                                mDown.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    ImageLoader.getInstance().displayImage(responseBean.getData().getHeadImgUrl(), mStudentHead, options);

                    historyExamDtoList = responseBean.getData().getHistoryExamDtoList();
                    for (int j = 0; j < historyExamDtoList.size(); j++) {
                        detailsCollectionEntityList = historyExamDtoList.get(j).getDetailsCollection();
                    }

                    if (historyExamDtoList != null && historyExamDtoList.size() != 0) {
                        refresh();
                        mHistoryList.setVisibility(View.VISIBLE);
                    }
                    myGridView.setAdapter(new GridViewAdapter(responseBean.getData().getStudentCourseDetails()));
                }
            }
        });
    }

    private String  formatTime(String time){
       long timeInMins= Tools.parseServerTime(time);
        if(timeInMins>0){
            return  simpleDateFormat.format(timeInMins);
        }
        return "";
    }

    private class GridViewAdapter extends BaseAdapter {

        private List<StudentInfoBean.DataEntity.StudentCourseDetailsEntity> studentCourseDetails = new ArrayList<>();

        public GridViewAdapter(List<StudentInfoBean.DataEntity.StudentCourseDetailsEntity> studentCourseDetails) {
            this.studentCourseDetails = studentCourseDetails;
        }

        @Override
        public int getCount() {
            return studentCourseDetails.size();
        }

        @Override
        public Object getItem(int position) {
            return studentCourseDetails.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.student_info_gridview_item, null);
                holder = new ViewHolder();
                holder.courseSubTypeName = (TextView) convertView.findViewById(R.id.gridview_courseSubTypeName);
                holder.lessonCount = (TextView) convertView.findViewById(R.id.gridview_lessonCount);
                holder.teacherName = (TextView) convertView.findViewById(R.id.gridview_teacherName);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.courseSubTypeName.setText(studentCourseDetails.get(position).getCourseSubTypeName()+"：");
            holder.lessonCount.setText(studentCourseDetails.get(position).getLessonCount() + "节");
            List<StudentInfoBean.DataEntity.StudentCourseDetailsEntity.StudentTeacherListEntity> studentTeacherList = new ArrayList<>();
            studentTeacherList = studentCourseDetails.get(position).getStudentTeacherList();
            String temp = "";
            for(int i= 0;i<studentTeacherList.size();i++){
                String teacherName = studentTeacherList.get(i).getTeacherName();
                if(i == 0){
                    temp = teacherName;
                }else{
                    temp +=(","+ teacherName);
                }
            }
            holder.teacherName.setText(temp);
            return convertView;
        }
    }

    private class ViewHolder {
        TextView courseSubTypeName;
        TextView lessonCount;
        TextView teacherName;
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

    void OnClick(View view) {
        switch (view.getId()) {
            case R.id.student_first:
                changeList.clear();
                showChooseMenu();
                break;
            case R.id.student_course:
                StudentCourseContent content = new StudentCourseContent();
                content.setStudentGuid(studentGuid);
                content.setStudentName(studentName);
                StudentCourseFragment.launch(getActivity(), content);
                break;
            case R.id.student_lesson_down:
                if(mRemarks.getEllipsize() != null){
                    mRemarks.setEllipsize(null);//展开
                    mRemarks.setMaxLines(Integer.MAX_VALUE);
                    mDown.setImageResource(R.drawable.arrow_up);
                }else{
                    mRemarks.setEllipsize(TextUtils.TruncateAt.END);//收缩
                    mRemarks.setMaxLines(1);
                    mDown.setImageResource(R.drawable.arrow_down);
                }
                break;
            case R.id.student_history_down:
                changeList.clear();
                showChooseMenu();
                break;
            case R.id.student_info_back:
                getActivity().finish();
                break;
        }
    }

    private void showChooseMenu() {
        mStudentInfoPopMenuContent = getActivity().getLayoutInflater().inflate(R.layout.pop_memu_changlesson, null);
        mPopupWindow.setContentView(mStudentInfoPopMenuContent);
        TextView title = (TextView) mStudentInfoPopMenuContent.findViewById(R.id.exam_time_set);
        title.setText(getString(R.string.student_info_change_history_lesson));
        View btnCancel = mStudentInfoPopMenuContent.findViewById(R.id.exam_time_cancel_time);
        btnCancel.setOnClickListener(mOnExamTimeClickListener);
        View btnFinish = mStudentInfoPopMenuContent.findViewById(R.id.exam_time_finish_time);
        btnFinish.setOnClickListener(mOnExamTimeClickListener);

        mPickView = (LoopView) mStudentInfoPopMenuContent.findViewById(R.id.picker_view);
        mPickView.setNotLoop();
        i = mPickView.getSelectedItem();

        for (int j = 0; j < historyExamDtoList.size(); j++) {
            int k = j + 1;
            changeList.add("第" + k + "次");
        }

        mPickView.setArrayList((ArrayList) changeList);
        mPickView.setInitPosition(0);

        showPopMenu();
    }

    private View.OnClickListener mOnExamTimeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.exam_time_cancel_time:
                    closePopWin();
                    break;
                case R.id.exam_time_finish_time:
                    i = mPickView.getSelectedItem();
                    if (changeList.size() != 0) {
                        int k = i + 1;
                        mFirst.setText("第" + k + "次");
                    } else {
                        mFirst.setText("请选择");
                    }
                    closePopWin();
                    refresh();
                    break;
            }
        }
    };

    public void refresh() {
        mHistoryExamType.setText(historyExamDtoList.get(i).getCourseTypeName());
        mHistoryExamCourseTypeName.setText(historyExamDtoList.get(i).getCourseTypeName());
        Date date = null;
        try {
            if(historyExamDtoList.get(i).getExamDate() != null){
                date = simpleDateFormat.parse(historyExamDtoList.get(i).getExamDate());
                mHistoryExamDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(date));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mHistoryTotalScore.setText(historyExamDtoList.get(i).getTotalScore());

        detailsCollectionEntityList = historyExamDtoList.get(i).getDetailsCollection();
        if (detailsCollectionEntityList == null || detailsCollectionEntityList.size() == 0) {
            mScore.setText("");
        } else {
            String temp = "";
            for (StudentInfoBean.DataEntity.HistoryExamDtoListEntity.DetailsCollectionEntity data : detailsCollectionEntityList) {
                temp += data.getCourseSubTypeName() + " " + data.getScore();
            }
            mScore.setText(temp);
        }
        if(historyExamDtoList.get(i).getRemark() != null){
            mHistoryRemarks.setText(historyExamDtoList.get(i).getRemark()+"");
        }
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
    }

    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }
}