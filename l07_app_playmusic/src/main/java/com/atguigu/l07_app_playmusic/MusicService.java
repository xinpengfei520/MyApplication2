package com.atguigu.l07_app_playmusic;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by shkstart on 2016/9/10 0010.
 */
public class MusicService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    MediaPlayer player;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getStringExtra("action");
        if("play".equals(action)){
            play();
        }else if("pause".equals(action)){
            pause();
        }else if("stop".equals(action)){
            stop();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void pause() {
        if (player != null) {
            player.pause();

        }
    }

    private void stop() {
        if (player != null) {
            player.stop();
            player.reset();
            player.release();//释放加载的文件
            player = null;//不要忘了！
        }
    }

    private void play() {
        if (player == null) {
            player = MediaPlayer.create(this, R.raw.everything_at_once);

        }
        if (player != null && !player.isPlaying()) {
            player.start();

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stop();//停止音乐
    }
}
