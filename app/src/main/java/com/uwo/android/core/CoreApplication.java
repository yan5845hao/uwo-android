package com.uwo.android.core;

import android.app.Application;
import com.uwo.android.api.TestValidate;
import com.uwo.android.http.core.HttpManager;

/**
 * Created by yanhao on 2017/5/2.
 */
public class CoreApplication extends Application{

    @Override
    public void onCreate() {

        HttpManager manager = HttpManager.getInstance();
        manager.setValidate(new TestValidate());
        super.onCreate();


    }
}
