package com.zhan.vip_teacher.ui.fragment.setting;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.zhan.framework.component.container.FragmentContainerActivity;
import com.zhan.framework.network.HttpClientUtils;
import com.zhan.framework.support.inject.ViewInject;
import com.zhan.framework.ui.fragment.ABaseFragment;
import com.zhan.framework.utils.ToastUtils;
import com.zhan.vip_teacher.R;
import com.zhan.vip_teacher.bean.FeedbackRequestBean;
import com.zhan.vip_teacher.bean.FeedbackResponseBean;
import com.zhan.vip_teacher.bean.UserInfo;
import com.zhan.vip_teacher.network.ApiUrls;
import com.zhan.vip_teacher.utils.Tools;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;

/**
 * Created by Administrator on 2016/3/29.
 */
public class FeedBackFragment extends ABaseFragment {
    private final static String TAG="FeedBackFragment";

    private final String USER_ID ="923c6bfe-bfa9-434f-91d5-63da78ab2bc7";
    private final String APP_ID ="fac0f83a-77f7-4275-ad62-4bec8f7b422b";

    @ViewInject(id = R.id.feedback_content)
    EditText mFeedback;

    @ViewInject(id = R.id.btn_feedback_finish ,click = "OnClick")
    View mSubmit;

    private RequestHandle mSubmitRequest;

    public static void launch(FragmentActivity activity) {
        FragmentContainerActivity.launch(activity, FeedBackFragment.class, null);
    }

    @Override
    protected int inflateContentView() {
        return R.layout.feedback_frag_layout;
    }

    @Override
    protected void layoutInit(LayoutInflater inflater, Bundle savedInstanceSate) {
        super.layoutInit(inflater, savedInstanceSate);
        getActivity().setTitle(getString(R.string.mine_setting_feedback_title));
    }

    @Override
    public void onDestroyView() {
        releaseRequest();
        super.onDestroyView();
    }

    void OnClick(View v){
        switch (v.getId()){
            case R.id.btn_feedback_finish:
                if(TextUtils.isEmpty(mFeedback.getText().toString())){
                    ToastUtils.toast(R.string.mine_setting_feedback_empty);
                }else{
                    submitFeedback();
                }
                break;
        }
    }

    private void submitFeedback(){
        if(isSubmitRequestProcessing()){
            return;
        }
        //Header
        Header[] headers = new Header[1];
        headers[0] = new BasicHeader("platform", "Android");

        FeedbackRequestBean requestBean=new FeedbackRequestBean();
        requestBean.setContent(mFeedback.getText().toString());
        requestBean.setAppid(APP_ID);
        requestBean.setMsgkey(String.valueOf(UserInfo.getCurrentUser().getUserID()));
        requestBean.setSubsystem("android");
        requestBean.setChecktime(getChecktime());
        requestBean.setCheckValue(getCheckValue());
        String jsonStr = JSON.toJSONString(requestBean);
        HttpEntity entity = new StringEntity(jsonStr,"utf-8");


        AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (responseBody != null) {
                    String content = new String(responseBody);
                    Log.i(TAG, "onFailure responseBody = " + content);
                }

                Log.i(TAG, "onFailure statusCode = " + statusCode);
                if (statusCode == 0) {
                    ToastUtils.toast("请求超时");
                }else{
                    ToastUtils.toast(com.zhan.framework.R.string.comm_unknow_service_error);
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String content = new String(responseBody);
                Log.i(TAG, "onSuccess statusCode = " + statusCode);
                Log.i(TAG, "onSuccess responseBody = " + content);
                if(getActivity()==null){
                    return;
                }

                FeedbackResponseBean bean = null;
                try{
                    bean = JSON.parseObject(content, FeedbackResponseBean.class);
                    if(bean!=null){
                        if(bean.getRetcode()==0){
                            getActivity().finish();
                            ToastUtils.toast(R.string.mine_setting_feedback_submit_success);
                        }else{
                            ToastUtils.toast(bean.getRetmsg());
                        }
                    }
                }catch (JSONException ex){
                    Log.e("Utils", "fromJson error : " + ex.getMessage());
                }
            }
        };

        mSubmitRequest=HttpClientUtils.post(ApiUrls.TEACHER_FEEDBACK, headers, entity, "application/json", responseHandler);
    }

    private String getCheckValue() {
        String checkValue = USER_ID + APP_ID + getChecktime();
        return Tools.md5(checkValue).toUpperCase();
    }

    private String getChecktime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        String dateStr = dateFormat.format(System.currentTimeMillis());
        return dateStr;
    }

    public boolean isSubmitRequestProcessing() {
        if (mSubmitRequest != null && !mSubmitRequest.isFinished()) {
            return true;
        }
        return false;
    }

    public void releaseRequest() {
        if (mSubmitRequest != null && !mSubmitRequest.isFinished()) {
            mSubmitRequest.cancel(true);
        }
    }

}
