package com.zhan.vip_teacher.ui.fragment.mine;


import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhan.framework.network.HttpRequestHandler;
import com.zhan.framework.support.inject.ViewInject;
import com.zhan.framework.ui.fragment.ABaseFragment;
import com.zhan.vip_teacher.R;
import com.zhan.vip_teacher.bean.MineBean;
import com.zhan.vip_teacher.bean.MineRequestBean;
import com.zhan.vip_teacher.bean.TeacherInfoRequestBean;
import com.zhan.vip_teacher.bean.TeacherInfoResponseBean;
import com.zhan.vip_teacher.bean.UserInfo;
import com.zhan.vip_teacher.bean.UserInfoHelp;
import com.zhan.vip_teacher.db.bean.MineResultBean;
import com.zhan.vip_teacher.network.ApiUrls;
import com.zhan.vip_teacher.ui.activity.MyLessonActivity;
import com.zhan.vip_teacher.ui.activity.MySignatureActivity;
import com.zhan.vip_teacher.ui.fragment.setting.SettingFragment;
import com.zhan.vip_teacher.utils.CacheUtility;
import com.zhan.vip_teacher.utils.Tools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by Administrator on 2016/3/1.
 */
public class MineFragment extends ABaseFragment {

    @ViewInject(id = R.id.mine_lesson, click = "OnClick")
    View mMineLesson;
    @ViewInject(id = R.id.mine_signature, click = "OnClick")
    View mMineSignature;
    @ViewInject(id = R.id.mine_setting, click = "OnClick")
    View mMineSetting;
    @ViewInject(id = R.id.mine_lessoned)
    TextView mLessonedCount;
    @ViewInject(id = R.id.mine_no_singin)
    TextView mNoSignInCount;
    @ViewInject(id = R.id.round1)
    RelativeLayout roundView1;
    @ViewInject(id = R.id.round2)
    RelativeLayout roundView2;

    private Long date_date;
    private String startTime = null;
    private String endTime = null;

    private MineRequestBean mRequestBean;

    @Override
    protected int inflateContentView() {
        return R.layout.mine_frag_layout;
    }

    @Override
    protected void layoutInit(LayoutInflater inflater, Bundle savedInstanceSate) {
        super.layoutInit(inflater, savedInstanceSate);

        ImageView itemImage_mylesson = getItemImage(mMineLesson);
        itemImage_mylesson.setImageResource(R.drawable.mine_mylesson_bg);


        TextView itemTitle = getItemTitle(mMineLesson);
        itemTitle.setText(getString(R.string.mine_lesson));

        TextView itemTitles = getItemTitle(mMineSignature);
        itemTitles.setText(getString(R.string.mine_signature));

        ImageView itemImage_mysignature = getItemImages(mMineSignature);
        itemImage_mysignature.setImageResource(R.drawable.mine_mysignature_bg);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        Date date = calendar.getTime();

        date_date = date.getTime();

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(date_date);
        calendar2.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        calendar2.set(Calendar.HOUR_OF_DAY, 0);
        calendar2.set(Calendar.MINUTE, 0);
        calendar2.set(Calendar.SECOND, 0);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        startTime = dateFormat.format(calendar2.getTime());
        calendar2.set(Calendar.DAY_OF_MONTH, calendar2.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar2.set(Calendar.HOUR_OF_DAY, 23);
        calendar2.set(Calendar.MINUTE, 59);
        calendar2.set(Calendar.SECOND, 59);
        endTime = dateFormat.format(calendar2.getTime());

        RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) roundView1.getLayoutParams();
        RelativeLayout.LayoutParams lp2 = (RelativeLayout.LayoutParams) roundView2.getLayoutParams();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels / 2 - 60;
        int screenHeight = screenWidth;
        lp1.width = screenWidth;
        lp1.height = screenHeight;
        lp1.setMargins(30, 40, 30, 60);
        lp2.width = screenWidth;
        lp2.height = screenHeight;
        lp2.setMargins(30, 40, 30, 60);
        roundView1.setLayoutParams(lp1);
        roundView2.setLayoutParams(lp2);
    }

