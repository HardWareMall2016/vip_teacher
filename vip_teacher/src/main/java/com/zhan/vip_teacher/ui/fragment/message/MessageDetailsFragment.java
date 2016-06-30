package com.zhan.vip_teacher.ui.fragment.message;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zhan.framework.component.container.FragmentArgs;
import com.zhan.framework.component.container.FragmentContainerActivity;
import com.zhan.framework.support.inject.ViewInject;
import com.zhan.framework.ui.fragment.ABaseFragment;
import com.zhan.vip_teacher.R;
import com.zhan.vip_teacher.base.Constants;
import com.zhan.vip_teacher.db.bean.LocalMessage;
import com.zhan.vip_teacher.db.bean.PushMessage;
import com.zhan.vip_teacher.utils.MessageUtility;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by WuYue on 2016/3/29.
 */
public class MessageDetailsFragment extends ABaseFragment {
    private final static String ARG_KEY = "MessageType";
    private String mMessageType;

    @ViewInject(id = R.id.message_list)
    private ListView mListView;

    private List<PushMessage> mPushMessage;

    private LayoutInflater mInflater;

    public static void launch(Activity from, String lessonId) {
        FragmentArgs args = new FragmentArgs();
        args.add(ARG_KEY, lessonId);
        FragmentContainerActivity.launch(from, MessageDetailsFragment.class, args);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMessageType = savedInstanceState == null ? (String) getArguments().getSerializable(ARG_KEY)
                : (String) savedInstanceState.getSerializable(ARG_KEY);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ARG_KEY, mMessageType);
    }

    @Override
    protected int inflateContentView() {
        return R.layout.fragment_message_details;
    }

    @Override
    protected void layoutInit(LayoutInflater inflater, Bundle savedInstanceSate) {
        super.layoutInit(inflater, savedInstanceSate);
        getActivity().setTitle(getString(R.string.message_center));
        if(Constants.MSG_TYPE_REMIND_FOR_CLASS.equals(mMessageType)){
            mPushMessage=MessageUtility.findMessages(PushMessage.class,mMessageType);
        }else{
            List<LocalMessage> localMessageList=MessageUtility.findMessages(LocalMessage.class,mMessageType);
            mPushMessage=new LinkedList<>();
            for(LocalMessage message:localMessageList){
                mPushMessage.add(message);
            }
        }
        mInflater=inflater;
        mListView.setAdapter(new MessageAdapter());
    }

    private class ViewHolder {
        ImageView icon;
        TextView content;
        TextView time;
    }

    private class MessageAdapter extends BaseAdapter {

        final SimpleDateFormat mFullDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        @Override
        public int getCount() {
            return mPushMessage==null?0:mPushMessage.size();
        }

        @Override
        public Object getItem(int i) {
            return mPushMessage.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if(view==null) {
                holder = new ViewHolder();
                view = mInflater.inflate(R.layout.message_details_list_item, null);
                holder.icon=(ImageView)view.findViewById(R.id.icon);
                holder.content=(TextView)view.findViewById(R.id.content);
                holder.time=(TextView)view.findViewById(R.id.time);

                if(Constants.MSG_TYPE_REMIND_FOR_CLASS.equals(mMessageType))
                    holder.icon.setImageResource(R.drawable.icon_msg_remind);

                view.setTag(holder);
            }else{
                holder=(ViewHolder)view.getTag();
            }

            PushMessage msg= mPushMessage.get(i);

            holder.content.setText(msg.getContent());
            holder.time.setText(mFullDateFormat.format(msg.getSendTime()));

            return view;
        }
    }
}
