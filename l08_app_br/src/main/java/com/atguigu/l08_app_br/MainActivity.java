package com.atguigu.l08_app_br;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


/**
 * 测试BroadcastReceiver的主界面
 */
public class MainActivity extends Activity {

    private MyReceiver2 receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 注册广播接收器
     */
    public void registBR(View v){

        if(receiver == null) {
            //1.创建广播接收器的对象
            receiver = new MyReceiver2();
            //2.创建IntentFilter的对象，指明当前接收器的action
            IntentFilter filter = new IntentFilter("com.atguigu.l08_br.MyReceiver1.Action");
            //3.注册
            registerReceiver(receiver,filter);
            Toast.makeText(MainActivity.this, "动态注册广播接收器", Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(MainActivity.this, "已经注册了广播接收器", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 解注册广播接收器
     * @param v
     */
    public void unRegistBR(View v) {

        if(receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;//别忘了
            Toast.makeText(MainActivity.this, "解注册广播接收器", Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(MainActivity.this, "已经解注册广播接收器", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 在退出当前Activity时，一定要解注册广播接收器。否则会出现内存泄漏的问题
     */
    @Override
    protected void onDestroy() {
            super.onDestroy();
        if(receiver != null) {

            unregisterReceiver(receiver);
            receiver = null;//别忘了
        }
    }
}
