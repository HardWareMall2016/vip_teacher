package com.zhan.vip_teacher.ui.fragment.student;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhan.framework.component.container.FragmentArgs;
import com.zhan.framework.component.container.FragmentContainerActivity;
import com.zhan.framework.network.HttpRequestHandler;
import com.zhan.framework.support.adapter.ABaseAdapter;
import com.zhan.framework.support.inject.ViewInject;
import com.zhan.framework.ui.fragment.ABaseFragment;
import com.zhan.framework.ui.fragment.AListFragment;
import com.zhan.framework.utils.ToastUtils;
import com.zhan.vip_teacher.R;
import com.zhan.vip_teacher.bean.StudentCourseBean;
import com.zhan.vip_teacher.bean.StudentCourseContent;
import com.zhan.vip_teacher.bean.StudentCourseListBean;
import com.zhan.vip_teacher.bean.StudentCourseListRequestBean;
import com.zhan.vip_teacher.bean.UserInfo;
import com.zhan.vip_teacher.network.ApiUrls;
import com.zhan.vip_teacher.ui.fragment.course.ClassFeedbackFragment;
import com.zhan.vip_teacher.utils.Tools;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/11.
 */
public class StudentCourseFragment extends AListFragment<StudentCourseBean, ArrayList<StudentCourseBean>> {

    private final static String ARG_KEY = "studentGuid";
    private int type = 0;//默认未上课
    private List<StudentCourseBean> testList ;
    @ViewInject(id = R.id.student_course_nolesson, click = "OnClick")
    TextView mNoLeasson ;
    @ViewInject(id = R.id.student_lessoned, click = "OnClick")
    TextView mLessoned;

    private StudentCourseContent content ;


