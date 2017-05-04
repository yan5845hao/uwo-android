package com.uwo.android.http.core;

import android.util.Log;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by yanhao on 2017/5/4.
 */
public class HttpRequest {

    private final String TAG = HttpRequest.class.getSimpleName();

    private HttpMapper mapper;

    private String params;

    private HttpURLConnection conn;

    private Map<String, Object> headers;

    public HttpRequest(HttpMapper mapper) throws Exception{
        this.mapper = mapper;
    }

    public void addParams(Map<String, Object> values){
        if(values.size() == 0)
            return;
        StringBuffer sb = new StringBuffer();
        for(String key : values.keySet()){
            sb.append(key);
            sb.append("=");
            sb.append(values.get(key));
            sb.append("&");
        }
        params = sb.toString().substring(0, sb.toString().length() - 1);
    }


    public void addHeaders(Map<String, Object> headers){
        this.headers = headers;
    }

    private void openConn() throws Exception{
        mapper.domain = (params != null && (mapper.method == HttpMethod.GET || mapper.method == HttpMethod.DELETE)) ? (mapper.domain += params) : mapper.domain;
        // 查看请求数据链接
        StringBuffer sb = new StringBuffer();
        sb.append("[").append(mapper.method).append("]").append(" ").append(mapper.domain);
        Log.w(TAG, sb.toString());
        URL url = new URL(mapper.domain);
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(mapper.method.name());
    }

    private void setHeaders(){
        // 头部信息
        for (String key:headers.keySet())
            conn.setRequestProperty(key, String.valueOf(headers.get(key)));
    }

    public HttpResponse execute(){
        try {
            openConn();
            setHeaders();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(false);
            if(params != null) {
                byte[] bytes = params.getBytes("UTF-8");
                OutputStream os = conn.getOutputStream();
                os.write(bytes, 0, bytes.length);
                os.flush();
                os.close();
            }
            conn.connect();
            return new HttpResponse(conn, mapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
