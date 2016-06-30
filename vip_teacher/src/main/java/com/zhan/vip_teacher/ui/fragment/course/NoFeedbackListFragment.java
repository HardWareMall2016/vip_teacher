package com.zhan.vip_teacher.ui.fragment.course;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhan.framework.component.container.FragmentContainerActivity;
import com.zhan.framework.support.adapter.ABaseAdapter;
import com.zhan.framework.support.inject.ViewInject;
import com.zhan.framework.ui.fragment.AListFragment;
import com.zhan.vip_teacher.R;
import com.zhan.vip_teacher.bean.NoFeedbackListItemBean;
import com.zhan.vip_teacher.bean.NoFeedbackListRequestBean;
import com.zhan.vip_teacher.bean.NoFeedbackListResponseBean;
import com.zhan.vip_teacher.bean.UserInfo;
import com.zhan.vip_teacher.network.ApiUrls;
import com.zhan.vip_teacher.utils.SendEventUtil;
import com.zhan.vip_teacher.utils.Tools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by WuYue on 2016/3/10.
 */
public class NoFeedbackListFragment extends AListFragment<NoFeedbackListItemBean,ArrayList<NoFeedbackListItemBean>> {

    private SimpleDateFormat mMainimeFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat mWeekFormat = new SimpleDateFormat("EEE");
    private SimpleDateFormat mSubTimeFormat = new SimpleDateFormat("HH:mm");

    public static void launch(Activity from) {
        FragmentContainerActivity.launch(from, NoFeedbackListFragment.class, null);
        SendEventUtil.sendEvent(2,"待反馈课程列表","");
    }

    @Override
    protected void layoutInit(LayoutInflater inflater, Bundle savedInstanceSate) {
        super.layoutInit(inflater, savedInstanceSate);
        getActivity().setTitle(R.string.unfeedback_course);
    }

    @Override
    protected ABaseAdapter.AbstractItemView<NoFeedbackListItemBean> newItemView() {
        return new AdapterItemView();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //ToastUtils.toast("Click Item " + position);
        NoFeedbackListItemBean data=getABaseAdapter().getDatas().get(position);
        ClassFeedbackFragment.launch(getActivity(),data.getLessonId());
    }

    @Override
    protected void requestData(RefreshMode mode) {
        NoFeedbackListRequestBean requestBean=new NoFeedbackListRequestBean();
        requestBean.setData(UserInfo.getCurrentUser().getUserID());

        startRequest(ApiUrls.GET_NO_FEEDBACK_LIST, requestBean, new PagingTask<NoFeedbackListResponseBean>(RefreshMode.reset) {

            @Override
            public NoFeedbackListResponseBean parseResponseToResult(String content) {
                return Tools.parseJson(content, NoFeedbackListResponseBean.class);
            }

            @Override
            public String verifyResponseResult(NoFeedbackListResponseBean result) {
                return Tools.verifyResponseResult(result);
            }

            @Override
            protected boolean resultIsEmpty(NoFeedbackListResponseBean result) {
                boolean isEmpty=true;
                if(result != null&&result.getData()!=null&&result.getData().size()>0){
                    isEmpty=false;
                }
                return isEmpty;
            }

            @Override
            protected void onSuccess(NoFeedbackListResponseBean responseBean) {
                super.onSuccess(responseBean);
            }

            @Override
            protected List<NoFeedbackListItemBean> parseResult(NoFeedbackListResponseBean noFeedbackListResponseBean) {
                List<NoFeedbackListItemBean> result = new LinkedList<>();
                for(NoFeedbackListResponseBean.DataEntity dataEntity:noFeedbackListResponseBean.getData()){
                    NoFeedbackListItemBean itemBean=new NoFeedbackListItemBean();
                    itemBean.setLessonId(dataEntity.getLessonId());
                    itemBean.setStartTime(dataEntity.getStartTime());
                    itemBean.setCourseSubType(dataEntity.getCourseSubType());
                    itemBean.setCourseSubTypeName(dataEntity.getCourseSubTypeName());
                    itemBean.setCourseType(dataEntity.getCourseType());
                    itemBean.setCourseTypeName(dataEntity.getCourseTypeName());
                    itemBean.setEndTime(dataEntity.getEndTime());
                    itemBean.setStudentName(dataEntity.getStudentName());

                    result.add(itemBean);
                }
                return result;
            }
        });
    }


    class AdapterItemView extends ABaseAdapter.AbstractItemView<NoFeedbackListItemBean> {
        @ViewInject(id = R.id.course_type)
        TextView courseType;

        @ViewInject(id = R.id.main_time)
        TextView mainTime;

        @ViewInject(id = R.id.sub_time)
        TextView subTime;

        @ViewInject(id = R.id.student)
        TextView student;

        @ViewInject(id = R.id.week)
        TextView week;

        @ViewInject(id = R.id.icon)
        ImageView icon;

        @Override
        public int inflateViewId() {
            return R.layout.no_feedback_item_layout;
        }

        @Override
        public void bindingData(View convertView, NoFeedbackListItemBean data) {
            student.setText(getString(R.string.title_student)+data.getStudentName());

            courseType.setText(data.getCourseTypeName()+" "+data.getCourseSubTypeName());

            String mainTimeStr=mMainimeFormat.format(Tools.parseServerTime(data.getStartTime()));
            mainTime.setText(mainTimeStr);

            String subStartTimeStr=mSubTimeFormat.format(Tools.parseServerTime(data.getStartTime()));
            String subEndTimeStr=mSubTimeFormat.format(Tools.parseServerTime(data.getEndTime()));
            subTime.setText(subStartTimeStr+"-"+subEndTimeStr);

            String weekTimeStr=mWeekFormat.format(Tools.parseServerTime(data.getStartTime()));
            week.setText(weekTimeStr);

            icon.setImageResource(Tools.getCourseTypeIconBySubTypeName(data.getCourseSubTypeName()));
        }
    }
}
