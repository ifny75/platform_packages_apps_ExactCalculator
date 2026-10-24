/* Copyright (C) 2026
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy at http://www.apache.org/licenses/LICENSE-2.0
 */
package com.android.calculator2;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/** A four-column keypad whose keys stay circular, including in short windows. */
public class CalculatorKeypad extends ViewGroup {
    private static final int COLUMNS = 4;
    private static final int ROWS = 5;
    private int mCellSize;
    private int mKeySize;

    public CalculatorKeypad(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int width = MeasureSpec.getSize(widthSpec);
        int preferredHeight = (width - getPaddingLeft() - getPaddingRight()) * ROWS / COLUMNS
                + getPaddingTop() + getPaddingBottom();
        int height = resolveSize(preferredHeight, heightSpec);
        mCellSize = Math.max(0, Math.min(
                (width - getPaddingLeft() - getPaddingRight()) / COLUMNS,
                (height - getPaddingTop() - getPaddingBottom()) / ROWS));
        int gap = Math.round(8 * getResources().getDisplayMetrics().density);
        mKeySize = Math.max(0, mCellSize - gap);
        int keySpec = MeasureSpec.makeMeasureSpec(mKeySize, MeasureSpec.EXACTLY);
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(keySpec, keySpec);
            if (child instanceof TextView) {
                ((TextView) child).setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        Math.min(mKeySize * 0.42f,
                                36 * getResources().getDisplayMetrics().scaledDensity));
            }
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left = (getWidth() - COLUMNS * mCellSize) / 2;
        int top = getHeight() - getPaddingBottom() - ROWS * mCellSize;
        int inset = (mCellSize - mKeySize) / 2;
        for (int i = 0; i < getChildCount(); i++) {
            int x = left + (i % COLUMNS) * mCellSize + inset;
            int y = top + (i / COLUMNS) * mCellSize + inset;
            getChildAt(i).layout(x, y, x + mKeySize, y + mKeySize);
        }
    }
}
