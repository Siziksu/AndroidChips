package com.siziksu.chips.ui.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.siziksu.chips.ui.view.ChipTextView;

import java.util.ArrayList;
import java.util.List;

public class MultiTextManager {

    private static final String COMA = ",";
    private static final String SUFFIX = ", ";

    private final Context context;
    private final MultiAutoCompleteTextView multiText;
    private List<String> entries = new ArrayList<>();

    public MultiTextManager(Context context, MultiAutoCompleteTextView multiText) {
        this.multiText = multiText;
        this.context = context;
    }

    public void init() {
        multiText.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        multiText.setMovementMethod(LinkMovementMethod.getInstance());
        multiText.setThreshold(1);
        multiText.setOnKeyListener((v, keyCode, event) -> false);
        multiText.setOnItemClickListener((parent, view, position, id) -> {
            String textString = multiText.getText().toString();
            if (textString.contains(COMA)) {
                List<String> chips = getChips(textString);
                setChips(chips);
            }
        });
    }

    public void setEntries(List<String> entries) {
        this.entries.clear();
        this.entries.addAll(entries);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, entries);
        multiText.setAdapter(adapter);
    }

    @NonNull
    private List<String> getChips(String textString) {
        List<String> chips = new ArrayList<>();
        String[] split = textString.split(COMA);
        for (String filter : split) {
            if (!TextUtils.isEmpty(filter.trim())) {
                chips.add(filter.trim());
            }
        }
        for (int i = chips.size() - 1; i >= 0; i--) {
            if (!entries.contains(chips.get(i))) {
                chips.remove(i);
            }
        }
        return chips;
    }

    private void setChips(List<String> chips) {
        multiText.setText(getSpannableStringBuilder(chips));
        multiText.setSelection(multiText.getText().length());
    }

    @NonNull
    private SpannableStringBuilder getSpannableStringBuilder(List<String> chips) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        for (int i = 0; i < chips.size(); i++) {
            String string = chips.get(i);
            builder.append(string).append(SUFFIX);
            createSpan(builder, string);
        }
        return builder;
    }

    private void createSpan(SpannableStringBuilder builder, final String string) {
        int spanStart = builder.length() - (string.length() + SUFFIX.length());
        int spanEnd = builder.length() - SUFFIX.length();
        ChipTextView textView = createChipTextView(string);
        BitmapDrawable drawable = convertViewToDrawable(textView);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        builder.setSpan(new ImageSpan(drawable), spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ClickableSpan() {

                            @Override
                            public void onClick(View widget) {
                                MultiAutoCompleteTextView text = (MultiAutoCompleteTextView) widget;
                                String textString = text.getText().toString().replace(string + SUFFIX, "");
                                List<String> chips = getChips(textString);
                                setChips(chips);
                            }
                        },
                        spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private ChipTextView createChipTextView(String text) {
        ChipTextView chipTextView = new ChipTextView(context);
        chipTextView.setText(text);
        return chipTextView;
    }

    private BitmapDrawable convertViewToDrawable(TextView view) {
        int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(spec, spec);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.translate(-view.getScrollX(), -view.getScrollY());
        view.draw(canvas);
        view.setDrawingCacheEnabled(true);
        Bitmap drawingCache = view.getDrawingCache();
        Bitmap viewBitmap = drawingCache.copy(Bitmap.Config.ARGB_8888, true);
        view.destroyDrawingCache();
        bitmap.recycle();
        drawingCache.recycle();
        return new BitmapDrawable(context.getResources(), viewBitmap);
    }
}
