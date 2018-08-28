package com.markchan.androidsurfaceviewboilerplate;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.os.Build.VERSION_CODES;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * @author Mark Chan <a href="markchan2gm@gmail.com">Contact me.</a>
 * @version 1.0
 * @since 8/28/18
 */
public class MarkSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    public MarkSurfaceView(Context context) {
        this(context, null, 0);
    }

    public MarkSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MarkSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = VERSION_CODES.LOLLIPOP)
    public MarkSurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private SurfaceHolder mHolder;
    /** 用于绘制的Canvas */
    private Canvas mCanvas;
    /** 子线程标志位 */
    private boolean mDrawing;

    private Paint mPaint;
    int mX;
    int mY;
    private Path mPath;

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        mHolder = getHolder();
        mHolder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setKeepScreenOn(true);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);
        mX = 0;
        mY = 400;
        mPath = new Path();
        mPath.moveTo(mX, mY);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mDrawing = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mDrawing = false;
    }

    @Override
    public void run() {
        while (mDrawing) {
            draw();
            mX += 1;
            mY = (int) (100 * Math.sin(mX * 2 * Math.PI / 180) + 400);
            mPath.lineTo(mX, mY);
        }
    }
    private void draw() {
        try {
            mCanvas = mHolder.lockCanvas();
            doDraw(mCanvas);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mCanvas != null) {
                mHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

    private void doDraw(Canvas canvas) {
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvas.drawColor(Color.WHITE);
        canvas.drawPath(mPath, mPaint);
    }
}
