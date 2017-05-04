package com.uwo.android.http.core;

import android.os.Handler;

/**
 * Created by yanhao on 2016/10/18.
 */
public abstract class HttpHandler extends Handler {

//    /**
//     * 执行方法
//     * @param o
//     */
//    public abstract Object invoke(Object o);

    /**
     * 成功返回
     * @return
     */
    public abstract void success(Object o);

    /**
     * 错误异常
     * @return
     */
    public abstract void error(Object o);

    /**
     * 返回
     * @param o
     */
    public abstract void callback(Object o);


}
