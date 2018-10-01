package com.atguigu.l08_app_br;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by xinpengfei on 2016/9/12.
 * 测试BroadcastReceiver的静态注册
 */
public class MyReceiver1 extends BroadcastReceiver {

    public MyReceiver1(){
        Log.e("TAG", "MyReceiver1()");
    }


    @Override
    public void onReceive(Context context, Intent intent) {

        int salary = intent.getIntExtra("salary",-1);
        Log.e("TAG", "MyReceiver1 onReceive() salary = " + salary);
    }
}
