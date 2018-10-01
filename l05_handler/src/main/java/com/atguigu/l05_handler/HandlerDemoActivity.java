package com.atguigu.l05_handler;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 使用Handler实现。
 * 实现步骤：
 * 1.点击“增加”或“减少”的按钮，实现+1或-1
 * 2.自动的实现+1或-1
 * 3.限制范围在[1,20]
 * 4.设置button的可操作性
 */
public class HandlerDemoActivity extends Activity implements View.OnClickListener {

    private static final int MESSAGE_INCRESE = 1;
    private static final int MESSAGE_DECRESE = 2;
    private TextView tv_demo_number;
    private Button btn_demo_increase;
    private Button btn_demo_decrease;
    private Button btn_demo_pause;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler_demo);

        init();
        
    }

    private void init() {

        tv_demo_number = (TextView) findViewById(R.id.tv_demo_number);
        btn_demo_increase = (Button) findViewById(R.id.btn_demo_increase);
        btn_demo_decrease = (Button) findViewById(R.id.btn_demo_decrease);
        btn_demo_pause = (Button)findViewById(R.id.btn_demo_pause);

        btn_demo_increase.setOnClickListener(this);
        btn_demo_decrease.setOnClickListener(this);
        btn_demo_pause.setOnClickListener(this);

    }

    private Handler handler = new Handler(){
      public void handleMessage(Message msg){
          //获取当前显示的数值
          String strNumber = tv_demo_number.getText().toString();
          int number = Integer.parseInt(strNumber);

          switch(msg.what){
              case MESSAGE_INCRESE :
                  number++;
                  tv_demo_number.setText(number + "");

                  if(number >= 20) {
                      Toast.makeText(HandlerDemoActivity.this,"已达到最大值",Toast.LENGTH_SHORT).show();

                      //设置暂停的可操作性
                      btn_demo_pause.setEnabled(false);
                      return;
                  }

                  //发送延迟消息
                  this.sendEmptyMessageDelayed(MESSAGE_INCRESE,300);

                  break;
              case MESSAGE_DECRESE :

                  //实现-1操作
                  number--;
                  tv_demo_number.setText(number + "");

                  if(number <= 1) {
                      Toast.makeText(HandlerDemoActivity.this,"已达到最小值",Toast.LENGTH_SHORT).show();

                      //设置暂停的可操作性
                      btn_demo_pause.setEnabled(false);
                      return;
                  }

                  //发送延迟消息
                  this.sendEmptyMessageDelayed(MESSAGE_DECRESE,300);
                  break;
          }
      }
    };


    @Override
    public void onClick(View v) {

        if(v == btn_demo_increase) {//自动增加

            btn_demo_increase.setEnabled(false);
            btn_demo_decrease.setEnabled(true);
            btn_demo_pause.setEnabled(true);

            //移除减少的消息
            handler.removeMessages(MESSAGE_DECRESE);

            //发送消息：空消息
            handler.sendEmptyMessage(MESSAGE_INCRESE);

        }else if(v == btn_demo_decrease) {//自动减少

            //修改button的可操作性
            btn_demo_decrease.setEnabled(false);
            btn_demo_increase.setEnabled(true);
            btn_demo_pause.setEnabled(true);

            //移除增加的消息
            handler.removeMessages(MESSAGE_INCRESE);

            //发送减少的消息
            handler.sendEmptyMessage(MESSAGE_DECRESE);

        }else if(v == btn_demo_pause) {//暂停
            //修改button的可操作性
            btn_demo_pause.setEnabled(false);
            btn_demo_increase.setEnabled(true);
            btn_demo_decrease.setEnabled(true);

            //移除增加和减少的消息
            handler.removeMessages(MESSAGE_INCRESE);
            handler.removeMessages(MESSAGE_DECRESE);
        }
    }
}
