package com.atguigu.l08_app_br;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 测试BroadcastReceiver的动态注册
 */
public class MyReceiver2 extends BroadcastReceiver {
    public MyReceiver2() {
        Log.e("TAG", "MyReceiver2()");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("TAG", "MyReceiver2  onReceive()");
    }
}
