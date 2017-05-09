package com.uwo.android.http.core;

import javax.xml.validation.Validator;
import java.lang.reflect.Proxy;

/**
 * Http 请求管理
 * Created by yanhao on 2016/10/17.
 */
public class HttpManager {

    private static HttpManager manager;

    private HttpValidate validate;

    public static HttpManager getInstance(){
        if(manager == null)
            manager = new HttpManager();
        return manager;
    }

    public void setValidate(HttpValidate validate){
        this.validate = validate;
    }

    /**
     * 请求
     */
    public <T> T proxyObject(Class<T> c, HttpHandler handler){
        return proxyObject(c, handler, null);
    }

    public <T> T proxyObject(Class<T> c, HttpHandler handler, HttpValidate validate){
        HttpProxy<T> proxy = new HttpProxy<T>(handler, validate);
        return (T)(Proxy.newProxyInstance(c.getClassLoader(), new Class[]{c}, proxy));
    }

}
