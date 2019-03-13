package com.gourav.led_test;

public class DeviceInfo {
    public String getDevicename() {
        return devicename;
    }

    public void setDevicename(String devicename) {
        this.devicename = devicename;
    }

    public String getDeviceadd() {
        return deviceadd;
    }

    public void setDeviceadd(String deviceadd) {
        this.deviceadd = deviceadd;
    }

    String devicename;

    public DeviceInfo(String devicename, String deviceadd) {
        this.devicename = devicename;
        this.deviceadd = deviceadd;
    }

    public DeviceInfo() {
    }

    String deviceadd;

}
