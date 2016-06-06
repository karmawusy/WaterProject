package cn.wsy.water;


import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.wsy.water.app.ViewApplication;
import cn.wsy.water.base.ThreeView;
import cn.wsy.water.db.ViewConfig;
import cn.wsy.water.utils.ViewShowProvider;
import cn.wsy.water.views.CircleButton;
import cn.wsy.water.views.KnobTableView;
import cn.wsy.water.views.OutputView;
import cn.wsy.water.views.RockerView;
import cn.wsy.water.views.SeekBarView;
import cn.wsy.water.views.SwitchView;
import cn.wsy.water.views.WheelView;


/**
 * 主要布局
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private FrameLayout mainLayout;
    private TextView noViewTip;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        mainLayout = (FrameLayout) getActivity().findViewById(R.id.main_contaner_layout);
        noViewTip = (TextView) getActivity().findViewById(R.id.main_noview_tip);

        List<ViewConfig> viewConfigs = ViewApplication.getInstance().getViewConfigs();
        if (viewConfigs.size() > 0) {
            mainLayout.removeAllViews();
            getViewForDb(viewConfigs);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        List<ViewConfig> viewConfigs = ViewApplication.getInstance().getViewConfigs();
        if (viewConfigs.size() > 0) {
            mainLayout.removeAllViews();
            getViewForDb(viewConfigs);
        }
    }

    private void addViews(View view) {
        noViewTip.setVisibility(View.GONE);
        mainLayout.addView(view);
    }

    public void getViewForDb(List<ViewConfig> viewConfigs) {

        for (ViewConfig viewConfig : viewConfigs) {
            getView(viewConfig);
        }
        if (viewConfigs.size() >0)
        MainActivity.instance.setTitle(viewConfigs.get(0).getLayout_name());
    }

    private void getView(ViewConfig viewConfig) {
        int type = viewConfig.view_Type;

        int defalut_color = 0;

        int press_color = 0;

        View view = null;

        switch (type) {
            case 0:
                view = ViewShowProvider.createRockerView(getActivity(),viewConfig.getView_X(), viewConfig.getView_Y(),defalut_color,press_color);
                break;
            case 1:
                view = ViewShowProvider.createWeelView(getActivity(),viewConfig.getView_X(), viewConfig.getView_Y(),defalut_color,press_color);
                break;
            case 2:
                view = ViewShowProvider.createSeekBar(getActivity(),viewConfig.getView_X(), viewConfig.getView_Y(),defalut_color,press_color);;
                break;
            case 3:
                view = ViewShowProvider.createCircleButton(getActivity(),viewConfig.getView_X(), viewConfig.getView_Y(),defalut_color,press_color);
                break;
            case 4:
                view = ViewShowProvider.createSwitchView(getActivity(),viewConfig.getView_X(), viewConfig.getView_Y(),defalut_color,press_color);
                break;
            case 5:
                view = ViewShowProvider.createThreeView(getActivity(),viewConfig.getView_X(), viewConfig.getView_Y(),defalut_color,press_color);
                break;
            case 6:
                view = ViewShowProvider.createKnobView(getActivity(),viewConfig.getView_X(), viewConfig.getView_Y(),defalut_color,press_color);
                break;
            case 7:
                view = ViewShowProvider.createOutputView(getActivity(),viewConfig.getView_X(), viewConfig.getView_Y(),defalut_color,press_color);
                break;
        }

        if (view != null){

            addViews(view);

        }
    }



}
