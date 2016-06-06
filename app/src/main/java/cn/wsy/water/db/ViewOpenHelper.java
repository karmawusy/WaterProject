package cn.wsy.water.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Kevin on 2016/4/25.
 */
public class ViewOpenHelper extends SQLiteOpenHelper {
    /**
     * Province表建表语句
     */
    private static final String MAOCO_WATER = "create table Config("
            + "_id integer primary key autoincrement,"
            + "layout_id integer,"
            + "layout_name CHAR(100),"
            + "create_time CHAR(100),"
            + "view_Type int,"
            + "default_color CHAR(100),"
            + "press_color CHAR(100),"
            + "view_X int,"
            + "view_Y int)";

    public ViewOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MAOCO_WATER);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
