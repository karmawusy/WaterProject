package cn.wsy.water.app;

/**
 * Created by Kevin on 2016/6/3.
 * Email：1026karma@gmail.com
 * Github：karmalove
 */
public class BlueDevice {
    private String device_name;
    private String device_address;

    public String getConnect() {
        return connect;
    }

    public void setConnect(String connect) {
        this.connect = connect;
    }

    private String connect;

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    private int image_id;

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getDevice_address() {
        return device_address;
    }

    public void setDevice_address(String device_address) {
        this.device_address = device_address;
    }

}
