package com.zhan.vip_teacher.ui.fragment.message;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhan.framework.support.inject.ViewInject;
import com.zhan.framework.ui.fragment.ABaseFragment;
import com.zhan.vip_teacher.R;
import com.zhan.vip_teacher.base.Constants;
import com.zhan.vip_teacher.db.bean.LocalMessage;
import com.zhan.vip_teacher.db.bean.PushMessage;
import com.zhan.vip_teacher.event.NewNotifyEvent;
import com.zhan.vip_teacher.utils.MessageUtility;
import com.zhan.vip_teacher.utils.ShareUtil;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2016/3/1.
 */
public class MessageFragment extends ABaseFragment implements View.OnClickListener{
    @ViewInject(id = R.id.msg_content)
    private LinearLayout mMessageContent;

    @ViewInject(id = R.id.empty_msg)
    private ImageView mImgEmpty;

    private final SimpleDateFormat mFullDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat mShotDateFormat = new SimpleDateFormat("MM-dd");
    private final SimpleDateFormat mTimeFormat = new SimpleDateFormat("HH:mm");

    private class ViewHolder{
        String messageType;
        View redDot;
        Object message;
    }

    @Override
    protected int inflateContentView() {
        return R.layout.message_frag_layout;
    }

    @Override
    protected void layoutInit(LayoutInflater inflater, Bundle savedInstanceSate) {
        super.layoutInit(inflater, savedInstanceSate);
        mMessageContent.removeAllViews();

        //上课提醒,来自推送
        PushMessage remindForClassMessage = MessageUtility.findLatestMessage(PushMessage.class, Constants.MSG_TYPE_REMIND_FOR_CLASS);
        populatePushMsgView(inflater, remindForClassMessage);

        //上课提醒,来自本地闹钟
        LocalMessage remindForSign=MessageUtility.findLatestMessage(LocalMessage.class,Constants.MSG_TYPE_REMIND_FOR_SIGN);
        populatePushMsgView(inflater, remindForSign);

        //签到成功提醒
        LocalMessage remindForSignSuccess=MessageUtility.findLatestMessage(LocalMessage.class,Constants.MSG_TYPE_REMIND_SUCCESS_SIGN);
        populatePushMsgView(inflater, remindForSignSuccess);

        checkShowEmptyView();
    }

    private void populatePushMsgView(LayoutInflater inflater, PushMessage pushMessage) {
        if(pushMessage==null){
            return;
        }
        View child = inflater.inflate(R.layout.message_frag_list_item, null);
        TextView tvTitle = (TextView) child.findViewById(R.id.title);
        TextView tvTime = (TextView) child.findViewById(R.id.time);
        View viewRead = child.findViewById(R.id.icon_read);
        TextView tvSummary = (TextView) child.findViewById(R.id.summary);

        tvTitle.setText(pushMessage.getTitle());
        tvTime.setText(getTimeStr(pushMessage.getSendTime()));

        if(pushMessage.isHasRead()){
            viewRead.setVisibility(View.INVISIBLE);
        }else{
            viewRead.setVisibility(View.VISIBLE);
        }

        tvSummary.setText(pushMessage.getContent());

        child.setOnClickListener(this);

        ViewHolder holder=new ViewHolder();
        holder.messageType=pushMessage.getMessageTypeCode();
        holder.redDot=viewRead;
        holder.message=pushMessage;
        child.setTag(holder);

        mMessageContent.addView(child);
    }

    private String getTimeStr(long time){
        //是否是当天
        String curDateStr=getFormatTimeStr(mFullDateFormat,System.currentTimeMillis());
        String timeStr=getFormatTimeStr(mFullDateFormat,time);

        //当天
        if(curDateStr.equals(timeStr)){
            return getFormatTimeStr(mTimeFormat,time);
        }else{
            return getFormatTimeStr(mShotDateFormat,time);
        }
    }

    private String getFormatTimeStr(SimpleDateFormat format,long time){
        String timeStr = format.format(time);
        return timeStr;
    }

    private void checkShowEmptyView(){
        if(mMessageContent.getChildCount()>0){
            mImgEmpty.setVisibility(View.GONE);
        }else{
            mImgEmpty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        ViewHolder viewHolder=(ViewHolder)view.getTag();
        if(Constants.MSG_TYPE_REMIND_FOR_CLASS.equals(viewHolder.messageType)){
            PushMessage pushMessage=(PushMessage)viewHolder.message;
            if(!pushMessage.isHasRead()){
                pushMessage.setHasRead(true);
                MessageUtility.updateMessage(pushMessage);
                viewHolder.redDot.setVisibility(View.INVISIBLE);
                EventBus.getDefault().post(new NewNotifyEvent());
            }
            MessageDetailsFragment.launch(getActivity(),Constants.MSG_TYPE_REMIND_FOR_CLASS);
        }else if(Constants.MSG_TYPE_REMIND_FOR_SIGN.equals(viewHolder.messageType)){
            LocalMessage pushMessage=(LocalMessage)viewHolder.message;
            if(!pushMessage.isHasRead()){
                pushMessage.setHasRead(true);
                MessageUtility.updateMessage(pushMessage);
                viewHolder.redDot.setVisibility(View.INVISIBLE);
                EventBus.getDefault().post(new NewNotifyEvent());
            }
            MessageDetailsFragment.launch(getActivity(),Constants.MSG_TYPE_REMIND_FOR_SIGN);
        }else if(Constants.MSG_TYPE_REMIND_SUCCESS_SIGN.equals(viewHolder.messageType)){
            LocalMessage pushMessage=(LocalMessage)viewHolder.message;
            if(!pushMessage.isHasRead()){
                pushMessage.setHasRead(true);
                MessageUtility.updateMessage(pushMessage);
                viewHolder.redDot.setVisibility(View.INVISIBLE);
                EventBus.getDefault().post(new NewNotifyEvent());
            }
            MessageDetailsFragment.launch(getActivity(),Constants.MSG_TYPE_REMIND_SUCCESS_SIGN);
        }
    }
}
