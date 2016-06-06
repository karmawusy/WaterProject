package cn.wsy.water.utils;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import cn.wsy.water.R;
import cn.wsy.water.base.ThreeView;
import cn.wsy.water.views.CircleButton;
import cn.wsy.water.views.KnobTableView;
import cn.wsy.water.views.OutputView;
import cn.wsy.water.views.RockerView;
import cn.wsy.water.views.SeekBarView;
import cn.wsy.water.views.SwitchView;
import cn.wsy.water.views.WheelView;

/**
 * 非编辑状态view创建提供者
 * Created by ducky on 16/5/17.
 */
public class ViewShowProvider {

    public ViewShowProvider() {
        //new Thread(new GameThread()).start();
    }

    public static SeekBarView createSeekBar(Context context, int x, int y, int defalutColor, int pressColor) {
        SeekBarView seekBarView = new SeekBarView(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        seekBarView.setLayoutParams(params);
        seekBarView.setX(x);
        seekBarView.setY(y);

        return seekBarView;
    }


    public static WheelView createWeelView(Context context, int x, int y, int defalutColor, int pressColor) {
        WheelView wheelView = new WheelView(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        wheelView.setLayoutParams(params);
        wheelView.setX(x);
        wheelView.setY(y);

        return wheelView;
    }

    public static RockerView createRockerView(Context context, int x, int y, int defalutColor, int pressColor) {
        RockerView rockerView = new RockerView(context, null);
        rockerView.setRockerBg(BitmapFactory.decodeResource(context.getResources(), R.drawable.rocket_bg));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(400, 400);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        rockerView.setLayoutParams(params);
        rockerView.setX(x);
        rockerView.setY(y);

        return rockerView;
    }

    //圆按钮
    public static CircleButton createCircleButton(Context context, int x, int y, int defalutColor, int pressColor) {
        CircleButton view = new CircleButton(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(200, 200);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        view.setLayoutParams(params);
        view.setX(x);
        view.setY(y);

        return view;
    }

    //量程表
    public static KnobTableView createKnobView(Context context, int x, int y, int defalutColor, int pressColor) {
        KnobTableView knobView = new KnobTableView(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        knobView.setLayoutParams(params);
        knobView.setX(x);
        knobView.setY(y);

        return knobView;
    }

    //开关
    public static SwitchView createSwitchView(Context context, int x, int y, int defalutColor, int pressColor) {
        SwitchView switchView = new SwitchView(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        switchView.setLayoutParams(params);
        switchView.setX(x);
        switchView.setY(y);

        return switchView;
    }

    //输出框
    public static OutputView createOutputView(Context context, int x, int y, int defalutColor, int pressColor) {
        OutputView outputView = new OutputView(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(650, 450);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        outputView.setLayoutParams(params);
        outputView.setX(x);
        outputView.setY(y);

        return outputView;
    }

    //三轴加速器
    public static ThreeView createThreeView(Context context, int x, int y, int defalutColor, int pressColor) {
        threeView = new ThreeView(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(350, 290);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        threeView.setLayoutParams(params);
        threeView.setX(x);
        threeView.setY(y);

        return threeView;
    }

    private static ThreeView threeView;

    class GameThread implements Runnable {
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                Message message = new Message();
                message.what = 2;
                // 发送消息
                handle.sendMessage(message);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    class MyThread implements Runnable {
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                Message message = new Message();
                message.what = 3;
                // 发送消息
                handle2.sendMessage(message);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private Handler handle;
    private Handler handle2;

    public void setMainViewHandler(Handler handle) {
        this.handle = handle;
        new Thread(new GameThread()).start();
    }

    public void updateMainViewData(Bundle bundle) {
        if (threeView != null) {
            threeView.setData(bundle);
            //Log.i("bundle--->", " " + bundle);
            threeView.invalidate();
        }
    }

    public void setAgainViewHandler(Handler handle2) {
        this.handle2 = handle2;
        new Thread(new MyThread()).start();
    }

    public void updateAgainViewData(Bundle bundle) {
        if (threeView != null) {
            threeView.setData(bundle);
            Log.i("bundle--->", " " + bundle);
            threeView.invalidate();
        }
    }

}
