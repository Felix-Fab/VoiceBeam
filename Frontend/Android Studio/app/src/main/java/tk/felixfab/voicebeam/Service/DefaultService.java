package tk.felixfab.voicebeam.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.neovisionaries.ws.client.WebSocketException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import tk.felixfab.voicebeam.API.HTTP;
import tk.felixfab.voicebeam.Activity.MainActivity;
import tk.felixfab.voicebeam.R;
import tk.felixfab.voicebeam.Timer.checkStatusTimer;
import tk.felixfab.voicebeam.Timer.checkWebSocketConnectionTimer;
import tk.felixfab.voicebeam.User.UserInfos;
import tk.felixfab.voicebeam.WebSocket.WebSocketManager;
import tk.felixfab.voicebeam.etc.Logger;
import tk.felixfab.voicebeam.etc.Var;

public class DefaultService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
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
                checkWebSocketConnectionTimer.startTimer();
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

        try {
            HttpURLConnection con = HTTP.createDefaultConnection("http://" + Var.Host + ":3000/manager/status", "POST");

            con.addRequestProperty("authorization","Bearer " + UserInfos.getAccessToken());

            con.setDoOutput(false);
            con.connect();

            switch (con.getResponseCode()){
                case 200:
                    Logger.writeSuccessMessage("Service destroy logout Code 200");
                break;

                case 401:
                    Logger.writeWarningMessage("Service destroy logout Code 401");
                break;

                case 403:
                    Logger.writeWarningMessage("Service destroy logout Code 403");
                break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
