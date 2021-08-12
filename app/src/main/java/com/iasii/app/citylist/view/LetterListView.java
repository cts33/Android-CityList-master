package com.iasii.app.citylist.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.iasii.app.citylist.R;
import com.iasii.app.citylist.utils.DensityUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by next on 2016/3/24.
 */
public class LetterListView extends View {

    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    private String[] defaultLets = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
            "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private List<String> letterList = new ArrayList<>();
    int choose = -1;
    private Paint paint = new Paint();

    private Context context;
    private int textSize = 15;
    private int width;

    private int textDefaultColor = Color.parseColor("#333333");
    private int textFocusColor = Color.RED;

    int paddingTop, paddingDown;
    private Drawable defaultBgDrawable;
    //每个字母占位的高度值 px
    private int singleHeight=24;

    public LetterListView(Context context) {
        this(context, null);
        this.context = context;
    }

    public LetterListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;

    }

    public LetterListView(Context context,
                          AttributeSet attrs,
                          int defStyle) {

        super(context, attrs, defStyle);

        this.context = context;


        initConfig();
    }


    private void initConfig() {
        width = DensityUtil.dp2px(context, 50);
        paddingTop = paddingDown = DensityUtil.dp2px(context, 10);

        defaultBgDrawable = getResources().getDrawable(R.drawable.letter_bg_shape);

        letterList.addAll(Arrays.asList(defaultLets));

    }

    public void setLetters(List<String> letters) {

        if (letters.isEmpty()) {
            return;
        }
        letterList.addAll(letters);

        invalidate();
    }

    /**
     * 设置字母字体大小
     *
     * @param textSize
     */
    public void setTextSize(int textSize, int textDefaultColor) {
        this.textSize = textSize;
        this.textDefaultColor = textDefaultColor;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height =0;
        for (int i = 0; i < letterList.size(); i++) {
            height+=singleHeight;
        }

        setMeasuredDimension(width, height);
    }

    private static final String TAG = "LetterListView";

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Log.d(TAG, "dispatchTouchEvent: onDraw");
        int letSize = letterList.size();



        for (int i = 0; i < letSize; i++) {
            paint.setColor(textDefaultColor);
            paint.setTextSize(textSize);

            paint.setAntiAlias(true);

            float xPos = width / 2 - paint.measureText(letterList.get(i)) / 2;
            float yPos = singleHeight * i + singleHeight;

            if (i == choose) {
                paint.setColor(textFocusColor);

                paint.setFakeBoldText(true);

            }
            canvas.drawText(letterList.get(i), xPos, yPos, paint);

//            paint.reset();
        }

    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = choose;
        //计算事件在哪个字母的下标index
        final int c = (int) (y / getHeight() * letterList.size());
        Log.d(TAG, "dispatchTouchEvent: y  = " + y + " getHeight()=" + getHeight());
        Log.d(TAG, "dispatchTouchEvent: c  = " + c);
        switch (action) {
            case MotionEvent.ACTION_DOWN:

                if (oldChoose != c && onTouchingLetterChangedListener != null) {
                    if (c >= 0 && c < letterList.size()) {
                        onTouchingLetterChangedListener.onTouchingLetterChanged(letterList.get(c));
                        choose = c;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (oldChoose != c && onTouchingLetterChangedListener != null) {
                    if (c >= 0 && c < letterList.size()) {
                        onTouchingLetterChangedListener.onTouchingLetterChanged(letterList.get(c));
                        choose = c;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:

                invalidate();

                break;
        }
        return true;
    }

    public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener listener) {
        this.onTouchingLetterChangedListener = listener;
    }


    public interface OnTouchingLetterChangedListener {
        void onTouchingLetterChanged(String s);
    }
}
