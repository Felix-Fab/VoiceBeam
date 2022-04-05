package tk.felixfab.voicebeam.Service;

import android.app.Notification;
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
import tk.felixfab.voicebeam.User.UserInfos;
import tk.felixfab.voicebeam.WebSocket.WebSocketManager;

public class WebSocketService extends JobService {


    @Override
    public boolean onStartJob(JobParameters params) {

        if(!WebSocketManager.isConnected()){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        WebSocketManager.connect("ws://" + MainActivity.Host + ":81");
                        WebSocketManager.ws.sendText("{\"key\": \"register\", \"username\": \"" + UserInfos.getUsername() + "\" }");
                    } catch (IOException | WebSocketException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
