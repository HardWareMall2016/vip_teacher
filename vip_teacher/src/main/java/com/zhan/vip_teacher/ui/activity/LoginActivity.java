package com.zhan.vip_teacher.ui.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.zhan.framework.network.HttpRequestHandler;
import com.zhan.framework.ui.activity.BaseActivity;
import com.zhan.framework.ui.widget.RippleButton;
import com.zhan.framework.utils.ToastUtils;
import com.zhan.vip_teacher.R;
import com.zhan.vip_teacher.bean.LoginRequestBean;
import com.zhan.vip_teacher.bean.UserInfo;
import com.zhan.vip_teacher.bean.UserInfoBean;
import com.zhan.vip_teacher.bean.UserInfoHelp;
import com.zhan.vip_teacher.network.ApiUrls;
import com.zhan.vip_teacher.utils.ShareUtil;
import com.zhan.vip_teacher.utils.Tools;

/**
 * Created by Administrator on 2016/3/7.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText mEdtNumber;
    private EditText mEdtPsw;
    private RippleButton mLogin;

    private String sNumberPwd;
    private String pwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showActionbar(false);
        setContentView(R.layout.activity_login);
        initView();

    }

    private void initView() {
        mEdtNumber = (EditText) findViewById(R.id.login_number);
        mEdtPsw = (EditText) findViewById(R.id.login_password);
        mLogin = (RippleButton) findViewById(R.id.btn_login);
        mLogin.setOnClickListener(this);

        String name = ShareUtil.getStringValue("username");
        if (name != null) {
            mEdtNumber.setText(name);
        }

        mEdtNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count >= 1) {
                    Drawable right = getResources().getDrawable(R.drawable.close);
                    right.setBounds(0, 0, right.getMinimumWidth(), right.getMinimumHeight());
                    mEdtNumber.setCompoundDrawables(null, null, right, null);
                    mEdtNumber.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                Drawable drawable = ((EditText) v).getCompoundDrawables()[2];
                                if (drawable == null) {
                                    return false;
                                }
                                if (event.getX() > v.getWidth() - v.getPaddingRight()
                                        - drawable.getIntrinsicWidth()) {
                                    //进入这表示图片被选中，可以处理相应的逻辑了
                                    ((EditText) v).setText("");
                                }
                            }
                            return false;
                        }
                    });
                } else {
                    mEdtNumber.setCompoundDrawables(null, null, null, null);
                    mEdtNumber.setOnTouchListener(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
//                if (Input()) {
//                    mLogin.setEnabled(true);
//                } else {
//                    mLogin.setEnabled(false);
//                }
            }
        });

        mEdtPsw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count >= 1) {
                    Drawable right = getResources().getDrawable(R.drawable.close);
                    right.setBounds(0, 0, right.getMinimumWidth(), right.getMinimumHeight());

                    mEdtPsw.setCompoundDrawables(null, null, right, null);
                    mEdtPsw.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                Drawable drawable = ((EditText) v).getCompoundDrawables()[2];
                                if (drawable == null) {
                                    return false;
                                }
                                if (event.getX() > v.getWidth() - v.getPaddingRight()
                                        - drawable.getIntrinsicWidth()) {
                                    //进入这表示图片被选中，可以处理相应的逻辑了
                                    ((EditText) v).setText("");
                                }
                            }
                            return false;
                        }
                    });
                } else {
                    mEdtPsw.setCompoundDrawables(null, null, null, null);
                    mEdtPsw.setOnTouchListener(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
//                if (Input()) {
//                    mLogin.setEnabled(true);
//                } else {
//                    mLogin.setEnabled(false);
//                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                if (!checkInput()) {
                    return;
                }
                sNumberPwd = mEdtNumber.getText().toString();
                pwd = mEdtPsw.getText().toString();
                LoginRequestBean request = new LoginRequestBean();
                request.setToken("");
                LoginRequestBean.DataEntity data = new LoginRequestBean.DataEntity();
                data.setLoginName(sNumberPwd);
                data.setPassWord(Tools.md5(pwd).substring(0, 20).toUpperCase());
                request.setData(data);

                showRotateProgressDialog(getString(R.string.login_logining), true);
                ShareUtil.setValue("username", sNumberPwd);
                startRequest(ApiUrls.TEACHER_LOGIN, request, new HttpRequestHandler() {

                    @Override
                    public void onRequestFinished(ResultCode resultCode, String result) {
                        closeRotateProgressDialog();
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
                        closeRotateProgressDialog();
                        UserInfoBean bean = Tools.parseJsonTostError(content, UserInfoBean.class);
                        if (bean != null && bean.getData() != null) {
                            UserInfo user = new UserInfo();
                            user.setToken(bean.getData().getToken());
                            user.setUserID(bean.getData().getUserID());
                            UserInfoHelp.updateUserInfo(bean, user);

                            Intent homePageIntent = new Intent(LoginActivity.this, MainActivity.class);
                            homePageIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homePageIntent);

                            finish();
                            ToastUtils.toast(R.string.login_success);
                        }
                    }
                });
                break;
        }
    }

    private boolean Input(){
        String studentPwd = mEdtNumber.getText().toString();
        if (TextUtils.isEmpty(studentPwd)) {
            return false;
        }

        String pwd = mEdtPsw.getText().toString();
        if (TextUtils.isEmpty(pwd)) {
            return false;
        }

        if (pwd.length() < 6 || pwd.length() > 16) {
            return false;
        }
        return true;
    }


    private boolean checkInput() {
        String studentPwd = mEdtNumber.getText().toString();
        if (TextUtils.isEmpty(studentPwd)) {
            ToastUtils.toast("请输入用户名");
            return false;
        }

        String pwd = mEdtPsw.getText().toString();
        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.toast("请输入密码");
            return false;
        }

        if (pwd.length() < 6 || pwd.length() > 16) {
            ToastUtils.toast(R.string.pwd_length_error);
            return false;
        }
        return true;
    }
}
