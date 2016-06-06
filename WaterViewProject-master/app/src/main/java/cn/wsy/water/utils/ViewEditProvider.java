package cn.wsy.water.utils;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import cn.wsy.water.R;
import cn.wsy.water.base.ThreeView;
import cn.wsy.water.interfaces.ViewOpenEditPop;
import cn.wsy.water.views.CircleButton;
import cn.wsy.water.views.KnobTableView;
import cn.wsy.water.views.OutputView;
import cn.wsy.water.views.RockerView;
import cn.wsy.water.views.SeekBarView;
import cn.wsy.water.views.SwitchView;
import cn.wsy.water.views.WheelView;

/**
 * 编辑控件提供者
 * Created by ducky on 16/5/17.
 */
public class ViewEditProvider {

    //默认

    public SeekBarView createSeekBar(Context context,ViewOpenEditPop listener) {
        SeekBarView seekBarView = new SeekBarView(context,listener);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        seekBarView.setLayoutParams(params);
        seekBarView.setIsPause(true);
        return seekBarView;
    }


    public WheelView createWeelView(Context context,ViewOpenEditPop listener) {
        WheelView wheelView = new WheelView(context,listener);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        wheelView.setLayoutParams(params);
        return wheelView;
    }

    public RockerView createRockerView(Context context,ViewOpenEditPop listener) {
        RockerView rockerView = new RockerView(context, listener);
        rockerView.setRockerBg(BitmapFactory.decodeResource(context.getResources(), R.drawable.rocket_bg));
        rockerView.setIsPause(true);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(400, 400);
        params.gravity = Gravity.CENTER;
        rockerView.setLayoutParams(params);
        return rockerView;
    }

    //圆按钮
    public CircleButton createCircleButton(Context context,ViewOpenEditPop listener) {
        CircleButton view = new CircleButton(context, listener);/** 必须注入才能回调打开pop*/
        view.setIsMove(true);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(200,200);
        params.gravity = Gravity.CENTER;
        view.setLayoutParams(params);
        return view;
    }

    //开关
    public SwitchView createSwitchView(Context context,ViewOpenEditPop listener) {
        SwitchView switchView = new SwitchView(context,listener);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        switchView.setLayoutParams(params);
        return switchView;
    }

    //量程表
    public KnobTableView createKnobView(Context context,ViewOpenEditPop listener) {
        KnobTableView knobView = new KnobTableView(context,listener);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        knobView.setLayoutParams(params);
        return knobView;
    }

    //输出框
    public OutputView createOutputView(Context context,ViewOpenEditPop listener) {
        OutputView outputView = new OutputView(context,listener);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(650, 450);
        params.gravity = Gravity.CENTER;
        outputView.setLayoutParams(params);
        outputView.setIsMove(true);
        return outputView;
    }

    //三轴加速器
    public ThreeView createThreeView(Context context,ViewOpenEditPop listener) {
        ThreeView threeView = new ThreeView(context,listener);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(350, 290);
        params.gravity = Gravity.CENTER;
        threeView.setLayoutParams(params);
        threeView.setIsMove(true);
        return threeView;
    }


    //有添加属性
    public static SeekBarView createSeekBar(Context context,int x, int y, int defalutColor, int pressColor,ViewOpenEditPop listener) {
        SeekBarView seekBarView = new SeekBarView(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(x, y, 0, 0);
        seekBarView.setLayoutParams(params);
        seekBarView.setIsPause(true);
        seekBarView.setPopListener(listener);
        return seekBarView;
    }


    public static WheelView createWeelView(Context context,int x, int y, int defalutColor, int pressColor,ViewOpenEditPop listener) {
        WheelView wheelView = new WheelView(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(x, y, 0, 0);
        wheelView.setLayoutParams(params);
//        wheelView.setX(x);
//        wheelView.setY(y);
        wheelView.setPopListener(listener);
        return wheelView;
    }

    public static RockerView createRockerView(Context context,int x, int y, int defalutColor, int pressColor,ViewOpenEditPop listener) {
        RockerView rockerView = new RockerView(context);
        rockerView.setRockerBg(BitmapFactory.decodeResource(context.getResources(), R.drawable.rocket_bg));
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(400,400);
        params.setMargins(x, y, 0, 0);
        rockerView.setLayoutParams(params);
        rockerView.setIsPause(true);
        rockerView.setPopListener(listener);
        return rockerView;
    }

    //圆按钮
    public static CircleButton createCircleButton(Context context,int x, int y, int defalutColor, int pressColor,ViewOpenEditPop listener) {

        CircleButton view = new CircleButton(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(200,200);
        params.setMargins(x, y, 0, 0);
        view.setLayoutParams(params);
//        view.setX(x);
//        view.setY(y);
        view.setPopListener(listener);

        return view;
    }

    //量程表
    public static KnobTableView createKnobView(Context context,int x, int y, int defalutColor, int pressColor,ViewOpenEditPop listener) {
        KnobTableView knobView = new KnobTableView(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(x, y, 0, 0);
        knobView.setLayoutParams(params);
//        knobView.setX(x);
//        knobView.setY(y);
        knobView.setPopListener(listener);
        return knobView;
    }

    //开关
    public static SwitchView createSwitchView(Context context,int x, int y, int defalutColor, int pressColor,ViewOpenEditPop listener) {
        SwitchView switchView = new SwitchView(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(x, y, 0, 0);
        switchView.setLayoutParams(params);
//        switchView.setX(x);
//        switchView.setY(y);
        switchView.setPopListener(listener);

        return switchView;
    }

    //输出框
    public static OutputView createOutputView(Context context,int x, int y, int defalutColor, int pressColo,ViewOpenEditPop listener) {
        OutputView outputView = new OutputView(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(650, 450);
        params.setMargins(x, y, 0, 0);
        outputView.setLayoutParams(params);
//        outputView.setX(x);
//        outputView.setY(y);
        outputView.setIsMove(true);
        outputView.setPopListenr(listener);
        return outputView;
    }

    //三轴加速器
    public static ThreeView createThreeView(Context context,int x, int y, int defalutColor, int pressColor,ViewOpenEditPop listener) {
        ThreeView threeView = new ThreeView(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(350, 290);
        params.setMargins(x, y, 0, 0);
        threeView.setLayoutParams(params);
//        threeView.setX(x);
//        threeView.setY(y);
        threeView.setIsMove(true);
        threeView.setPopListenr(listener);
        return threeView;
    }


}
