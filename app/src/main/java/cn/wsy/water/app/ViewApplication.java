package cn.wsy.water.app;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import cn.wsy.water.db.LayoutIndexBean;
import cn.wsy.water.db.ViewConfig;
import cn.wsy.water.db.ViewDB;

/**
 * 全局存储对象
 * Created by Wusy on 2016/4/30.
 */
public class ViewApplication extends Application{

    //控件缓存由MainActivity存储
    private List<ViewConfig> viewConfigs = new ArrayList<>();
    List<LayoutIndexBean> layoutIDs = new ArrayList<>();

    private static ViewApplication instance;
    public static ViewApplication getInstance(){
        if (instance == null){
            instance = new ViewApplication();
        }

        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        readDataBaseForView();
    }

    /**
     * 获取数据库数据
     */
    public void readDataBaseForView(){
        ViewDB viewDB = ViewDB.getInstance(this);
        layoutIDs = viewDB.getLayoutId();
        if (layoutIDs!=null && layoutIDs.size() >0)
            viewConfigs = viewDB.getViewConfig(layoutIDs.get(0).getLayout_id());
    }

    public void readView(String LayoutId){
        ViewDB viewDB = ViewDB.getInstance(this);

        viewConfigs = viewDB.getViewConfig(LayoutId);
    }

    public List<ViewConfig> getViewConfigs() {
        return viewConfigs;
    }

    public void setViewConfigs(List<ViewConfig> viewConfigs) {
        this.viewConfigs = viewConfigs;
    }

    public List<LayoutIndexBean> getLayoutIDs() {
        return layoutIDs;
    }

    public void setLayoutIDs(List<LayoutIndexBean> layoutIDs) {
        this.layoutIDs = layoutIDs;
    }
}
