package cn.wsy.water;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import cn.wsy.water.utils.ViewEditProvider;
import cn.wsy.water.utils.ViewShowProvider;
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
public class AgainEditFragment extends Fragment implements ViewOpenEditPop {

    private List<ViewConfig> viewConfigs = new ArrayList<>();
    private Context context;

    private TextView tvTip;
    private MoveViewLayout mainLayout;
    private EditPopWindowView popWindowView;

    private String oldLayoutId = "";
    private ViewShowProvider mViewShowProvider;

    public AgainEditFragment() {
        // Required empty public constructor
        Log.i("karma","hello againedit!");
        mViewShowProvider=new ViewShowProvider();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_again_edit, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();

        mainLayout = (MoveViewLayout) getActivity().findViewById(R.id.edit_contaner);
        tvTip = (TextView) getActivity().findViewById(R.id.edit_tip);

        initViewForDb();

    }

    public void initViewForDb() {

        viewConfigs = ViewApplication.getInstance().getViewConfigs();

        if (viewConfigs.size() > 0) {
            mainLayout.removeAllViews();
            for (ViewConfig view : viewConfigs) {
                getView(view);
            }
        }
//        mainLayout.resetAllChildView();
//        mainLayout.invalidate();
        if (viewConfigs.size() >0) {
            MainActivity.instance.setTitle(viewConfigs.get(0).getLayout_name());
            oldLayoutId = viewConfigs.get(0).getLayout_id();
        }
    }

    private void getView(ViewConfig viewConfig) {
        int type = viewConfig.view_Type;

        int postionX = viewConfig.getView_X();
        int postionY = viewConfig.getView_Y();
//        int defalut_color = viewConfig.getDefault_color();
        int defalut_color = 0;
        int press_color = 0;

        View view = null;

        switch (type) {
            case 0:
                view = ViewEditProvider.createRockerView(context, postionX, postionY, defalut_color, press_color,this);
                break;
            case 1:
                view = ViewEditProvider.createWeelView(context,postionX,postionY,defalut_color,press_color,this);
                break;
            case 2:
                view = ViewEditProvider.createSeekBar(context,postionX,postionY,defalut_color,press_color,this);
                break;
            case 3:
                view = ViewEditProvider.createCircleButton(context,postionX,postionY,defalut_color,press_color,this);
                break;
            case 4:
                view = ViewEditProvider.createSwitchView(context,postionX,postionY,defalut_color,press_color,this);
                break;
            case 5:
                view = ViewEditProvider.createThreeView(context,postionX,postionY,defalut_color,press_color,this);
                break;
            case 6:
                view = ViewEditProvider.createKnobView(context,postionX,postionY,defalut_color,press_color,this);
                break;
            case 7:
                view = ViewEditProvider.createOutputView(context,postionX,postionY,defalut_color,press_color,this);
                break;
        }

        if (view !=null){

            addViews(view);

        }
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
            viewDB.updateViewConfig(config,oldLayoutId);
        }


//        for (ViewConfig viewConfig : viewConfigs){
//            viewDB.updateViewConfig(viewConfig);
//        }

        Toast.makeText(getActivity(), "保存成功！", Toast.LENGTH_SHORT).show();
        ViewApplication.getInstance().readDataBaseForView();
        MainActivity.instance.showMyLayout();

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

    }
    public void setAgainHandler(Handler handle) {
        mViewShowProvider.setAgainViewHandler(handle);
    }

    public void updateAgainData(Bundle bundle) {
        mViewShowProvider.updateAgainViewData(bundle);
    }
}
