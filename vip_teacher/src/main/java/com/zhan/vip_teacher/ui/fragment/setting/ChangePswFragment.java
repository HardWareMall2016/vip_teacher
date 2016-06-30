package com.zhan.vip_teacher.ui.fragment.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.zhan.framework.component.container.FragmentContainerActivity;
import com.zhan.framework.network.HttpRequestHandler;
import com.zhan.framework.support.inject.ViewInject;
import com.zhan.framework.ui.fragment.ABaseFragment;
import com.zhan.framework.ui.widget.RippleButton;
import com.zhan.framework.utils.ToastUtils;
import com.zhan.vip_teacher.R;
import com.zhan.vip_teacher.bean.ModifyCodeBean;
import com.zhan.vip_teacher.bean.ModifyCodeRequestBean;
import com.zhan.vip_teacher.bean.UserInfo;
import com.zhan.vip_teacher.network.ApiUrls;
import com.zhan.vip_teacher.ui.activity.MainActivity;
import com.zhan.vip_teacher.utils.Tools;

/**
 * Created by Administrator on 2016/3/4.
 */
public class ChangePswFragment extends ABaseFragment {

    @ViewInject(id = R.id.btn_modify_code, click = "OnClick")
    RippleButton mFinish;
    @ViewInject(id = R.id.modify_current_code)
    EditText mCurrentCode;
    @ViewInject(id = R.id.modify_new_password)
    EditText mNewCode;
    @ViewInject(id = R.id.modify_again_password)
    EditText mAgainCode;

    private String oldPass;
    private String mStrPassword;

    @Override
    protected int inflateContentView() {
        return R.layout.changpsw_frag_layout;
    }

    public static void launch(FragmentActivity activity) {
        FragmentContainerActivity.launch(activity, ChangePswFragment.class, null);
    }

    @Override
    protected void layoutInit(LayoutInflater inflater, Bundle savedInstanceSate) {
        super.layoutInit(inflater, savedInstanceSate);
        getActivity().setTitle(getString(R.string.mine_person_change_password));
        mCurrentCode.addTextChangedListener(watcher);
        mNewCode.addTextChangedListener(watcher);
        mAgainCode.addTextChangedListener(watcher);
    }

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (check()) {
                mFinish.setEnabled(true);
            } else {
                mFinish.setEnabled(false);
            }
        }
    };

    void OnClick(View v) {
        switch (v.getId()) {
            case R.id.btn_modify_code:
                if (checkPassword()) {
                    changePwd();
                }
                break;
        }
    }

    private void changePwd() {
        ModifyCodeRequestBean request = new ModifyCodeRequestBean();
        ModifyCodeRequestBean.DataEntity data = new ModifyCodeRequestBean.DataEntity();
        data.setOldPassWord(Tools.md5(oldPass).substring(0, 20).toUpperCase());
        data.setNewPassWord(Tools.md5(mStrPassword).substring(0, 20).toUpperCase());
        data.setUserID(UserInfo.getCurrentUser().getUserID());
        request.setData(data);

        //showRotateProgressDialog(getString(R.string.chang_password), true);
        startRequest(ApiUrls.TEACHER_UPDATETEACHERPASSWORG, request, new HttpRequestHandler() {
            @Override
            public void onRequestFinished(ResultCode resultCode, String result) {
                //closeRotateProgressDialog();
            }

            @Override
            public void onRequestFailed(String errorMsg) {
                super.onRequestFailed(errorMsg);
                ToastUtils.toast(errorMsg);
            }

            @Override
            public void onRequestFailedNoNetwork() {
                super.onRequestFailedNoNetwork();
                ToastUtils.toast(R.string.network_disconnect);
            }

            @Override
            public void onTimeout() {
                super.onTimeout();
                ToastUtils.toast(R.string.network_timeout);
            }

            @Override
            public void onRequestSucceeded(String content) {
                super.onRequestSucceeded(content);
                //closeRotateProgressDialog();
                ModifyCodeBean bean = Tools.parseJsonTostError(content, ModifyCodeBean.class);
                if (bean != null) {
                    ToastUtils.toast(R.string.chang_password_finish);
                    UserInfo.logout();
                    Intent homePageIntent = new Intent(getActivity(), MainActivity.class);
                    homePageIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(homePageIntent);
                }
            }
        });
    }

    private boolean check(){
        oldPass = mCurrentCode.getText().toString().trim();
        mStrPassword = mNewCode.getText().toString();

        String rePwd = mAgainCode.getText().toString();

        if (TextUtils.isEmpty(mStrPassword)) {
            return false;
        }

        if (TextUtils.isEmpty(rePwd)) {
            return false;
        }

        if (mStrPassword.length() < 6 || mStrPassword.length() > 16) {
            return false;
        }
        if (TextUtils.isEmpty(oldPass)) {
            return false;
        }

        if (oldPass.length() < 6 || oldPass.length() > 16) {
            return false;
        }

        if (!mStrPassword.equals(rePwd)) {
            return false;
        }
        return true;
    }

    private boolean checkPassword() {
        oldPass = mCurrentCode.getText().toString().trim();
        mStrPassword = mNewCode.getText().toString();

        String rePwd = mAgainCode.getText().toString();

        if (TextUtils.isEmpty(mStrPassword)) {
            ToastUtils.toast(R.string.pwd_empty);
            return false;
        }

        if (TextUtils.isEmpty(rePwd)) {
            ToastUtils.toast(R.string.reinput_pwd);
            return false;
        }

        if (mStrPassword.length() < 6 || mStrPassword.length() > 16) {
            ToastUtils.toast(R.string.pwd_length_error);
            return false;
        }
        if (TextUtils.isEmpty(oldPass)) {
            ToastUtils.toast(R.string.reinput_pwd);
            return false;
        }

        if (oldPass.length() < 6 || oldPass.length() > 16) {
            ToastUtils.toast(R.string.pwd_length_error);
            return false;
        }

        if (!mStrPassword.equals(rePwd)) {
            ToastUtils.toast(R.string.re_pwd_not_equal);
            return false;
        }
        return true;
    }


}
