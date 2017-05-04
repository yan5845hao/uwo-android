package com.uwo.android.http.core;


import android.util.Log;
import com.uwo.android.http.annotations.RequestHeader;
import com.uwo.android.http.annotations.RequestMapper;
import com.uwo.android.http.annotations.RequestParam;

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

        if(handler == null)
            return execute(domain, mapper.method(), headers, params);
        else {
            new Thread(new HttpRunnable(domain, mapper.method(), headers, params)).start();
        }
        return null;
    }

    private String execute(String url, HttpMethod method, Map<String, Object> params, Map<String, Object> headers){
        // 处理请求方式
        switch (method){
            case GET:
            case DELETE:
                return request(url + "?" + queryStr(params), method, headers);
            case POST:
            case PUT:
            default:
                 return request(url, method, headers, queryStr(params));
        }
    }


    /**
     * queryStr
     * @param values
     * @return
     */
    private String queryStr(Map<String, Object> values){
        if(values.size() == 0)
            return "";
        StringBuffer sb = new StringBuffer();
        for(String key:values.keySet()){
            sb.append(key);
            sb.append("=");
            sb.append(values.get(key));
            sb.append("&");
        }
        return sb.toString().substring(0, sb.toString().length() - 1);
    }

    /**
     * 请求
     * @param realUrl
     * @param method
     * @return
     */
    private String request(String realUrl, HttpMethod method, Map<String, Object> headers){
        return request(realUrl, method, headers, null);
    }

    /**
     * 请求
     * @param realUrl
     * @param method
     * @param params
     * @return
     */
    private String request(String realUrl, HttpMethod method, Map<String, Object> headers, String params){
        try {
            // 查看请求数据链接
            StringBuffer sb = new StringBuffer();
            sb.append("[").append(method).append("]").append(" ").append(realUrl);
            Log.w(TAG, sb.toString());
            // 查看请求数据链接
            URL url = new URL(realUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method.name());
            // 头部信息
            for (String key:headers.keySet())
                conn.setRequestProperty(key, String.valueOf(headers.get(key)));
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(false);
            if(params != null) {
                Log.w(TAG, params);
                byte[] bytes = params.getBytes("UTF-8");
                OutputStream os = conn.getOutputStream();
                os.write(bytes, 0, bytes.length);
                os.flush();
                os.close();

            }
            conn.connect();
            return new HttpResponse(conn).getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    class HttpRunnable implements Runnable{

        private String url;

        private HttpMethod method;

        private Map<String, Object> params;

        private Map<String, Object> headers;

        public HttpRunnable(String url, HttpMethod method, Map<String, Object> params, Map<String, Object> headers){
            this.url = url;
            this.method = method;
            this.params = params;
            this.headers = headers;
        }


        @Override
        public void run() {
            final String result = execute(url, method, headers, params);
            handler.post(new Runnable() {
                @Override
                public void run() {

                    if (validate == null)
                        handler.callback(result);
                    else if (validate.validateResult(result))
                        handler.success(result);
                    else
                        handler.error(result);
                }
            });
        }
    }


}
