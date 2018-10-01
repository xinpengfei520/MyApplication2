package local;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 *
 * 一、如何创建一个Service:
 * 1.创建一个Service类的子类
 * 2.在功能清单文件中注册Service
 * 3.在子类中重写相应的生命周期方法
 *
 * 二、测试Service的生命周期方法。
 * start的方式：
 * 第1次启动：构造器---onCreate()---onStartCommond()
 *
 * 第n次启动（n >1):执行一次startService(),就调用一次：onStartCommond()
 *
 * 第1次停止：onDestroy()
 *
 * 第n次停止(n > 1):没有响应。（因为唯一的服务已经被销毁了）
 *
 * bind的方式：
 * 第1次绑定：构造器---onCreate() ---onBind()---ServiceConnection类的onServiceConnected()
 *
 * 第n次绑定（n >1):ServiceConnection类的onServiceConnected()
 *
 * 第1次解绑：onUnBind()---onDestroy()
 *
 * 第n此解绑（n > 1):抛出异常
 *
 */

public class MyService extends Service {

   public MyService(){
       Log.e("TAG","MyService()");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("TAG", "onCreate()");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("TAG", "onUnbind()");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.e("TAG", "onDestroy()");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("TAG", "onStartCommond()");
        String data = intent.getStringExtra("data");
        Log.e("TAG", data);
        return super.onStartCommand(intent, flags, startId);
    }
}
