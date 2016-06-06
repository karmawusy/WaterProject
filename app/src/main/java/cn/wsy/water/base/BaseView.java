package cn.wsy.water.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * 添加触摸控制标志
 * Created by Wusy on 2016/4/30.
 */
public class BaseView extends View {

    private boolean isMove = false;

    public BaseView(Context context) {
        super(context);
    }

    public BaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setIsMove(boolean isMove) {
        this.isMove = isMove;
    }

    public boolean getIsNotMove() {
        return !isMove;
    }
}
