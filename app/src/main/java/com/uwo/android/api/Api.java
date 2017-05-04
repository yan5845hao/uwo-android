package com.uwo.android.api;

import com.uwo.android.http.annotations.RequestMapper;
import com.uwo.android.http.annotations.RequestParam;

/**
 * Created by yanhao on 2017/5/2.
 */
@RequestMapper("http://api.map.baidu.com")
public interface Api {

    @RequestMapper(value = "/geocoder/v2/")
    Object geocoder(
            @RequestParam(name = "address") String address,
            @RequestParam(name = "output") String output,
            @RequestParam(name = "ak") String ak,
            @RequestParam(name = "callback") String callback
    );

}
