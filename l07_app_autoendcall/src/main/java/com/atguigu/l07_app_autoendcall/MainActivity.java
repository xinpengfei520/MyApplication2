package com.atguigu.l07_app_autoendcall;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    private TelephonyManager telephonyManager;
    private PhoneStateListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 启动来电监听的回调方法
     *
     * @param v
     */
    public void startCallListener(View v) {

        if (telephonyManager == null) {
            //获取电话服务的管理器
            telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

            //提供监听来电状态的监听器
            listener = new PhoneStateListener() {

                /* @see TelephonyManager#CALL_STATE_IDLE :空闲状态 (来电之前或挂断之后)
                 * @see TelephonyManager#CALL_STATE_RINGING：响铃状态
                 * @see TelephonyManager#CALL_STATE_OFFHOOK：通话状态
                 */

                @Override
                public void onCallStateChanged(int state, String incomingNumber) {

                    switch (state) {
                        case TelephonyManager.CALL_STATE_IDLE:
                            Log.e("TAG", "空闲状态");
                            break;
                        case TelephonyManager.CALL_STATE_RINGING:
                            Log.e("TAG", "响铃状态");
                            if ("120".equals(incomingNumber)) {
                                endCall();
                            }
                            break;
                        case TelephonyManager.CALL_STATE_OFFHOOK:
                            Log.e("TAG", "通话状态");
                            break;
                    }

                }
            };

            //实现来电的监听.
            //参数2：指明监听来电状态
            telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
            Toast.makeText(MainActivity.this, "启用来电监听", Toast.LENGTH_SHORT).show();
        }

    }

    private void endCall() {

        try {
            //获取到ServiceManager类
            Class clazz = Class.forName("android.os.ServiceManager");//alt + shift + z
            //调用其静态方法getService()
            Method method = clazz.getDeclaredMethod("getService", String.class);
            IBinder iBinder = (IBinder) method.invoke(null, Context.TELEPHONY_SERVICE);

            //结束通话
            ITelephony iTelephony = ITelephony.Stub.asInterface(iBinder);
            iTelephony.endCall();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 结束来电监听的回调方法
     *
     * @param v
     */
    public void endCallListener(View v) {

        if (listener != null) {
            telephonyManager.listen(listener, PhoneStateListener.LISTEN_NONE);
            listener = null;
        }
        Toast.makeText(MainActivity.this, "关闭来电监听", Toast.LENGTH_SHORT).show();
    }
}
