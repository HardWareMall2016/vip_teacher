package com.zhan.vip_teacher.utils;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.zhan.framework.network.HttpClientUtils;
import com.zhan.vip_teacher.bean.SignEventRequestBean;
import com.zhan.vip_teacher.bean.UserInfo;
import com.zhan.vip_teacher.network.ApiUrls;

import java.util.Calendar;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;

/**
 * Created by WuYue on 2016/4/20.
 */
public class SendEventUtil {

    public static void sendEvent(int eventId, String eventName, String content) {

        if (UserInfo.getCurrentUser() == null || !UserInfo.getCurrentUser().isLogined()) {
            return;
        }

        try {
            Calendar calendar = Calendar.getInstance();

            if(!TextUtils.isEmpty(content)){
                content=content.replace("\r\n","");
            }

            SignEventRequestBean requestBean = new SignEventRequestBean();
            requestBean.setAppid("fac0f83a77f74275ad624bec8f7b422b");
            requestBean.setModelId("8");
            requestBean.setYear(String.valueOf(calendar.get(Calendar.YEAR)));
            requestBean.setEventTypeId(String.valueOf(eventId));
            requestBean.setEvent(eventName);
            requestBean.setContent(content);
            requestBean.setPlatform("Android");

            String jsonStr = JSON.toJSONString(requestBean);
            Log.i("SendEvent", jsonStr);
            HttpEntity entity = new StringEntity(jsonStr, "utf-8");

            Header[] headers = new Header[1];
            headers[0] = new BasicHeader("platform", "Android");
            HttpClientUtils.post(ApiUrls.ADD_EVENT, headers, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int code, Header[] headers, byte[] bytes) {
                    String content = new String(bytes);
                    Log.i("SendEvent", "onSuccess statusCode = " + code);
                    Log.i("SendEvent", "onSuccess responseBody = " + content);
                }

                @Override
                public void onFailure(int code, Header[] headers, byte[] bytes, Throwable throwable) {
                    if (bytes != null) {
                        String content = new String(bytes);
                        Log.i("SendEvent", "onFailure responseBody = " + content);
                    }
                    Log.i("SendEvent", "onFailure statusCode = " + code);
                }
            });

        } catch (Exception exception) {
            Log.e("SendEvent", "SendEvent Error = " + exception.getMessage());
        }
    }
}
