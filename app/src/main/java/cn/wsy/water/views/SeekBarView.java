package cn.wsy.water.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SeekBar;

import cn.wsy.water.R;
import cn.wsy.water.app.Contacts;
import cn.wsy.water.interfaces.ViewOpenEditPop;

/**
 * 自定义SeekBar 这次屏蔽开关、纵向
 * Created by wsy on 2016/4/13.
 */
public class SeekBarView extends FrameLayout {

    private boolean isPause;
    private SeekBar seekBar;
    private ViewOpenEditPop popListener;

    public SeekBarView(Context context) {
        super(context);
        init(context);
    }

    public SeekBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SeekBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    public SeekBarView(Context context,ViewOpenEditPop popListener){
        this(context);
        this.popListener=popListener;
        if (popListener!=null){
            popListener.showEditPopWindow(Contacts.SEEKBARVIEW_TYPE,this, R.layout.slider_layout);
        }
    }

    public void setPopListener(ViewOpenEditPop popListener) {
        this.popListener = popListener;
    }

    public void setIsPause(boolean isPause) {
        this.isPause = isPause;
    }


    int currentProgess = 0;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_my_seekbar, this);
        seekBar = (SeekBar) view.findViewById(R.id.my_seekbar);


        //添加屏蔽开关
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (isPause) {
                    seekBar.setProgress(currentProgess);
                } else {
                    currentProgess = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                currentProgess = seekBar.getProgress();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }
    float dx=0,dy=0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN){
            dx = event.getX();
            dy = event.getY();

        }else if (event.getAction() == MotionEvent.ACTION_UP){
            /**以松开手移动距离作为判定标准！！！！**/
            float moveX = event.getX() - dx;
            float moveY = event.getY() - dy;

            if (moveX < 0){moveX = -moveX;}
            if (moveY < 0){moveY = -moveY;}

            if ((popListener !=null &&moveX>5)||(popListener !=null &&moveY>5)){
                popListener.dimissPopWindow();
            }else{
                if (popListener !=null)
                popListener.showEditPopWindow(Contacts.SEEKBARVIEW_TYPE, this,R.layout.slider_layout);
            }
        }
        return true;
    }


}
