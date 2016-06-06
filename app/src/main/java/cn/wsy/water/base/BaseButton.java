package cn.wsy.water.base;

import android.content.Context;
import android.widget.Button;

/**
 * Created by Wusy on 2016/4/30.
 */
public class BaseButton extends Button{

    private boolean isMove = false;

    public BaseButton(Context context) {
        super(context);
    }

    public void setIsMove(boolean isMove) {
        this.isMove = isMove;
    }

    public boolean isNotMove() {
        return !isMove;
    }
}
