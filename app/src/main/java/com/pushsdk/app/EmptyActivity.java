package com.pushsdk.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * 空activity，用来拉起整个app，启动后立即关闭
 */
public class EmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        finish();
    }
}
