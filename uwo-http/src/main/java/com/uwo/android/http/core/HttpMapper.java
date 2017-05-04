package com.uwo.android.http.core;

/**
 * Created by yanhao on 2017/5/4.
 */
class HttpMapper {

    String domain;

    String action;

    HttpMethod method;

    HttpFormat format;


    public HttpMapper(String domain, String action, HttpMethod method, HttpFormat format){
        this.domain = domain;
        this.action = action;
        this.method = method;
        this.format = format;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public HttpFormat getFormat() {
        return format;
    }

    public void setFormat(HttpFormat format) {
        this.format = format;
    }
}