    public static void launch(FragmentActivity activity, StudentCourseContent content) {
        FragmentArgs args = new FragmentArgs();
        args.add(ARG_KEY, content);
        FragmentContainerActivity.launch(activity, StudentCourseFragment.class, args);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        content = savedInstanceState == null ? (StudentCourseContent) getArguments().getSerializable(ARG_KEY)
                : (StudentCourseContent) savedInstanceState.getSerializable(ARG_KEY);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ARG_KEY, content);
    }

    @Override
    protected void layoutInit(LayoutInflater inflater, Bundle savedInstanceSate) {
        super.layoutInit(inflater, savedInstanceSate);
        getActivity().setTitle(content.getStudentName());
        if(type == 0){
            mNoLeasson.setBackgroundResource(R.drawable.bg_dark_blue_underline);
            mNoLeasson.setTextColor(getResources().getColor(R.color.blue));

            mLessoned.setBackgroundColor(Color.WHITE);
            mLessoned.setTextColor(getResources().getColor(R.color.text_color_content));
        }else {
            mLessoned.setBackgroundResource(R.drawable.bg_dark_blue_underline);
            mLessoned.setTextColor(getResources().getColor(R.color.blue));

            mNoLeasson.setBackgroundColor(Color.WHITE);
            mNoLeasson.setTextColor(getResources().getColor(R.color.text_color_content));
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //super.onItemClick(parent, view, position, id);
        //Log.i("wuyue","position = "+position+" , LessonId = "+getAdapterItems().get(position).getLessonId());
        ClassFeedbackFragment.launch(getActivity(), getAdapterItems().get(position).getLessonId());
    }

    @Override
    protected int inflateContentView() {
        return R.layout.student_course_frag_layout;
    }

    @Override
    protected ABaseAdapter.AbstractItemView<StudentCourseBean> newItemView() {
        return new SampleItemView();
    }


    @Override
    protected void requestData(RefreshMode mode) {
        StudentCourseListRequestBean request = new StudentCourseListRequestBean();
        StudentCourseListRequestBean.DataEntity noClassData= new StudentCourseListRequestBean.DataEntity();
        noClassData.setStudentGuid(content.getStudentGuid());
        noClassData.setTeacherId(UserInfo.getCurrentUser().getUserID());
        noClassData.setType(type);
        request.setData(noClassData);
        showRotateProgressDialog(getString(R.string.loading), true);
        startRequest(ApiUrls.GET_STUDENT_LESSON_BYTEACHERID, request, new PagingTask<StudentCourseListBean>(mode) {
            @Override
            public void onRequestFinished(ResultCode resultCode, String result) {
                super.onRequestFinished(resultCode, result);
                switch (resultCode) {
                    case success:
                        closeRotateProgressDialog();
                        break;
                    case canceled:
                        setViewVisiable(emptyLayout, View.VISIBLE);
                        break;
                    default:
                        //refreshView(viewHolder, ViewStatus.LOADING_FAILED);
                        ToastUtils.toast(result);
                }
            }

            @Override
            public StudentCourseListBean parseResponseToResult(String content) {
                return Tools.parseJson(content, StudentCourseListBean.class);
            }

            public String verifyResponseResult(StudentCourseListBean result) {
                return Tools.verifyResponseResult(result);
            }

            @Override
            protected List<StudentCourseBean> parseResult(StudentCourseListBean studentCourseListBean) {
                //closeRotateProgressDialog();
                testList = new ArrayList<>();
                if(studentCourseListBean != null){
                    for (int i = 0; i < studentCourseListBean.getData().getListClass().size(); i++) {
                        StudentCourseListBean.DataEntity.ListClassEntity item = studentCourseListBean.getData().getListClass().get(i);
                        StudentCourseBean bean = new StudentCourseBean();
                        bean.setStartTime(item.getStartTime());
                        bean.setEndTime(item.getEndTime());
                        bean.setLessonState(item.getLessonState());
                        bean.setLessonStateName(item.getLessonStateName());
                        bean.setTeacherName(item.getTeacherName());
                        bean.setCourseType(item.getCourseType());
                        bean.setCourseTypeName(item.getCourseTypeName());
                        bean.setCourseSubType(item.getCourseSubType());
                        bean.setCourseSubTypeName(item.getCourseSubTypeName());
                        bean.setLessonId(item.getLessonId());
                        testList.add(bean);
                    }
                }
                return testList;
            }
        });
    }

    void OnClick(View view){
        switch (view.getId()){
            case R.id.student_course_nolesson:
                mNoLeasson.setBackgroundResource(R.drawable.bg_dark_blue_underline);
                mNoLeasson.setTextColor(getResources().getColor(R.color.blue));
                mLessoned.setBackgroundColor(Color.WHITE);
                mLessoned.setTextColor(getResources().getColor(R.color.text_color_content));
                type = 0;
                requestData();
                break;
            case R.id.student_lessoned:
                mLessoned.setBackgroundResource(R.drawable.bg_dark_blue_underline);
                mLessoned.setTextColor(getResources().getColor(R.color.blue));
                mNoLeasson.setBackgroundColor(Color.WHITE);
                mNoLeasson.setTextColor(getResources().getColor(R.color.text_color_content));
                type = 1;
                requestData();
                break;
        }
    }

    class SampleItemView extends ABaseAdapter.AbstractItemView<StudentCourseBean> {
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
        public void bindingData(View convertView, StudentCourseBean data) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String time = simpleDateFormat.format(Tools.parseServerTime(data.getStartTime()));
            mTime.setText(time);
            SimpleDateFormat mLessonTimeFormat = new SimpleDateFormat("HH:mm");
            String startTime = mLessonTimeFormat.format(Tools.parseServerTime(data.getStartTime()));
            String endTime = mLessonTimeFormat.format(Tools.parseServerTime(data.getEndTime()));
            coursetime.setText(startTime + "-" + endTime);
            courseType.setText(data.getCourseTypeName() + data.getCourseSubTypeName());
            if(TextUtils.isEmpty(data.getLessonStateName())){
                lessonStatus.setText(getString(R.string.lesson_status_name) + getString(R.string.lesson_status_no_lesson));
            }else{
                lessonStatus.setText(getString(R.string.lesson_status_name) + data.getLessonStateName());
            }
            student.setText("老师：" + data.getTeacherName());
            icon.setImageResource(Tools.getCourseTypeIconBySubTypeName(data.getCourseSubTypeName()));
        }
    }
}
