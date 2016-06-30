package com.zhan.vip_teacher.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/4/7.
 */
public class LodingImageView extends ImageView {

    public LodingImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        ((AnimationDrawable)getDrawable()).start();
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        ((AnimationDrawable)getDrawable()).stop();
        super.onDetachedFromWindow();
    }
}
