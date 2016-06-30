package com.zhan.vip_teacher.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zhan.vip_teacher.R;
import com.zhan.vip_teacher.base.Constants;
import com.zhan.vip_teacher.db.bean.DailyLesson;
import com.zhan.vip_teacher.db.bean.LocalMessage;
import com.zhan.vip_teacher.event.NewNotifyEvent;
import com.zhan.vip_teacher.ui.activity.MainActivity;
import com.zhan.vip_teacher.utils.CacheUtility;
import com.zhan.vip_teacher.utils.MessageUtility;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;


/**
 * Created by WuYue on 2016/5/13.
 */
public class VipReceiver extends BroadcastReceiver {
    private static final String TAG = "VipReceiver";

    public final static String ACTION_SIGN_REMIND = "com.zhan.SIGN_REMIND";

    public final static String INTENT_KEY_LESSON_ID = "lesson_id";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction());
        Log.d(TAG, "[MyReceiver] Ext data - " + intent.getLongExtra(INTENT_KEY_LESSON_ID, 0));
        if (ACTION_SIGN_REMIND.equals(intent.getAction())) {
            recordAndNotifyMsg(context,intent.getLongExtra(INTENT_KEY_LESSON_ID, 0));
        }
    }

    private void recordAndNotifyMsg(Context context,long lessonId) {
        int messageId;
        LocalMessage latestMsg = MessageUtility.findLatestMessage(LocalMessage.class);
        if (latestMsg == null) {
            messageId = 1;
        } else {
            messageId = Integer.valueOf(latestMsg.getMessageID()) + 1;
        }

        DailyLesson lesson = CacheUtility.findData(DailyLesson.class, lessonId);
        if (lesson == null) {
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("今天(MM月dd日),HH:mm");

        String msgFormat = "%s %s-%s，记得上课哟！";//"今天(05月13日)，18:00 托福-阅读 记得上课哟！"
        String msgContent = String.format(msgFormat, dateFormat.format(lesson.getStartTime()), lesson.CourseTypeName, lesson.CourseSubTypeName);

        Log.d(TAG, "[MyReceiver] msgContent - " + msgContent);

        LocalMessage remindForClassMsg = new LocalMessage();
        remindForClassMsg.setContent(msgContent);
        remindForClassMsg.setHasRead(false);
        remindForClassMsg.setMessageID(String.valueOf(messageId));
        remindForClassMsg.setMessageTypeCode(Constants.MSG_TYPE_REMIND_FOR_SIGN);
        remindForClassMsg.setMessageTypeName("签到提醒");
        remindForClassMsg.setTitle("签到提醒");
        remindForClassMsg.setSendTime(System.currentTimeMillis());
        remindForClassMsg.setReceiveTime(System.currentTimeMillis());
        MessageUtility.insertMessage(remindForClassMsg);

        showNotification(context, "签到提醒", msgContent, messageId);

        EventBus.getDefault().post(new NewNotifyEvent());
    }


    public void showNotification(Context context, String contentTitle, String contentText, int notifyId) {

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder= new Notification.Builder(context)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setSmallIcon(R.mipmap.logo)
                .setContentIntent(contentIntent);

        Notification notification;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            notification = builder.build();
        }else{
            notification=builder.getNotification();
        }

        notification.defaults=Notification.DEFAULT_VIBRATE|Notification.DEFAULT_SOUND;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(notifyId, notification);
    }
}
