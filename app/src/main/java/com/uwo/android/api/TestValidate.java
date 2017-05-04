package com.uwo.android.api;

import com.uwo.android.http.core.HttpValidate;
import org.json.JSONObject;

/**
 * Created by yanhao on 2017/5/2.
 */
public class TestValidate implements HttpValidate {

    @Override
    public boolean validateResult(Object result) {
        try {
            JSONObject json = new JSONObject(String.valueOf(result));
            if(json.getInt("status") == 240)
                return false;
            return true;
        }catch(Exception e){
            return false;
        }
    }

}
