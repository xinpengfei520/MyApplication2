package com.atguigu.l07_app_endcall;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void endCall(View v){

        //获取到ServiceManager类
        try {
            //使用反射的方式
            Class clazz = Class.forName("android.os.ServiceManager");//alt + shift + z
            //调用其静态方法getService()
            Method method = clazz.getDeclaredMethod("getService",String.class);
            IBinder iBinder = (IBinder) method.invoke(null, Context.TELEPHONY_SERVICE);

            //结束通话
            ITelephony iTelephony = ITelephony.Stub.asInterface(iBinder);
            iTelephony.endCall();
            Toast.makeText(this, "结束通话", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
