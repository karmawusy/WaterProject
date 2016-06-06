package cn.wsy.water.db;

/**
 * 我的布局 首页实体类
 * Created by Wusy on 2016/4/30.
 */
public class LayoutIndexBean {

    private String layout_id;
    private String layout_name;
    private String createTime;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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
}
