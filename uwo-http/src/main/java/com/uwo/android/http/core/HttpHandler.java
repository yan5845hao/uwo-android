package com.uwo.android.http.core;

import android.os.Handler;

/**
 * Created by yanhao on 2016/10/18.
 */
public abstract class HttpHandler extends Handler {


    /**
     * 成功返回
     * @return
     */
    public abstract void success(String action, Object o);

    /**
     * 错误异常
     * @return
     */
    public abstract void error(String action, Object o);

    /**
     * 返回
     * @param o
     */
    public abstract void callback(String action, Object o);


}
