package com.zhan.vip_teacher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.zhan.vip_teacher.db.bean.PushMessage;
import com.zhan.vip_teacher.event.NewNotifyEvent;
import com.zhan.vip_teacher.receiver.bean.MessageBean;
import com.zhan.vip_teacher.ui.activity.MainActivity;
import com.zhan.vip_teacher.utils.MessageUtility;
import com.zhan.vip_teacher.utils.ShareUtil;
import com.zhan.vip_teacher.utils.Tools;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class JPushReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";

	@Override
	public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
		Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
		
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
                        
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
        	Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
        	//processCustomMessage(context, bundle);
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
			parseNotification(bundle);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
			openNotification(context,bundle);
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
        	
        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
        	boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
        	Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
        	Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
	}

	private void parseNotification(Bundle bundle) {
		MessageBean bean = parseToMessageBean(bundle);
		if(bean!=null&&MessageBean.MSG_TYPE_NOTIFY_FOR_CLASS.equals(bean.getMessageTypeCode())){

			PushMessage remindForClassMsg=new PushMessage();
			remindForClassMsg.setContent(bean.getContent());
			remindForClassMsg.setHasRead(false);
			remindForClassMsg.setMessageID(bean.getMessageID());
			remindForClassMsg.setMessageTypeCode(bean.getMessageTypeCode());
			remindForClassMsg.setMessageTypeName(bean.getMessageTypeName());
			remindForClassMsg.setTitle(bean.getTitle());
			remindForClassMsg.setSendTime(Tools.parseServerTime(bean.getCreateTime()));
			remindForClassMsg.setReceiveTime(System.currentTimeMillis());

			MessageUtility.insertMessage(remindForClassMsg);

			EventBus.getDefault().post(new NewNotifyEvent());

		}
	}

	private void openNotification(Context context,Bundle bundle) {
		MessageBean bean = parseToMessageBean(bundle);
		if(bean!=null&&MessageBean.MSG_TYPE_NOTIFY_FOR_CLASS.equals(bean.getMessageTypeCode())){
			Intent homePageIntent = new Intent(context, MainActivity.class);
			homePageIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
			context.startActivity(homePageIntent);
		}
	}

	private MessageBean parseToMessageBean(Bundle bundle){
		if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
			Log.i(TAG, "This message has no Extra data");
		}
		String json=bundle.getString(JPushInterface.EXTRA_EXTRA);
		Log.i(TAG, "Notification EXTRA_EXTRA : " +json);
		MessageBean bean = null;
		try{
			bean = JSON.parseObject(json, MessageBean.class);
		}catch (com.alibaba.fastjson.JSONException ex){
			Log.e("Utils", "fromJson error : " + ex.getMessage());
		}
		return bean;
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
					Log.i(TAG, "This message has no Extra data");
					continue;
				}
				Log.i(TAG, "EXTRA_EXTRA : "+bundle.getString(JPushInterface.EXTRA_EXTRA));
				try {
					JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
					Iterator<String> it =  json.keys();

					while (it.hasNext()) {
						String myKey = it.next().toString();
						sb.append("\nkey:" + key + ", value: [" +
								myKey + " - " +json.optString(myKey) + "]");
					}
				} catch (JSONException e) {
					Log.e(TAG, "Get message extra JSON error!");
				}

			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}
}
