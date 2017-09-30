package com.pushsdk.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(MyApplication.getInstance().isFromPushSdk()){
            //todo 如果是从pushsdk进来的，可能执行某些操作，比如跳转我的消息
        }
    }
}
