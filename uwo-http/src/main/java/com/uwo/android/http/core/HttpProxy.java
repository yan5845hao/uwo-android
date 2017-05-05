package com.uwo.android.http.core;


import android.util.JsonReader;
import android.util.Log;
import com.uwo.android.http.annotations.RequestHeader;
import com.uwo.android.http.annotations.RequestMapper;
import com.uwo.android.http.annotations.RequestParam;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;

import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * HTTP 请求代理
 * Created by yanhao on 16/10/16.
 */
class HttpProxy<T> implements InvocationHandler {

    private final String TAG = HttpProxy.class.getSimpleName();

    private HttpHandler handler;

    private HttpValidate validate;

    public HttpProxy(HttpHandler handler, HttpValidate validate) {
        this.handler = handler;
        this.validate = validate;
    }

    /**
     * 执行接口方法
     */
    @Override
    public Object invoke(Object o, Method method, Object[] values)
            throws Throwable {
        // 获取接口
        Class<?>[] inters = o.getClass().getInterfaces();
        if(inters == null || inters.length <= 0)
            return null;

        String domain = "";

        // 查看接口中是否存在存在RequestMapper注解
        for(Class<?> interClazz:inters){
            domain = interClazz.isAnnotationPresent(RequestMapper.class) ? interClazz.getAnnotation(RequestMapper.class).value() : "";
            if(domain != null)
                break;
        }

        // 获取方法中的注解 获取请求链接
        RequestMapper mapper = method.getAnnotation(RequestMapper.class);
        domain += mapper.value();
        HttpMapper httpMapper = new HttpMapper(domain, method.getName(), mapper.method(), mapper.format());
        // 获取请求参数
        Map<String, Object> params = new HashMap<String, Object>();
        // 获取请求头部信息
        Map<String, Object> headers = new HashMap<String, Object>();
        values = null == values ? new Object[0] : values;
        Annotation[][] annotationses = method.getParameterAnnotations();
        for(int i = 0; i < annotationses.length; i ++){
            Annotation[] ans = annotationses[i];
            Object value = values[i];
            for(Annotation an: ans){
                if(an instanceof RequestParam) {
                    RequestParam param = (RequestParam) an;
                    params.put(param.name(), (value != null) ? value : param.value());
                }else{
                    RequestHeader header = (RequestHeader)an;
                    headers.put(header.name(), (value != null) ? value : header.value());
                }
            }
        }
        // 判断是否传递了handler 如果没有传递handler直接返回值 有则handler返回值
        if(handler == null)
            return execute(httpMapper, params, headers);
        else {
            new Thread(new HttpRunnable(httpMapper, params, headers)).start();
        }
        return null;
    }

    private String execute(HttpMapper mapper, Map<String, Object> params, Map<String, Object> headers){
        return request(mapper, headers, params);
//        switch (mapper.format){
//            case JSON:
//                result = JsonAddAction(result, mapper);
//                break;
//            case XML:
//                result = JsonAddAction(result, mapper);
//                break;
//            default:
//                result = JsonAddAction(result, mapper);
//                break;
//        }
//
//        return result;
    }

    /**
     * 添加action
     * @param result
     * @param mapper
     * @return
     */
    private String JsonAddAction(String result, HttpMapper mapper){
        try{
            JSONObject json = new JSONObject(result);
            json.put("action", mapper.action);
            return json.toString();
        }catch(Exception e){
            try{
                JSONObject json = new JSONObject();
                json.put("result", result);
                json.put("action", mapper.action);
                return json.toString();
            }catch(Exception e1) {
                return result;
            }
        }
    }

    /**
     * 请求
     * @param mapper
     * @param headers
     * @param params
     * @return
     */
    private String request(HttpMapper mapper, Map<String, Object> headers, Map<String, Object> params){
        try {
            // 查看请求数据链接
            HttpRequest request = new HttpRequest(mapper);
            request.addParams(params);
            request.addHeaders(headers);
            return request.execute().getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * http请求线程
     */
    class HttpRunnable implements Runnable{

        private HttpMapper mapper;

        private Map<String, Object> params;

        private Map<String, Object> headers;

        public HttpRunnable(HttpMapper mapper, Map<String, Object> params, Map<String, Object> headers){
            this.mapper = mapper;
            this.params = params;
            this.headers = headers;
        }


        @Override
        public void run() {
            final String result = execute(mapper, headers, params);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (validate == null)
                        handler.callback(mapper.action, result);
                    else if (validate.validateResult(result))
                        handler.success(mapper.action, result);
                    else
                        handler.error(mapper.action, result);
                }
            });
        }
    }


}
