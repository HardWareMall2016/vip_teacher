package com.zhan.vip_teacher.ui.fragment.setting;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhan.framework.component.container.FragmentContainerActivity;
import com.zhan.framework.support.inject.ViewInject;
import com.zhan.framework.ui.fragment.ABaseFragment;
import com.zhan.vip_teacher.R;
import com.zhan.vip_teacher.bean.UserInfo;

/**
 * Created by Administrator on 2016/3/4.
 */
public class PersonFragment extends ABaseFragment{

    @ViewInject(id = R.id.person_change_psw ,click = "OnClick")
    LinearLayout mChangePsw ;
    @ViewInject(id = R.id.tv_teachername)
    TextView mTeacherName ;
    @ViewInject(id = R.id.tv_phonenumber)
    TextView mPhoneNumber ;
    @ViewInject(id = R.id.tv_teacher_qq)
    TextView mTeacherQQ;
    @ViewInject(id = R.id.tv_teacher_address)
    TextView mTeacherAddress ;

    @Override
    protected int inflateContentView() {
        return R.layout.person_frag_layout;
    }

    public static void launch(FragmentActivity activity) {
        FragmentContainerActivity.launch(activity, PersonFragment.class, null);
    }

    @Override
    protected void layoutInit(LayoutInflater inflater, Bundle savedInstanceSate) {
        super.layoutInit(inflater, savedInstanceSate);
        getActivity().setTitle(getString(R.string.mine_person));
        mTeacherName.setText(UserInfo.getCurrentUser().getTrueName());
        mPhoneNumber.setText(UserInfo.getCurrentUser().getMobile());
        mTeacherQQ.setText(UserInfo.getCurrentUser().getQq());
        mTeacherAddress.setText(UserInfo.getCurrentUser().getHomeAddr());
    }

    void OnClick(View v){
        switch (v.getId()){
            case R.id.person_change_psw:
                ChangePswFragment.launch(getActivity());
                break;
        }
    }
}
