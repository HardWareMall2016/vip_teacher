package com.zhan.vip_teacher.ui.fragment.search;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.zhan.framework.component.container.FragmentArgs;
import com.zhan.framework.component.container.FragmentContainerActivity;
import com.zhan.framework.support.adapter.ABaseAdapter;
import com.zhan.framework.support.inject.ViewInject;
import com.zhan.framework.ui.fragment.AListFragment;
import com.zhan.vip_teacher.R;
import com.zhan.vip_teacher.bean.SearchResultListRequestBean;
import com.zhan.vip_teacher.db.bean.StudentBean;
import com.zhan.vip_teacher.bean.SearchContent;
import com.zhan.vip_teacher.bean.StudentListBean;
import com.zhan.vip_teacher.bean.StudentListRequestBean;
import com.zhan.vip_teacher.bean.UserInfo;
import com.zhan.vip_teacher.network.ApiUrls;
import com.zhan.vip_teacher.ui.fragment.student.StudentInfoFragment;
import com.zhan.vip_teacher.utils.SendEventUtil;
import com.zhan.vip_teacher.utils.Tools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/9.
 */
public class SearchResultFragment extends AListFragment<StudentBean, ArrayList<StudentBean>> {
    private final static String ARG_KEY = "bean";
    private StatusContent mStatusContent;
    private List<StudentBean> searchRstList;
    private DisplayImageOptions options;

    @Override
    protected int inflateContentView() {
        return R.layout.search_result_frg_layout;
    }

