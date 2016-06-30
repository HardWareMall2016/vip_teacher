package com.zhan.vip_teacher.ui.fragment.search;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhan.framework.component.container.FragmentContainerActivity;
import com.zhan.framework.support.inject.ViewInject;
import com.zhan.framework.ui.fragment.ABaseFragment;
import com.zhan.framework.ui.widget.RippleButton;
import com.zhan.framework.utils.ToastUtils;
import com.zhan.framework.view.pickerview.LoopView;
import com.zhan.vip_teacher.R;
import com.zhan.vip_teacher.bean.CommDictionary;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/4.
 */
public class SearchFragment extends ABaseFragment {

    @ViewInject(id = R.id.et_search)
    EditText mEtSearch;

    @ViewInject(id = R.id.btnSearch, click = "OnClick")
    RippleButton mSearch;

    @ViewInject(id = R.id.search_change_lesson, click = "OnClick")
    LinearLayout mChangeLesson;

    @ViewInject(id = R.id.tv_search_lesson)
    TextView mTVChangLesson;

    @ViewInject(id = R.id.picker_view)
    LoopView mPickView;

    private View mChangLessonPopMenuContent;
    private PopupWindow mPopupWindow;
    private List<String> values_name = new ArrayList<>();
    private List<String> values_id = new ArrayList<>();
    private int i = 0;
    private CommDictionary mCommDictionary;

    public static void launch(FragmentActivity activity) {
        FragmentContainerActivity.launch(activity, SearchFragment.class, null);
    }

    @Override
    protected int inflateContentView() {
        return R.layout.activity_search;
    }

    @Override
    protected void layoutInit(LayoutInflater inflater, Bundle savedInstanceSate) {
        super.layoutInit(inflater, savedInstanceSate);
        getActivity().setTitle(getString(R.string.search_title));
        intiPopMenu();

        values_name.clear();
        values_id.clear();

        CommDictionary.getCommDictionary(new CommDictionary.GetCallback() {
            @Override
            public void onGetResult(CommDictionary data) {
                mCommDictionary = data;
                if (mCommDictionary != null) {
                    if (mCommDictionary.getCourseProgress() != null && mCommDictionary.getCourseProgress().size() > 0) {
                        List<CommDictionary.CourseProgressEntity> typeList = mCommDictionary.getCourseProgress();
                        values_name.add(getString(R.string.please_select_homework_score));
                        values_id.add(null);
                        for (CommDictionary.CourseProgressEntity item : typeList) {
                            values_name.add(item.getName());
                            values_id.add(item.getId());
                        }
                    }
                }
            }
        });
    }

    private void intiPopMenu() {
        /* 第一个参数弹出显示view 后两个是窗口大小 */
        mPopupWindow = new PopupWindow(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        /* 设置背景显示 */
        int bgColor = getResources().getColor(com.zhan.framework.R.color.main_background);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(bgColor));
        /* 设置触摸外面时消失 */
        mPopupWindow.setOutsideTouchable(true);
        /* 设置系统动画 */
        mPopupWindow.setAnimationStyle(R.style.pop_menu_animation);
        mPopupWindow.update();
        mPopupWindow.setTouchable(true);
        /* 设置点击menu以外其他地方以及返回键退出 */
        mPopupWindow.setFocusable(true);
    }

    void OnClick(View v) {
        switch (v.getId()) {
            case R.id.btnSearch:
                if(checkSearch()){
                StatusContent content = new StatusContent();
                content.setArg1(mEtSearch.getText().toString());
                if(mTVChangLesson.getText().toString().equals("请选择")){
                    content.setArg2(null);
                }else{
                    content.setArg2(values_id.get(mPickView.getSelectedItem()));
                }
                SearchResultFragment.launch(getActivity(), content);
               }
                break;
            case R.id.search_change_lesson:
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mTVChangLesson.getWindowToken(), 0); //强制隐藏键盘
                showChooseMenu();
                break;
        }
    }

    private boolean checkSearch() {
//        if (TextUtils.isEmpty(mEtSearch.getText().toString().trim())) {
//            ToastUtils.toast(R.string.search_name);
//            return false;
//        }
        if(TextUtils.isEmpty(mEtSearch.getText().toString().trim())){
            if (mTVChangLesson.getText().toString().equals("请选择")) {
                ToastUtils.toast(R.string.search_chang_lesson);
                return false;
            }
        }

        return true;
    }

    private void showChooseMenu() {
        mChangLessonPopMenuContent = getActivity().getLayoutInflater().inflate(R.layout.pop_memu_changlesson, null);
        mPopupWindow.setContentView(mChangLessonPopMenuContent);
        View btnCancel = mChangLessonPopMenuContent.findViewById(R.id.exam_time_cancel_time);
        btnCancel.setOnClickListener(mOnExamTimeClickListener);
        View btnFinish = mChangLessonPopMenuContent.findViewById(R.id.exam_time_finish_time);
        btnFinish.setOnClickListener(mOnExamTimeClickListener);

        mPickView = (LoopView) mChangLessonPopMenuContent.findViewById(R.id.picker_view);
        mPickView.setNotLoop();
        i = mPickView.getSelectedItem();

        mPickView.setArrayList((ArrayList) values_name);
        mPickView.setInitPosition(0);

        showPopMenu();
    }

    private View.OnClickListener mOnExamTimeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.exam_time_cancel_time:
                    closePopWin();
                    break;
                case R.id.exam_time_finish_time:
                    i = mPickView.getSelectedItem();
                    mTVChangLesson.setText(values_name.get(i));
                    closePopWin();
                    break;
            }
        }
    };

    public boolean closePopWin() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
            return true;
        }
        return false;
    }

    private void showPopMenu() {
        if (mPopupWindow != null && !mPopupWindow.isShowing()) {
            /* 最重要的一步：弹出显示 在指定的位置(parent) 最后两个参数 是相对于 x / y 轴的坐标 */
            mPopupWindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
            backgroundAlpha(0.7f);
            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    backgroundAlpha(1f);
                }
            });
        }
    }

    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }

}
