package tk.felixfab.voicebeam.Timer;

import android.media.AudioDeviceInfo;
import android.media.AudioManager;

import com.neovisionaries.ws.client.WebSocketException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Timer;
import java.util.TimerTask;

import tk.felixfab.voicebeam.API.HTTP;
import tk.felixfab.voicebeam.Activity.MainActivity;
import tk.felixfab.voicebeam.User.UserInfos;
import tk.felixfab.voicebeam.WebSocket.WebSocketManager;
import tk.felixfab.voicebeam.etc.Logger;

public class WebSocketTimer {

    private static Timer timer = new Timer();

    public static void startTimer() {

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                if (!WebSocketManager.isConnected()) {
                    try {
                        WebSocketManager.connect("ws://5.181.151.118:81");
                    } catch (IOException | WebSocketException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 0, 10000);
    }

    public static void stopTimer(){
        timer.cancel();
    }
}
