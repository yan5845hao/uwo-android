package com.uwo.android.http.core;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * Created by yanhao on 2017/3/28.
 */
public class HttpResponse {

    private HttpMapper mapper;

    private HttpURLConnection conn;

    public HttpResponse(HttpURLConnection conn, HttpMapper mapper){
        this.conn = conn;
        this.mapper = mapper;
    }

    /**
     * 获取status
     * @return
     */
    public int getResponseCode(){
        try {
            return this.conn.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取conntentType
     * @return
     */
    public String getContentType(){
        return this.conn.getContentType();
    }

    public InputStream getInputStream(){
        try {
            return this.conn.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public InputStream getErrorStream(){
        Log.w("HttpResponse", "Error " + this.conn);
        return this.conn.getErrorStream();
    }

    public String getBody() {
        try {
            return getStreamAsString(getInputStream(), "UTF-8");
        }catch(Exception e){
            try {
                return getStreamAsString(getErrorStream(), "UTF-8");
            }catch(Exception e1){
                return "";
            }
        }
    }

    private String getStreamAsString(InputStream in, String charset) throws IOException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int count = 0;
            int len = 0;
            while((count = in.read(bytes)) > 0){
                baos.write(bytes, 0, count);
                len += count;
            }
            Log.w(HttpResponse.class.getSimpleName(), baos.toString());
            return new String(baos.toByteArray(), charset);
        } finally {
            if(in != null) {
                in.close();
            }
        }
    }

}
