package tk.felixfab.voicebeam.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.neovisionaries.ws.client.WebSocketException;

import java.io.IOException;

import tk.felixfab.voicebeam.WebSocket.WebSocketManager;

public class WebSocketService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    WebSocketManager.connectToSocket("ws://5.181.151.118:81");
                } catch (IOException | WebSocketException e) {
                    e.printStackTrace();
                }
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
