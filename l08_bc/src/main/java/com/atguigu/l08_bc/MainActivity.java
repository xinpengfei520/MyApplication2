package com.atguigu.l08_bc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

/**
 * 测试发送广播的主界面
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 发送一般广播
     * @param v
     */
    public void sendNormalBC(View v) {

        Intent intent = new Intent("com.atguigu.108_br.MyReceiver1.Action");
        intent.putExtra("salary",11000);
        this.sendBroadcast(intent);
        Toast.makeText(MainActivity.this, "发送一般广播", Toast.LENGTH_SHORT).show();

    }

    /**
     * 发送有序广播
     * @param v
     */
    public void sendOrderBC(View v) {
        Intent intent = new Intent("com.atguigu.l08_br.MyReceiver3.Action");
        this.sendOrderedBroadcast(intent,null);
        Toast.makeText(MainActivity.this, "发送有序广播", Toast.LENGTH_SHORT).show();
    }
}
