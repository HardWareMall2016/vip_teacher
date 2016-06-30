package com.zhan.vip_teacher.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.zhan.framework.ui.activity.ActionBarActivity;

/**
 * Created by WuYue on 2016/5/13.
 */
public class OperatorGuideManager {
    private ActionBarActivity mActivity;
    private View mGuideView;

    public OperatorGuideManager(ActionBarActivity activity) {
        mActivity = activity;
    }

    /***
     * 显示引导窗口，点击内容自动
     * @param guideView
     */
    /*public void showGuideWindow(View guideView) {
        showGuideWindow(guideView, true);
    }*/

    //img.setScaleType(ImageView.ScaleType.FIT_XY);
    public void showGuideWindow(View guideView) {
        //先初始化状态
        hideGuideWindow();

        mGuideView = guideView;
        // 设置了引导图片
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mGuideView.setLayoutParams(params);

        mActivity.getRootView().addView(mGuideView);// 添加引导

        /*if(clickToHide){
            // 点击图层之后，将图层移除
            mGuideView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    hideGuideWindow();
                }
            });
        }*/
    }

    public void hideGuideWindow(){
        if(mGuideView!=null){
            mActivity.getRootView().removeView(mGuideView);
            mGuideView=null;
        }
    }
}
