package com.atguigu.l06_app_keyevent;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

/**
 * 2s内连续点击两次“返回键”，退出当前Activity
 * <p/>
 * 问题一：内存溢出：main(String[] args){main(args)}
 * <p/>
 * 问题二：内存泄漏：存在一块没有引用指向的内存空间，但是此内存空间还不能被正确的回收。
 * 举例：在Activity中发送了延迟消息，在Activity退出之前，没有移除未被执行的消息。
 * >如果内存泄漏过多，就会导致内存溢出。
 */

public class MainActivity extends Activity {

    private boolean flag = true;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 1:
                    flag = true;
                    Log.e("TAG", "handleMessage()");
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (flag) {
            flag = false;
            Toast.makeText(MainActivity.this, "再点击一次返回键退出", Toast.LENGTH_SHORT).show();
            //发送一个延迟消息

            handler.sendEmptyMessageDelayed(1, 2000);
            return true;
        }

        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onDestroy() {

        Log.e("TAG", "onDestroy()");
        //方式一：
        handler.removeMessages(1);
        //方式二：
//      handler.removeCallbacksAndMessages(null);

        super.onDestroy();
    }
}
