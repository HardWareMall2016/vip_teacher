// Copyright 2013 Square, Inc.

package com.zhan.vip_teacher.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.zhan.vip_teacher.R;
import com.zhan.vip_teacher.base.App;
import com.zhan.vip_teacher.base.Constants;
import com.zhan.vip_teacher.db.bean.RestTime;
import com.zhan.vip_teacher.db.bean.WeekLesson;
import com.zhan.vip_teacher.db.bean.WeekPublicLesson;
import com.zhan.vip_teacher.utils.Tools;


public class CalendarCellView extends TextView {

  public enum CellType{X_HEAD,NONE,LESSON,PUBLIC_LESSON,MORE,REST}

  @SuppressWarnings("UnusedDeclaration") //
  public CalendarCellView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  private RestTime mRestTime;

  public RestTime getRestTime(){
    return mRestTime;
  }

  public void setCell(CellType cellType, Object object){
    switch (cellType){
      case X_HEAD:
        setBackgroundColor(Color.parseColor("#ffffffff"));
        setText((String) object);
        break;
      case NONE:
        setBackgroundColor(Color.parseColor("#ffffffff"));
        setText(null);
        mRestTime=null;
        break;
      case REST:
        Drawable bg=getBackground();
        if(bg!=null){
          if(bg instanceof ColorDrawable){
            setBackgroundColor(Color.parseColor("#fff5f5f5"));
          }else{
            bg.setColorFilter(0xfff5f5f5, PorterDuff.Mode.MULTIPLY);
          }
        }else{
          setBackgroundColor(Color.parseColor("#fff5f5f5"));
        }
        mRestTime= (RestTime) object;

        break;
      case LESSON:
        WeekLesson lesson=(WeekLesson)object;

        if(lesson.isFirstLesson()&&lesson.isFeedBack()){
          setBackgroundResource(R.drawable.bg_lesson_status_first_lesson_feedback);
        }else if(lesson.isFirstLesson()){
          setBackgroundResource(R.drawable.bg_lesson_status_first_lesson);
        }else if(Constants.COURSE_STATUS_IN_TIME.equals(lesson.getSignStatus())){
          setBackgroundResource(R.drawable.bg_lesson_status_begun_lesson);
        }else if(Constants.COURSE_STATUS_STUDENT_LATE.equals(lesson.getSignStatus())){
          setBackgroundResource(R.drawable.bg_lesson_status_be_late);
        }else if(Constants.COURSE_STATUS_EARLY.equals(lesson.getSignStatus())){
          setBackgroundResource(R.drawable.bg_lesson_status_finish_lesson_early);
        }else if(Constants.COURSE_STATUS_STUDENT_ABSENTEEISM.equals(lesson.getSignStatus())){
          setBackgroundResource(R.drawable.bg_lesson_status_absenteeism);
        }else {
          setBackgroundResource(R.drawable.bg_lesson_status_missing_lesson);
        }

        /*String html = "<img src='" + Tools.getBlueCourseTypeIconBySubTypeName(lesson.getCourseSubTypeName()) + "'/>";
        Html.ImageGetter imgGetter = new Html.ImageGetter() {
          @Override
          public Drawable getDrawable(String source) {
            int id = Integer.parseInt(source);
            Drawable d = getResources().getDrawable(id);
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            return d;
          }
        };
        CharSequence charSequence = Html.fromHtml(html, imgGetter, null);
        setText(charSequence);*/
        SpannableString spannable = new SpannableString("[img]");
        Drawable drawable = getResources().getDrawable(Tools.getBlueCourseTypeIconBySubTypeName(lesson.getCourseSubTypeName()));
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        spannable.setSpan(new ImageSpan(drawable), 0, "[img]".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        setText(spannable);

        if(!TextUtils.isEmpty(lesson.getStudentName())){
          append("\n");
          SpannableString spStudent = new SpannableString(lesson.getStudentName());
          spStudent.setSpan(new AbsoluteSizeSpan(12, true), 0, lesson.getStudentName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
          spStudent.setSpan(new ForegroundColorSpan(0xff20a6fb),  0, lesson.getStudentName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
          append(spStudent);
        }

        break;
      case PUBLIC_LESSON:
        WeekPublicLesson publicLesson=(WeekPublicLesson)object;
        if("1".equals(publicLesson.getSignStatus())){
          setBackgroundResource(R.drawable.bg_lesson_status_open_course_finished);
        }else{
          setBackgroundResource(R.drawable.bg_lesson_status_open_course);
        }

        String publicHtml = "<img src='" + Tools.getBlueCourseTypeIconBySubTypeName(null) + "'/>";
        Html.ImageGetter publicImgGetter = new Html.ImageGetter() {
          @Override
          public Drawable getDrawable(String source) {
            int id = Integer.parseInt(source);
            Drawable d = getResources().getDrawable(id);
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            return d;
          }
        };
        CharSequence publicCharSequence = Html.fromHtml(publicHtml, publicImgGetter, null);
        setText(publicCharSequence);
        break;
      case MORE:
        setBackgroundResource(R.drawable.bg_lesson_status_common);

        String moreHtml = "<img src='" + R.drawable.week_course_3 + "'/>";

        Integer num=(Integer)object;
        if(num==2){
          moreHtml = "<img src='" + R.drawable.week_course_2 + "'/>";
        }

        Html.ImageGetter moreImgGetter = new Html.ImageGetter() {
          @Override
          public Drawable getDrawable(String source) {
            int id = Integer.parseInt(source);
            Drawable d = getResources().getDrawable(id);
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            return d;
          }
        };
        CharSequence moreCharSequence = Html.fromHtml(moreHtml, moreImgGetter, null);
        setText(moreCharSequence);
        break;
    }
  }

}
