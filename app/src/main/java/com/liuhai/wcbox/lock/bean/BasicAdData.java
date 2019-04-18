package com.liuhai.wcbox.lock.bean;

import java.io.Serializable;

public class BasicAdData implements Serializable {
    public  int ret;
    public AdDetial data;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public AdDetial getData() {
        return data;
    }

    public void setData(AdDetial data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String msg;



}
