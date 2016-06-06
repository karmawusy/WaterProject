package cn.wsy.water;


import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cn.wsy.water.app.Contacts;
import cn.wsy.water.app.ViewApplication;
import cn.wsy.water.base.MoveViewLayout;
import cn.wsy.water.base.ThreeView;
import cn.wsy.water.db.ViewConfig;
import cn.wsy.water.db.ViewDB;
import cn.wsy.water.interfaces.ViewOpenEditPop;
import cn.wsy.water.viewAction.CircleButtonPopAction;
import cn.wsy.water.viewAction.KnobPopAction;
import cn.wsy.water.viewAction.OutputPopAction;
import cn.wsy.water.viewAction.RockerPopAction;
import cn.wsy.water.viewAction.SeekBarPopAction;
import cn.wsy.water.viewAction.SwitchPopAction;
import cn.wsy.water.viewAction.ThreePopAction;
import cn.wsy.water.viewAction.WheelPopAction;
import cn.wsy.water.views.CircleButton;
import cn.wsy.water.views.EditPopWindowView;
import cn.wsy.water.views.KnobTableView;
import cn.wsy.water.views.OutputView;
import cn.wsy.water.views.RockerView;
import cn.wsy.water.views.SeekBarView;
import cn.wsy.water.views.SwitchView;
import cn.wsy.water.views.WheelView;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditFragment extends Fragment implements ViewOpenEditPop {

    private ThreeView threeView;


    private TextView tvTip;
    public static EditFragment instance;
    private MoveViewLayout mainLayout;
    private EditPopWindowView popWindowView;

    private Context context;

    private List<ViewConfig> allViews = new ArrayList<>();

    public EditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        threeView = new ThreeView(context);
        new Thread(new GameThread()).start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i("kevin", "创建视图！");
        return inflater.inflate(R.layout.fragment_edit, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        instance = this;
        mainLayout = (MoveViewLayout) getActivity().findViewById(R.id.edit_contaner);
        tvTip = (TextView) getActivity().findViewById(R.id.edit_tip);

        MainActivity.instance.setTitle("添加佈局");

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("kevin", "生命开始");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("kevin", "销毁视图");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("kevin", "生命结束");

    }


    public void addViews(View view) {
        tvTip.setVisibility(View.GONE);
        mainLayout.addView(view);
        mainLayout.setIsMove(true);
    }

    public void deleteCurrentView() {
        mainLayout.deleteCurView();
    }

    //保存按鈕action
    public void saveAllViewsToDB(String name, String time) {
        ViewDB viewDB = ViewDB.getInstance(getActivity());
        String id = UUID.randomUUID().toString().substring(0, 7);
        for (int i = 0; i < mainLayout.getChildCount(); i++) {
            ViewConfig config = new ViewConfig();
            config.setLayout_name(name);
            config.setCreate_ime(time);
            config.setLayout_id(id);
            View view = mainLayout.getChildAt(i);

            if (view == tvTip) {
                continue;
            }

            if (view instanceof RockerView) {
                config.setView_Type(Contacts.ROCKERVIEW_TYPE);
                config.setView_X((int) view.getX());
                config.setView_Y((int) view.getY());
            } else if (view instanceof WheelView) {
                config.setView_Type(Contacts.WHELLVIEW_TYPE);
                config.setView_X((int) view.getX());
                config.setView_Y((int) view.getY());
            } else if (view instanceof SeekBarView) {
                config.setView_Type(Contacts.SEEKBARVIEW_TYPE);
                config.setView_X((int) view.getX());
                config.setView_Y((int) view.getY());
            } else if (view instanceof CircleButton) {
                config.setView_Type(Contacts.CIRCLEBUTTON_TYPE);
                config.setView_X((int) view.getX());
                config.setView_Y((int) view.getY());
            } else if (view instanceof SwitchView) {
                config.setView_Type(Contacts.SWTCHVIEW_TYPE);
                config.setView_X((int) view.getX());
                config.setView_Y((int) view.getY());
            } else if (view instanceof ThreeView) {
                config.setView_Type(Contacts.THREEVIEW_TYPE);
                config.setView_X((int) view.getX());
                config.setView_Y((int) view.getY());
            } else if (view instanceof KnobTableView) {
                config.setView_Type(Contacts.KNOBTABLE_TYPE);
                config.setView_X((int) view.getX());
                config.setView_Y((int) view.getY());
            } else if (view instanceof OutputView) {
                config.setView_Type(Contacts.OUTPUTVIEW_TYPE);
                config.setView_X((int) view.getX());
                config.setView_Y((int) view.getY());
            }
            viewDB.saveViewConfig(config);
        }

        Toast.makeText(getActivity(), "保存成功！", Toast.LENGTH_SHORT).show();
        ViewApplication.getInstance().readDataBaseForView();
        MainActivity.instance.showMyLayout();
        MainActivity.instance.hidSaveBtn();
        MainActivity.instance.hidDeleteBtn();


    }

    @Override
    public void showEditPopWindow(int type, View widget, int layoutID) {
        popWindowView = new EditPopWindowView(getActivity(), layoutID);
        popWindowView.showAtLocation(getActivity().getLayoutInflater().inflate(R.layout.fragment_edit, null), Gravity.RIGHT, 0, 0);

        View parentView = popWindowView.getView();
        /**这里根据不同的组件类型注入的LayoutId,初始化不同的操作对象，这里举例我的按钮**/
        if (type == Contacts.ROCKERVIEW_TYPE) {

            RockerPopAction rockerPopAction = new RockerPopAction((RockerView) widget, parentView, getActivity(),this);

        } else if (type == Contacts.WHELLVIEW_TYPE) {

            WheelPopAction wheelPopAction = new WheelPopAction((WheelView) widget, parentView, getActivity(),this);

        } else if (type == Contacts.SEEKBARVIEW_TYPE) {

            SeekBarPopAction seekBarPopAction = new SeekBarPopAction((SeekBarView) widget, parentView, getActivity(),this);

        } else if (type == Contacts.CIRCLEBUTTON_TYPE) {

            CircleButtonPopAction action = new CircleButtonPopAction((CircleButton) widget, parentView, getActivity(),this);

        } else if (type == Contacts.SWTCHVIEW_TYPE) {

            SwitchPopAction switchPopAction = new SwitchPopAction((SwitchView) widget, parentView, getActivity(),this);

        } else if (type == Contacts.THREEVIEW_TYPE) {
            ThreePopAction threePopAction = new ThreePopAction((ThreeView) widget, parentView, getActivity(),this);


        } else if (type == Contacts.KNOBTABLE_TYPE) {

            KnobPopAction knobPopAction = new KnobPopAction((KnobTableView) widget, parentView, getActivity(),this);
        } else if (type == Contacts.OUTPUTVIEW_TYPE) {

            OutputPopAction outputPopAction = new OutputPopAction((OutputView) widget, parentView, getActivity(),this);
        }

    }

    @Override
    public void dimissPopWindow() {
        popWindowView.dismiss();
    }


    /**
     * 控件创建
     */
    public SeekBarView createSeekBar() {
        SeekBarView seekBarView = new SeekBarView(getActivity(), this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        seekBarView.setLayoutParams(params);
        seekBarView.setIsPause(true);
        addViews(seekBarView);
        return seekBarView;
    }


    public WheelView createWeelView() {
        WheelView wheelView = new WheelView(getActivity(), this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        wheelView.setLayoutParams(params);
        addViews(wheelView);
        return wheelView;
    }

    public RockerView createRockerView() {
        RockerView rockerView = new RockerView(getActivity(), this);
        rockerView.setRockerBg(BitmapFactory.decodeResource(getResources(), R.drawable.rocket_bg));
        rockerView.setIsPause(true);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(400, 400);
        params.gravity = Gravity.CENTER;
        rockerView.setLayoutParams(params);
        addViews(rockerView);
        return rockerView;
    }

    //圆按钮
    public CircleButton createCircleButton() {
        CircleButton view = new CircleButton(getActivity(), this);/** 必须注入才能回调打开pop*/
        view.setIsMove(true);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(200, 200);
        params.gravity = Gravity.CENTER;
        view.setLayoutParams(params);
        addViews(view);
        return view;
    }

    //开关
    public SwitchView createSwitchView() {
        SwitchView switchView = new SwitchView(getActivity(), this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        switchView.setLayoutParams(params);
        addViews(switchView);
        return switchView;
    }

    //量程表
    public KnobTableView createKnobView() {
        KnobTableView knobView = new KnobTableView(getActivity(), this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        knobView.setLayoutParams(params);
        addViews(knobView);
        return knobView;
    }

    //输出框
    public OutputView createOutputView() {
        OutputView outputView = new OutputView(getActivity(), this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(650, 450);
        params.gravity = Gravity.CENTER;
        outputView.setLayoutParams(params);
        outputView.setIsMove(true);
        addViews(outputView);
        return outputView;
    }

    //三轴加速器
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public ThreeView createThreeView() {
        threeView = new ThreeView(getActivity(), this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(350, 290);
        params.gravity = Gravity.CENTER;
        threeView.setLayoutParams(params);
        threeView.setIsMove(true);
        addViews(threeView);
        return threeView;
    }

    class GameThread implements Runnable {
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                Message message = new Message();
                message.what = 1;
                // 发送消息
                handle.sendMessage(message);
                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private Handler handle;

    public void setHandler(Handler handle) {
        this.handle = handle;
    }

    public void updateData(Bundle bundle) {
        threeView.setData(bundle);
        threeView.invalidate();
    }

}
