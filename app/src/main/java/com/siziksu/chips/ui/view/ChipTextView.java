package com.siziksu.chips.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import com.siziksu.chips.R;

public class ChipTextView extends TextView {

    private DisplayMetrics metrics;

    public ChipTextView(Context context) {
        super(context);
        init();
    }

    public ChipTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        metrics = getResources().getDisplayMetrics();
        setBackgroundResource(R.drawable.chip_background);
        setTextColor(Color.WHITE);
        setPadding((int) getPX(12), (int) getPX(8), (int) getPX(12), (int) getPX(8));
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        setGravity(Gravity.CENTER);
        setMaxLines(1);
        setCompoundDrawablesWithIntrinsicBounds(R.drawable.chip_dismiss, 0, 0, 0);
        setCompoundDrawablePadding((int) getPX(4));
    }

    private float getPX(float dp) {
        return dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
