package com.zhan.vip_teacher.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.zhan.framework.ui.activity.BaseActivity;
import com.zhan.vip_teacher.R;

/**
 * Created by Administrator on 2016/3/7.
 */
public class StartUpActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showActionbar(false);
        ImageView img = new ImageView(this);
        img.setImageResource(R.mipmap.startup_page);

        img.setScaleType(ImageView.ScaleType.FIT_XY);
        setContentView(img);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                // Create an Intent that will start the Main Activity.
                Intent mainIntent = new Intent(StartUpActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, 2 * 1000);
    }
}
