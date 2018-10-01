package com.atguigu.l06_app_motionevent;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

    private ImageView iv_main_logo;
    private int downX, downY;
    private RelativeLayout relativeLayout;
    private int maxRight;
    private int maxBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv_main_logo = (ImageView) findViewById(R.id.iv_main_logo);
        relativeLayout = (RelativeLayout) iv_main_logo.getParent();//获取ImageView的父视图对象

        //给ImageView设置触摸事件的监听
        iv_main_logo.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //记录点击的位置的坐标
                int eventX = (int) event.getRawX();
                int eventY = (int) event.getRawY();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (maxRight == 0) {
                            //获取屏幕的右边界和下边界的坐标值
                            maxRight = relativeLayout.getRight();
                            maxBottom = relativeLayout.getBottom();
                            Log.e("TAG", "maxRight = " + maxRight + ",maxBottom = " + maxBottom);
                        }
                        downX = eventX;
                        downY = eventY;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //计算水平和垂直方向移动的距离
                        int dx = eventX - downX;
                        int dy = eventY - downY;

                        //获取当前的imageView的left,top,right,bottom
                        int left = iv_main_logo.getLeft();
                        int top = iv_main_logo.getTop();
                        int right = iv_main_logo.getRight();
                        int bottom = iv_main_logo.getBottom();

                        left += dx;
                        right += dx;
                        top += dy;
                        bottom += dy;

                        //限制left
                        if (left < 0) {
                            right -= left;
                            left = 0;
                        }

                        //限制top
                        if (top < 0) {
                            bottom -= top;
                            top = 0;
                        }

                        //限制right
                        if (right > maxRight) {
                            left -= (right - maxRight);
                            right = maxRight;
                        }

                        //限制bottom
                        if (bottom > maxBottom) {
                            top -= (bottom - maxBottom);
                            bottom = maxBottom;
                        }

                        //重新定位
                        iv_main_logo.layout(left, top, right, bottom);

                        //更新坐標
                        downX = eventX;
                        downY = eventY;

                        break;

                }

                return true;//表示当前触摸消费了此事件
            }
        });

    }
}
