package com.uwo.android.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.uwo.android.api.Api;
import com.uwo.android.http.core.HttpHandler;
import com.uwo.android.http.core.HttpManager;

public class MainActivity extends AppCompatActivity {

    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        btn = (Button)findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request();
            }
        });
    }


    private void request(){
        HttpManager manager = HttpManager.getInstance();
        Api o =  manager.proxyObject(Api.class, new HttpHandler() {

            @Override
            public void success(String action, Object o) {
                Toast.makeText(MainActivity.this, "SUCCESS", Toast.LENGTH_LONG).show();
            }

            @Override
            public void error(String action, Object o) {
                Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_LONG).show();
            }

            @Override
            public void callback(String action, Object o) {
                Toast.makeText(MainActivity.this, "CALLBACK", Toast.LENGTH_LONG).show();
            }

        });

        o.geocoder("北京市海淀区上地十街10号", "json", "E4805d16520de693a3fe707cdc962045", "showLocation");

    }

}
