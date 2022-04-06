package tk.felixfab.voicebeam.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.neovisionaries.ws.client.WebSocketException;

import java.io.IOException;

import tk.felixfab.voicebeam.Activity.MainActivity;
import tk.felixfab.voicebeam.R;
import tk.felixfab.voicebeam.Timer.checkStatusTimer;
import tk.felixfab.voicebeam.User.UserInfos;
import tk.felixfab.voicebeam.WebSocket.WebSocketManager;
import tk.felixfab.voicebeam.etc.Var;

public class DefaultService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        final NotificationChannel channel = new NotificationChannel(Var.NotificationChannelID,Var.Appname, NotificationManager.IMPORTANCE_DEFAULT);

        NotificationManager mn = getSystemService(NotificationManager.class);

        mn.createNotificationChannel(channel);
        Notification.Builder b = new Notification.Builder(this,Var.NotificationChannelID);
        b.setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(Var.Appname)
                .setContentText("Check Messages");

        startForeground(Var.DefaultServiceForegroundID,b.build());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(!WebSocketManager.isConnected()){
                        WebSocketManager.connect("ws://" + Var.Host + ":81");
                        WebSocketManager.ws.sendText("{\"key\": \"register\", \"username\": \"" + UserInfos.getUsername() + "\" }");
                    }
                } catch (IOException | WebSocketException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                checkStatusTimer.startTimer();
            }
        }).start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
