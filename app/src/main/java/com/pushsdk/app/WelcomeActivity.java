package com.pushsdk.app;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * 欢迎页，没什么用，纯粹模拟正常app环境
 */
public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //一般情况下，欢迎页3s后跳转首页
                Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
                startActivity(intent);
            }
        },3*1000);
    }
}