    public static void launch(FragmentActivity activity, StatusContent bean) {
        FragmentArgs args = new FragmentArgs();
        args.add(ARG_KEY, bean);
        FragmentContainerActivity.launch(activity, SearchResultFragment.class, args);
        //activity.finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStatusContent = savedInstanceState == null ? (StatusContent) getArguments().getSerializable(ARG_KEY)
                : (StatusContent) savedInstanceState.getSerializable(ARG_KEY);
        options= buldDisplayImageOptions();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ARG_KEY, mStatusContent);
    }

    @Override
    protected void layoutInit(LayoutInflater inflater, Bundle savedInstanceSate) {
        super.layoutInit(inflater, savedInstanceSate);
        getActivity().setTitle(getString(R.string.search_result));
    }

    @Override
    protected ABaseAdapter.AbstractItemView<StudentBean> newItemView() {
        return new SampleItemView();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SearchContent content = new SearchContent();
        content.setCourseId(searchRstList.get((int) id).getCourseId());
        content.setGuid(searchRstList.get((int) id).getStudentGuid());
        StudentInfoFragment.launch(getActivity(), content);
    }

    public DisplayImageOptions buldDisplayImageOptions(){
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.student_avatar)
                .showImageForEmptyUri(R.drawable.student_avatar)
                .showImageOnFail(R.drawable.student_avatar)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
    }


    @Override
    protected void requestData(RefreshMode mode) {
        SendEventUtil.sendEvent(3,"搜索学生","学生姓名:"+mStatusContent.getArg1()+" ；课程进度:"+mStatusContent.getArg2());
        if(mStatusContent.getArg2() != null){
            StudentListRequestBean request = new StudentListRequestBean();
            StudentListRequestBean.DataEntity data = new StudentListRequestBean.DataEntity();
            data.setUserID(UserInfo.getCurrentUser().getUserID());
            data.setStudentCEName(mStatusContent.getArg1());
            data.setProgressStatusSection(mStatusContent.getArg2());
            request.setData(data);

            showRotateProgressDialog(getString(R.string.loading), true);
            startRequest(ApiUrls.TEACHER_GETTEACHERSTUDENTCOURSEINFOLIST, request, new PagingTask<StudentListBean>(mode) {
                @Override
                public StudentListBean parseResponseToResult(String content) {
                    return Tools.parseJson(content, StudentListBean.class);
                }

                public String verifyResponseResult(StudentListBean result) {
                    closeRotateProgressDialog();
                    return Tools.verifyResponseResult(result);
                }

                @Override
                protected boolean resultIsEmpty(StudentListBean result) {
                    closeRotateProgressDialog();
                    boolean isEmpty = true;
                    if (result != null && result.getData() != null) {
                        isEmpty = false;
                    }
                    return isEmpty;
                }

                @Override
                protected List<StudentBean> parseResult(StudentListBean studentListBean) {
                    closeRotateProgressDialog();
                    searchRstList = new ArrayList<>();
                    for (int i = 0; i < studentListBean.getData().size(); i++) {
                        StudentListBean.DataEntity item = studentListBean.getData().get(i);
                        StudentBean bean = new StudentBean();
                        bean.setCourseType(item.getCourseType());
                        bean.setCourseType_ToName(item.getCourseType_ToName());
                        bean.setScheduleCount(item.getScheduleCount());
                        bean.setProgressStatus(item.getProgressStatus());
                        bean.setProgressStatus_ToName(item.getProgressStatus_ToName());
                        bean.setCName(item.getCName());
                        bean.setHeadImgUrl(item.getHeadImgUrl());
                        bean.setTrueName(item.getTrueName());
                        bean.setSpecialCount(item.getSpecialCount());
                        bean.setStudyConsultant(item.getStudyConsultant());
                        bean.setProductId(item.getProductId());
                        bean.setCourseId(item.getCourseId());
                        bean.setStudentGuid(item.getStudentGuid());
                        bean.setSubCourseType_ToName(item.getSubCourseType_ToName());
                        searchRstList.add(bean);
                    }
                    return searchRstList;
                }
            });
        }else {
            SearchResultListRequestBean request = new SearchResultListRequestBean();
            SearchResultListRequestBean.DataEntity data = new SearchResultListRequestBean.DataEntity();
            data.setUserID(UserInfo.getCurrentUser().getUserID());
            data.setStudentCEName(mStatusContent.getArg1());
            request.setData(data);

            showRotateProgressDialog(getString(R.string.loading), true);
            startRequest(ApiUrls.TEACHER_GETTEACHERSTUDENTCOURSEINFOLIST, request, new PagingTask<StudentListBean>(mode) {
                @Override
                public StudentListBean parseResponseToResult(String content) {
                    return Tools.parseJson(content, StudentListBean.class);
                }

                public String verifyResponseResult(StudentListBean result) {
                    closeRotateProgressDialog();
                    return Tools.verifyResponseResult(result);
                }

                @Override
                protected boolean resultIsEmpty(StudentListBean result) {
                    closeRotateProgressDialog();
                    boolean isEmpty = true;
                    if (result != null && result.getData() != null) {
                        isEmpty = false;
                    }
                    return isEmpty;
                }

                @Override
                protected List<StudentBean> parseResult(StudentListBean studentListBean) {
                    closeRotateProgressDialog();
                    searchRstList = new ArrayList<>();
                    for (int i = 0; i < studentListBean.getData().size(); i++) {
                        StudentListBean.DataEntity item = studentListBean.getData().get(i);
                        StudentBean bean = new StudentBean();
                        bean.setCourseType(item.getCourseType());
                        bean.setCourseType_ToName(item.getCourseType_ToName());
                        bean.setScheduleCount(item.getScheduleCount());
                        bean.setProgressStatus(item.getProgressStatus());
                        bean.setProgressStatus_ToName(item.getProgressStatus_ToName());
                        bean.setCName(item.getCName());
                        bean.setHeadImgUrl(item.getHeadImgUrl());
                        bean.setTrueName(item.getTrueName());
                        bean.setSpecialCount(item.getSpecialCount());
                        bean.setStudyConsultant(item.getStudyConsultant());
                        bean.setProductId(item.getProductId());
                        bean.setCourseId(item.getCourseId());
                        bean.setStudentGuid(item.getStudentGuid());
                        bean.setSubCourseType_ToName(item.getSubCourseType_ToName());
                        searchRstList.add(bean);
                    }
                    return searchRstList;
                }
            });
        }




    }

    class SampleItemView extends ABaseAdapter.AbstractItemView<StudentBean> {
        @ViewInject(id = R.id.student_name)
        TextView studentName;
        @ViewInject(id = R.id.student_lesson_plan)
        TextView lessonPlan;
        @ViewInject(id = R.id.student_lesson_schedulecount)
        TextView lessonScheduleCount;
        @ViewInject(id = R.id.student_list_item_head)
        ImageView mHead;
        @ViewInject(id = R.id.student_lesson_ture_name)
        TextView mTureName ;

        @Override
        public int inflateViewId() {
            return R.layout.student_list_item;
        }

        @Override
        public void bindingData(View convertView, StudentBean data) {
            studentName.setText(data.getCName());
            lessonPlan.setText(getString(R.string.student_lesson_plan) + data.getProgressStatus_ToName());
            mTureName.setText(getString(R.string.student_lesson_ture_name)+data.getTrueName());
            lessonScheduleCount.setText(getString(R.string.student_lesson_schedulecount)+ data.getCourseType_ToName()+data.getSubCourseType_ToName()+"-"
                    +data.getScheduleCount() + "课时/共"+data.getSpecialCount()+"课时");
            ImageLoader.getInstance().displayImage(data.getHeadImgUrl(), mHead, options);
        }
    }
}
