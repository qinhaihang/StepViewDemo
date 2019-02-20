package com.sensetime.qinhaihang_vendor.stepviewdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author qinhaihang_vendor
 * @version $Rev$
 * @time 2019/1/21 14:54
 * @des
 * @packgename com.sensetime.qinhaihang_vendor.stepviewdemo
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes
 */
public class StepView extends View {

    private Context mContext;
    private Paint mPaint;
    private int mStepCount;
    private int mStepDefaultBgId;

    //控件默认的宽高
    int defaultWidth = 300;
    int defaultHeight = 200;

    int dividerX = 0;
    int bgX;

    private int mStepDividerID;
    private Bitmap mStepBgBitmap = null;
    private Bitmap mStepDividerBitmap = null;
    private Bitmap mStepSelectBgBitmap;
    private Bitmap mStepSelectSymbolBitmap;
    private Paint mTextPaint;

    private int mTextColor;
    private int mTextSelectColor;
    private float mTextSize;
    private int mStepNumSelect = 0;
    private int mStepSelectBgId;
    private int mStepSelectSymbolId;
    private int mRecordSelectX;

    public StepView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public StepView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public StepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        mContext = context;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StepView);

        mStepCount = typedArray.getInt(R.styleable.StepView_step_count, 1);
        mStepDefaultBgId = typedArray.getResourceId(R.styleable.StepView_step_default_bg, R.mipmap.collect_circle_step_unselect);
        mStepDividerID = typedArray.getResourceId(R.styleable.StepView_step_divider, R.mipmap.xvxian);
        mStepSelectBgId = typedArray.getResourceId(R.styleable.StepView_step_select_bg, R.mipmap.collect_circle_step_select);
        mStepSelectSymbolId = typedArray.getResourceId(R.styleable.StepView_step_select_symbol, R.mipmap.duihao);
        mTextColor = typedArray.getColor(R.styleable.StepView_step_text_default_color,Color.BLACK);
        mTextSelectColor = typedArray.getColor(R.styleable.StepView_step_text_select_color,Color.BLACK);
        mTextSize = typedArray.getDimension(R.styleable.StepView_step_text_size,30);

        typedArray.recycle();

        initPaint();

        if (mStepDividerID != 0) {
            mStepDividerBitmap = BitmapFactory.decodeResource(mContext.getResources(), mStepDividerID);
        }
        if (mStepSelectBgId != 0) {
            mStepSelectBgBitmap = BitmapFactory.decodeResource(mContext.getResources(), mStepSelectBgId);
        }
        if (mStepSelectSymbolId != 0) {
            mStepSelectSymbolBitmap = BitmapFactory.decodeResource(mContext.getResources(), mStepSelectSymbolId);
        }
        mStepBgBitmap = BitmapFactory.decodeResource(mContext.getResources(), mStepDefaultBgId);
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int withSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT
                && getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(defaultWidth, defaultHeight);
        } else if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(defaultWidth, heightSize);
        } else if (getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(withSize, defaultHeight);
        } else{
            setMeasuredDimension(withSize, heightSize);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (null != mStepDividerBitmap) {
            drawWithDivider(canvas);
        }
    }

    private void drawWithDivider(Canvas canvas) {

        for (int i = 0; i < mStepCount; i++) {

            if (i < mStepNumSelect) {
                drawSelectBg(canvas,i);
                drawSymbol(canvas, i);
            } else {
                drawDefaultBg(canvas,i);
                drawText(canvas, i);
            }

            if (i != mStepCount - 1) {
                canvas.drawBitmap(mStepDividerBitmap, dividerX,
                        mStepBgBitmap.getHeight() < mStepSelectBgBitmap.getHeight()
                                 ? mStepSelectBgBitmap.getHeight() / 2 : mStepBgBitmap.getHeight() / 2,
                        mPaint);
            }

        }

        defaultHeight = mStepBgBitmap.getHeight() < mStepSelectBgBitmap.getHeight() ?
                mStepSelectBgBitmap.getHeight() : mStepBgBitmap.getHeight();

        defaultWidth = mStepBgBitmap.getWidth() < mStepSelectBgBitmap.getWidth()
                ? mStepSelectBgBitmap.getWidth() * mStepCount + mStepDividerBitmap.getWidth() * mStepCount
                : mStepBgBitmap.getWidth() * mStepCount + mStepDividerBitmap.getWidth() * mStepCount;

        requestLayout();
    }

    private void drawSelectBg(Canvas canvas, int i){
        int selectBgX = mStepSelectBgBitmap.getWidth() * i + mStepDividerBitmap.getWidth() * i;
        canvas.drawBitmap(mStepSelectBgBitmap, selectBgX, 0, mPaint);
        dividerX = mStepSelectBgBitmap.getWidth() * (i + 1) + mStepDividerBitmap.getWidth() * i;
        mRecordSelectX = dividerX + mStepDividerBitmap.getWidth();
    }

    private void drawDefaultBg(Canvas canvas, int i){
        if(i >= mStepNumSelect){
            int defaultBgX = mRecordSelectX +
                    (mStepBgBitmap.getWidth() + mStepDividerBitmap.getWidth())*(i - mStepNumSelect);
            canvas.drawBitmap(mStepBgBitmap, defaultBgX, 0, mPaint);
            dividerX = mRecordSelectX
                    + mStepBgBitmap.getWidth()*(i - mStepNumSelect + 1)
                    + mStepDividerBitmap.getWidth()*(i - mStepNumSelect);
        }

    }

    private void drawText(Canvas canvas, int i) {
        String text = 1 + i + "";
        float textWidth = mTextPaint.measureText(text);
        float textHeightHalf = Math.abs(mTextPaint.ascent() + mTextPaint.descent()) / 2;
        mTextPaint.setColor(mTextColor);
        canvas.drawText(text,
                mRecordSelectX
                        + mStepBgBitmap.getWidth() / 2 - textWidth / 2
                        + (mStepBgBitmap.getWidth() + mStepDividerBitmap.getWidth()) * (i - mStepNumSelect),
                mStepBgBitmap.getHeight() / 2 + textHeightHalf, mTextPaint);

    }

    private void drawSymbol(Canvas canvas, int i) {
        if (null != mStepSelectSymbolBitmap) {

            if (i == mStepNumSelect - 1) {
                mTextPaint.setColor(mTextSelectColor);
                String text = 1 + i + "";
                float textWidth = mTextPaint.measureText(text);
                float textHeightHalf = Math.abs(mTextPaint.ascent() + mTextPaint.descent()) / 2;
                canvas.drawText(text,
                        mStepSelectBgBitmap.getWidth() / 2 - textWidth / 2 + (mStepSelectBgBitmap.getWidth() + mStepDividerBitmap.getWidth()) * i,
                        mStepSelectBgBitmap.getHeight() / 2 + textHeightHalf, mTextPaint);
            } else {
                canvas.drawBitmap(mStepSelectSymbolBitmap,
                        mStepSelectBgBitmap.getWidth() / 2 - mStepSelectSymbolBitmap.getWidth() / 2
                                + (mStepSelectBgBitmap.getWidth() + mStepDividerBitmap.getWidth()) * i,
                        mStepSelectBgBitmap.getWidth() / 2 - mStepSelectSymbolBitmap.getHeight() / 2, mPaint);
            }
        }

    }

    public void setStep(int stepNum) {
        if (stepNum <= mStepCount) {
            mStepNumSelect = stepNum;
            invalidate();
        }
    }

}
