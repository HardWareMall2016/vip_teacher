package com.zhan.vip_teacher.ui.fragment.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhan.framework.component.container.FragmentContainerActivity;
import com.zhan.framework.support.inject.ViewInject;
import com.zhan.framework.ui.fragment.ABaseFragment;
import com.zhan.framework.utils.ToastUtils;
import com.zhan.vip_teacher.R;
import com.zhan.vip_teacher.bean.UserInfo;
import com.zhan.vip_teacher.ui.activity.MainActivity;
import com.zhan.vip_teacher.utils.CacheUtility;
import com.zhan.vip_teacher.utils.LessonNotifyManager;

/**
 * Created by Administrator on 2016/3/4.
 */
public class SettingFragment extends ABaseFragment {

    @ViewInject(id = R.id.setting_teacher_name)
    TextView mTeacherName;
    @ViewInject(id = R.id.teacher_phone)
    TextView mTeacherPhone;
    @ViewInject(id = R.id.setting_lesson_head, click = "OnClick")
    LinearLayout mSettingHead;

    @ViewInject(id = R.id.my_setting_quit, click = "OnClick")
    RelativeLayout mQuit;

    @ViewInject(id = R.id.clear_cache, click = "OnClick")
    View mViewClearCache;

    @ViewInject(id = R.id.setting_feedback, click = "OnClick")
    View mFeedBack ;

    @ViewInject(id = R.id.left_img)
    ImageView mTeacherAvatar ;

    private DisplayImageOptions options;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        options = buldDisplayImageOptions();
    }

    @Override
    protected int inflateContentView() {
        return R.layout.setting_frag_layout;
    }

    public static void launch(FragmentActivity activity) {
        FragmentContainerActivity.launch(activity, SettingFragment.class, null);
    }

    @Override
    protected void layoutInit(LayoutInflater inflater, Bundle savedInstanceSate) {
        super.layoutInit(inflater, savedInstanceSate);
        getActivity().setTitle(getString(R.string.mine_setting));
        mTeacherName.setText(UserInfo.getCurrentUser().getTrueName());
        mTeacherPhone.setText(UserInfo.getCurrentUser().getMobile());
        ImageLoader.getInstance().displayImage(UserInfo.getCurrentUser().getHeadImgUrl(), mTeacherAvatar, options);

    }


    void OnClick(View v) {
        switch (v.getId()) {
            case R.id.setting_lesson_head:
                PersonFragment.launch(getActivity());
                break;
            case R.id.my_setting_quit:
                LessonNotifyManager lessonNotifyManager = new LessonNotifyManager(getActivity());
                lessonNotifyManager.cancelTodayLessonsNotifyAlarm();
                UserInfo.logout();
                ToastUtils.toast(R.string.my_setting_quit);
                Intent homePageIntent = new Intent(getActivity(), MainActivity.class);
                homePageIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homePageIntent);
                break;
            case R.id.clear_cache:
                CacheUtility.clearAllCacheData();
                ToastUtils.toast(R.string.mine_setting_clear_cache_finish);
                break;
            case R.id.setting_feedback:
                FeedBackFragment.launch(getActivity());
                break;
        }
    }

    public DisplayImageOptions buldDisplayImageOptions() {
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.teacher_avatar)
                .showImageForEmptyUri(R.drawable.teacher_avatar)
                .showImageOnFail(R.drawable.teacher_avatar)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
    }
}
