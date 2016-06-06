package cn.wsy.water.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Karma on 2016/4/26.
 * 数据库
 */
public class ViewDB {
    /**
     * 数据库名
     */
    public static final String DB_NAME = "view_config";
    /**
     * 数据库版本
     */
    public static final int VERSION = 1;
    private static ViewDB sViewDB;
    private SQLiteDatabase db;

    /**
     * 将构造方法私有化
     */
    private ViewDB(Context context) {
        ViewOpenHelper viewOpenHelper = new ViewOpenHelper(context, DB_NAME, null, VERSION);
        db = viewOpenHelper.getWritableDatabase();
    }

    /**
     * 获取ViewDB实例
     *
     * @param context
     * @return
     */
    public synchronized static ViewDB getInstance(Context context) {
        if (sViewDB == null) {
            sViewDB = new ViewDB(context);
        }
        return sViewDB;
    }

   /* *//**
     * 保存xy坐标
     * @param view_Id
     * @param view_X
     * @param view_Y
     *//*
    public void saveXY(String view_Id,float view_X,float view_Y){
        ContentValues values=new ContentValues();
        values.put("view_Id",view_Id);
        values.put("view_X",view_X);
        values.put("view_Y",view_Y);
        db.insert("Config",null,values);
    }
    public float getXY(){
        //查询Config表中的所有数据
        Cursor cursor=db.query("Config",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                String view_Id=cursor.getString(cursor.getColumnIndex("view_Id"));
                float view_X=cursor.getFloat(cursor.getColumnIndex("view_X"));
                float view_Y=cursor.getFloat(cursor.getColumnIndex("view_Y"));
            }while (cursor.moveToNext());
        }
        return 2.0f;
    }*/

    /**
     * 保存自定义view坐标
     *
     * @param viewConfig
     */
    public void saveViewConfig(ViewConfig viewConfig) {
        ContentValues values = new ContentValues();
        values.put("layout_id", viewConfig.getLayout_id());
        values.put("layout_name", viewConfig.getLayout_name());
        values.put("create_time", viewConfig.getCreate_ime());
        values.put("view_Type", viewConfig.getView_Type());
        values.put("default_color", viewConfig.getDefault_color());
        values.put("press_color", viewConfig.getPress_color());
        values.put("view_X", viewConfig.getView_X());
        values.put("view_Y", viewConfig.getView_Y());
        Long state = db.insert("Config", null, values);
        Log.i("wusy state", Long.toString(state));
    }

    public void deleteViewConfig(String laoutId) {
        String[] args = {laoutId};

        int state  = db.delete("Config", "layout_id = ?", args);

        Log.i("wusy >>>>","delete state "+state);
    }

    public void updateViewConfig(ViewConfig viewConfig,String oldLayoutId) {

        this.deleteViewConfig(oldLayoutId);

        ContentValues values = new ContentValues();
        values.put("layout_id", viewConfig.getLayout_id());
        values.put("layout_name", viewConfig.getLayout_name());
        values.put("create_time", viewConfig.getCreate_ime());
        values.put("view_Type", viewConfig.getView_Type());
        values.put("default_color", viewConfig.getDefault_color());
        values.put("press_color", viewConfig.getPress_color());
        values.put("view_X", viewConfig.getView_X());
        values.put("view_Y", viewConfig.getView_Y());
        Long state = db.insert("Config", null, values);
        Log.i("wusy state", Long.toString(state));
    }

    public List<LayoutIndexBean> getLayoutId() {
        String[] colums = new String[]{"layout_id", "layout_name", "create_time"};

        List<LayoutIndexBean> result = new ArrayList<>();
        Cursor cursor = db.query("Config", colums, null, null, "layout_id", null, "_id DESC");
        if (cursor.moveToFirst()) {
            do {
                LayoutIndexBean bean = new LayoutIndexBean();
                bean.setLayout_id(cursor.getString(cursor.getColumnIndex("layout_id")));
                bean.setLayout_name(cursor.getString(cursor.getColumnIndex("layout_name")));
                bean.setCreateTime(cursor.getString(cursor.getColumnIndex("create_time")));
                result.add(bean);
            } while (cursor.moveToNext());
        }
        return result;
    }

    /**
     * 根据layoutid查找view
     *
     * @param layoutId
     * @return
     */
    public List<ViewConfig> getViewConfig(String layoutId) {
        List<ViewConfig> viewConfigs = new ArrayList<ViewConfig>();
        String[] selectionArg = new String[]{layoutId};

        Cursor cursor = db.query("Config", null, "layout_id = ?", selectionArg, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                ViewConfig viewConfig = new ViewConfig();
                viewConfig.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
                viewConfig.setView_Type(cursor.getInt(cursor.getColumnIndex("view_Type")));
                viewConfig.setView_X(cursor.getInt(cursor.getColumnIndex("view_X")));
                viewConfig.setView_Y(cursor.getInt(cursor.getColumnIndex("view_Y")));
                viewConfig.setLayout_name(cursor.getString(cursor.getColumnIndex("layout_name")));
                viewConfig.setLayout_id(cursor.getString(cursor.getColumnIndex("layout_id")));
                viewConfig.setDefault_color(cursor.getString(cursor.getColumnIndex("default_color")));
                viewConfig.setPress_color(cursor.getString(cursor.getColumnIndex("press_color")));
                viewConfigs.add(viewConfig);
            } while (cursor.moveToNext());
        }
        return viewConfigs;
    }

    /**
     * 读取自定义view坐标
     *
     * @return
     */
    public List<ViewConfig> getViewConfig() {
        List<ViewConfig> viewConfigs = new ArrayList<ViewConfig>();
        Cursor cursor = db.query("Config", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                ViewConfig viewConfig = new ViewConfig();
                viewConfig.setView_Type(cursor.getInt(cursor.getColumnIndex("view_Type")));
                viewConfig.setView_X(cursor.getInt(cursor.getColumnIndex("view_X")));
                viewConfig.setView_Y(cursor.getInt(cursor.getColumnIndex("view_Y")));
                viewConfig.setLayout_name(cursor.getString(cursor.getColumnIndex("layout_name")));
                viewConfig.setLayout_id(cursor.getString(cursor.getColumnIndex("layout_id")));
                viewConfig.setDefault_color(cursor.getString(cursor.getColumnIndex("default_color")));
                viewConfig.setPress_color(cursor.getString(cursor.getColumnIndex("press_color")));
                viewConfigs.add(viewConfig);
            } while (cursor.moveToNext());
        }
        return viewConfigs;
    }
}
