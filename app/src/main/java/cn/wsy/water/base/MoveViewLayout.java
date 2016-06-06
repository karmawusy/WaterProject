package cn.wsy.water.base;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.List;

import cn.wsy.water.MainActivity;

/**
 * 移动父容器，必须在最顶层
 * Created by wsy on 2016/4/12.
 */
public class  MoveViewLayout extends FrameLayout {

    private ViewDragHelper dragHelper;
    //Views
    private List<View> views = new ArrayList<>();
    private View currentView;
    private PopupWindow mPopupWindow;
    private View popupWindowView;


    private boolean isMove = true;//是否支持子控件移动

    public MoveViewLayout(Context context) {
        super(context);
        init();
    }

    public MoveViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void deleteCurView(){
        removeView(currentView);
    }

    public View getCurrentView() {
        return currentView;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }


    public void setIsMove(boolean isMove) {
        this.isMove = isMove;
    }

    public void addView(View view){
        if (view !=null){
            views.add(view);
        }
        super.addView(view);
    }

    private void init() {
        dragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {

            @Override
            public boolean tryCaptureView(View view, int i) {
                if (isMove) {
                    return views.contains(view);
                } else {
                    return false;
                }
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                int lefPad = getPaddingLeft();
                int realWidth = getWidth() - child.getWidth();
                int newLeft = Math.min(Math.max(left, lefPad), realWidth);
                return newLeft;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                int topPad = getPaddingTop();
                int realHeight = getHeight() - child.getHeight();
                int newTop = Math.min(Math.max(top, topPad), realHeight);
                return newTop;
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                currentView = changedView;
                MainActivity.instance.showDeleteBtn();
                super.onViewPositionChanged(changedView, left, top, dx, dy);

                Log.i("wusy",left +" "+top +" "+dx+" "+dy + " " );

                //移动重置关键
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(changedView.getWidth(), changedView.getHeight());
                layoutParams.setMargins(left, top, 0, 0);
                changedView.setLayoutParams(layoutParams);

            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return getMeasuredWidth() - child.getMeasuredWidth();
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return getMeasuredHeight() - child.getMeasuredHeight();
            }

        });
    }

    @Override
    public void computeScroll() {
        if (dragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            dragHelper.cancel();
            return false;
        }
        return dragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        dragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        for (int i = 0; i < getChildCount(); i++) {
            views.add(getChildAt(i));
        }
        Log.i("wusy >>>>>", views.size() + "");
    }

    public void resetAllChildView(){
        views.clear();
        for (int i = 0; i < getChildCount(); i++) {
            views.add(getChildAt(i));
        }

        Log.i("wusy reset >>>>>", views.size() + "");
    }
}
