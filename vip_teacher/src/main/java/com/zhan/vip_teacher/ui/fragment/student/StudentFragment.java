package com.zhan.vip_teacher.ui.fragment.student;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.zhan.framework.support.adapter.ABaseAdapter;
import com.zhan.framework.support.inject.ViewInject;
import com.zhan.framework.ui.activity.ActionBarActivity;
import com.zhan.framework.ui.fragment.AListFragment;
import com.zhan.framework.utils.ToastUtils;
import com.zhan.vip_teacher.R;
import com.zhan.vip_teacher.db.bean.StudentBean;
import com.zhan.vip_teacher.bean.SearchContent;
import com.zhan.vip_teacher.bean.StudentListBean;
import com.zhan.vip_teacher.bean.StudentListRequestBean;
import com.zhan.vip_teacher.bean.UserInfo;
import com.zhan.vip_teacher.network.ApiUrls;
import com.zhan.vip_teacher.ui.fragment.search.SearchFragment;
import com.zhan.vip_teacher.utils.CacheUtility;
import com.zhan.vip_teacher.utils.OperatorGuideManager;
import com.zhan.vip_teacher.utils.ShareUtil;
import com.zhan.vip_teacher.utils.Tools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/1.
 */
public class StudentFragment extends AListFragment<StudentBean, ArrayList<StudentBean>> {

    int progressStatusSection = -1;
    @ViewInject(id = R.id.search, click = "OnClick")
    ImageView mSearch;
    @ViewInject(id = R.id.student_lessoning, click = "OnClick")
    TextView mLessoning;
    @ViewInject(id = R.id.student_lesson_again, click = "OnClick")
    TextView mLessonAgain;
    @ViewInject(id = R.id.student_lesson_finish, click = "OnClick")
    TextView mLessonFinish;
    @ViewInject(id = R.id.rl_student)
    RelativeLayout mRelativeLayout ;
    private List<StudentBean> studentList;
    private DisplayImageOptions options;
    private StudentListRequestBean request;


    @Override
    protected int inflateContentView() {
        return R.layout.student_frag_layout;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        options = buldDisplayImageOptions();
    }

