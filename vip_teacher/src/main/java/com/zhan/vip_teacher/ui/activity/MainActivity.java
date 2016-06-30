package com.zhan.vip_teacher.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.melnykov.fab.FloatingActionButton;
import com.umeng.analytics.MobclickAgent;
import com.zhan.framework.network.Connectivity;
import com.zhan.framework.network.HttpClientUtils;
import com.zhan.framework.ui.activity.ActionBarActivity;
import com.zhan.framework.ui.activity.BaseActivity;
import com.zhan.framework.ui.fragment.ABaseFragment;
import com.zhan.framework.utils.PixelUtils;
import com.zhan.framework.utils.ToastUtils;
import com.zhan.vip_teacher.R;
import com.zhan.vip_teacher.base.App;
import com.zhan.vip_teacher.base.Constants;
import com.zhan.vip_teacher.bean.UpgradeBean;
import com.zhan.vip_teacher.bean.UserInfo;
import com.zhan.vip_teacher.db.bean.DailyLesson;
import com.zhan.vip_teacher.db.bean.LocalMessage;
import com.zhan.vip_teacher.db.bean.PushMessage;
import com.zhan.vip_teacher.event.NewNotifyEvent;
import com.zhan.vip_teacher.network.ApiUrls;
import com.zhan.vip_teacher.ui.fragment.course.CourseFragment;
import com.zhan.vip_teacher.ui.fragment.message.MessageFragment;
import com.zhan.vip_teacher.ui.fragment.mine.MineFragment;
import com.zhan.vip_teacher.ui.fragment.student.StudentFragment;
import com.zhan.vip_teacher.ui.widget.RedDotView;
import com.zhan.vip_teacher.utils.LessonNotifyManager;
import com.zhan.vip_teacher.utils.MessageUtility;
import com.zhan.vip_teacher.utils.OperatorGuideManager;
import com.zhan.vip_teacher.utils.ShareUtil;
import com.zhan.vip_teacher.utils.Tools;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.message.BasicHeader;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    public final static String EXT_KEY_SHOW_PAGE = "show_page";

    public final static String TAG_COURSE = "Course";
    public final static String TAG_STUDENT = "Student";
    public final static String TAG_MESSAGEE = "Message";
    public final static String TAG_MINE = "Mine";

    //Views
    private TextView mTVCourse;
    private TextView mTVMessage;
    private TextView mTVStudent;
    private TextView mTVMine;

    private List<Page> mPageList = new ArrayList<Page>();
    private Page mCurPage;
    private RequestHandle mUpgradeHandle;
    private String mVersionName;
    private RedDotView mredDotView;

    private LessonNotifyManager mLessonNotifyManager;

    private boolean mHasRegister=false;

    private class Page {
        String TAG;
        ABaseFragment pageFragment;
        int FocusIconResId;
        int UnFocusIconResId;
        TextView BottomTitle;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!checkIsLogin()) {
            return;
        }
        showActionbar(false);
        setContentView(R.layout.activity_main);
        //禁止默认的页面统计方式，这样将不会再自动统计Activity。
        MobclickAgent.openActivityDurationTrack(false);
        initView();
        initPages();

        checkUpgrade();

        mHasRegister=true;
        EventBus.getDefault().register(this);

        mLessonNotifyManager=new LessonNotifyManager(this);
        showRemindBtn();

        if (ShareUtil.VALUE_TURN_ON.equals(ShareUtil.getStringValue(ShareUtil.DAILY_COURSE_PAGE, ShareUtil.VALUE_TURN_ON))) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(R.drawable.daily_couse_guid);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            final OperatorGuideManager operatorGuideManager = new OperatorGuideManager(this);
            operatorGuideManager.showGuideWindow(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    operatorGuideManager.hideGuideWindow();
                    ShareUtil.setValue(ShareUtil.DAILY_COURSE_PAGE, ShareUtil.VALUE_TURN_OFF);
                }
            });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void newMessgeEventBus(NewNotifyEvent event){
        if(hasUnReadMessage()){
            mredDotView.setVisibility(View.VISIBLE);
        }else {
            mredDotView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        if(mHasRegister){
            mHasRegister=false;
            EventBus.getDefault().unregister(this);
            /*Intent it=new Intent(this,VipService.class);
            stopService(it);*/
        }
        mHandler.removeCallbacks(mGetRemindLessonRunnable);
        if(mLessonNotifyManager!=null){
            mLessonNotifyManager.release();
        }
        super.onDestroy();
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        if (checkIsLogin()) {
            setJPushAlias(String.valueOf(UserInfo.getCurrentUser().getUserID()));
        }
        refreshRemindBtn();
    }

    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mGetRemindLessonRunnable);
        MobclickAgent.onPause(this);
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    private boolean checkIsLogin() {
        if (UserInfo.getCurrentUser() == null || !UserInfo.getCurrentUser().isLogined()) {
            setJPushAlias("");
            Intent loginIntent = new Intent(this, LoginActivity.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginIntent);
            finish();
            return false;
        }
        return true;
    }



    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(UserInfo.getCurrentUser()==null||!UserInfo.getCurrentUser().isLogined()){
            return;
        }
        if ((Intent.FLAG_ACTIVITY_CLEAR_TOP & intent.getFlags()) != 0) {
            //退出跳转到主页,强制刷新主页
            String showPage = intent.getStringExtra(EXT_KEY_SHOW_PAGE);

            if (TextUtils.isEmpty(showPage)) {
                showCoursePage();
            }else{
                showPage(showPage);
            }
        }
    }

    private void initView() {
        mTVCourse = (TextView) findViewById(R.id.home_page_course);
        mTVStudent = (TextView) findViewById(R.id.home_page_student);
        mTVMessage = (TextView) findViewById(R.id.home_page_message);
        mTVMine = (TextView) findViewById(R.id.home_page_mine);
        mredDotView = (RedDotView) findViewById(R.id.mine_reddotview);

        if(hasUnReadMessage()){
            mredDotView.setVisibility(View.VISIBLE);
        }else {
            mredDotView.setVisibility(View.GONE);
        }

        mTVCourse.setOnClickListener(this);
        mTVStudent.setOnClickListener(this);
        mTVMessage.setOnClickListener(this);
        mTVMine.setOnClickListener(this);
    }

    private void initPages() {
        mPageList.clear();
        //课程
        Page page = new Page();
        page.TAG = TAG_COURSE;
        page.pageFragment = new CourseFragment();
        page.FocusIconResId = R.drawable.main_icon_course_selected;
        page.UnFocusIconResId = R.drawable.main_icon_course;
        page.BottomTitle = mTVCourse;
        mPageList.add(page);

        //学生
        page = new Page();
        page.TAG = TAG_STUDENT;
        page.pageFragment = new StudentFragment();
        page.FocusIconResId = R.drawable.main_icon_student_selected;
        page.UnFocusIconResId = R.drawable.main_icon_student;
        page.BottomTitle = mTVStudent;
        mPageList.add(page);

        //消息
        page = new Page();
        page.TAG = TAG_MESSAGEE;
        page.pageFragment = new MessageFragment();
        page.FocusIconResId = R.drawable.main_icon_message_pressed;
        page.UnFocusIconResId = R.drawable.main_icon_message;
        page.BottomTitle = mTVMessage;
        mPageList.add(page);

        //我的
        page = new Page();
        page.TAG = TAG_MINE;
        page.pageFragment = new MineFragment();
        page.FocusIconResId = R.drawable.main_icon_my_pressed;
        page.UnFocusIconResId = R.drawable.main_icon_my;
        page.BottomTitle = mTVMine;
        mPageList.add(page);

        String showPage = getIntent().getStringExtra(EXT_KEY_SHOW_PAGE);
        if (TextUtils.isEmpty(showPage)) {
            showPage = TAG_COURSE;
        }
        showPage(showPage);
    }

    private void showCoursePage() {
        showPage(TAG_COURSE);
        ((CourseFragment)mCurPage.pageFragment).setForceRefreshToday(true);
    }

    private void showPage(String tag) {
        for (Page page : mPageList) {
            if (page.TAG.equals(tag)) {
                if (mCurPage != page) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.content, page.pageFragment);
                    transaction.commit();
                    page.BottomTitle.setCompoundDrawablesWithIntrinsicBounds(0, page.FocusIconResId, 0, 0);
                    page.BottomTitle.setTextColor(getResources().getColor(R.color.blue));
                    onPageChange(mCurPage, page);
                }
                mCurPage = page;
            } else {
                page.BottomTitle.setCompoundDrawablesWithIntrinsicBounds(0, page.UnFocusIconResId, 0, 0);
                page.BottomTitle.setTextColor(getResources().getColor(R.color.text_color_content));
            }
        }
    }

    private void onPageChange(Page oldPage, Page newPage) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_page_course:
                showPage(TAG_COURSE);
                break;
            case R.id.home_page_student:
                showPage(TAG_STUDENT);
                break;
            case R.id.home_page_message:
                showPage(TAG_MESSAGEE);
                break;
            case R.id.home_page_mine:
                showPage(TAG_MINE);
                break;
        }
    }

    private void setJPushAlias(final String jPushAlias) {
        //极光推送注册别名
        Log.i("MainActivity", "jPushAlias = " + jPushAlias);
        //用户存在并且现在保存的别名和用户ID不是同一个
        if (!jPushAlias.equals(ShareUtil.getStringValue(ShareUtil.JPUSH_ALIAS))) {
            JPushInterface.setAlias(App.getInstance(), jPushAlias, new TagAliasCallback() {
                @Override
                public void gotResult(int responseCode, String alias, Set<String> tags) {
                    switch (responseCode) {
                        case 0:
                            Log.i("MainActivity", "alias success");
                            ShareUtil.setValue(ShareUtil.JPUSH_ALIAS, jPushAlias);
                            break;
                        case 6002:
                            Log.i("MainActivity", "Failed to set alias and tags due to timeout. Try again after 60s.");
                            break;
                        default:
                            Log.e("MainActivity", "Failed with errorCode = " + responseCode);
                            break;
                    }
                }
            });
        }
    }

    private void checkUpgrade() {
        //只在WIFI下更新
        if (!Connectivity.isConnectedWifi(this)) {
            return;
        }
        if (mUpgradeHandle != null && !mUpgradeHandle.isFinished()) {
            return;
        }

        Header[] headers = new Header[2];
        headers[0] = new BasicHeader("platform", "Android");
        headers[1] = new BasicHeader("version", getVersion(getApplicationContext()));

        RequestParams requestParams = new RequestParams();
        requestParams.put("type", "android");
        requestParams.put("version", getVersion(getApplicationContext()));
        requestParams.put("name", getString(R.string.app_name));

        mUpgradeHandle = HttpClientUtils.post(ApiUrls.APPCLINE_UPGRADE, headers,requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String content = new String(bytes);
                Log.e("MainActivity", "Upgrade info : " + content);
                try {
                    final UpgradeBean bean = JSON.parseObject(content, UpgradeBean.class);
                    if (bean != null && !TextUtils.isEmpty(bean.getDatas().getUrl())) {

                        AlertDialog.Builder dlgBuilder = new AlertDialog.Builder(MainActivity.this);
                        dlgBuilder.setTitle(R.string.new_version).
                                setPositiveButton(R.string.update_now, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Tools.installApp(bean.getDatas().getUrl());
                                        dialog.dismiss();
                                    }
                                });

                        dlgBuilder.setMessage(bean.getDatas().getIntroduce());

                        //判断是否强制升级
                        if (bean.getDatas().getForcedUpdate() == 1) {
                            dlgBuilder.setCancelable(false);
                        } else {
                            dlgBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                        }
                        dlgBuilder.show();
                    }
                } catch (JSONException exp) {
                    Log.e("MainActivity", "fromJson error : " + exp.getMessage());
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });

    }

    private boolean needUpgrade(String newVersion) {
        if (TextUtils.isEmpty(newVersion)) {
            return false;
        }
        String[] arrNewVer = newVersion.split("\\.");
        if (arrNewVer.length != 3) {
            return false;
        }
        boolean needUpgrade = false;
        String[] curVer = getCurVersion().split("\\.");
        for (int i = 0; i < arrNewVer.length; i++) {
            int newVerSeg = Integer.parseInt(arrNewVer[i]);
            int curVerSeg = Integer.parseInt(curVer[i]);
            if (newVerSeg > curVerSeg) {
                needUpgrade = true;
                break;
            }
        }
        return needUpgrade;
    }

    private String getCurVersion() {
        try {
            PackageManager manager = App.ctx.getPackageManager();
            PackageInfo info = manager.getPackageInfo(App.ctx.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            return "1.0.0";
        }
    }

    private static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            return "1.0.0";
        }
    }

    private boolean hasUnReadMessage(){
        //上课提醒,来自推送
        PushMessage remindForClassMessage = MessageUtility.findLatestMessage(PushMessage.class, Constants.MSG_TYPE_REMIND_FOR_CLASS);

        if(remindForClassMessage!=null&&!remindForClassMessage.isHasRead()){
            return true;
        }
        //签到提醒,来自本地闹钟
        LocalMessage remindForSign=MessageUtility.findLatestMessage(LocalMessage.class,Constants.MSG_TYPE_REMIND_FOR_SIGN);

        if(remindForSign!=null&&!remindForSign.isHasRead()){
            return true;
        }

        //签到成功提醒
        LocalMessage remindForSignSuccess=MessageUtility.findLatestMessage(LocalMessage.class,Constants.MSG_TYPE_REMIND_SUCCESS_SIGN);

        if(remindForSignSuccess!=null&&!remindForSignSuccess.isHasRead()){
            return true;
        }

        return false;
    }

    private void showRemindBtn(){
        mRemindBtn =new FloatingActionButton(this);
        mRemindBtn.setImageResource(R.drawable.remind_for_class);
        mRemindBtn.setScaleType(ImageView.ScaleType.CENTER);
        mRemindBtn.setColorNormal(0xffc0c0c0);
        mRemindBtn.setColorPressed(0xffc0c0c0);
        mRemindBtn.setColorRipple(0xffF06292);
        mRemindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRemindLesson != null) {
                    mLessonNotifyManager.remindForLesson((int) mRemindLesson.getLessonId());
                } else {
                    ToastUtils.toast(R.string.remind_for_class_no_lesson);
                }
            }
        });

        FrameLayout.LayoutParams params = getLayoutParams();
        mRemindBtn.setLayoutParams(params);

        getRootView().addView(mRemindBtn);

        mRemindBtn.setOnTouchListener(new View.OnTouchListener() {
            private float startRawX,startRawY;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float dx;
                float dy;
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
                        params.gravity = Gravity.LEFT | Gravity.TOP;
                        startRawX=motionEvent.getRawX();
                        startRawY=motionEvent.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        dx=Math.abs(motionEvent.getRawX()-startRawX);
                        dy=Math.abs(motionEvent.getRawY()-startRawY);
                        if(dx>PixelUtils.dp2px(5)||dy>PixelUtils.dp2px(5)){
                            moveViewWithFinger(view, motionEvent.getRawX(), motionEvent.getRawY());
                        }else{

                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        dx=Math.abs(motionEvent.getRawX()-startRawX);
                        dy=Math.abs(motionEvent.getRawY()-startRawY);
                        if(dx>PixelUtils.dp2px(5)||dy>PixelUtils.dp2px(5)){

                        }else{
                            view.performClick();
                        }
                        break;
                }
                return true;
            }
        });

        mHandler.post(mGetRemindLessonRunnable);
    }

    private void moveViewWithFinger(View view, float rawX, float rawY) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.leftMargin = (int) rawX - view.getWidth() / 2;
        params.topMargin = (int) rawY - getSystemStatusBarHeight() - view.getHeight() / 2;
        view.setLayoutParams(params);

        App.ctx.setFabTopMargin(params.topMargin);
        App.ctx.setFabLeftMargin(params.leftMargin);
    }

    private FrameLayout.LayoutParams getLayoutParams(){
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        if(App.ctx.getFabLeftMargin()==0&&App.ctx.getFabTopMargin()==0){
            params.gravity= Gravity.RIGHT|Gravity.BOTTOM;
            params.bottomMargin=PixelUtils.dp2px(60);
        }else{
            params.leftMargin=App.ctx.getFabLeftMargin();
            params.topMargin=App.ctx.getFabTopMargin();
        }

        return params;
    }

    private void refreshRemindBtn(){
        if(mRemindBtn!=null){
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mRemindBtn.getLayoutParams();
            if(App.ctx.getFabLeftMargin()!=0&&App.ctx.getFabTopMargin()!=0){
                params.gravity = Gravity.LEFT | Gravity.TOP;
                params.leftMargin=App.ctx.getFabLeftMargin();
                params.topMargin=App.ctx.getFabTopMargin();
                mRemindBtn.requestLayout();
            }
            mHandler.removeCallbacks(mGetRemindLessonRunnable);
            mHandler.post(mGetRemindLessonRunnable);
        }
    }

    public int getSystemStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    private android.os.Handler mHandler = new android.os.Handler() {

    };

    private Runnable mGetRemindLessonRunnable=new Runnable(){
        @Override
        public void run() {
            mRemindLesson=mLessonNotifyManager.getRemindLesson();
            if(mRemindLesson==null){
                mRemindBtn.setColorNormal(0xffc0c0c0);
                mRemindBtn.setColorPressed(0xffc0c0c0);
            }else{
                mRemindBtn.setColorNormal(0xff20a6fb);
                mRemindBtn.setColorPressed(0xff1b8dd6);
            }
            //每十秒轮询
            mHandler.removeCallbacks(mGetRemindLessonRunnable);
            mHandler.postDelayed(mGetRemindLessonRunnable,10000);
        }
    };

    private DailyLesson mRemindLesson;
    private FloatingActionButton mRemindBtn;
}
