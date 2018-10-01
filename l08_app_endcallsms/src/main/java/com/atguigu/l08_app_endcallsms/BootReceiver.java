package com.atguigu.l08_app_endcallsms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
    public BootReceiver() {
        Log.e("TAG", "BootReceiver()");
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("TAG", "开机完成的广播");

        Intent intent1 = new Intent(context,CallService.class);
        context.startService(intent1);

    }
}