    @Override
    public void requestData() {
        mRequestBean = getMineRequestBean();
        startRequest(ApiUrls.GET_NOTSIGNIN_AND_LESSONCOUNT, mRequestBean, new BaseHttpRequestTask<MineBean>() {
            @Override
            public MineBean parseResponseToResult(String content) {
                return Tools.parseJsonTostError(content, MineBean.class);
            }

            @Override
            public void beforeOnPrepare() {
                List<MineResultBean> cacheDatas=CacheUtility.findCacheData(ApiUrls.GET_NOTSIGNIN_AND_LESSONCOUNT, mRequestBean, MineResultBean.class);
                if(cacheDatas==null||cacheDatas.size()==0){
                    showRotateProgressDialog(getString(R.string.loading), true);
                }else {
                    MineResultBean bean=cacheDatas.get(0);
                    populateView(bean);
                }
            }

            @Override
            protected void onSuccess(MineBean responseBean) {
                super.onSuccess(responseBean);
                if(getActivity()==null){
                    return;
                }
                closeRotateProgressDialog();
                if (responseBean != null) {
                    MineResultBean bean=new MineResultBean();
                    bean.setLessonCount(responseBean.getData().getLessonCount());
                    bean.setNotSigninCount(responseBean.getData().getNotSigninCount());
                    List<MineResultBean> beanList=new ArrayList<>();
                    beanList.add(bean);

                    CacheUtility.addCacheDataList(ApiUrls.GET_NOTSIGNIN_AND_LESSONCOUNT, mRequestBean, beanList, MineResultBean.class);

                    populateView(bean);
                }
            }
        });
    }

    private MineRequestBean getMineRequestBean() {
        MineRequestBean request = new MineRequestBean();
        MineRequestBean.DataEntity dataEntity = new MineRequestBean.DataEntity();
        dataEntity.setUserID(UserInfo.getCurrentUser().getUserID());
        dataEntity.setQueryDate(startTime);
        dataEntity.setEndDate(endTime);
        request.setData(dataEntity);
        return request;
    }

    private void populateView(MineResultBean bean){
        mLessonedCount.setText(bean.getLessonCount() + "");
        if(bean.getNotSigninCount() < 0){
            mNoSignInCount.setText("0");
        }else{
            mNoSignInCount.setText(bean.getNotSigninCount() + "");
        }
    }

    private ImageView getItemImage(View mMineLesson) {
        return (ImageView) mMineLesson.findViewById(R.id.left_img);
    }


    private TextView getItemTitle(View mMineLesson) {
        return (TextView) mMineLesson.findViewById(R.id.title);
    }

    private ImageView getItemImages(View mMineSignature) {
        return (ImageView) mMineSignature.findViewById(R.id.left_img);
    }

    void OnClick(View view) {
        switch (view.getId()) {
            case R.id.mine_lesson:
                startActivity(new Intent(getActivity(), MyLessonActivity.class));
                break;
            case R.id.mine_signature:
                startActivity(new Intent(getActivity(), MySignatureActivity.class));
                break;
            case R.id.mine_setting:
                update();
                SettingFragment.launch(getActivity());
                break;
        }
    }

    private void update() {
        TeacherInfoRequestBean requestBean = new TeacherInfoRequestBean();
        requestBean.setData(UserInfo.getCurrentUser().getUserID());
        startRequest(ApiUrls.TEACHER_INFO, requestBean, new HttpRequestHandler() {
            @Override
            public void onRequestSucceeded(String content) {
                super.onRequestSucceeded(content);
                TeacherInfoResponseBean bean = Tools.parseJson(content, TeacherInfoResponseBean.class);
                if (bean.getData() != null && bean.getCode() == 0) {
                    UserInfoHelp.updateTeacherInfo(bean);
                }
            }
        });
    }

}
