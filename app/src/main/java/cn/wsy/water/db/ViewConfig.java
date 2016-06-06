package cn.wsy.water.db;

/**
 * Created by Kevin on 2016/4/26.
 * 自定义控件坐标配置类
 */
public class ViewConfig {
    //自定義控件以來佈局名稱與id
    public int _id;
    public String layout_id;
    public String layout_name;
    //默认颜色
    public String default_color;
    //点击颜色
    public String press_color;
    //自定义控件type
    public int view_Type;
    //自定义控件X坐标
    public int view_X;
    //自定义控件Y坐标
    public int view_Y;
    //time
    public String create_ime;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getDefault_color() {
        return default_color;
    }

    public void setDefault_color(String default_color) {
        this.default_color = default_color;
    }

    public String getPress_color() {
        return press_color;
    }

    public void setPress_color(String press_color) {
        this.press_color = press_color;
    }

    public String getCreate_ime() {
        return create_ime;
    }

    public void setCreate_ime(String create_ime) {
        this.create_ime = create_ime;
    }

    public String getLayout_id() {
        return layout_id;
    }

    public void setLayout_id(String layout_id) {
        this.layout_id = layout_id;
    }

    public String getLayout_name() {
        return layout_name;
    }

    public void setLayout_name(String layout_name) {
        this.layout_name = layout_name;
    }

    public int getView_Type() {
        return view_Type;
    }

    public void setView_Type(int view_Type) {
        this.view_Type = view_Type;
    }

    public int getView_X() {
        return view_X;
    }

    public void setView_X(int view_X) {
        this.view_X = view_X;
    }

    public int getView_Y() {
        return view_Y;
    }

    public void setView_Y(int view_Y) {
        this.view_Y = view_Y;
    }
}
