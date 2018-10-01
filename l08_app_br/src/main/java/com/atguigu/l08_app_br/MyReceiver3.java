package com.atguigu.l08_app_br;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 测试BroadcastReceiver的动态注册
 */
public class MyReceiver3 extends BroadcastReceiver {
    public MyReceiver3() {
        Log.e("TAG", "MyReceiver3()");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("TAG", "MyReceiver3  onReceive()");
    }
}
