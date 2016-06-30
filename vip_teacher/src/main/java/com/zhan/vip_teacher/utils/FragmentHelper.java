package com.zhan.vip_teacher.utils;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.umeng.analytics.MobclickAgent;
import com.zhan.framework.ui.fragment.ABaseFragment;
import com.zhan.framework.ui.fragment.BaseFragmentHelper;
import com.zhan.vip_teacher.R;

/**
 * Created by WuYue on 2016/3/30.
 */
public class FragmentHelper extends BaseFragmentHelper {
    private final String TAG="FragmentHelper";
    private Activity mActivity;
    private String mPageName;
    @Override
    protected void bindFragment(ABaseFragment fragment) {
        super.bindFragment(fragment);
        Log.i(TAG,"bindFragment : "+fragment);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity=activity;
        Log.i(TAG, "onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
    }

    @Override
    public void onCreateView(View rootView, Bundle savedInstanceState) {
        super.onCreateView(rootView, savedInstanceState);
        Log.i(TAG, "onCreateView");
    }

    @Override
    public void onResume() {
        super.onResume();
        mPageName= (String) mActivity.getTitle();
        String defTitle=mActivity.getString(R.string.app_name);
        if(TextUtils.isEmpty(mPageName)||defTitle.equals(mPageName)){
            mPageName=getFragment().getClass().getSimpleName();
        }
        Log.i(TAG, "PageName = "+mPageName);
        MobclickAgent.onPageStart(mPageName); //统计页面，"MainScreen"为页面名称，可自定义
        //统计时长
        MobclickAgent.onResume(mActivity);
        Log.i(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
        //统计时长
        MobclickAgent.onPause(mActivity);
        Log.i(TAG, "onPause");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "onDestroyView");
    }
}
