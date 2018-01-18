package com.colin.signature.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * 作者：张明云
 * 链接：https://www.jianshu.com/p/c4f017603413
 * 來源：简书
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 */

public class SignatureView extends View{
    private static final float STROKE_WIDTH = 5f;
    /**
     * Need to track this so the dirty region can accommodate the stroke.
     **/
    private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;

    static final int BACKGROUND_COLOR = Color.WHITE;
    static final int BRUSH_COLOR = Color.BLACK;

    private WindowManager.LayoutParams lp;

    private Bitmap mBitmap;

    private Paint mPaint;
    private Path mPath;

    private float lastX, lastY;
    private final RectF dirtyRect = new RectF();
    private int width, height;
    private Canvas cacheCanvas;

    public SignatureView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initRes();
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void clear() {
        if (cacheCanvas != null) {
            mPath.reset();
            mPaint.setColor(BACKGROUND_COLOR);
            cacheCanvas.drawPaint(mPaint);
            mPaint.setColor(Color.BLACK);
            cacheCanvas.drawColor(Color.WHITE);
            invalidate();
        }
    }

    private void initRes() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPath = new Path();

    }

    @Override
    protected void onDraw(Canvas canvas) {
//        cacheCanvas = canvas;
        canvas.drawBitmap(mBitmap, 0, 0, null);
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPath.moveTo(eventX, eventY);
                lastX = eventX;
                lastY = eventY;
                return true;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                resetDirtyRect(eventX, eventY);
                int historySize = event.getHistorySize();
                for (int i = 0; i < historySize; i++) {
                    float historyX = event.getHistoricalX(i);
                    float historyY = event.getHistoricalY(i);
                    expandDirtyRect(historyX, historyY);
                    mPath.lineTo(historyX, historyY);
                }
                mPath.lineTo(eventX, eventY);
                cacheCanvas.drawPath(mPath, mPaint);
                break;
            default:
                return false;
        }

        invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH), (int) (dirtyRect.top - HALF_STROKE_WIDTH), (int) (dirtyRect.right + HALF_STROKE_WIDTH), (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

        lastX = eventX;
        lastY = eventY;
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, (int) (h * 0.8), Bitmap.Config.ARGB_8888);
        cacheCanvas = new Canvas(mBitmap);
        cacheCanvas.drawColor(Color.WHITE);
    }

    /**
     * Called when replaying history to ensure the dirty region includes all
     * points.
     */
    private void expandDirtyRect(float historicalX, float historicalY) {
        if (historicalX < dirtyRect.left) {
            dirtyRect.left = historicalX;
        } else if (historicalX > dirtyRect.right) {
            dirtyRect.right = historicalX;
        }
        if (historicalY < dirtyRect.top) {
            dirtyRect.top = historicalY;
        } else if (historicalY > dirtyRect.bottom) {
            dirtyRect.bottom = historicalY;
        }
    }

    /**
     * Resets the dirty region when the motion event occurs.
     */
    private void resetDirtyRect(float eventX, float eventY) {
        // The lastTouchX and lastTouchY were set when the ACTION_DOWN
        // motion event occurred.
        dirtyRect.left = Math.min(lastX, eventX);
        dirtyRect.right = Math.max(lastX, eventX);
        dirtyRect.top = Math.min(lastY, eventY);
        dirtyRect.bottom = Math.max(lastY, eventY);
    }

}
