// Copyright 2012 Square, Inc.
package com.zhan.vip_teacher.ui.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhan.framework.utils.PixelUtils;

import static android.view.View.MeasureSpec.AT_MOST;
import static android.view.View.MeasureSpec.EXACTLY;
import static android.view.View.MeasureSpec.makeMeasureSpec;

/** TableRow that draws a divider between each cell. To be used with {@link CalendarGridView}. */
public class CalendarRowView extends ViewGroup implements View.OnClickListener {
  //private boolean isHeaderRow;
  private /*MonthView.*/CalendarCellItemClickListener listener;

  public CalendarRowView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  public void addView(View child, int index, ViewGroup.LayoutParams params) {
    //child.setOnClickListener(this);
    super.addView(child, index, params);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    //long start = System.currentTimeMillis();
    final int totalWidth = MeasureSpec.getSize(widthMeasureSpec);
    int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
    int sizeHeight = MeasureSpec.getSize(heightMeasureSpec) - getPaddingRight() - getPaddingLeft();
    int rowHeight = 0;
    for (int c = 0, numChildren = getChildCount(); c < numChildren; c++) {
      final View child = getChildAt(c);
      // Calculate width cells, making sure to cover totalWidth.
      int l = ((c + 0) * totalWidth) / getChildCount();
      int r = ((c + 1) * totalWidth) / getChildCount();
      int cellSize = r - l;
      int cellWidthSpec = makeMeasureSpec(cellSize, EXACTLY);
      int cellHeightSpec = /*isHeaderRow ? makeMeasureSpec(cellSize, AT_MOST) :cellWidthSpec*/ heightMeasureSpec;

      //child.measure(cellWidthSpec, cellHeightSpec);
      child.measure(cellWidthSpec,
              MeasureSpec.makeMeasureSpec(sizeHeight, modeHeight == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : modeHeight));
      // The row height is the height of the tallest cell.
      if (child.getMeasuredHeight() > rowHeight) {
        rowHeight = child.getMeasuredHeight();
      }
    }
    final int widthWithPadding = totalWidth + getPaddingLeft() + getPaddingRight();
    final int heightWithPadding = rowHeight + getPaddingTop() + getPaddingBottom();
    setMeasuredDimension(widthWithPadding, heightWithPadding);
    //Logr.d("Row.onMeasure %d ms", System.currentTimeMillis() - start);
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    long start = System.currentTimeMillis();
    int cellHeight = bottom - top;
    int width = (right - left);
    for (int c = 0, numChildren = getChildCount(); c < numChildren; c++) {
      final View child = getChildAt(c);
      int l = ((c + 0) * width) / getChildCount();
      int r = ((c + 1) * width) / getChildCount();
      child.layout(l, 0, r, cellHeight);
    }
    //Logr.d("Row.onLayout %d ms", System.currentTimeMillis() - start);
  }

  /*public void setIsHeaderRow(boolean isHeaderRow) {
    this.isHeaderRow = isHeaderRow;
  }*/

  @Override
  public void onClick(View v) {
    // Header rows don't have a click listener
    if (listener != null) {
      //listener.handleClick((MonthCellDescriptor) v.getTag());
      listener.handleClick(v);
    }
  }

  public void setListener(/*MonthView.*/CalendarCellItemClickListener listener) {
    for (int i = 1; i < getChildCount(); i++) {
      getChildAt(i).setOnClickListener(this);
    }
    this.listener = listener;
  }

  public void setCellBackground(int resId) {
    for (int i = 0; i < getChildCount(); i++) {
      getChildAt(i).setBackgroundResource(resId);
    }
  }

  public void setCellTextColor(int resId) {
    for (int i = 0; i < getChildCount(); i++) {
      ((TextView) getChildAt(i)).setTextColor(resId);
    }
  }

  public void setCellTextColor(ColorStateList colors) {
    for (int i = 0; i < getChildCount(); i++) {
      ((TextView) getChildAt(i)).setTextColor(colors);
    }
  }

  public void setTypeface(Typeface typeface) {
    for (int i = 0; i < getChildCount(); i++) {
      ((TextView) getChildAt(i)).setTypeface(typeface);
    }
  }
}
