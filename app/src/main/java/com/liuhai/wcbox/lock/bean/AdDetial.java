package com.liuhai.wcbox.lock.bean;

import java.io.Serializable;
import java.util.List;

public class AdDetial implements Serializable {
    public int err_code;
    public String err_msg;

    public int getErr_code() {
        return err_code;
    }

    public void setErr_code(int err_code) {
        this.err_code = err_code;
    }

    public String getErr_msg() {
        return err_msg;
    }

    public void setErr_msg(String err_msg) {
        this.err_msg = err_msg;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<AdDate> getData() {
        return data;
    }

    public void setData(List<AdDate> data) {
        this.data = data;
    }

    public String key;
    public  List<AdDate> data;

}