    @Override
    protected void layoutInit(LayoutInflater inflater, Bundle savedInstanceSate) {
        super.layoutInit(inflater, savedInstanceSate);
        if (progressStatusSection == -1) {
            mLessoning.setBackgroundResource(R.drawable.bg_dark_blue_underline);
            mLessonAgain.setBackgroundColor(Color.WHITE);
            mLessonFinish.setBackgroundColor(Color.WHITE);

            mLessoning.setTextColor(getResources().getColor(R.color.blue));
            mLessonAgain.setTextColor(getResources().getColor(R.color.student_title));
            mLessonFinish.setTextColor(getResources().getColor(R.color.student_title));
        } else if (progressStatusSection == -2) {
            mLessonAgain.setBackgroundResource(R.drawable.bg_dark_blue_underline);
            mLessoning.setBackgroundColor(Color.WHITE);
            mLessonFinish.setBackgroundColor(Color.WHITE);

            mLessonAgain.setTextColor(getResources().getColor(R.color.blue));
            mLessoning.setTextColor(getResources().getColor(R.color.student_title));
            mLessonFinish.setTextColor(getResources().getColor(R.color.student_title));
        } else if (progressStatusSection == -3) {
            mLessonFinish.setBackgroundResource(R.drawable.bg_dark_blue_underline);
            mLessonAgain.setBackgroundColor(Color.WHITE);
            mLessoning.setBackgroundColor(Color.WHITE);

            mLessonFinish.setTextColor(getResources().getColor(R.color.blue));
            mLessoning.setTextColor(getResources().getColor(R.color.student_title));
            mLessonAgain.setTextColor(getResources().getColor(R.color.student_title));
        }

        if(ShareUtil.VALUE_TURN_ON.equals(ShareUtil.getStringValue(ShareUtil.STUDENT_PAGE, ShareUtil.VALUE_TURN_ON))){
            ImageView imageView = new ImageView(getActivity());
            imageView.setImageResource(R.drawable.student_guide);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            final OperatorGuideManager operatorGuideManager=new OperatorGuideManager((ActionBarActivity) getActivity());
            operatorGuideManager.showGuideWindow(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    operatorGuideManager.hideGuideWindow();
                    ShareUtil.setValue(ShareUtil.STUDENT_PAGE,ShareUtil.VALUE_TURN_OFF);
                }
            });
        }
    }


    public DisplayImageOptions buldDisplayImageOptions() {
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
    protected ABaseAdapter.AbstractItemView<StudentBean> newItemView() {
        return new SampleItemView();
    }


    @Override
    protected void requestData(RefreshMode mode) {
        this.releaseAllRequest();
        request = new StudentListRequestBean();
        StudentListRequestBean.DataEntity data = new StudentListRequestBean.DataEntity();
        data.setUserID(UserInfo.getCurrentUser().getUserID());
        data.setProgressStatusSection(String.valueOf(progressStatusSection));
        data.setStudentCEName("");
        request.setData(data);
        //showRotateProgressDialog(getString(R.string.loading), true);
        startRequest(ApiUrls.TEACHER_GETTEACHERSTUDENTCOURSEINFOLIST, request, new PagingTask<StudentListBean>(mode) {

            @Override
            public StudentListBean parseResponseToResult(String content) {
                return Tools.parseJson(content, StudentListBean.class);
            }

            public String verifyResponseResult(StudentListBean result) {
                return Tools.verifyResponseResult(result);
            }

            @Override
            public void onRequestFinished(ResultCode resultCode, String result) {
                super.onRequestFinished(resultCode, result);
                switch (resultCode) {
                    case success:
                       // closeRotateProgressDialog();
                        break;
                    case canceled:
                        break;
                    default:
                        //refreshView(viewHolder, ViewStatus.LOADING_FAILED);
                        ToastUtils.toast(result);
                }
            }

            //此时将缓存的数据，绑定到listview上
            @Override
            public void beforeOnPrepare() {
                studentList = CacheUtility.findCacheData(ApiUrls.TEACHER_GETTEACHERSTUDENTCOURSEINFOLIST, request, StudentBean.class);
                if (getRefreshView().getAdapter() == null)
                    getRefreshView().setAdapter(getAdapter());
                setAdapterItems(new ArrayList<StudentBean>());
                addItems(studentList);
                notifyDataSetChanged();
            }

            @Override
            protected List<StudentBean> parseResult(StudentListBean studentListBean) {
                closeRotateProgressDialog();
                studentList = new ArrayList<>();
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

                    studentList.add(bean);
                }

                //缓存起来
                CacheUtility.addCacheDataList(ApiUrls.TEACHER_GETTEACHERSTUDENTCOURSEINFOLIST, request, studentList, StudentBean.class);
                return studentList;
            }
        });
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
            if(data.getTrueName() != null){
                mTureName.setText(getString(R.string.student_lesson_ture_name)+data.getTrueName());
            }else{
                mTureName.setText(getString(R.string.student_lesson_ture_name));
            }
            lessonScheduleCount.setText(getString(R.string.student_lesson_schedulecount)+data.getCourseType_ToName()+data.getSubCourseType_ToName()+"-"
                    +data.getScheduleCount() + "课时/共"+data.getSpecialCount()+"课时");
            ImageLoader.getInstance().displayImage(data.getHeadImgUrl(), mHead, options);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SearchContent content = new SearchContent();
        content.setCourseId(studentList.get((int) id).getCourseId());
        content.setGuid(studentList.get((int) id).getStudentGuid());
        StudentInfoFragment.launch(getActivity(), content);
    }

    void OnClick(View v) {
        switch (v.getId()) {
            case R.id.search:
                SearchFragment.launch(getActivity());
                break;
            case R.id.student_lessoning:
                progressStatusSection = -1;
                mLessoning.setBackgroundResource(R.drawable.bg_dark_blue_underline);
                mLessoning.setTextColor(getResources().getColor(R.color.blue));

                mLessonAgain.setBackgroundColor(Color.WHITE);
                mLessonAgain.setTextColor(getResources().getColor(R.color.student_title));

                mLessonFinish.setBackgroundColor(Color.WHITE);
                mLessonFinish.setTextColor(getResources().getColor(R.color.student_title));
                requestData();
                break;
            case R.id.student_lesson_again:
                progressStatusSection = -2;
                mLessoning.setBackgroundColor(Color.WHITE);
                mLessoning.setTextColor(getResources().getColor(R.color.student_title));

                mLessonAgain.setBackgroundResource(R.drawable.bg_dark_blue_underline);
                mLessonAgain.setTextColor(getResources().getColor(R.color.blue));

                mLessonFinish.setBackgroundColor(Color.WHITE);
                mLessonFinish.setTextColor(getResources().getColor(R.color.student_title));
                requestData();
                break;
            case R.id.student_lesson_finish:
                progressStatusSection = -3;
                mLessoning.setBackgroundColor(Color.WHITE);
                mLessoning.setTextColor(getResources().getColor(R.color.student_title));

                mLessonAgain.setBackgroundColor(Color.WHITE);
                mLessonAgain.setTextColor(getResources().getColor(R.color.student_title));

                mLessonFinish.setBackgroundResource(R.drawable.bg_dark_blue_underline);
                mLessonFinish.setTextColor(getResources().getColor(R.color.blue));
                requestData();
                break;
        }
    }

}
