package com.atguigu.l07_service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import local.MyService;

/**
 * 测试Service的启动。
 * 启动方式一：start的方式
 * 通过启动的方式，在当前进程中创建Service，而且是单例的。
 * Service与启动者（比如：Activity)没有关联关系
 * 如果退出当前的应用，在进入时，只要进程没有销毁，仍然可以操作之前创建的Service。
 * 通过intent,可以实现Activity向Service传送数据。但是，反之，Service不能向Activity传送数据
 * <p/>
 * 启动方式二：bind的方式
 * 通过intent,可以实现Activity向Service传送数据，反之，Service可以通过onBind()将数据返回给Activity.
 * Service与启动者（比如：Activity)有关联关系
 * 如果绑定Service的Activity要退出时，一定要将绑定的Service解绑
 * bind的方式与start的方式对应的Service的生命周期不同
 */

public class MainActivity extends AppCompatActivity {

    private ServiceConnection conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //如何使用方式一启动服务
    public void startMyService(View v) {

        Intent intent = new Intent(this, MyService.class);
        intent.putExtra("data", "今天是911纪念日");
        startService(intent);
        Toast.makeText(this, "启动服务", Toast.LENGTH_SHORT).show();
    }

    //如何使用方式一停止服务
    public void stopMyService(View v) {

        Intent intent = new Intent(this, MyService.class);
        stopService(intent);
        Toast.makeText(this, "停止服务", Toast.LENGTH_SHORT).show();
    }

    //如何使用方式二绑定服务
    public void bindMyService(View v) {
        if (conn == null) {
            conn = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    Log.e("TAG", "ServiceConnection onServiceConnected()");
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    Log.e("TAG", "ServiceConnection onServiceDisconnected()");
                }
            };
            Intent intent = new Intent(this, MyService.class);
            bindService(intent, conn, Context.BIND_AUTO_CREATE);//参数3：只要绑定服务，服务就会自动的创建
            Toast.makeText(MainActivity.this, "绑定服务", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(MainActivity.this, "已经绑定了服务", Toast.LENGTH_SHORT).show();
        }
    }

    //如何使用方式二解绑服务
    public void unbindMyService(View v) {
        if (conn != null) {
            unbindService(conn);
            conn = null;//不要忘了
            Toast.makeText(MainActivity.this, "解绑服务", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "已经解绑了服务", Toast.LENGTH_SHORT).show();
        }
    }

    //保证Activity在销毁之前，将Service解绑。保证不出现内存的泄漏
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (conn != null) {
            unbindService(conn);
            conn = null;
        }
    }

    //********************************
    //如何使用方式二绑定服务
    /*public void bindMyService(View v) {
        // if(conn == null){
        conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.e("TAG", "ServiceConnection onServiceConnected()");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.e("TAG", "ServiceConnection onServiceDisconnected()");
            }
        };
        Intent intent = new Intent(this, MyService.class);
        bindService(intent,conn, Context.BIND_AUTO_CREATE);//参数3：只要绑定服务，服务就会自动的创建

        Toast.makeText(MainActivity.this, "绑定服务成功", Toast.LENGTH_SHORT).show();
//        }else{
//            Toast.makeText(MainActivity.this, "已经绑定了服务", Toast.LENGTH_SHORT).show();
//        }


    }
    //如何使用方式二解绑服务
    public void unbindMyService(View v) {
        //if(conn != null){
        unbindService(conn);
        conn = null;//不要忘了！
        Toast.makeText(MainActivity.this, "解绑服务成功", Toast.LENGTH_SHORT).show();

//        }else{
//            Toast.makeText(MainActivity.this, "已经解绑了服务", Toast.LENGTH_SHORT).show();
//        }

    }*/
}
