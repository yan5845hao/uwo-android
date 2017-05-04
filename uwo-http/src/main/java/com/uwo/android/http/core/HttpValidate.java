package com.uwo.android.http.core;

/**
 * Created by yanhao on 2017/5/2.
 */
public interface HttpValidate {

    /**
     * http 返回校验 true success， false error
     * @param result
     * @return
     */
    boolean validateResult(Object result);

}
