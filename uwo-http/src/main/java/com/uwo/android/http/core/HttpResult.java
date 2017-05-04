package com.uwo.android.http.core;

/**
 * Created by yanhao on 2017/3/28.
 */
public class HttpResult {

    private int code;

    private String msg;

    public HttpResult(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
