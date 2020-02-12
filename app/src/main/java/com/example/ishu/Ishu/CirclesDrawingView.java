package com.example.ishu.Ishu;

import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.nfc.Tag;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;


public class CirclesDrawingView extends View {

    private static class RectArea {
        String type;
        int id;
        int centerX;
        int centerY;
        float height,width;
        RectArea(int centerX, int centerY, float height,float width,int id,String type) {
            this.type = type;
            this.id = id;
            this.height = height;
            this.width = width;
            this.centerX = centerX;
            this.centerY = centerY;
        }
        @Override
        public String toString() {
            return "Rect[" + centerX + ", " + centerY + ", " + height +","+width+ ","+id+"]";
        }
    }

    private static final String TAG = "RectDrawingView";
    private Bitmap mBitmap = null;
    private Rect mMeasuredRect;
    private SharedPreferences prefs;
    private Paint mRectPaint;
    private Paint mPaint;
    private float height,width,window_width,window_height;
    private String name;
    private static final int CIRCLES_LIMIT = 5;
    private List<RectArea> mRect = new ArrayList<RectArea>(CIRCLES_LIMIT);
    private HashSet<RectArea> mRectDel = new HashSet<RectArea>(CIRCLES_LIMIT);
    private SparseArray<RectArea> mRectPointer = new SparseArray<RectArea>(CIRCLES_LIMIT);
    public CirclesDrawingView(final Context ct) {
        super(ct);
        init(ct);
    }

    public CirclesDrawingView(final Context ct, final AttributeSet attrs) {
        super(ct, attrs);
        init(ct);
    }

    public CirclesDrawingView(final Context ct, final AttributeSet attrs, final int defStyle) {
        super(ct, attrs, defStyle);
        init(ct);
    }

    private void init(final Context ct) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(25);
        prefs = PreferenceManager.getDefaultSharedPreferences(ct);
        name = String.valueOf(prefs.getString("name","dd"));
        height = prefs.getFloat("height",78);
        width = prefs.getFloat("width",0);
        mBitmap = BitmapFactory.decodeResource(ct.getResources(), R.drawable.up);
        mRectPaint = new Paint();
        mRectPaint.setColor(Color.BLACK);
        mRectPaint.setStrokeWidth(8);
        mRectPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void onDraw(final Canvas canv) {
        window_height = Float.parseFloat(String.valueOf(getHeight()));
        window_width = Float.parseFloat(String.valueOf(getWidth()));
        float temp = (window_width-40)/width;
        for(int i=0;i<10;i++)
        {
            int x = prefs.getInt("Rect"+i+"h",0);
            int y = prefs.getInt("Rect"+i+"w",0);
            String tx = prefs.getString("Rect"+i+"t"," ");
            Boolean flag = true;
            for(RectArea rect:mRect){
                if(rect.centerX==100 && rect.centerY==100 && rect.height == (y*temp) && rect.width == (x*temp)){
                    flag = false ;
                }
            }
            if(x!=0 && y!=0 && flag==true)
            {
                RectArea touchedRect = new RectArea(100,100,y*temp,x*temp,i,tx);
                mRect.add(touchedRect);
            }
        }
        normalRect();
        canv.drawBitmap(mBitmap, null, mMeasuredRect, null);
        canv.drawRect(20,20,temp*width+20,temp*height+20,mRectPaint);
        for (RectArea rect : mRect) {
            canv.drawRect(rect.centerX,rect.centerY,rect.width+rect.centerX,rect.height+rect.centerY,mRectPaint);
            String pageTitle = rect.type;
            RectF bounds = new RectF(new Rect(rect.centerX,rect.centerY,(int)rect.height,(int)rect.width));
            bounds.right = mPaint.measureText(pageTitle, 0, pageTitle.length());
            bounds.bottom = mPaint.descent() - mPaint.ascent();
            bounds.left += (rect.width - bounds.right) / 2.0f;
            bounds.top += (rect.height - bounds.bottom) / 2.0f;
            mPaint.setColor(Color.BLACK);
            canv.drawText(pageTitle, bounds.left, bounds.top - mPaint.ascent(), mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        boolean handled = false;

        RectArea touchedCircle;
        int xTouch;
        int yTouch;
        int pointerId;
        int actionIndex = event.getActionIndex();
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                Log.w(TAG,"Down");
                clearCirclePointer();
                xTouch = (int) event.getX(0);
                yTouch = (int) event.getY(0);
                touchedCircle = obtainTouchedCircle(xTouch, yTouch);
                if(touchedCircle != null) {
                    mRectDel.add(touchedCircle);
                    touchedCircle.centerX = xTouch;
                    touchedCircle.centerY = yTouch;
                    mRectPointer.put(event.getPointerId(0), touchedCircle);
                }
                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                Log.w(TAG, "Pointer down");
                pointerId = event.getPointerId(actionIndex);
                xTouch = (int) event.getX(actionIndex);
                yTouch = (int) event.getY(actionIndex);
                touchedCircle = obtainTouchedCircle(xTouch, yTouch);
                mRectPointer.put(pointerId, touchedCircle);
                touchedCircle.centerX = xTouch;
                touchedCircle.centerY = yTouch;
                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_MOVE:
                final int pointerCount = event.getPointerCount();

                Log.w(TAG, "Move");

                for (actionIndex = 0; actionIndex < pointerCount; actionIndex++) {
                    pointerId = event.getPointerId(actionIndex);
                    xTouch = (int) event.getX(actionIndex);
                    yTouch = (int) event.getY(actionIndex);
                    touchedCircle = mRectPointer.get(pointerId);
                    if (null != touchedCircle) {
                        touchedCircle.centerX = xTouch;
                        touchedCircle.centerY = yTouch;
                    }
                }
                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_UP:
                clearCirclePointer();
                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_POINTER_UP:
                pointerId = event.getPointerId(actionIndex);
                mRectPointer.remove(pointerId);
                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_CANCEL:
                handled = true;
                break;

            default:
                break;
        }
        return super.onTouchEvent(event) || handled;
    }

    private void clearCirclePointer() {
        Log.w(TAG, "clearCirclePointer");
        mRectPointer.clear();
    }

    private RectArea obtainTouchedCircle(final int xTouch, final int yTouch) {
        RectArea touchedRect = getTouchedCircle(xTouch, yTouch);
        return touchedRect;
    }

    private RectArea getTouchedCircle(final int xTouch, final int yTouch) {
        RectArea touched = null;
        Log.w(TAG, "X :" + xTouch+"Y :"+ yTouch);
        int z=0;
        for (RectArea rect : mRect) {
            if (xTouch >= rect.centerX && xTouch <= (rect.centerX+rect.width) && yTouch >= rect.centerY && yTouch <=(rect.centerY+rect.height)) {
                touched = rect;
                break;
            }
        }
        return touched;
    }

    private void normalRect(){
        for(int i=0;i<mRect.size();i++){
            for(int j=i+1;j<mRect.size();j++){
                RectArea tx,ty;
                tx = mRect.get(i);
                ty = mRect.get(j);
                if(tx.id==ty.id){
                    if(tx.centerX==100){
                        mRect.remove(i);
                    }
                    else {
                        mRect.remove(j);
                    }
                }
            }
        }
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mMeasuredRect = new Rect(0, 0, getMeasuredWidth(), getMeasuredHeight());
    }
}