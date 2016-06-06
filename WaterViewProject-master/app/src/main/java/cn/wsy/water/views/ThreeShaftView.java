package cn.wsy.water.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import cn.wsy.water.R;

/**
 * Created by Wusy on 2016/4/30.
 */
public class ThreeShaftView extends FrameLayout {

    private View view;
    private Context context;

    public ThreeShaftView(Context context) {
        super(context);
    }

    private void initView(Context context){

        this.context = context;

        View view = LayoutInflater.from(context).inflate(R.layout.view_knob_layout,this);
    }


}
