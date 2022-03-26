package tk.felixfab.voicebeam.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import tk.felixfab.voicebeam.Timer.checkStatusTimer;
import tk.felixfab.voicebeam.User.UserInfos;

public class CheckStatusService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                checkStatusTimer.startTimer();
            }
        }).start();

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
