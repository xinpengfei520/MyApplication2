package com.atguigu.l05_handler;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * 一、如何联网下载数据
 * 1.主线程：显示提示视图
 * 2.分线程：联网下载数据
 * 3.主线程：更新提示视图
 * <p/>
 * 二、如何使用Handler实现线程间的通信
 * 1.创建Handler的对象，并重写handleMessage()
 * 2.需要创建Message的对象，指明其id,并且封装要发送的数据
 * 3.使用Handler来发送消息
 * 4.在handleMessage()实现消息的处理
 * <p/>
 * <p/>
 * 三、Handler的作用：
 * 1.可以用来发送消息
 * 即时消息                         延迟消息
 * 发送空消息                sendEmptyMessage(int what)       sendEmptyMessageDelayed(int what,long millitime)
 * 发送非空消息              sendMessage(Message mess)        sendMessageDelayed(Message mess,long millitime)
 * <p/>
 * 注：对于延迟消息来说，不是延迟发送，而是延迟处理
 * 2.可以处理消息: 主要调用Handler的handleMessage()，除此之外，还有另外的2个位置可以处理消息。
 * 3.移除消息：removeMessages(int what):移除指定id的所有的未被执行的消息
 * removeCallbacksAndMessages(null):移除所有的未被执行的消息
 */
public class HandlerTestActivity extends Activity {

    private static final int MESSAGE_OK = 1;
    private static final int MESSAGE_ERROR = 2;
    private EditText et_handler1_result;
    private ProgressBar pb_handler1_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler_test);

        et_handler1_result = (EditText) findViewById(R.id.et_handler1_result);
        pb_handler1_loading = (ProgressBar) findViewById(R.id.pb_handler1_loading);
    }

    //handler的使用：1.创建Handler的对象，并重写handleMessage()
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {//此处的方法，也是回调方法

            //handler的使用：4.在handleMessage()实现消息的处理---在主线程中执行的
            switch (msg.what) {
                case MESSAGE_OK:
                    String content = (String) msg.obj;
                    et_handler1_result.setText(content);
                    break;
                case MESSAGE_ERROR:
                    Toast.makeText(HandlerTestActivity.this, "数据加载失败", Toast.LENGTH_SHORT).show();

                    break;
            }

            pb_handler1_loading.setVisibility(View.INVISIBLE);
        }

    };

    /**
     * 使用runOnUiThread实现分线程向主线程发送数据
     *
     * @param v
     */
    public void getSubmit1(View v) {
        //1.主线程：显示提示视图
        pb_handler1_loading.setVisibility(View.VISIBLE);

        //2.分线程：联网下载数据
        new Thread() {
            @Override
            public void run() {
                final String content = getStringData();

                //3.主线程：更新提示视图
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (content == null) {
                            Toast.makeText(HandlerTestActivity.this, "加载数据失败", Toast.LENGTH_SHORT).show();

                        } else {
                            et_handler1_result.setText(content);

                        }
                        // //设置ProgressBar的消失
                        pb_handler1_loading.setVisibility(View.INVISIBLE);//快捷键：ctrl + alt + /
                    }
                });
            }
        }.start();
    }

    private String getStringData() {

        HttpURLConnection conn = null;
        InputStream is = null;

        try {

            String path = "http://192.168.56.1:8080/Test/index.jsp?userName=Lily&age=18";
            // 2.将此地址封装在URL对象中
            URL url = new URL(path);
            // 3.获取与服务器的连接的对象
            conn = (HttpURLConnection) url.openConnection();

            // 4.设置连接的请求参数
            // 4.1设置请求方式
            conn.setRequestMethod("GET");
            // 4.2设置连接的超时时间
            conn.setConnectTimeout(5000);
            // 4.3设置读取数据的超时时间
            conn.setReadTimeout(5000);
            //5.获取连接
            conn.connect();

            // 6.获取响应码，如果响应码是200，表示正确的获取到了服务器端返回的数据
            if (conn.getResponseCode() == 200) {
                //7.获取输入流
                is = conn.getInputStream();
                // 8.将输入流中的数据转换为字符串，并设置给EditText显示
                String content = fromInputStreamToString(is);

                return content;

            } else {

                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    /**
     * 从输入流中读取内容，返回一个内容的字符串
     *
     * @param fis
     * @return
     * @throws IOException
     */
    private String fromInputStreamToString(InputStream fis) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[10];
        int len;
        while ((len = fis.read(buffer)) != -1) {

            baos.write(buffer, 0, len);
        }
        return baos.toString();
    }

    /**
     * 使用Handler实现分线程向主线程发送数据
     *
     * @param v
     */
    public void getSubmit2(View v) {

        //1.主线程：显示提示视图
        pb_handler1_loading.setVisibility(View.VISIBLE);

        //2.分线程：联网下载数据
        new Thread() {
            @Override
            public void run() {
                final String content = getStringData();

                //3.将分线程中得到的数据封装在Message中
                Message message = Message.obtain();
                //handler的使用：2.需要创建Message的对象，指明其id,并且封装要发送的数据
                if (content != null) {

                    message.what = MESSAGE_OK;
                    message.obj = content;//携带数据
                    //handler的使用：3.使用Handler来发送消息
                    handler.sendMessage(message);
                } else {
                    //发送空消息：方式一：
//                    message.what = MESSAGE_ERROR;
//                    handler.sendMessage(message);
                    //发送空消息：方式二：
                    handler.sendEmptyMessage(MESSAGE_ERROR);
                }

            }
        }.start();
    }
}
