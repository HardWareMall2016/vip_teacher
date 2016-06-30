package com.zhan.vip_teacher.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import com.zhan.framework.ui.activity.BaseActivity;
import com.zhan.framework.ui.fragment.ARefreshFragment;
import com.zhan.framework.utils.Logger;
import com.zhan.vip_teacher.R;
import com.zhan.vip_teacher.bean.FirstEvent;
import com.zhan.vip_teacher.ui.fragment.mine.MySignatureFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2016/3/17.
 */
public class MySignatureActivity extends BaseActivity implements ViewPager.OnPageChangeListener {


    public final static int VIEW_PAGE_SIZE = 200;
    private ViewPager mViewPager;
    private ViewPageFragAdapter mAdapter;
    private int mCurrentPosition = VIEW_PAGE_SIZE - 1;//我的签到 不能向右滑，只能左滑，一进去显示的是当月
    private final SparseArray<MySignatureFragment> fragments = new SparseArray<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        showActionbarUnderline(false);
        setContentView(R.layout.activity_mylsignature);
        setTitle(getString(R.string.mine_signature));

        mViewPager = (ViewPager) findViewById(R.id.my_signature_viewpager);
        mAdapter = new ViewPageFragAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setCurrentItem(VIEW_PAGE_SIZE -1);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void helloEventBus(FirstEvent event){
        if("left".equals(event.getMsg())){
            mViewPager.setCurrentItem(mViewPager.getCurrentItem()-1);
        }else{
            mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mHandler.removeCallbacks(refreshFragmentRunnable);
        mHandler.removeCallbacks(releaseFragmentRunnable);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        mCurrentPosition = position;
        // 释放资源
        mHandler.removeCallbacks(releaseFragmentRunnable);
        mHandler.postDelayed(releaseFragmentRunnable, Math.round(2.0f * 1000));
        // 刷新当前显示
        mHandler.removeCallbacks(refreshFragmentRunnable);
        mHandler.postDelayed(refreshFragmentRunnable, 700);
    }


    Handler mHandler = new Handler() {

    };

    Runnable refreshFragmentRunnable = new Runnable() {
        @Override
        public void run() {
            MySignatureFragment fragment = fragments.get(mCurrentPosition);
            if (fragment != null) {
                fragment.requestData(ARefreshFragment.RefreshMode.reset);
            }
        }
    };

    Runnable releaseFragmentRunnable = new Runnable() {
        @Override
        public void run() {
            Log.i("MySignatureActivity", String.format("准备释放第%d个fragment的资源", mCurrentPosition + 1));
            Log.i("MySignatureActivity", String.format("准备释放第%d个fragment的资源", mCurrentPosition - 1));
            releaseFragment(mCurrentPosition + 1);
            releaseFragment(mCurrentPosition - 1);
        }
    };

    public void releaseFragment(int position) {
        if (position < VIEW_PAGE_SIZE && position >= 0) {
            MySignatureFragment fragment = fragments.get(position);
            if (fragment != null) {
                Log.i("MySignatureActivity", String.format("释放第%d个fragment的资源", position));
                fragment.releaseAllRequest();
            } else {
                Log.i("MySignatureActivity", String.format("释放的第%d个fragment不存在", position));
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    //我的签到 不能向右滑，只能左滑，一进去显示的是当月
    class ViewPageFragAdapter extends FragmentPagerAdapter {
        public ViewPageFragAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            MySignatureFragment fragment = fragments.get(position);
            if (fragment == null) {
                //计算每页对应的日期
                int todayPosition = VIEW_PAGE_SIZE -1;
                int offset = position - todayPosition;
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                calendar.add(Calendar.MONTH, offset);
                Date date = calendar.getTime();

                fragment = MySignatureFragment.newInstance(date.getTime());
                fragments.put(position, fragment);
            }

            return fragment;
        }

        @Override
        public int getCount() {
            return VIEW_PAGE_SIZE;
        }
    }
}
