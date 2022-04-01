package tk.felixfab.voicebeam.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.neovisionaries.ws.client.WebSocketException;

import java.io.IOException;

import tk.felixfab.voicebeam.Timer.WebSocketTimer;
import tk.felixfab.voicebeam.User.UserInfos;
import tk.felixfab.voicebeam.WebSocket.WebSocketManager;

public class WebSocketService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Handler handler = new Handler();
        Runnable mhandlerTaskRunnable = new Runnable() {
            @Override
            public void run() {
                if(UserInfos.getEmail() != null){

                    if (!WebSocketManager.isConnected()) {
                        try {
                            WebSocketManager.connect("ws://5.181.151.118:81");
                            WebSocketManager.ws.sendText("{\"key\": \"register\", \"username\": \"" + UserInfos.getUsername() + "\" }");
                        } catch (IOException | WebSocketException e) {
                            e.printStackTrace();
                        }
                    }else{
                        WebSocketManager.ws.sendText("Hallo");
                    }
                }
            }
        };

        handler.postDelayed(mhandlerTaskRunnable,1000);
        mhandlerTaskRunnable.run();

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
