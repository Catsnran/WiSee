package com.wisee.ui.views;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.*;

/**
 * Custom View untuk menggambar tulisan tangan.
 * Menghasilkan Bitmap hitam-di-putih untuk KNN.
 *
 * FIX #4: requestDisallowInterceptTouchEvent(true) agar ScrollView
 * tidak mencuri touch event saat user menggambar.
 */
public class DrawingView extends View {

    private Bitmap  bitmap;
    private Canvas  bitmapCanvas;
    private Paint   drawPaint;
    private Paint   bgPaint;
    private Path    path;
    private float   lastX, lastY;

    // Stroke size: besar agar mudah ditulis anak-anak
    private static final float STROKE_WIDTH = 18f;

    public DrawingView(Context context) { this(context, null); }
    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        path = new Path();

        drawPaint = new Paint();
        drawPaint.setColor(Color.BLACK);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(STROKE_WIDTH);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        bgPaint = new Paint();
        bgPaint.setColor(Color.WHITE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        if (w > 0 && h > 0) {
            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            bitmapCanvas = new Canvas(bitmap);
            bitmapCanvas.drawRect(0, 0, w, h, bgPaint);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (bitmap != null) canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.drawPath(path, drawPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX(), y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                path.moveTo(x, y);
                lastX = x; lastY = y;
                break;

            case MotionEvent.ACTION_MOVE:
                path.quadTo(lastX, lastY, (x + lastX) / 2, (y + lastY) / 2);
                lastX = x; lastY = y;
                if (bitmapCanvas != null) {
                    bitmapCanvas.drawPath(path, drawPaint);
                    path.reset();
                    path.moveTo(lastX, lastY);
                }
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                path.lineTo(x, y);
                if (bitmapCanvas != null) bitmapCanvas.drawPath(path, drawPaint);
                path.reset();
                invalidate();
                break;
        }
        return true;
    }

    /** Bersihkan canvas */
    public void clear() {
        path.reset();
        if (bitmap != null && bitmapCanvas != null) {
            bitmapCanvas.drawRect(0, 0, bitmap.getWidth(), bitmap.getHeight(), bgPaint);
        }
        invalidate();
    }

    /** Ambil bitmap hasil drawing */
    public Bitmap getBitmap() {
        return bitmap;
    }

    /** Cek apakah canvas kosong (hampir seluruhnya putih) */
    public boolean isEmpty() {
        if (bitmap == null) return true;
        int w = bitmap.getWidth(), h = bitmap.getHeight();
        int step = Math.max(1, w * h / 500);
        int white = 0, total = 0;
        for (int i = 0; i < w * h; i += step) {
            int px = bitmap.getPixel(i % w, i / w);
            if (((px >> 16) & 0xFF) > 230) white++;
            total++;
        }
        return (float) white / total > 0.97f;
    }

    /** Set stroke width */
    public void setStrokeWidth(float width) {
        drawPaint.setStrokeWidth(width);
    }

    /** Set stroke color */
    public void setStrokeColor(int color) {
        drawPaint.setColor(color);
    }
}
