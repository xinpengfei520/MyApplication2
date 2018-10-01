package com.atguigu.l05_handler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * testHandler回调方法
     *
     * @param view
     */
    public void testHandler(View view) {
        Intent intent = new Intent(this, HandlerTestActivity.class);
        startActivity(intent);
    }


    /**
     * handlerDemo回调方法
     *
     * @param view
     */
    public void handlerDemo(View view) {
        Intent intent = new Intent(this, HandlerDemoActivity.class);
        startActivity(intent);

    }

    /**
     * testAsyncTask回调方法
     *
     * @param view
     */
    public void testAsyncTask(View view) {
        Intent intent = new Intent(this, AsyncTaskTestActivity.class);
        startActivity(intent);
    }
}
