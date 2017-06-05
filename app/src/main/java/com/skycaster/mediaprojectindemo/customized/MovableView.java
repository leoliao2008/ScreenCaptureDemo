package com.skycaster.mediaprojectindemo.customized;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

/**
 * Created by 廖华凯 on 2017/6/5.
 */

public class MovableView extends View {
    private FrameLayout.LayoutParams mLayoutParams;
    public MovableView(Context context) {
        this(context,null);
    }

    public MovableView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MovableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mLayoutParams= (FrameLayout.LayoutParams) getLayoutParams();
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

    }


    private int x;
    private int y;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mLayoutParams==null){
            return false;
        }
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                x= (int) event.getRawX();
                y= (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                mLayoutParams.leftMargin+= event.getRawX()-x;
                mLayoutParams.topMargin+= event.getRawY()-y;
                setLayoutParams(mLayoutParams);
                requestLayout();
                x= (int) event.getRawX();
                y= (int) event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return true;
    }
}
